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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.MultiMapOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentNameVersionToken;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseNameToken;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseSummary;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.request.data.ComponentUsageEnum;
import com.blackducksoftware.sdk.codecenter.request.data.RequestApplicationComponentToken;
import com.blackducksoftware.sdk.codecenter.request.data.RequestCreate;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;

public class SampleCreateRequest extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateRequest(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application for the request")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application for the request")
    protected String applicationVersion;

    @Argument(index = 5, required = true, metaVar = "componentName", usage = "the name of the catalog component for the request")
    protected String componentName;

    @Argument(index = 6, required = true, metaVar = "componentVersion", usage = "the version of the catalog component for the request")
    protected String componentVersion;

    @Argument(index = 7, required = false, multiValued = true, handler = MultiMapOptionHandler.class, metaVar = "<attribute>=<value>",
            usage = "set the value of an attribute for the request; may repeat a name to set multiple values (ex: location=Internal passbasicsecurity=No)")
    protected Map<String, Collection<String>> attributeMap;

    @Option(name = "-l", aliases = { "--license" }, metaVar = "<name>", usage = "the name of an overriding license for the component")
    protected String licenseName;

    @Option(name = "-u", aliases = { "--usage" }, handler = ComponentUsageEnumOptionHandler.class, usage = "the usage for the component")
    protected ComponentUsageEnum usage;

    // // TODO: determine how/if these work
    // @Option(name = "-a", aliases = { "--approver" }, metaVar = "<username>", usage = "the username of an approver for
    // the request")
    // private List<String> approvers;
    //
    // @Option(name = "-d", aliases = { "--developer" }, metaVar = "<username>", usage = "the username of a developer
    // for the request")
    // private List<String> developers;

    @Option(name = "-r", aliases = { "--requester", "-o", "--owner" }, metaVar = "<username>",
            usage = "the username of a user to make the request in the name of (default: SDK user)")
    protected String owner;

    @Option(name = "-n", aliases = { "--no-submit" }, usage = "don't submit the request")
    protected boolean noSubmit;

    public SampleCreateRequest(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
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

        // Create the license token
        LicenseNameOrIdToken license;

        if (licenseName != null) {
            LicenseNameToken licenseNameToken = new LicenseNameToken();
            licenseNameToken.setName(licenseName);
            license = licenseNameToken;
        } else {
            // Get the license from the component
            LicenseSummary licenseSummary = proxy.getColaApi().getCatalogComponent(component).getDeclaredLicenses().get(0);
            license = licenseSummary.getId();
        }

        // Create the owner token
        UserNameToken owner = null;

        if (this.owner != null) {
            owner = new UserNameToken();
            owner.setName(this.owner);
        }

        // Create the request create bean
        RequestCreate createRequest = new RequestCreate();
        createRequest.setApplicationComponentToken(request);
        createRequest.setLicenseId(license);
        createRequest.setOwner(owner);
        createRequest.setSubmit(!noSubmit);
        createRequest.setUsage(usage);
        createRequest.getAttributeValues().addAll(attributes);

        // Create the request
        proxy.getRequestApi().createRequest(createRequest);

        // Print success information
        System.out.println("Successfully created a request for component \"" + componentName + "\" version \"" + componentVersion + "\" for application \""
                + applicationName + "\" version \"" + applicationVersion + "\".");
    }

    public static class ComponentUsageEnumOptionHandler extends BDEnumOptionHandler<ComponentUsageEnum> {

        // Don't allow NULLIFY
        private static final ComponentUsageEnum[] allowedEnumConstants = Arrays.copyOf(ComponentUsageEnum.values(), ComponentUsageEnum.values().length - 1);

        public ComponentUsageEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super ComponentUsageEnum> setter) {
            super(parser, option, setter, false, allowedEnumConstants);
        }

    }

}
