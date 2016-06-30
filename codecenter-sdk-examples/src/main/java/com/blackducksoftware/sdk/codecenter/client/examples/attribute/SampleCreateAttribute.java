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
package com.blackducksoftware.sdk.codecenter.client.examples.attribute;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.attribute.data.AbstractAttribute;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeIdToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public abstract class SampleCreateAttribute<T extends AbstractAttribute> extends BDCodeCenterSample {

    @Argument(index = 3, required = true, metaVar = "name", usage = "the name of the attribute to create")
    protected String name;

    @Argument(index = 4, required = true, metaVar = "description", usage = "the description of the attribute")
    protected String description;

    @Option(name = "--inactive", usage = "make inactive")
    protected boolean isInactive = false;

    @Option(name = "--index", usage = "include in search index")
    protected boolean index = false;

    public SampleCreateAttribute(String[] args) {
        super(args);
    }

    @Override
    public final void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the attribute bean
        T attribute = createAttribute();
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setActive(!isInactive);
        attribute.setIncludeInIndex(index);

        // Create the attribute
        AttributeIdToken attributeId = proxy.getAttributeApi().createAttribute(attribute);

        // Print success information
        attribute.setId(attributeId);
        System.out.println(getSuccessString(attribute));
    }

    protected String getSuccessString(T attribute) {
        String typeName = getTypeName();
        return "Successfully created " + (typeName != null ? typeName + " " : "") + "attribute \"" + name + "\" with attribute ID " + attribute.getId().getId()
                + ".";
    }

    protected String getTypeName() {
        return null;
    }

    protected abstract T createAttribute() throws Exception;

}
