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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

public class DateOptionHandler extends OptionHandler<Date> {

    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat();

    private final SimpleDateFormat dateFormat;

    public DateOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Date> setter) {
        this(parser, option, setter, DEFAULT_DATE_FORMAT);
    }

    public DateOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Date> setter, SimpleDateFormat dateFormat) {
        super(parser, option, setter);
        this.dateFormat = dateFormat;
    }

    protected final SimpleDateFormat getFormat() {
        return dateFormat;
    }

    @Override
    public final int parseArguments(Parameters params) throws CmdLineException {
        Date date;
        try {
            date = parse(params.getParameter(0));
        } catch (ParseException e) {
            throw new CmdLineException(owner, e);
        }

        setter.addValue(date);
        return 1;
    }

    protected Date parse(String source) throws ParseException {
        return getFormat().parse(source);
    }

    @Override
    public String getDefaultMetaVariable() {
        return getFormat().toPattern();
    }

}
