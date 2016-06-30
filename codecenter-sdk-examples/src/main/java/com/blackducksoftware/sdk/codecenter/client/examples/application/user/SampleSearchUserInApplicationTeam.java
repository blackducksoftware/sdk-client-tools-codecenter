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
package com.blackducksoftware.sdk.codecenter.client.examples.application.user;

import java.util.Collection;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.springframework.util.StringUtils;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.SampleSearch;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.UserRolePageFilter;
import com.blackducksoftware.sdk.codecenter.role.data.ApplicationRoleAssignment;
import com.blackducksoftware.sdk.codecenter.user.data.User;

public class SampleSearchUserInApplicationTeam extends SampleSearch<ApplicationRoleAssignment> {

    public static void main(String[] args) {
        new SampleSearchUserInApplicationTeam(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to search for users in")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the application version")
    protected String applicationVersion;

    @Argument(index = 5, required = false, metaVar = "searchString", usage = "part of the username to search for; omit to retrieve all")
    protected String searchString;

    public SampleSearchUserInApplicationTeam(String[] args) {
        super(args);
    }

    @Override
    protected Collection<ApplicationRoleAssignment> search(CodeCenterServerProxy proxy) throws Exception {
        // We'll need this later; get the log statement out of the way now
        proxy.getUserApi();

        // Create the user page filter and application token
        UserRolePageFilter pageFilter = new UserRolePageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(Integer.MAX_VALUE);

        ApplicationNameVersionToken appToken = new ApplicationNameVersionToken();
        appToken.setName(applicationName);
        appToken.setVersion(applicationVersion);

        // Search for users in the application team
        List<ApplicationRoleAssignment> assignments = proxy.getApplicationApi().searchUserInApplicationTeam(appToken, searchString != null ? searchString : "",
                pageFilter);
        return assignments;
    }

    @Override
    protected String getSearchExpression() {
        return "users" + (searchString != null ? " matching \"" + searchString + "\"" : "") + " in the team for application \"" + applicationName
                + "\" version \"" + applicationVersion + "\"";
    }

    @Override
    protected void print(ApplicationRoleAssignment assignment, CodeCenterServerProxy proxy) throws Exception {
        // Retrieve the user's details
        User user = proxy.getUserApi().getUser(assignment.getUserIdToken());

        // Print user assignment details
        boolean hasFirstName = StringUtils.hasText(user.getFirstName());
        boolean hasLastName = StringUtils.hasText(user.getLastName());
        System.out.println((hasFirstName ? (hasLastName ? user.getFirstName() + " " + user.getLastName() : user.getFirstName())
                : "?" + (hasLastName ? " " + user.getLastName() : "")) + " (" + user.getName().getName() + "): " + assignment.getRoleNameToken().getName());
    }

}
