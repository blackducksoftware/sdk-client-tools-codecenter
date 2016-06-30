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
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.PermissionEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.RoleTypeEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.role.data.PermissionEnum;
import com.blackducksoftware.sdk.codecenter.role.data.RoleCreate;
import com.blackducksoftware.sdk.codecenter.role.data.RoleIdToken;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.sdk.codecenter.role.data.RoleTypeEnum;

public class SampleCreateRole extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateRole(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "roleName", usage = "the name of the role to create")
    protected String roleName;

    @Argument(index = 4, required = true, handler = RoleTypeEnumOptionHandler.class, metaVar = "roleType", usage = "the role type")
    protected RoleTypeEnum roleType;

    @Argument(index = 5, required = false, multiValued = true, handler = PermissionEnumOptionHandler.class, metaVar = "permission",
            usage = "a permission to grant to the role")
    protected List<PermissionEnum> permissions;

    @Option(name = "-d", aliases = { "--description" }, usage = "the description of the role")
    protected String description;

    public SampleCreateRole(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the role create bean
        RoleNameToken roleNameToken = new RoleNameToken();
        roleNameToken.setName(roleName);

        Set<PermissionEnum> permissionsSet = EnumSet.copyOf(permissions);

        RoleCreate role = new RoleCreate();
        role.setName(roleNameToken);
        role.setDescription(description);
        role.setRoleType(roleType);
        role.getPermissions().addAll(permissionsSet);

        // Create the role
        RoleIdToken roleId = proxy.getRoleApi().createRole(role);

        // Print success information
        System.out.println("Successfully created " + BDEnumOptionHandler.enumToString(roleType) + " role \"" + roleName + "\" with " + permissionsSet.size()
                + " permission" + (permissionsSet.size() != 1 ? "s" : "") + " and role ID " + roleId.getId() + ".");
    }

}
