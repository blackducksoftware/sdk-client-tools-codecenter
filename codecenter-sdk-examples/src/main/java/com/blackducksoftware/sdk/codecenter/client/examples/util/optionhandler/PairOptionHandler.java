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
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.client.examples.util.Pair;

public class PairOptionHandler<T1, T2> extends OptionHandler<Pair<T1, T2>> {

    public PairOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Pair<T1, T2>> setter) {
        super(parser, option, setter);
    }

    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        setter.addValue(createPair(params.getParameter(0), params.getParameter(1)));
        return 2;
    }

    @SuppressWarnings("unchecked")
    protected Pair<T1, T2> createPair(String s1, String s2) {
        return (Pair<T1, T2>) new Pair<>(s1, s2);
    }

    @Override
    public String getDefaultMetaVariable() {
        String oneVal = Messages.DEFAULT_META_STRING_OPTION_HANDLER.format();
        return oneVal + " " + oneVal;
    }

}
