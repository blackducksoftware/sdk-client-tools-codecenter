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
package com.blackducksoftware.sdk.codecenter.client.examples.user;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public class SampleDeleteUser extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleDeleteUser(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "username", usage = "the username of the user to delete")
    protected String username;

    public SampleDeleteUser(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the username token
        UserNameToken userNameToken = new UserNameToken();
        userNameToken.setName(username);

        // Create the application
        proxy.getUserApi().deleteUser(userNameToken);

        // Print success information
        System.out.println("Successfully deleted user \"" + username + "\".");
    }

}
