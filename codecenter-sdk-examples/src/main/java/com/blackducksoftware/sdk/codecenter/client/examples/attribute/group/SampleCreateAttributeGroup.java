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
package com.blackducksoftware.sdk.codecenter.client.examples.attribute.group;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroupIdToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroupTypeEnum;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.AttributeGroupTypeEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public class SampleCreateAttributeGroup extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateAttributeGroup(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "name", usage = "the name of the attribute group to create")
    protected String name;

    @Argument(index = 4, required = true, handler = AttributeGroupTypeEnumOptionHandler.class, metaVar = "type",
            usage = "the type of attribute group to create")
    protected AttributeGroupTypeEnum type;

    @Argument(index = 5, required = false, metaVar = "description", usage = "the description of the attribute group")
    protected String description;

    public SampleCreateAttributeGroup(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the attribute group
        AttributeGroupIdToken groupId = proxy.getAttributeApi().createAttributeGroup(name, description, type);

        // Print success information
        System.out.println(
                "Successfully created the " + BDEnumOptionHandler.enumToString(type) + " attribute group \"" + name + "\" with ID " + groupId.getId() + ".");
    }

}
