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
package com.blackducksoftware.sdk.codecenter.client.examples;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.kohsuke.args4j.ParserProperties;
import org.springframework.util.StringUtils;

import com.blackducksoftware.sdk.codecenter.client.examples.util.Printer;
import com.blackducksoftware.sdk.codecenter.client.examples.util.SimplePrinter;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;

public abstract class SamplePrinter extends BDCodeCenterSample {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private String padFormat;

    private DatePrinter datePrinter;

    public SamplePrinter(String[] args) {
        super(args);
    }

    public SamplePrinter(String[] args, ParserProperties parserProperties) {
        super(args, parserProperties);
    }

    protected void print(String field, Object value) {
        print(field, value, SimplePrinter.INSTANCE);
    }

    protected <V> void print(String field, V value, Printer<? super V> printer) {
        if (value != null) {
            String str = printer.print(value);
            System.out.println(pad(field + ": ") + wrap(str));
        }
    }

    protected void print(String field, String str) {
        if (StringUtils.hasText(str)) {
            print(field, (Object) str);
        }
    }

    protected <E extends Enum<E>> void print(String field, E value) {
        if (value != null) {
            print(field, BDEnumOptionHandler.enumToString(value));
        }
    }

    protected void print(String field, Date date) {
        if (datePrinter == null) {
            datePrinter = new DatePrinter(getDateFormat());
        }

        print(field, date, datePrinter);
    }

    protected DateFormat getDateFormat() {
        return DATE_FORMAT;
    }

    @SuppressWarnings("cast")
    protected final <V> void print(String field, Iterable<? extends V> values) {
        // javac 1.7 (and earlier) thinks this cast to (Printer<? super V>) is unnecessary, but that's a bug due to poor
        // type inference. javac 1.8 correctly recognizes that, without it, this call is ambiguous between print(String,
        // V, Printer<? super V>) and print(String, Iterable<? extends V>, Printer<? super V>). Don't remove (or let
        // your IDE remove) this cast!
        print(field, values, (Printer<? super V>) SimplePrinter.INSTANCE);
    }

    protected <V> void print(String field, Iterable<? extends V> values, Printer<? super V> printer) {
        if (values != null) {
            boolean first = true;
            for (V value : values) {
                String str = printer.print(value);
                System.out.println(pad(first ? field + ": " : "") + wrap(str));
                first = false;
            }
        }
    }

    protected String wrap(String str) {
        return str.replaceAll("\\r?\\n", pad(" ."));
    }

    protected String pad(String str) {
        if (padFormat == null) {
            padFormat = "%-" + getFieldWidth() + "s";
        }

        return String.format(padFormat, str);
    }

    protected int getFieldWidth() {
        return 24;
    }

    private static class DatePrinter implements Printer<Date> {

        private final DateFormat dateFormat;

        public DatePrinter(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public String print(Date date) {
            return dateFormat.format(date);
        }

    }

}
