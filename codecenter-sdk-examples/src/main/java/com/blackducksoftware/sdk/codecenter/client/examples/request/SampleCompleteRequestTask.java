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
package com.blackducksoftware.sdk.codecenter.client.examples.request;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.approval.data.TaskNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.MultiMapOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.request.data.RequestApplicationComponentToken;
import com.blackducksoftware.sdk.codecenter.request.data.RequestTaskNameOrIdToken;

public class SampleCompleteRequestTask extends SampleCompleteApprovalOrTask {

    public static void main(String[] args) {
        new SampleCompleteRequestTask(args).run();
    }

    @Argument(index = 7, required = true, metaVar = "taskName", usage = "the name of the task to complete")
    protected String taskName;

    @Argument(index = 8, required = true, metaVar = "transitionName", usage = "the name of the task completion transition to follow (ex: Done)")
    protected String transitionName;

    @Argument(index = 9, required = false, multiValued = true, handler = MultiMapOptionHandler.class, metaVar = "<attribute>=<value>",
            usage = "set the value of an attribute for the task; may repeat a name to set multiple values")
    protected Map<String, Collection<String>> attributeMap;

    public SampleCompleteRequestTask(String[] args) {
        super(args);
    }

    @Override
    protected void run(CodeCenterServerProxy proxy, RequestApplicationComponentToken request) throws Exception {
        // Parse the attribute values
        List<AttributeValue> attributes = parseAttributes(attributeMap);

        // Create the request task token
        TaskNameToken task = new TaskNameToken();
        task.setName(taskName);

        RequestTaskNameOrIdToken requestTask = new RequestTaskNameOrIdToken();
        requestTask.setRequestId(request);
        requestTask.setTaskId(task);

        // Complete the request approval
        proxy.getRequestApi().completeRequestTask(requestTask, attributes, transitionName);
    }

    @Override
    protected String getActedUpon() {
        return "task \"" + taskName + "\" with transition \"" + transitionName + "\"";
    }

}
