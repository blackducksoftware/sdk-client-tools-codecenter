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

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationCreate;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationIdToken;
import com.blackducksoftware.sdk.codecenter.approval.data.WorkflowNameToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public class SampleCreateApplication extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateApplication(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to create")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application to create")
    protected String applicationVersion;

    @Argument(index = 5, required = true, metaVar = "owner", usage = "the owner to assign to the application")
    protected String owner;

    @Argument(index = 6, required = true, metaVar = "ownerRole", usage = "the role of the owner (ex. \"Application Administrator\")")
    protected String ownerRole;

    @Argument(index = 7, required = true, metaVar = "workflowName", usage = "the name of the workflow to use in the application (ex. \"Parallel\", \"Serial\")")
    protected String workflowName;

    @Argument(index = 8, required = false, metaVar = "description", usage = "the description of the application")
    protected String description;

    public SampleCreateApplication(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the application create bean
        ApplicationCreate appBean = new ApplicationCreate();
        appBean.setName(applicationName);
        appBean.setVersion(applicationVersion);
        appBean.setDescription(description);

        UserNameToken owner = new UserNameToken();
        owner.setName(this.owner);
        appBean.setOwnerId(owner);

        RoleNameToken role = new RoleNameToken();
        role.setName(ownerRole);
        appBean.setOwnerRoleId(role);

        WorkflowNameToken workflow = new WorkflowNameToken();
        workflow.setName(workflowName);
        appBean.setWorkflowId(workflow);

        // Set the required attribute values
        List<AttributeValue> attrs = new ArrayList<>();

        AttributeValue typeAttr = new AttributeValue();
        AttributeNameToken typeAttrName = new AttributeNameToken();
        typeAttrName.setName("type");
        typeAttr.setAttributeId(typeAttrName);
        typeAttr.getValues().add("Internal");
        attrs.add(typeAttr);

        AttributeValue urlAttr = new AttributeValue();
        AttributeNameToken urlAttrName = new AttributeNameToken();
        urlAttrName.setName("projecturl");
        urlAttr.setAttributeId(urlAttrName);
        urlAttr.getValues().add("http://www.example.com");
        attrs.add(urlAttr);

        appBean.getAttributeValues().addAll(attrs);

        // Create the application
        ApplicationIdToken appId = proxy.getApplicationApi().createApplication(appBean);

        // Print success information
        System.out.println("Successfully created application \"" + applicationName + "\" version \"" + applicationVersion + "\" with application ID "
                + appId.getId() + ".");
    }

}
