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

import java.util.Collection;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.PositiveIntOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public abstract class SampleSearch<T> extends SamplePrinter {

    @Option(name = "-n", handler = PositiveIntOptionHandler.class, usage = "maximum number of results")
    protected int limit = 10;

    public SampleSearch(String[] args) {
        super(args);
    }

    public SampleSearch(String[] args, ParserProperties parserProperties) {
        super(args, parserProperties);
    }

    @Override
    public final void run(CodeCenterServerProxy proxy) throws Exception {
        // Search for results
        Collection<T> results = search(proxy);

        // Print results
        print(results, proxy);
    }

    protected abstract Collection<T> search(CodeCenterServerProxy proxy) throws Exception;

    protected void print(Collection<T> results, CodeCenterServerProxy proxy) throws Exception {
        // Print success information
        System.out.println("Successfully searched for " + getSearchExpression() + ".");
        boolean atLimit = results.size() == limit;
        System.out.println("Found " + (atLimit ? "at least " : "") + results.size() + " result" + (results.size() != 1 ? "s" : "")
                + (atLimit ? ". Consider restricting your search" : "") + (results.size() != 0 ? ":" : "."));
        System.out.println();

        for (T result : results) {
            // Print single result details
            print(result, proxy);
        }
    }

    protected abstract String getSearchExpression();

    protected abstract void print(T result, CodeCenterServerProxy proxy) throws Exception;

}
