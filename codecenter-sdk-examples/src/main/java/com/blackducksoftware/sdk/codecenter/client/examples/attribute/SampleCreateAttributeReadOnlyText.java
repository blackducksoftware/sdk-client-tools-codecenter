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

import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeTypeEnum;
import com.blackducksoftware.sdk.codecenter.attribute.data.SimpleAttributeDefinition;

public class SampleCreateAttributeReadOnlyText extends SampleCreateAttribute<SimpleAttributeDefinition> {

    public static void main(String[] args) {
        new SampleCreateAttributeReadOnlyText(args).run();
    }

    @Argument(index = 5, required = true, metaVar = "value", usage = "the read-only text to present")
    protected String value;

    public SampleCreateAttributeReadOnlyText(String[] args) {
        super(args);
    }

    @Override
    protected String getTypeName() {
        return "read-only text";
    }

    @Override
    protected SimpleAttributeDefinition createAttribute() {
        SimpleAttributeDefinition attribute = new SimpleAttributeDefinition();
        attribute.setType(AttributeTypeEnum.READ_ONLY_TEXT);
        attribute.setDefaultValue(value);

        return attribute;
    }

}
