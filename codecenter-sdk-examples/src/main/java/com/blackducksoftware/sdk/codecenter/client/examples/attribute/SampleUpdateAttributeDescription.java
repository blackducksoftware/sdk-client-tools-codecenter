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

import com.blackducksoftware.sdk.codecenter.attribute.data.AbstractAttribute;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public class SampleUpdateAttributeDescription extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleUpdateAttributeDescription(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "name", usage = "the name of the attribute to update")
    protected String name;

    @Argument(index = 4, required = true, metaVar = "description", usage = "the updated description of the attribute")
    protected String description;

    public SampleUpdateAttributeDescription(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the attribute name token
        AttributeNameToken attributeNameToken = new AttributeNameToken();
        attributeNameToken.setName(name);

        // Get the original attribute bean
        AbstractAttribute attribute = proxy.getAttributeApi().getAttribute(attributeNameToken);

        // Update the attribute bean
        attribute.setDescription(description);

        // Update the attribute
        proxy.getAttributeApi().updateAttribute(attribute);

        // Print success information
        System.out.println("Successfully updated the description of attribute \"" + name + "\" to:\n" + description);
    }

}
