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

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.approval.data.WebResourceNameToken;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.request.data.RequestApplicationComponentToken;
import com.blackducksoftware.sdk.codecenter.request.data.RequestWebResourceNameOrIdToken;

public class SampleCompleteWebResourceTask extends SampleCompleteApprovalOrTask {

    public static void main(String[] args) {
        new SampleCompleteWebResourceTask(args).run();
    }

    @Argument(index = 7, required = true, metaVar = "taskName", usage = "the name of the web resource task to complete")
    protected String taskName;

    @Argument(index = 8, required = true, metaVar = "transitionName", usage = "the name of the task completion transition to follow (ex: Done)")
    protected String transitionName;

    @Argument(index = 9, required = false, metaVar = "comment", usage = "a comment for the task completion")
    protected String comment;

    public SampleCompleteWebResourceTask(String[] args) {
        super(args);
    }

    @Override
    protected void run(CodeCenterServerProxy proxy, RequestApplicationComponentToken request) throws Exception {
        // Create the web resource task token
        WebResourceNameToken webResource = new WebResourceNameToken();
        webResource.setName(taskName);

        RequestWebResourceNameOrIdToken requestWebResource = new RequestWebResourceNameOrIdToken();
        requestWebResource.setRequestId(request);
        requestWebResource.setWorkflowWebResourceDefinitionNameOrIdToken(webResource);

        // Complete the web resource task
        proxy.getRequestApi().completeWebResourceTask(requestWebResource, transitionName, comment);
    }

    @Override
    protected String getActedUpon() {
        return "web resource task \"" + taskName + "\" with transition \"" + transitionName + "\"";
    }

}
