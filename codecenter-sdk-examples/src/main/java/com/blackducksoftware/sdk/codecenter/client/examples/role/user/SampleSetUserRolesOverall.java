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

import com.blackducksoftware.sdk.codecenter.role.data.UserOverAllRoleAssignment;
import com.blackducksoftware.sdk.codecenter.role.data.UserRoleAssignment;

public class SampleSetUserRolesOverall extends SampleSetUserRoles {

    public static void main(String[] args) {
        new SampleSetUserRolesOverall(args).run();
    }

    @Argument(index = 4, required = true, multiValued = true, metaVar = "username", usage = "the username of a user to be assigned to the role")
    protected List<String> usernames;

    public SampleSetUserRolesOverall(String[] args) {
        super(args);
    }

    @Override
    protected Collection<String> getUsernames() {
        return usernames;
    }

    @Override
    protected UserRoleAssignment createRoleAssignment() {
        // Create the overall role assignment bean
        UserRoleAssignment assignment = new UserOverAllRoleAssignment();

        return assignment;
    }

    @Override
    protected String getRoleDescription(String roleName) {
        return "the overall role \"" + roleName + "\"";
    }

}
