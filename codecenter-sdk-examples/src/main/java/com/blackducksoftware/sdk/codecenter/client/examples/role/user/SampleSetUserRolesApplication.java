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
package com.blackducksoftware.sdk.codecenter.client.examples.role.user;

import java.util.Collection;
import java.util.List;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.role.data.ApplicationRoleAssignment;
import com.blackducksoftware.sdk.codecenter.role.data.UserRoleAssignment;

public class SampleSetUserRolesApplication extends SampleSetUserRoles {

    public static void main(String[] args) {
        new SampleSetUserRolesApplication(args).run();
    }

    @Argument(index = 4, required = true, metaVar = "applicationName", usage = "the name of the application for which to assign the roles")
    protected String applicationName;

    @Argument(index = 5, required = true, metaVar = "applicationVersion", usage = "the version of the application for which to assign the roles")
    protected String applicationVersion;

    @Argument(index = 6, required = true, multiValued = true, metaVar = "username", usage = "the username of a user to be assigned to the role")
    protected List<String> usernames;

    public SampleSetUserRolesApplication(String[] args) {
        super(args);
    }

    @Override
    protected Collection<String> getUsernames() {
        return usernames;
    }

    @Override
    protected UserRoleAssignment createRoleAssignment() {
        // Create the application role assignment bean
        ApplicationNameVersionToken nameVersionToken = new ApplicationNameVersionToken();
        nameVersionToken.setName(applicationName);
        nameVersionToken.setVersion(applicationVersion);

        ApplicationRoleAssignment assignment = new ApplicationRoleAssignment();
        assignment.setApplicationNameVersionToken(nameVersionToken);

        return assignment;
    }

    @Override
    protected String getRoleDescription(String roleName) {
        return "the role \"" + roleName + "\" for the application \"" + applicationName + "\" version \"" + applicationVersion + "\"";
    }

}
