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
package com.blackducksoftware.sdk.codecenter.client.examples.attribute.group;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.approval.data.Rule;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroupNameToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeGroupUpdate;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.RuleOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public class SampleUpdateAttributeGroupSetTriggers extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleUpdateAttributeGroupSetTriggers(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "name", usage = "the name of the attribute group to set the triggers for")
    protected String name;

    @Argument(index = 4, required = false, handler = RuleOptionHandler.class, metaVar = "rule", usage = "the triggers")
    protected Rule rule;

    // @Option(name = "--clean", aliases = { "-c" }, usage = "remove all existing triggers")
    // private boolean clean = false;

    public SampleUpdateAttributeGroupSetTriggers(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the attribute group token
        AttributeGroupNameToken nameToken = new AttributeGroupNameToken();
        nameToken.setName(name);

        // Create the attribute group update bean
        AttributeGroupUpdate updateBean = new AttributeGroupUpdate();
        updateBean.setName(name);
        updateBean.setAttributeGroupId(nameToken);
        updateBean.setExpression(rule);

        // Update the attribute group's triggers
        proxy.getAttributeApi().updateAttributeGroup(updateBean);

        // Print success information
        System.out.println("Successfully set " + rule.getConditions().size() + " trigger" + (rule.getConditions().size() != 1 ? "s" : "")
                + " for attribute group \"" + name + "\".");
    }

}
