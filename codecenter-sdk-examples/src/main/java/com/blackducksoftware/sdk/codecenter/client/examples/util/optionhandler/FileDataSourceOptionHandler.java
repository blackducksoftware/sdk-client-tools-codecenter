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

import java.io.File;
import java.io.IOException;

import javax.activation.FileDataSource;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class FileDataSourceOptionHandler extends OneArgumentOptionHandler<FileDataSource> {

    public FileDataSourceOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super FileDataSource> setter) {
        super(parser, option, setter);
    }

    @Override
    protected FileDataSource parse(String argument) throws CmdLineException {
        File file = new File(argument);
        FileDataSource ds = new FileDataSource(file);

        // Ensure that the file exists and is readable
        try {
            ds.getInputStream().read();
        } catch (IOException e) {
            throw new CmdLineException(owner, e);
        }

        return ds;
    }

}
