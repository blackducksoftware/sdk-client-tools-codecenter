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

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.administration.data.ServerNameToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationIdToken;
import com.blackducksoftware.sdk.codecenter.application.data.ProjectNameToken;
import com.blackducksoftware.sdk.codecenter.approval.data.WorkflowNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

// TODO: test this, because I couldn't get my servers to sync properly
public class SampleImportProtexProject extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleImportProtexProject(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "serverName", usage = "the name (not the URL) of the Protex server to import from")
    protected String serverName;

    @Argument(index = 4, required = true, metaVar = "projectName", usage = "the name of the Protex project to import")
    protected String projectName;

    @Argument(index = 5, required = true, metaVar = "owner", usage = "the owner to assign to the application")
    protected String owner;

    @Argument(index = 6, required = true, metaVar = "ownerRole", usage = "the role of the owner (ex. \"Application Administrator\")")
    protected String ownerRole;

    @Argument(index = 7, required = true, metaVar = "workflowName", usage = "the name of the workflow to use in the application (ex. \"Parallel\", \"Serial\")")
    protected String workflowName;

    @Option(name = "-n", aliases = { "--no-submit", "--no-submit-components" }, usage = "don't submit component requests")
    protected boolean noSubmit = false;

    public SampleImportProtexProject(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create all the tokens
        ServerNameToken server = new ServerNameToken();
        server.setName(serverName);

        ProjectNameToken project = new ProjectNameToken();
        project.setName(projectName);
        project.setServerId(server);

        UserNameToken owner = new UserNameToken();
        owner.setName(this.owner);

        RoleNameToken role = new RoleNameToken();
        role.setName(ownerRole);

        WorkflowNameToken workflow = new WorkflowNameToken();
        workflow.setName(workflowName);

        // Import the project
        ApplicationIdToken appId = proxy.getApplicationApi().importProtexProject(project, owner, role, workflow, !noSubmit);

        // Print success information
        System.out.println("Successfully imported project \"" + projectName + "\" from Protex with application ID " + appId.getId() + ".");
    }

}
