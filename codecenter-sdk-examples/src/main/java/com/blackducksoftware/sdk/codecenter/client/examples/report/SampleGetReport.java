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
package com.blackducksoftware.sdk.codecenter.client.examples.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.FileTypePathOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.report.data.ExportFormatEnum;
import com.blackducksoftware.sdk.codecenter.report.data.ReportNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.report.data.ReportNameToken;
import com.blackducksoftware.sdk.codecenter.report.data.ReportParams;

public abstract class SampleGetReport extends BDCodeCenterSample {

    @Argument(index = 3, required = true, handler = ReportPathOptionHandler.class, metaVar = "saveLocation",
            usage = "the local path to save the report to (including the filename and extension, pdf or xls)")
    protected Path saveLocation;

    public SampleGetReport(String[] args) {
        super(args);
    }

    @Override
    public final void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the report name token
        ReportNameToken reportNameToken = new ReportNameToken();
        reportNameToken.setReportName(getReportName());

        // Create the list of parameters
        List<ReportParams> params = new ArrayList<>();

        Map<String, String> paramsMap = getParams();
        if (paramsMap != null) {
            for (Entry<String, String> paramEntry : paramsMap.entrySet()) {
                ReportParams param = new ReportParams();
                param.setKey(paramEntry.getKey());
                param.setValue(paramEntry.getValue());

                params.add(param);
            }
        }

        // Parse the extension into a format
        ExportFormatEnum format;

        String extension = FileTypePathOptionHandler.getExtension(saveLocation);
        if (extension.equalsIgnoreCase("pdf")) {
            format = ExportFormatEnum.PDF;
        } else if (extension.equalsIgnoreCase("xls")) {
            format = ExportFormatEnum.EXCEL;
        } else {
            throw new AssertionError("\"" + extension + "\" is not a valid report file type");
        }

        // Retrieve the report content
        byte[] data = getReport(proxy, reportNameToken, params, format);

        // Write the report content
        try (OutputStream outputStream = new FileOutputStream(new File(saveLocation.toString()))) {
            outputStream.write(data);
        }

        // Print success information
        System.out.println("Successfully retrieved the " + getReportNamePretty() + " and saved it to \"" + saveLocation + "\".");
    }

    protected abstract String getReportName();

    protected abstract Map<String, String> getParams();

    protected abstract byte[] getReport(CodeCenterServerProxy proxy, ReportNameOrIdToken reportNameOrIdToken, List<ReportParams> reportParams,
            ExportFormatEnum exportFormatEnum) throws Exception;

    protected abstract String getReportNamePretty();

    public static class ReportPathOptionHandler extends FileTypePathOptionHandler {

        public ReportPathOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Path> setter) {
            super(parser, option, setter, "pdf", "xls");
        }

    }

}
