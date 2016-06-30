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

import com.blackducksoftware.sdk.codecenter.approval.data.AssigneeRule;
import com.blackducksoftware.sdk.codecenter.approval.data.Rule;
import com.blackducksoftware.sdk.codecenter.approval.data.TaskAssigneeRule;
import com.blackducksoftware.sdk.codecenter.approval.data.TaskNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.RuleOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public class SampleConfigureTaskAssigneeRule extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleConfigureTaskAssigneeRule(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "taskName", usage = "the name of the task to configure an assignee rule for")
    protected String taskName;

    @Argument(index = 4, required = true, metaVar = "username", usage = "the name of the user to attach to the assignee rule")
    protected String username;

    @Argument(index = 5, required = true, handler = RuleOptionHandler.class, metaVar = "rule", usage = "the rule to set")
    protected Rule rule;

    public SampleConfigureTaskAssigneeRule(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the task and user tokens
        TaskNameToken taskToken = new TaskNameToken();
        taskToken.setName(taskName);

        UserNameToken userToken = new UserNameToken();
        userToken.setName(username);

        // Create the attribute group update bean
        AssigneeRule assigneeRule = new AssigneeRule();
        assigneeRule.setAssignee(userToken);
        assigneeRule.setRule(rule);

        TaskAssigneeRule taskAssigneeRule = new TaskAssigneeRule();
        taskAssigneeRule.setTaskId(taskToken);
        taskAssigneeRule.getAssigneeRules().add(assigneeRule);

        // Add the task assignee rule
        proxy.getApprovalApi().configureTaskAssigneeRule(taskAssigneeRule);

        // Print success information
        System.out.println("Successfully created a rule for assigning task \"" + taskName + "\" to user \"" + username + "\".");
    }

}
