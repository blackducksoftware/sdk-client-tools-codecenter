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

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public class SampleRemoveUserInApplicationTeam extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleRemoveUserInApplicationTeam(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to remove the user from")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the application version")
    protected String applicationVersion;

    @Argument(index = 5, required = true, metaVar = "username", usage = "the username of the user to remove a role from in the application team")
    protected String username;

    @Argument(index = 6, required = true, metaVar = "roleName", usage = "the name of the role to remove from the user (ex. \"Application Administrator\")")
    protected String roleName;

    public SampleRemoveUserInApplicationTeam(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the application and role tokens
        ApplicationNameVersionToken appNameVersionToken = new ApplicationNameVersionToken();
        appNameVersionToken.setName(applicationName);
        appNameVersionToken.setVersion(applicationVersion);

        UserNameToken usernameToken = new UserNameToken();
        usernameToken.setName(username);

        RoleNameToken roleToken = new RoleNameToken();
        roleToken.setName(roleName);

        // Remove the user role from the application team
        proxy.getApplicationApi().removeUserInApplicationTeam(appNameVersionToken, usernameToken, roleToken);

        // Print success information
        System.out.println("Successfully removed role \"" + roleName + "\" from user \"" + username + "\" in application \"" + applicationName + "\" version \""
                + applicationVersion + "\".");
    }

}
