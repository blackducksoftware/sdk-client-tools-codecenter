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

import java.nio.file.Path;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.PathOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class FileTypePathOptionHandler extends PathOptionHandler {

    public static String getExtension(Path path) {
        String fileName = path.getFileName().toString();
        int i = fileName.lastIndexOf('.');
        return i >= 0 ? fileName.substring(i + 1) : "";
    }

    private final String[] validExtensions;

    protected FileTypePathOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Path> setter, String... validExtensions) {
        super(parser, option, setter);
        this.validExtensions = validExtensions;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Path parse(String argument) throws CmdLineException {
        Path path = super.parse(argument);
        String extension = getExtension(path);

        for (String validExtension : validExtensions) {
            if (extension.equalsIgnoreCase(validExtension)) {
                return path;
            }
        }

        throw new CmdLineException(owner, "\"" + extension + "\" is not a valid file type for \"" + option.toString() + "\"");
    }

}
