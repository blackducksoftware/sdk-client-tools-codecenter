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
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.MultiMapOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.request.data.RequestApplicationComponentToken;
import com.blackducksoftware.sdk.codecenter.request.data.RequestApprovalActionEnum;
import com.blackducksoftware.sdk.codecenter.request.data.RequestApprovalNameOrIdToken;

public class SampleCompleteRequestApproval extends SampleCompleteApprovalOrTask {

    public static void main(String[] args) {
        new SampleCompleteRequestApproval(args).run();
    }

    @Argument(index = 7, required = true, metaVar = "approvalName", usage = "the name of the approval to complete (ex: Security)")
    protected String approvalName;

    @Argument(index = 8, required = false, multiValued = true, handler = MultiMapOptionHandler.class, metaVar = "<attribute>=<value>",
            usage = "set the value of an attribute for the approval; may repeat a name to set multiple values (ex: passbasicsecurity=Yes)")
    protected Map<String, Collection<String>> attributeMap;

    @Option(name = "-r", aliases = { "--reject" }, usage = "reject the approval")
    protected boolean reject = false;

    public SampleCompleteRequestApproval(String[] args) {
        super(args);
    }

    @Override
    protected void run(CodeCenterServerProxy proxy, RequestApplicationComponentToken request) throws Exception {
        // Parse the attribute values
        List<AttributeValue> attributes = parseAttributes(attributeMap);

        // Create the request approval token
        ApprovalNameToken approval = new ApprovalNameToken();
        approval.setName(approvalName);

        RequestApprovalNameOrIdToken requestApproval = new RequestApprovalNameOrIdToken();
        requestApproval.setRequestId(request);
        requestApproval.setApprovalId(approval);

        // Complete the request approval
        proxy.getRequestApi().completeRequestApproval(requestApproval, attributes,
                reject ? RequestApprovalActionEnum.REJECTED : RequestApprovalActionEnum.APPROVED);
    }

    @Override
    protected String getAction() {
        return reject ? "rejected" : "approved";
    }

    @Override
    protected String getActedUpon() {
        return "approval \"" + approvalName + "\"";
    }

}
