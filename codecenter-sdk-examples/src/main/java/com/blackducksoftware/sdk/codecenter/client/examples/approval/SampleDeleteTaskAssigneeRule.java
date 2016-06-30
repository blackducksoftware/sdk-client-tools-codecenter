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
package com.blackducksoftware.sdk.codecenter.client.examples.approval;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.approval.data.TaskNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public class SampleDeleteTaskAssigneeRule extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleDeleteTaskAssigneeRule(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "taskName", usage = "the name of the task to delete the assignee rule for")
    protected String taskName;

    public SampleDeleteTaskAssigneeRule(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the task tokens
        TaskNameToken taskToken = new TaskNameToken();
        taskToken.setName(taskName);

        // Delete the task assignee rule
        proxy.getApprovalApi().deleteTaskAssigneeRule(taskToken);

        // Print success information
        System.out.println("Successfully deleted the rule for assigning task \"" + taskName + "\".");
    }

}
