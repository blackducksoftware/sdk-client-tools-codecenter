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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Objects;
import java.util.ResourceBundle;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.OptionHandler;

public class BDCmdLineParser extends CmdLineParser {

    protected final String programName;

    public BDCmdLineParser(Object bean, String programName) {
        super(bean);
        this.programName = programName;
    }

    public BDCmdLineParser(Object bean, String programName, ParserProperties parserProperties) {
        super(bean, parserProperties);
        this.programName = programName;
    }

    @SuppressWarnings("resource")
    @Override
    public final void printSingleLineUsage(OutputStream out) {
        Objects.requireNonNull(out, "OutputStream");

        printSingleLineUsage(new OutputStreamWriter(out), null);
    }

    @Override
    public final void printSingleLineUsage(Writer w, ResourceBundle rb) {
        printSingleLineUsage(w, rb, OptionHandlerFilter.PUBLIC);
    }

    public void printSingleLineUsage(Writer w, ResourceBundle rb, OptionHandlerFilter filter) {
        Objects.requireNonNull(w, "Writer");

        PrintWriter pw = new PrintWriter(w);
        String allButOptions;

        StringWriter sw1 = new StringWriter();
        PrintWriter pw1 = new PrintWriter(sw1);
        pw1.print("Usage: ");
        pw1.print(programName);
        for (OptionHandler<?> h : getArguments()) {
            printSingleLineOption(pw1, h, rb, filter);
        }
        pw1.flush();
        allButOptions = sw1.toString();

        pw.print(allButOptions);

        String options;
        String optionsGroup = " [options]";

        StringWriter sw2 = new StringWriter();
        PrintWriter pw2 = new PrintWriter(sw2);
        for (OptionHandler<?> h : getOptions()) {
            printSingleLineOption(pw2, h, rb, filter);
        }
        pw2.flush();
        options = sw2.toString();

        pw.print((options.length() <= optionsGroup.length() * 2) ? options : optionsGroup);
        pw.flush();
    }

    protected void printSingleLineOption(PrintWriter pw, OptionHandler<?> h, ResourceBundle rb, OptionHandlerFilter filter) {
        if (!filter.select(h)) {
            return;
        }

        String nameAndMeta = getSingleLineOptionNameAndMeta(h, rb);
        boolean isPlainArg = h.option.isArgument() && Normalizer.normalize(nameAndMeta, Form.NFD).matches("[\\w ]+");

        pw.print(' ');
        if (!h.option.required()) {
            pw.print('[');
        }
        if (isPlainArg) {
            pw.print('<');
        }
        pw.print(nameAndMeta);
        if (isPlainArg) {
            pw.print('>');
        }
        if (h.option.isMultiValued()) {
            pw.print(" ...");
        }
        if (!h.option.required()) {
            pw.print(']');
        }
    }

    protected String getSingleLineOptionNameAndMeta(OptionHandler<?> h, ResourceBundle rb) {
        return h.getNameAndMeta(rb, getProperties());
    }

}
