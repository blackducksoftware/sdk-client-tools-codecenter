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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.sdk.codecenter.role.data.UserRoleAssignment;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public abstract class SampleSetUserRoles extends BDCodeCenterSample {

    public SampleSetUserRoles(String[] args) {
        super(args);
    }

    @Argument(index = 3, required = true, metaVar = "roleName", usage = "the name of the role to assign to users")
    protected String roleName;

    @Override
    public final void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the role name token
        RoleNameToken roleNameToken = new RoleNameToken();
        roleNameToken.setName(roleName);

        // Create the user role assignments
        Set<String> usernamesSet = new HashSet<>(getUsernames());
        List<UserRoleAssignment> assignments = new ArrayList<>(usernamesSet.size());

        for (String username : usernamesSet) {
            UserNameToken userNameToken = new UserNameToken();
            userNameToken.setName(username);

            UserRoleAssignment assignment = createRoleAssignment();
            assignment.setRoleNameToken(roleNameToken);
            assignment.setUserNameToken(userNameToken);

            assignments.add(assignment);
        }

        // Assign the role to the users
        proxy.getRoleApi().setUserRoles(assignments);

        // Print success information
        System.out.println("Successfully assigned " + (assignments.size() != 1 ? assignments.size() + " user" + (assignments.size() != 1 ? "s" : "")
                : "\"" + assignments.get(0).getUserNameToken().getName() + "\"") + " to " + getRoleDescription(roleName) + ".");
    }

    protected abstract Collection<String> getUsernames();

    protected abstract UserRoleAssignment createRoleAssignment();

    protected abstract String getRoleDescription(String roleName);

}
