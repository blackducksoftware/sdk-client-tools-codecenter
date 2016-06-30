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

import java.util.List;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.attribute.data.MultiSelectAttributeDefinition;

public class SampleCreateAttributeMultiSelect extends SampleCreateAttributeNotReadOnly<MultiSelectAttributeDefinition> {

    public static void main(String[] args) {
        new SampleCreateAttributeMultiSelect(args).run();
    }

    @Argument(index = 6, required = true, multiValued = true, metaVar = "option",
            usage = "an available option; prepend an asterisk to make an option selected by default (ex: \"*Selected option\" \"Unselected option\")")
    protected List<String> options;

    public SampleCreateAttributeMultiSelect(String[] args) {
        super(args);
    }

    @Override
    protected String getTypeName() {
        return "multiple select";
    }

    protected boolean isCheckBox() {
        return false;
    }

    @Override
    protected MultiSelectAttributeDefinition createAttribute_() {
        MultiSelectAttributeDefinition attribute = new MultiSelectAttributeDefinition();
        attribute.setRadio(isCheckBox());

        for (String option : options) {
            option = option.trim();

            if (option.startsWith("*")) {
                option = option.substring(1).trim();
                attribute.getDefaultOptions().add(option);
            }

            attribute.getOptions().add(option);
        }

        return attribute;
    }

}
