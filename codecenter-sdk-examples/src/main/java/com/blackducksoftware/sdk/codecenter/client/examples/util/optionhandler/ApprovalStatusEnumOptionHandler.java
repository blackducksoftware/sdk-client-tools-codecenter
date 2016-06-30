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

import com.blackducksoftware.sdk.codecenter.common.data.ApprovalStatusEnum;

public class ApprovalStatusEnumOptionHandler extends BDEnumOptionHandler<ApprovalStatusEnum> {

    public static String enumToString(ApprovalStatusEnum value) {
        switch (value) {
        case NOTSUBMITTED:
            return "not submitted";
        case MOREINFO:
            return "more info";
        default:
            return BDEnumOptionHandler.enumToString(value);
        }
    }

    public ApprovalStatusEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super ApprovalStatusEnum> setter) {
        super(parser, option, setter, false, ApprovalStatusEnum.class);
    }

    @Override
    protected String toString(ApprovalStatusEnum value) {
        return enumToString(value);
    }

}
