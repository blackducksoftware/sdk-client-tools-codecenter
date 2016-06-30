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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.PathOptionHandler;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.Date_yyyy_MM_dd_OptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public class SampleGetAuditReport extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleGetAuditReport(args).run();
    }

    @Argument(index = 3, required = true, handler = PathOptionHandler.class, metaVar = "saveLocation",
            usage = "the local path to save the report to (including the filename and extension, csv)")
    protected Path saveLocation;

    @Argument(index = 4, required = true, multiValued = true, metaVar = "username", usage = "the name of a user to collect info about")
    protected List<String> usernames;

    @Option(name = "--from", handler = Date_yyyy_MM_dd_OptionHandler.class, usage = "the date from which to start collecting info (default: 1970/01/01)")
    protected Date fromDateInput;

    @Option(name = "--to", handler = Date_yyyy_MM_dd_OptionHandler.class, usage = "the date through which to collect info (default: today)")
    protected Date toDateInput;

    public SampleGetAuditReport(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the user name tokens
        Set<String> usernamesSet = new HashSet<>(usernames);
        List<UserNameOrIdToken> users = new ArrayList<>(usernamesSet.size());

        for (String username : usernamesSet) {
            UserNameToken userNameToken = new UserNameToken();
            userNameToken.setName(username);

            users.add(userNameToken);
        }

        // Handle unspecified input dates
        Date fromDate = fromDateInput;
        if (fromDate == null) {
            fromDate = new Date(0);
        }

        Date toDate = toDateInput;
        if (toDate == null) {
            toDate = Calendar.getInstance().getTime();
        }

        // Retrieve the report content
        byte[] data = proxy.getReportApi().getAdminAuditReport(users, fromDate, toDate);

        // Write the report content
        try (OutputStream outputStream = new FileOutputStream(new File(saveLocation.toString()))) {
            outputStream.write(data);
        }

        // Print success information
        System.out.println(
                "Successfully retrieved an audit report for " + (usernamesSet.size() != 1 ? usernamesSet.size() + " users" : "\"" + usernames.get(0) + "\"")
                        + (fromDateInput != null ? " from " + Date_yyyy_MM_dd_OptionHandler.DATE_FORMAT.format(fromDateInput) : "")
                        + (toDateInput != null ? " through " + Date_yyyy_MM_dd_OptionHandler.DATE_FORMAT.format(toDateInput) : "") + " and saved it to \""
                        + saveLocation + "\".");
    }

}
