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

import java.util.Comparator;

import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.OptionHandler;

@SuppressWarnings("rawtypes")
public class BDOptionSorter implements Comparator<OptionHandler> {

    private final ParserProperties properties;

    public BDOptionSorter(ParserProperties properties) {
        this.properties = properties;
    }

    @Override
    public int compare(OptionHandler o1, OptionHandler o2) {
        String name1 = o1.getNameAndMeta(null, properties).replaceFirst("^\\-+", "");
        String name2 = o2.getNameAndMeta(null, properties).replaceFirst("^\\-+", "");
        return name1.compareToIgnoreCase(name2);
    }

}
