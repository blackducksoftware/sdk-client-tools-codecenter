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
import com.blackducksoftware.sdk.codecenter.user.data.UserCreate;
import com.blackducksoftware.sdk.codecenter.user.data.UserIdToken;

public class SampleCreateUser extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateUser(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "username", usage = "the username of the user to create")
    protected String username;

    @Argument(index = 4, required = true, metaVar = "password", usage = "the password of the user to create")
    protected String password;

    @Option(name = "-f", aliases = { "--first-name" }, usage = "the user's first name")
    protected String firstName;

    @Option(name = "-l", aliases = { "--last-name" }, usage = "the user's last name")
    protected String lastName;

    @Option(name = "-t", aliases = { "--title", "--job-title" }, usage = "the user's job title")
    protected String jobTitle;

    @Option(name = "-e", aliases = { "--email" }, usage = "the user's email address")
    protected String email;

    @Option(name = "-p", aliases = { "--phone" }, usage = "the user's phone number")
    protected String phone;

    @Option(name = "--location", usage = "the user's location")
    protected String location;

    @Option(name = "-d", aliases = { "--dept", "--department" }, usage = "the user's department")
    protected String department;

    @Option(name = "--active", forbids = { "--inactive" }, usage = "make the user active")
    protected boolean isActive = true;

    @Option(name = "--inactive", forbids = { "--active" }, usage = "make the user inactive")
    protected boolean isInactive = false;

    public SampleCreateUser(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the user create bean
        UserCreate user = new UserCreate();
        user.setName(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJobTitle(jobTitle);
        user.setEmail(email);
        user.setPhone(phone);
        user.setLocation(location);
        user.setDepartment(department);
        user.setActive(!isInactive);

        // Create the application
        UserIdToken userId = proxy.getUserApi().createUser(user);

        // Print success information
        System.out.println("Successfully created user \"" + username + "\" with user ID " + userId.getId() + ".");
    }

}
