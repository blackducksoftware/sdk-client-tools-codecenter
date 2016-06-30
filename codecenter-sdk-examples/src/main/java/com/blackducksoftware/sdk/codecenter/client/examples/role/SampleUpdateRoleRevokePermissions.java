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

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.PermissionEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.role.data.PermissionEnum;
import com.blackducksoftware.sdk.codecenter.role.data.Role;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.sdk.codecenter.role.data.RoleUpdate;

public class SampleUpdateRoleRevokePermissions extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleUpdateRoleRevokePermissions(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "roleName", usage = "the name of the role to update")
    protected String roleName;

    @Argument(index = 4, required = true, multiValued = true, handler = PermissionEnumOptionHandler.class, metaVar = "permission",
            usage = "a permission to revoke from the role")
    protected List<PermissionEnum> permissions;

    public SampleUpdateRoleRevokePermissions(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the role name token
        RoleNameToken roleNameToken = new RoleNameToken();
        roleNameToken.setName(roleName);

        // Get the original role bean
        Role originalRole = proxy.getRoleApi().getRole(roleNameToken);

        // Create the role update bean
        Set<PermissionEnum> permissionsDelta = EnumSet.copyOf(permissions);
        permissionsDelta.retainAll(originalRole.getPermissions());

        RoleUpdate role = new RoleUpdate();
        role.setName(roleNameToken);
        role.getPermissions().addAll(originalRole.getPermissions());
        role.getPermissions().removeAll(permissionsDelta);

        // Update the role
        proxy.getRoleApi().updateRole(role);

        // Print success information
        System.out.println((permissionsDelta.isEmpty() ? "Revoked " : "Successfully revoked ")
                + (permissionsDelta.size() != 1 ? permissionsDelta.size() + " existing permissions"
                        : "the \"" + PermissionEnumOptionHandler.enumToString(permissionsDelta.iterator().next()) + "\" permission")
                + " from role \"" + roleName + "\".");
    }

}
