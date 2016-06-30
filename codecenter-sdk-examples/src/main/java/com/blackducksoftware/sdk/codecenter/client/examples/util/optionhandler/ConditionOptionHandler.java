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
package com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.approval.data.ApplicationFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalNameToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ComponentFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ComponentUseFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.Condition;
import com.blackducksoftware.sdk.codecenter.approval.data.DynamicFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.DynamicOrStaticFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ExpressionFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ExpressionStaticFieldsEnum;
import com.blackducksoftware.sdk.codecenter.approval.data.OperatorEnum;
import com.blackducksoftware.sdk.codecenter.approval.data.StaticFieldToken;
import com.blackducksoftware.sdk.codecenter.approval.data.VulnerabilityFieldToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.util.FieldOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Pair;

public class ConditionOptionHandler extends FieldOptionHandler<Condition, ExpressionFieldToken> {

    public ConditionOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Condition> setter) {
        super(parser, option, setter);
    }

    @Override
    protected Pair<ExpressionFieldToken, Integer> parseField(Parameters params, int pos) throws CmdLineException {
        // Parse the trigger type
        ExpressionFieldToken field;

        String typeParam = params.getParameter(pos);
        if (typeParam.equalsIgnoreCase("Application")) {
            field = new ApplicationFieldToken();
        } else if (typeParam.equalsIgnoreCase("Component")) {
            field = new ComponentFieldToken();
        } else if (typeParam.matches("(?i)Component ?(Use|Request)")) {
            field = new ComponentUseFieldToken();
        } else if (typeParam.equalsIgnoreCase("Vulnerability")) {
            field = new VulnerabilityFieldToken();
        } else {
            // throw new CmdLineException(owner, Messages.ILLEGAL_OPERAND, "trigger type", typeParam);
            ApprovalNameToken approval = new ApprovalNameToken();
            approval.setName(typeParam);

            ApprovalFieldToken approvalField = new ApprovalFieldToken();
            approvalField.setId(approval);

            field = approvalField;
        }

        // Parse the attribute name
        DynamicOrStaticFieldToken fieldToken;

        String attributeParam = params.getParameter(pos + 1);
        ExpressionStaticFieldsEnum fieldEnum = BDEnumOptionHandler.stringToEnum(attributeParam, ExpressionStaticFieldsEnum.values());
        if (fieldEnum != null) {
            StaticFieldToken staticFieldToken = new StaticFieldToken();
            staticFieldToken.setField(fieldEnum);
            fieldToken = staticFieldToken;
        } else {
            AttributeNameToken nameToken = new AttributeNameToken();
            nameToken.setName(attributeParam);
            DynamicFieldToken dynamicFieldToken = new DynamicFieldToken();
            dynamicFieldToken.setAttributeId(nameToken);
            fieldToken = dynamicFieldToken;
        }

        // Return the field and number of parameters consumed
        field.setField(fieldToken);
        return new Pair<>(field, 2);
    }

    @Override
    protected void set(ExpressionFieldToken field, OperatorEnum operator, String value) throws CmdLineException {
        Condition condition = new Condition();
        condition.setField(field);
        condition.setOperator(operator);
        condition.getValues().add(value);

        setter.addValue(condition);
    }

    @Override
    protected String getFieldMetaVariable() {
        return "(Application | Component | \"Component Use\" | Vulnerability | <approvalName>) <attribute>";
    }

}
