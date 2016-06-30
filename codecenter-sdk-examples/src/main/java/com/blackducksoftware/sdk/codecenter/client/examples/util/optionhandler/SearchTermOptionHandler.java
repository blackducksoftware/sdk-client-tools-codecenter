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

import com.blackducksoftware.sdk.codecenter.approval.data.OperatorEnum;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.client.examples.util.FieldOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Pair;
import com.blackducksoftware.sdk.codecenter.cola.data.DynamicFieldSearchTerm;
import com.blackducksoftware.sdk.codecenter.cola.data.SearchTerm;
import com.blackducksoftware.sdk.codecenter.cola.data.StaticField;
import com.blackducksoftware.sdk.codecenter.cola.data.StaticFieldSearchTerm;

public abstract class SearchTermOptionHandler extends FieldOptionHandler<SearchTerm, Object> {

    public SearchTermOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super SearchTerm> setter) {
        super(parser, option, setter);
    }

    @Override
    protected Pair<Object, Integer> parseField(Parameters params, int pos) throws CmdLineException {
        // Parse the field name
        Object field;

        String fieldParam = params.getParameter(pos);
        StaticField fieldEnum = BDEnumOptionHandler.stringToEnum(fieldParam, StaticField.values());
        if (fieldEnum != null) {
            field = fieldEnum;
        } else {
            field = parseDynamicField(fieldParam);
        }

        // Return the field and number of parameters consumed
        return new Pair<>(field, 1);
    }

    protected abstract AttributeNameOrIdToken parseDynamicField(String fieldParam);

    @Override
    protected void set(Object field, OperatorEnum operator, String value) throws CmdLineException {
        SearchTerm searchTerm;

        if (field instanceof StaticField) {
            StaticFieldSearchTerm staticFieldSearchTerm = new StaticFieldSearchTerm();
            staticFieldSearchTerm.setField((StaticField) field);
            staticFieldSearchTerm.setOperator(operator);
            staticFieldSearchTerm.getValues().add(value);
            searchTerm = staticFieldSearchTerm;
        } else {
            DynamicFieldSearchTerm dynamicFieldSearchTerm = new DynamicFieldSearchTerm();
            dynamicFieldSearchTerm.setId((AttributeNameOrIdToken) field);
            dynamicFieldSearchTerm.setOperator(operator);
            dynamicFieldSearchTerm.getValues().add(value);
            searchTerm = dynamicFieldSearchTerm;
        }

        setter.addValue(searchTerm);
    }

}
