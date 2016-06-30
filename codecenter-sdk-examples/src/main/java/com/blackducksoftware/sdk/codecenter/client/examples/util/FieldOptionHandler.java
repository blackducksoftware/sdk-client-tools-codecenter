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

import java.util.ResourceBundle;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.approval.data.OperatorEnum;

public abstract class FieldOptionHandler<T, F> extends OptionHandler<T> {

    public FieldOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super T> setter) {
        super(parser, option, setter);
    }

    @Override
    public final int parseArguments(Parameters params) throws CmdLineException {
        // Parse the field
        Pair<F, Integer> parseFieldResult = parseField(params, 0);
        F field = parseFieldResult.getX();
        int pos = parseFieldResult.getY();

        // Parse the operator
        OperatorEnum operator;

        String operatorParam = params.getParameter(pos);
        if (operatorParam.equals("~")) {
            operator = OperatorEnum.CONTAINS;
        } else if (operatorParam.equals("!~")) {
            operator = OperatorEnum.DOES_NOT_CONTAIN;
        } else if (operatorParam.equals("=")) {
            operator = OperatorEnum.EQUALS;
        } else if (operatorParam.equals("!=")) {
            operator = OperatorEnum.DOES_NOT_EQUAL;
        } else {
            throw new CmdLineException(owner, Messages.ILLEGAL_OPERAND, getOperatorName(), operatorParam);
        }

        // Parse the value
        String value = params.getParameter(pos + 1);

        // Put it all together
        set(field, operator, value);
        return pos + 2;
    }

    protected abstract Pair<F, Integer> parseField(Parameters params, int pos) throws CmdLineException;

    protected String getOperatorName() {
        return "operator";
    }

    protected abstract void set(F field, OperatorEnum operator, String value) throws CmdLineException;

    @Override
    public final String getDefaultMetaVariable() {
        return "(" + getFieldMetaVariable() + " ( ~ | !~ | = | != ) " + getValueMetaVariable() + ")";
    }

    protected String getFieldMetaVariable() {
        return "<field>";
    }

    protected String getValueMetaVariable() {
        return "<value>";
    }

    @Override
    public String getMetaVariable(ResourceBundle rb) {
        return getDefaultMetaVariable();
    }

}
