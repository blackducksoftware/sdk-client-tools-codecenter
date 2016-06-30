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

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.role.data.PermissionEnum;

public class PermissionEnumOptionHandler extends BDEnumOptionHandler<PermissionEnum> {

    public static String enumToString(PermissionEnum value) {
        return enumNameToString(value.name().substring("PERMISSION_".length()));
    }

    public PermissionEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super PermissionEnum> setter) {
        super(parser, option, setter, PermissionEnum.class);
    }

    @Override
    protected String toString(PermissionEnum value) {
        return enumToString(value);
    }

}
