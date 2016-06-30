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

import com.blackducksoftware.sdk.codecenter.client.examples.SamplePrinter;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Printer;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentRelease;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentReleaseNameVersionToken;
import com.blackducksoftware.sdk.codecenter.cola.data.KbLicenseSummary;

public class SampleGetKbComponentRelease extends SamplePrinter {

    public static void main(String[] args) {
        new SampleGetKbComponentRelease(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "componentName", usage = "the name of the component to get information about")
    protected String componentName;

    @Argument(index = 4, required = true, metaVar = "componentVersion", usage = "the version of the component to get information about")
    protected String componentVersion;

    public SampleGetKbComponentRelease(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the KB component release token
        KbComponentReleaseNameVersionToken releaseToken = new KbComponentReleaseNameVersionToken();
        releaseToken.setName(componentName);
        releaseToken.setVersion(componentVersion);

        // Get the KB component release
        KbComponentRelease release = proxy.getColaApi().getKbComponentRelease(releaseToken);

        // Print success information
        System.out.println("Successfully retrieved KB component release:");
        System.out.println();
        print("Name", release.getName());
        print("Version", release.getVersion());
        print("Description", release.getDescription());
        if (release.isDeprecated()) {
            print("Deprecated", "yes");
        }
        print("Release Date", release.getReleaseDate());
        print("Declared Licenses", release.getDeclaredLicenses(), new Printer<KbLicenseSummary>() {
            @Override
            public String print(KbLicenseSummary license) {
                return license.getNameToken().getName();
            }
        });
        print("Homepage", release.getHomepage());
        print("Categories", release.getCategories());
        print("DB Environments", release.getDbEnvironments());
        print("Intended Audiences", release.getIntendedAudiences());
        print("Operating Systems", release.getOperatingSystems());
        print("Programming Languages", release.getProgrammingLanguages());
        print("Project Maturity", release.getProjectMaturity());
        print("User Interfaces", release.getUserInterfaces());
        print("Number Contributors", release.getNumberContributors());

        if (release.getVulnerabilityInfo() != null) {
            print("Vulnerabilities", release.getVulnerabilityInfo().getHighPriorityCount() + " high, " + release.getVulnerabilityInfo().getMediumPriorityCount()
                    + " medium, " + release.getVulnerabilityInfo().getLowPriorityCount() + " low");
        }
    }

}
