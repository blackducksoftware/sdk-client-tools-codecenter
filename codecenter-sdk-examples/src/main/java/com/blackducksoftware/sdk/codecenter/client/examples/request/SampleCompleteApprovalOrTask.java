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
package com.blackducksoftware.sdk.codecenter.client.examples.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.ParserProperties;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentNameVersionToken;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.request.data.RequestApplicationComponentToken;

public abstract class SampleCompleteApprovalOrTask extends BDCodeCenterSample {

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application for the request")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application for the request")
    protected String applicationVersion;

    @Argument(index = 5, required = true, metaVar = "componentName", usage = "the name of the catalog component for the request")
    protected String componentName;

    @Argument(index = 6, required = true, metaVar = "componentVersion", usage = "the version of the catalog component for the request")
    protected String componentVersion;

    public SampleCompleteApprovalOrTask(String[] args) {
        super(args);
    }

    public SampleCompleteApprovalOrTask(String[] args, ParserProperties parserProperties) {
        super(args, parserProperties);
    }

    @Override
    public final void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the request token
        ApplicationNameVersionToken application = new ApplicationNameVersionToken();
        application.setName(applicationName);
        application.setVersion(applicationVersion);

        ComponentNameVersionToken component = new ComponentNameVersionToken();
        component.setName(componentName);
        component.setVersion(componentVersion);

        RequestApplicationComponentToken request = new RequestApplicationComponentToken();
        request.setApplicationId(application);
        request.setComponentId(component);

        // Complete the request approval/task
        run(proxy, request);

        // Print success information
        System.out.println("Successfully " + getAction() + " the " + getActedUpon() + " for the request for component \"" + componentName + "\" version \""
                + componentVersion + "\" for application \"" + applicationName + "\" version \"" + applicationVersion + "\".");
    }

    protected abstract void run(CodeCenterServerProxy proxy, RequestApplicationComponentToken request) throws Exception;

    protected String getAction() {
        return "completed";
    }

    protected abstract String getActedUpon();

    protected List<AttributeValue> parseAttributes(Map<String, Collection<String>> attributeMap) {
        // Parse the attribute values
        List<AttributeValue> attributes = new ArrayList<>();

        if (attributeMap != null) {
            for (Entry<String, Collection<String>> attributeEntry : attributeMap.entrySet()) {
                String attributeName = attributeEntry.getKey();
                Collection<String> attributeValues = attributeEntry.getValue();

                AttributeNameToken nameToken = new AttributeNameToken();
                nameToken.setName(attributeName);

                AttributeValue attribute = new AttributeValue();
                attribute.setAttributeId(nameToken);
                attribute.getValues().addAll(attributeValues);

                attributes.add(attribute);
            }
        }

        return attributes;
    }

}
