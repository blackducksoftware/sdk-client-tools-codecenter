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

import java.util.List;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.approval.data.Approval;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalNameToken;
import com.blackducksoftware.sdk.codecenter.approval.data.Board;
import com.blackducksoftware.sdk.codecenter.approval.data.BoardIdToken;
import com.blackducksoftware.sdk.codecenter.approval.data.BoardPageFilter;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroup;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroupIdToken;
import com.blackducksoftware.sdk.codecenter.client.examples.SamplePrinter;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Pair;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.fault.SdkFault;

public class SampleGetApproval extends SamplePrinter {

    public static void main(String[] args) {
        new SampleGetApproval(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "name", usage = "the name of the approval to get information about (ex: \"Security\")")
    protected String name;

    public SampleGetApproval(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // We'll need this later; get the log statement out of the way now
        proxy.getAttributeApi();

        // Create the approval token
        ApprovalNameToken approvalToken = new ApprovalNameToken();
        approvalToken.setName(name);

        // Get the approval
        Approval approval = proxy.getApprovalApi().getApproval(approvalToken);

        // Print success information
        System.out.println("Successfully got information about approval \"" + name + "\":");
        System.out.println();

        print("Description", approval.getDescription());
        print("Default Board", getBoard(proxy, approval.getDefaultBoardId()).getName());
        print("Board/Approver Selection", approval.getBoardApproverSelectionType());

        @SuppressWarnings("unchecked")
        Pair<String, AttributeGroupIdToken>[] attributeGroupPairs = new Pair[] { new Pair<>("General", approval.getGeneralAttributeGroupId()),
                new Pair<>("Approved", approval.getApprovedAttributeGroupId()), new Pair<>("Rejected", approval.getRejectedAttributeGroupId()),
                new Pair<>("Deferred", approval.getDeferredAttributeGroupId()), new Pair<>("Appealed", approval.getAppealedAttributeGroupId()) };

        for (Pair<String, AttributeGroupIdToken> attributeGroupPair : attributeGroupPairs) {
            if (attributeGroupPair.getY() != null) {
                AttributeGroup attributeGroup = proxy.getAttributeApi().getAttributeGroup(attributeGroupPair.getY());
                print(attributeGroupPair.getX() + " Attribute Group", attributeGroup.getName());
            }
        }
    }

    protected Board getBoard(CodeCenterServerProxy proxy, BoardIdToken boardIdToken) throws SdkFault {
        // TODO: find a non-hacky way to do this
        BoardPageFilter pageFilter = new BoardPageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(Integer.MAX_VALUE);

        List<Board> boards = proxy.getApprovalApi().searchBoards("", pageFilter);

        for (Board board : boards) {
            if (board.getBoardId().getId().equals(boardIdToken.getId())) {
                return board;
            }
        }

        return null;
    }

    @Override
    protected int getFieldWidth() {
        return 28;
    }

}
