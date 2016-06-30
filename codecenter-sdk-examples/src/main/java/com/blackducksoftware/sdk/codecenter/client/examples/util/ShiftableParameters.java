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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.spi.Parameters;

public class ShiftableParameters implements Parameters {

    public static ShiftableParameters of(Parameters params) {
        return new ShiftableParameters(params);
    }

    private final Parameters params;

    private int pos = 0;

    public ShiftableParameters(Parameters params) {
        this.params = params;
    }

    @Override
    public String getParameter(int idx) throws CmdLineException {
        return params.getParameter(pos + idx);
    }

    @Override
    public int size() {
        return Math.max(params.size() - pos, 0);
    }

    public void shift(int offset) {
        pos += offset;
    }

    public int getOffset() {
        return pos;
    }

}
