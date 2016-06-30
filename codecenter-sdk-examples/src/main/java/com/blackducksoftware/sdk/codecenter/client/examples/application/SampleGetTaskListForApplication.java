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
package com.blackducksoftware.sdk.codecenter.client.examples.application;

import java.util.List;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.SamplePrinter;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.TaskInstance;
import com.blackducksoftware.sdk.codecenter.request.data.TaskColumn;
import com.blackducksoftware.sdk.codecenter.request.data.TaskInstancePageFilter;

public class SampleGetTaskListForApplication extends SamplePrinter {

    public static void main(String[] args) {
        new SampleGetTaskListForApplication(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to get the task list for")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application to get the task list for")
    protected String applicationVersion;

    public SampleGetTaskListForApplication(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the application token and page filter
        ApplicationNameVersionToken appNameVersion = new ApplicationNameVersionToken();
        appNameVersion.setName(applicationName);
        appNameVersion.setVersion(applicationVersion);

        TaskInstancePageFilter pageFilter = new TaskInstancePageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(Integer.MAX_VALUE);
        pageFilter.setSortAscending(false);
        pageFilter.setSortedColumn(TaskColumn.CREATION_DATE);

        // Get the task list
        List<TaskInstance> tasks = proxy.getApplicationApi().getTaskListForApplication(appNameVersion, pageFilter);

        // Print success information
        System.out.println("Successfully got the task list for application \"" + applicationName + "\" version \"" + applicationVersion + "\".");
        System.out.println("Found " + tasks.size() + " task" + (tasks.size() != 1 ? "s" : "") + (tasks.size() != 0 ? ":" : "."));

        for (TaskInstance task : tasks) {
            System.out.println();
            print("Component Name", task.getComponentName().getName());
            print("Component Version", task.getComponentName().getVersion());
            if (task.getApprovalDefinitionName() != null) {
                print("Approval Name", task.getApprovalDefinitionName().getName());
            }
            if (task.getTaskName() != null) {
                print("Task Name", task.getTaskName().getName());
            }
            if (task.getBoardName() != null) {
                print("Board/Task Group", task.getBoardName().getName());
            }
            print("Date Created", task.getCreationDate());
            print("Days Passed", task.getDaysPassed());
            System.out.println();
        }
    }

}
