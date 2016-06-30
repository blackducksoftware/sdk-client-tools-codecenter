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
package com.blackducksoftware.sdk.codecenter.client.examples.role;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;

public class SampleDeleteRole extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleDeleteRole(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "roleName", usage = "the name of the role to delete")
    protected String roleName;

    public SampleDeleteRole(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the role name token
        RoleNameToken roleNameToken = new RoleNameToken();
        roleNameToken.setName(roleName);

        // Delete the role
        proxy.getRoleApi().deleteRole(roleNameToken);

        // Print success information
        System.out.println("Successfully deleted role \"" + roleName + "\".");
    }

}
