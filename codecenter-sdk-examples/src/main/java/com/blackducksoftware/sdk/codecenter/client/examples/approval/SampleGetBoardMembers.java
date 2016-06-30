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
package com.blackducksoftware.sdk.codecenter.client.examples.approval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.Argument;
import org.springframework.util.StringUtils;

import com.blackducksoftware.sdk.codecenter.approval.data.BoardNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.role.data.BoardRoleAssignment;
import com.blackducksoftware.sdk.codecenter.user.data.User;
import com.blackducksoftware.sdk.codecenter.user.data.UserPageFilter;

public class SampleGetBoardMembers extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleGetBoardMembers(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "boardName", usage = "the name of the board to list the members of (ex: \"License Board\")")
    protected String boardName;

    public SampleGetBoardMembers(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // We'll need this later; get the log statement out of the way now
        proxy.getRoleApi();

        // Create the user page filter and board name token
        UserPageFilter pageFilter = new UserPageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(Integer.MAX_VALUE);

        BoardNameToken boardNameToken = new BoardNameToken();
        boardNameToken.setName(boardName);

        // Get the board members and their role assignments
        List<User> users = proxy.getApprovalApi().getBoardMembers(boardNameToken, pageFilter);
        Map<String, BoardRoleAssignment> assignmentsMap = new HashMap<>();

        List<BoardRoleAssignment> assignments = proxy.getRoleApi().getBoardRoles(boardNameToken);
        for (BoardRoleAssignment assignment : assignments) {
            assignmentsMap.put(assignment.getUserNameToken().getName(), assignment);
        }

        // Print success information
        System.out.println("Successfully got the board members of board \"" + boardName + "\".");
        System.out.println("Found " + users.size() + " member" + (users.size() != 1 ? "s" : "") + (users.size() != 0 ? ":" : "."));
        System.out.println();

        for (User user : users) {
            // Print user details
            boolean hasFirstName = StringUtils.hasText(user.getFirstName());
            boolean hasLastName = StringUtils.hasText(user.getLastName());
            System.out.println((hasFirstName ? (hasLastName ? user.getFirstName() + " " + user.getLastName() : user.getFirstName())
                    : "?" + (hasLastName ? " " + user.getLastName() : "")) + " (" + user.getName().getName() + "): "
                    + assignmentsMap.get(user.getName().getName()).getRoleNameToken().getName());
        }
    }

}
