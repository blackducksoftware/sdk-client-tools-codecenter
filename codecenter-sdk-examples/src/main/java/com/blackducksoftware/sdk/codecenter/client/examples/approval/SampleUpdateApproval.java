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

import java.util.Collection;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.approval.data.Approval;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalNameToken;
import com.blackducksoftware.sdk.codecenter.approval.data.ApprovalUpdate;
import com.blackducksoftware.sdk.codecenter.approval.data.AssignmentTypeEnum;
import com.blackducksoftware.sdk.codecenter.approval.data.BoardNameToken;
import com.blackducksoftware.sdk.codecenter.approval.data.Rule;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroupNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroupNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.EqualityUtils;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.RuleOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.fault.SdkFault;

public class SampleUpdateApproval extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleUpdateApproval(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "name", usage = "the name of the approval to update (ex: \"Security\")")
    protected String name;

    @Option(name = "-n", aliases = { "--name" }, usage = "the updated name")
    protected String newName;

    @Option(name = "-d", aliases = { "--description" }, usage = "the updated description")
    protected String description;

    @Option(name = "-b", aliases = { "--board", "--default-board" }, usage = "the default board")
    protected String defaultBoard;

    @Option(name = "-s", aliases = { "--selection", "--selection-type" }, handler = AssignmentTypeEnumOptionHandler.class,
            usage = "the board/approver selection logic")
    protected AssignmentTypeEnum selectionType;

    @Option(name = "--general", aliases = { "--general-group" }, usage = "the general attribute group")
    protected String generalGroup;

    @Option(name = "--approved", aliases = { "--approved-group" }, usage = "the approved attribute group")
    protected String approvedGroup;

    @Option(name = "--rejected", aliases = { "--rejected-group" }, usage = "the rejected attribute group")
    protected String rejectedGroup;

    @Option(name = "--deferred", aliases = { "--deferred-group" }, usage = "the deferred attribute group")
    protected String deferredGroup;

    @Option(name = "--appealed", aliases = { "--appealed-group" }, usage = "the appealed attribute group")
    protected String appealedGroup;

    @Option(name = "-a", aliases = { "--approval-rule" }, handler = RuleOptionHandler.class, usage = "an approval rule to add")
    protected List<Rule> approvalRules;

    @Option(name = "-r", aliases = { "--rejection-rule" }, handler = RuleOptionHandler.class, usage = "a rejection rule to add")
    protected List<Rule> rejectionRules;

    @Option(name = "-c", aliases = { "--clear", "--clear-rules" }, usage = "clear existing approval/rejection rules")
    protected boolean clearRules = false;

    public SampleUpdateApproval(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // We'll need this later; get the log statement out of the way now
        proxy.getAttributeApi();

        // Create the approval update
        ApprovalNameToken approvalToken = new ApprovalNameToken();
        approvalToken.setName(name);

        ApprovalUpdate approvalUpdate = new ApprovalUpdate();
        approvalUpdate.setId(approvalToken);
        approvalUpdate.setName(newName);
        approvalUpdate.setDescription(description);

        if (defaultBoard != null) {
            BoardNameToken board = new BoardNameToken();
            board.setName(defaultBoard);
            approvalUpdate.setDefaultBoardId(board);
        }

        approvalUpdate.setBoardApproverSelectionType(selectionType);

        approvalUpdate.setGeneralAttributeGroupId(createAttributeGroup(generalGroup));
        approvalUpdate.setApprovedAttributeGroupId(createAttributeGroup(approvedGroup));
        approvalUpdate.setRejectedAttributeGroupId(createAttributeGroup(rejectedGroup));
        approvalUpdate.setDeferredAttributeGroupId(createAttributeGroup(deferredGroup));
        approvalUpdate.setAppealedAttributeGroupId(createAttributeGroup(appealedGroup));

        // Set the rules, merging into existing rules if not clearing
        if (!clearRules) {
            Approval approval = proxy.getApprovalApi().getApproval(approvalToken);
            approvalUpdate.getApprovalRule().addAll(approval.getApprovalRule());
            approvalUpdate.getRejectionRule().addAll(approval.getRejectionRule());
        }

        if (approvalRules != null) {
            addAll(approvalUpdate.getApprovalRule(), approvalRules, proxy);
        }
        if (rejectionRules != null) {
            addAll(approvalUpdate.getRejectionRule(), rejectionRules, proxy);
        }

        // Update the approval
        proxy.getApprovalApi().updateApproval(approvalUpdate);

        // Print success information
        System.out.println("Successfully updated approval \"" + name + "\".");
        System.out.println();
    }

    protected AttributeGroupNameOrIdToken createAttributeGroup(String groupName) {
        AttributeGroupNameToken attributeGroup;

        if (groupName != null) {
            attributeGroup = new AttributeGroupNameToken();
            attributeGroup.setName(groupName);
        } else {
            attributeGroup = null;
        }

        return attributeGroup;
    }

    protected void addAll(Collection<Rule> rules, Iterable<? extends Rule> rulesToAdd, CodeCenterServerProxy proxy) throws SdkFault {
        addLoop:
        for (Rule ruleToAdd : rulesToAdd) {
            for (Rule rule : rules) {
                if (EqualityUtils.equals(ruleToAdd, rule, proxy)) {
                    continue addLoop;
                }
            }
            rules.add(ruleToAdd);
        }
    }

    public static class AssignmentTypeEnumOptionHandler extends BDEnumOptionHandler<AssignmentTypeEnum> {

        public AssignmentTypeEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super AssignmentTypeEnum> setter) {
            super(parser, option, setter, AssignmentTypeEnum.class);
        }

    }

}
