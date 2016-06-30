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

import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.report.data.ExportFormatEnum;
import com.blackducksoftware.sdk.codecenter.report.data.ReportNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.report.data.ReportParams;

public class SampleGetMainReport extends SampleGetReport {

    public static void main(String[] args) {
        new SampleGetMainReport(args).run();
    }

    @Argument(index = 4, required = true, metaVar = "reportName",
            usage = "the name of the report to retrieve (ex: \"Components Pending Approval by Approval Board\")")
    protected String reportName;

    @Argument(index = 5, required = false, metaVar = "params", usage = "extra report parameters (ex: boardId=\"License Board\")")
    protected Map<String, String> params;

    public SampleGetMainReport(String[] args) {
        super(args);
    }

    @Override
    protected String getReportName() {
        return reportName;
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }

    @Override
    protected byte[] getReport(CodeCenterServerProxy proxy, ReportNameOrIdToken reportNameOrIdToken, List<ReportParams> reportParams,
            ExportFormatEnum exportFormatEnum) throws Exception {
        // Retrieve the report contents
        return proxy.getReportApi().getReport(reportNameOrIdToken, reportParams, exportFormatEnum);
    }

    @Override
    protected String getReportNamePretty() {
        return "\"" + reportName + "\" report";
    }

}
