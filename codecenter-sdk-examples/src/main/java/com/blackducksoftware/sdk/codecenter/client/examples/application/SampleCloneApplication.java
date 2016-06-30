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

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationClone;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationIdToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.application.data.InheritApprovalsTypeEnum;
import com.blackducksoftware.sdk.codecenter.approval.data.WorkflowNameToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AbstractAttribute;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public class SampleCloneApplication extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCloneApplication(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to clone")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application to clone")
    protected String applicationVersion;

    @Argument(index = 5, required = true, metaVar = "cloneApplicationName", usage = "the name of the application clone to create")
    protected String cloneApplicationName;

    @Argument(index = 6, required = true, metaVar = "cloneApplicationVersion", usage = "the version of the application clone to create")
    protected String cloneApplicationVersion;

    @Argument(index = 7, required = true, metaVar = "workflowName",
            usage = "the name of the workflow to use in the application clone (ex. \"Parallel\", \"Serial\")")
    protected String workflowName;

    @Option(name = "--submit-requests", usage = "submit the cloned requests")
    protected boolean submitRequests;

    @Option(name = "--inherit-approvals", depends = { "--submit-requests" }, usage = "inherit approvals")
    protected boolean inheritApprovals;

    @Option(name = "--inherit-workflow-version", depends = { "--submit-requests" }, usage = "use the existing workflow version rather than the latest")
    protected boolean inheritWorkflowVersion;

    @Option(name = "--inherit-attachments", usage = "inherit attachments")
    protected boolean inheritAttachments;

    @Option(name = "--inherit-vuln", usage = "inherit vunlerability review information")
    protected boolean inheritVuln;

    public SampleCloneApplication(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the application clone bean
        ApplicationIdToken cloneAppId = null;

        ApplicationNameVersionToken appNameVersionToken = new ApplicationNameVersionToken();
        appNameVersionToken.setName(applicationName);
        appNameVersionToken.setVersion(applicationVersion);
        WorkflowNameToken wfToken = new WorkflowNameToken();
        wfToken.setName(workflowName);
        RoleNameToken roleToken = new RoleNameToken();
        roleToken.setName("Application Developer");
        UserNameToken ownerToken = new UserNameToken();
        ownerToken.setName("tcook");

        ApplicationClone appClone = new ApplicationClone();
        appClone.setApplicationId(appNameVersionToken);
        appClone.setDescription("Sample client to create clone application");
        appClone.setInheritApprovalsType(InheritApprovalsTypeEnum.DO_NOT_INHERIT);
        appClone.setName(cloneApplicationName);
        appClone.setVersion(cloneApplicationVersion);
        appClone.setUseProtexstatus(true);
        appClone.setInheritAttachments(inheritAttachments);
        appClone.setOwnerId(ownerToken);
        appClone.setOwnerRoleId(roleToken);
        appClone.setWorkflowId(wfToken);
        appClone.setInheritVulnInfo(Boolean.valueOf(inheritVuln));
        appClone.setSubmitRequests(Boolean.valueOf(submitRequests));
        appClone.setInheritApprovalsType(inheritApprovals ? inheritWorkflowVersion ? InheritApprovalsTypeEnum.INHERIT_EXISTING_REQUEST_WORKFLOW_VERSION
                : InheritApprovalsTypeEnum.INHERIT_LATEST_WORKFLOW_VERSION : InheritApprovalsTypeEnum.DO_NOT_INHERIT);

        // Set the required attribue values
        List<AttributeValue> attributesValues = new ArrayList<>();

        AttributeValue typeAttr = new AttributeValue();
        AttributeNameToken typeAttrName = new AttributeNameToken();
        typeAttrName.setName("type");
        AbstractAttribute attribute1 = proxy.getAttributeApi().getAttribute(typeAttrName);
        typeAttr.setAttributeId(attribute1.getId());
        typeAttr.getValues().add("Internal");
        attributesValues.add(typeAttr);

        AttributeValue urlAttr = new AttributeValue();
        AttributeNameToken urlAttrName = new AttributeNameToken();
        urlAttrName.setName("projecturl");
        AbstractAttribute attribute2 = proxy.getAttributeApi().getAttribute(urlAttrName);
        urlAttr.setAttributeId(attribute2.getId());
        urlAttr.getValues().add("http://welcome.com");
        attributesValues.add(urlAttr);

        appClone.getAttributeValues().addAll(attributesValues);

        // Clone the application
        cloneAppId = proxy.getApplicationApi().clone(appClone);

        // Print success information
        System.out.println("Successfully cloned application \"" + applicationName + "\" version \"" + applicationVersion + "\" to application \""
                + cloneApplicationName + "\" version \"" + cloneApplicationVersion + "\" with application ID " + cloneAppId.getId() + ".");
    }

}
