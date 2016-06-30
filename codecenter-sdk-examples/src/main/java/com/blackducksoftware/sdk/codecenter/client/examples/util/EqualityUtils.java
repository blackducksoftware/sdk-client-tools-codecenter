/*
 * Black Duck Software Suite SDK
 * Copyright (C) 2016 Black Duck Software, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package com.blackducksoftware.sdk.codecenter.client.examples.util;

import java.util.Objects;

import com.blackducksoftware.sdk.codecenter.approval.data.ApplicationFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalIdToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalNameToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ComponentFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ComponentUseFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.Condition;
import com.blackducksoftware.sdk.codecenter.approval.data.DynamicFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.DynamicOrStaticFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ExpressionFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.Rule;
import com.blackducksoftware.sdk.codecenter.approval.data.StaticFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.VulnerabilityFieldToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeIdToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.fault.SdkFault;

public final class EqualityUtils {

    public static boolean equals(Rule r1, Rule r2, CodeCenterServerProxy proxy) throws SdkFault {
        return containsAll(r1.getConditions(), r2.getConditions(), proxy) && containsAll(r2.getConditions(), r1.getConditions(), proxy);
    }

    public static boolean containsAll(Iterable<? extends Condition> c1s, Iterable<? extends Condition> c2s, CodeCenterServerProxy proxy) throws SdkFault {
        c2Loop:
        for (Condition c2 : c2s) {
            for (Condition c1 : c1s) {
                if (equals(c1, c2, proxy)) {
                    continue c2Loop;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean equals(Condition c1, Condition c2, CodeCenterServerProxy proxy) throws SdkFault {
        return c1.getValues().equals(c2.getValues()) && (c1.getOperator() == c2.getOperator()) && equals(c1.getField(), c2.getField(), proxy);

    }

    public static boolean equals(ExpressionFieldToken f1, ExpressionFieldToken f2, CodeCenterServerProxy proxy) throws SdkFault {
        if (f1 == null) {
            return f2 == null;

        } else if (f2 == null) {
            return false;
        }

        if (!equals(f1.getField(), f2.getField(), proxy)) {
            return false;
        }

        if (f1 instanceof ApplicationFieldToken) {
            return f2 instanceof ApplicationFieldToken;

        } else if (f1 instanceof ApprovalFieldToken) {
            return (f2 instanceof ApprovalFieldToken) && equals(((ApprovalFieldToken) f1).getId(), ((ApprovalFieldToken) f2).getId(), proxy);

        } else if (f1 instanceof ComponentFieldToken) {
            return f2 instanceof ComponentFieldToken;

        } else if (f1 instanceof ComponentUseFieldToken) {
            return (f2 instanceof ComponentUseFieldToken)
                    && equals(((ComponentUseFieldToken) f1).getApprovalId(), ((ComponentUseFieldToken) f2).getApprovalId(), proxy);

        } else if (f1 instanceof VulnerabilityFieldToken) {
            return f2 instanceof VulnerabilityFieldToken;

        } else {
            throw getUnsupportedSubclassException(ExpressionFieldToken.class, f1);
        }
    }

    public static boolean equals(DynamicOrStaticFieldToken f1, DynamicOrStaticFieldToken f2, CodeCenterServerProxy proxy) throws SdkFault {
        if (f1 instanceof DynamicFieldToken) {
            return (f2 instanceof DynamicFieldToken) && equals(((DynamicFieldToken) f1).getAttributeId(), ((DynamicFieldToken) f2).getAttributeId(), proxy);

        } else if (f1 instanceof StaticFieldToken) {
            return (f2 instanceof StaticFieldToken) && (((StaticFieldToken) f1).getField() == ((StaticFieldToken) f2).getField());

        } else if (f1 == null) {
            return f2 == null;

        } else {
            throw getUnsupportedSubclassException(DynamicOrStaticFieldToken.class, f1);
        }
    }

    public static boolean equals(AttributeNameOrIdToken a1, AttributeNameOrIdToken a2, CodeCenterServerProxy proxy) throws SdkFault {
        if (a1 instanceof AttributeIdToken) {
            return Objects.equals(((AttributeIdToken) a1).getId(),
                    (a2 instanceof AttributeIdToken ? (AttributeIdToken) a2 : proxy.getAttributeApi().getAttribute(a2).getId()).getId());

        } else if (a1 instanceof AttributeNameToken) {
            return Objects.equals(((AttributeNameToken) a1).getName(),
                    a2 instanceof AttributeNameToken ? ((AttributeNameToken) a2).getName() : proxy.getAttributeApi().getAttribute(a2).getName());

        } else if (a1 == null) {
            return a2 == null;

        } else {
            throw getUnsupportedSubclassException(AttributeNameOrIdToken.class, a1);
        }
    }

    public static boolean equals(ApprovalNameOrIdToken a1, ApprovalNameOrIdToken a2, CodeCenterServerProxy proxy) throws SdkFault {
        if (a1 instanceof ApprovalIdToken) {
            return Objects.equals(((ApprovalIdToken) a1).getId(),
                    (a2 instanceof ApprovalIdToken ? (ApprovalIdToken) a2 : proxy.getApprovalApi().getApproval(a2).getId()).getId());

        } else if (a1 instanceof ApprovalNameToken) {
            return Objects.equals(((ApprovalNameToken) a1).getName(),
                    a2 instanceof ApprovalNameToken ? ((ApprovalNameToken) a2).getName() : proxy.getApprovalApi().getApproval(a2).getName());

        } else if (a1 == null) {
            return a2 == null;

        } else {
            throw getUnsupportedSubclassException(ApprovalNameOrIdToken.class, a1);
        }
    }

    private static RuntimeException getUnsupportedSubclassException(Class<?> superclass, Object o) {
        return new UnsupportedOperationException("Unsupported " + superclass.getSimpleName() + " subclass: " + o.getClass().getSimpleName());
    }

    private EqualityUtils() {}

}
