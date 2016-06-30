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
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;
import com.blackducksoftware.sdk.codecenter.user.data.UserUpdate;

public class SampleUpdateUser extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleUpdateUser(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "oldUsername", usage = "the username of the user to update")
    protected String oldUsername;

    @Option(name = "-u", aliases = { "--username" }, usage = "the user's updated username")
    protected String newUsername;

    @Option(name = "--pass", aliases = { "--password" }, usage = "the user's updated password")
    protected String password;

    @Option(name = "-f", aliases = { "--first-name" }, usage = "the user's updated first name")
    protected String firstName;

    @Option(name = "-l", aliases = { "--last-name" }, usage = "the user's updated last name")
    protected String lastName;

    @Option(name = "-t", aliases = { "--title", "--job-title" }, usage = "the user's updated job title")
    protected String jobTitle;

    @Option(name = "-e", aliases = { "--email" }, usage = "the user's updated email address")
    protected String email;

    @Option(name = "--phone", usage = "the user's updated phone number")
    protected String phone;

    @Option(name = "--location", usage = "the user's updated location")
    protected String location;

    @Option(name = "-d", aliases = { "--dept", "--department" }, usage = "the user's updated department")
    protected String department;

    @Option(name = "--active", forbids = { "--inactive" }, usage = "make the user active")
    protected boolean isActive = false;

    @Option(name = "--inactive", forbids = { "--active" }, usage = "make the user inactive")
    protected boolean isInactive = false;

    public SampleUpdateUser(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the user create bean
        UserNameToken userNameToken = new UserNameToken();
        userNameToken.setName(oldUsername);

        UserUpdate user = new UserUpdate();
        user.setId(userNameToken);
        user.setName(newUsername);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJobTitle(jobTitle);
        user.setEmail(email);
        user.setPhone(phone);
        user.setLocation(location);
        user.setDepartment(department);
        user.setActive(isActive ? Boolean.TRUE : isInactive ? Boolean.FALSE : null);

        // Create the application
        proxy.getUserApi().updateUser(user);

        // Print success information
        System.out.println("Successfully updated user \"" + oldUsername + "\".");
    }

}
