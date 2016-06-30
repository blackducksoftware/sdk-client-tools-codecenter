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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.MapOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class MultiMapOptionHandler extends MapOptionHandler {

    public MultiMapOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Map<?, ?>> setter) {
        super(parser, option, setter);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addToMap(Map m, String key, String value) {
        Collection<String> values = (Collection<String>) m.get(key);

        if (values == null) {
            values = createNewValueCollection();
            m.put(key, values);
        }

        values.add(value);
    }

    protected Collection<String> createNewValueCollection() {
        return new ArrayList<>();
    }

}
