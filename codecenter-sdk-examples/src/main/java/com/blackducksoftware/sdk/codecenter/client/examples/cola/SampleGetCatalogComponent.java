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
package com.blackducksoftware.sdk.codecenter.client.examples.cola;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.attribute.data.AbstractAttribute;
import com.blackducksoftware.sdk.codecenter.client.examples.SamplePrinter;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Printer;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.Component;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentNameVersionToken;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseSummary;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;

public class SampleGetCatalogComponent extends SamplePrinter {

    public static void main(String[] args) {
        new SampleGetCatalogComponent(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "componentName", usage = "the name of the component to get information about")
    protected String componentName;

    @Argument(index = 4, required = true, metaVar = "componentVersion", usage = "the version of the component to get information about")
    protected String componentVersion;

    public SampleGetCatalogComponent(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // We'll need this later; get the log statement out of the way now
        proxy.getAttributeApi();

        // Create the component token
        ComponentNameVersionToken componentToken = new ComponentNameVersionToken();
        componentToken.setName(componentName);
        componentToken.setVersion(componentVersion);

        // Get the component
        Component component = proxy.getColaApi().getCatalogComponent(componentToken);

        // Print success information
        System.out.println("Successfully retrieved catalog component:");
        System.out.println();
        print("Name", component.getName());
        print("Version", component.getVersion());
        print("Description", component.getDescription());
        if (component.isDeprecated()) {
            print("Deprecated", "yes");
        }
        print("Release Date", component.getReleaseDate());
        print("Declared Licenses", component.getDeclaredLicenses(), new Printer<LicenseSummary>() {
            @Override
            public String print(LicenseSummary license) {
                return license.getNameToken().getName();
            }
        });
        print("Homepage", component.getHomepage());
        print("Categories", component.getCategories());
        print("DB Environments", component.getDbEnvironments());
        print("Intended Audiences", component.getIntendedAudiences());
        print("Operating Systems", component.getOperatingSystems());
        print("Programming Languages", component.getProgrammingLanguages());
        print("Project Maturity", component.getProjectMaturity());
        print("User Interfaces", component.getUserInterfaces());
        print("Number Contributors", component.getNumberContributors());

        for (AttributeValue attributeValue : component.getAttributeValues()) {
            AbstractAttribute attribute = proxy.getAttributeApi().getAttribute(attributeValue.getAttributeId());
            print(attribute.getName(), attributeValue.getValues());
        }

        if (component.getVulnerabilityInfo() != null) {
            print("Vulnerabilities",
                    component.getVulnerabilityInfo().getHighPriorityCount() + " high, " + component.getVulnerabilityInfo().getMediumPriorityCount()
                            + " medium, " + component.getVulnerabilityInfo().getLowPriorityCount() + " low");
        }
    }

}
