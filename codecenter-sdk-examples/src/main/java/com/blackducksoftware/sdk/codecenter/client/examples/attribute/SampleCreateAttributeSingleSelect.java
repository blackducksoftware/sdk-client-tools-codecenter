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
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.FieldSetter;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.attribute.data.SingleSelectAttributeDefinition;

public class SampleCreateAttributeSingleSelect extends SampleCreateAttributeNotReadOnly<SingleSelectAttributeDefinition> {

    public static void main(String[] args) {
        new SampleCreateAttributeSingleSelect(args).run();
    }

    @Argument(index = 6, required = true, multiValued = true, handler = SingleSelectOptionHandler.class, metaVar = "option",
            usage = "an available option; prepend an asterisk to make an option selected by default (ex: \"*Selected option\" \"Unselected option\")")
    protected SingleSelectAttributeDefinition attribute;

    public SampleCreateAttributeSingleSelect(String[] args) {
        super(args);
    }

    @Override
    protected String getTypeName() {
        return "single select";
    }

    protected boolean isRadio() {
        return false;
    }

    @Override
    protected SingleSelectAttributeDefinition createAttribute_() {
        attribute.setCheckBox(isRadio());

        return attribute;
    }

    public static class SingleSelectOptionHandler extends OptionHandler<SingleSelectAttributeDefinition> {

        public SingleSelectOptionHandler(CmdLineParser parser, OptionDef option, Setter<SingleSelectAttributeDefinition> setter) {
            super(parser, option, setter);
            if (setter.asFieldSetter() == null) {
                throw new IllegalArgumentException(SingleSelectOptionHandler.class.getSimpleName() + " can only work with fields");
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public int parseArguments(Parameters params) throws CmdLineException {
            FieldSetter fs = setter.asFieldSetter();
            SingleSelectAttributeDefinition attribute = (SingleSelectAttributeDefinition) fs.getValue();

            if (attribute == null) {
                attribute = new SingleSelectAttributeDefinition();
                fs.addValue(attribute);
            }

            String option = params.getParameter(0).trim();

            if (option.startsWith("*")) {
                option = option.substring(1).trim();

                if (attribute.getDefaultOption() == null) {
                    attribute.setDefaultOption(option);
                } else {
                    throw new CmdLineException(owner,
                            "More than one default option specified: \"" + attribute.getDefaultOption() + "\" and \"" + option + "\"");
                }
            }

            attribute.getOptions().add(option);
            return 1;
        }

        @Override
        public String getDefaultMetaVariable() {
            return null;
        }

    }

}
