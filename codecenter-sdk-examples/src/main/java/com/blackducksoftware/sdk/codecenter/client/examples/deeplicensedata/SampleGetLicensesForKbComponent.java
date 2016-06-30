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
package com.blackducksoftware.sdk.codecenter.client.examples.deeplicensedata;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentRelease;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentReleaseNameVersionToken;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.ComponentReleaseAggregate;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.License;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.LicenseFamily;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.LicenseOrFamily;

public class SampleGetLicensesForKbComponent extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleGetLicensesForKbComponent(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "componentName", usage = "the name of the KB component to get deep license data for")
    protected String componentName;

    @Argument(index = 4, required = true, metaVar = "componentVersion", usage = "the version of the KB component to get deep license data for")
    protected String componentVersion;

    public SampleGetLicensesForKbComponent(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the KB component release token
        KbComponentReleaseNameVersionToken releaseToken = new KbComponentReleaseNameVersionToken();
        releaseToken.setName(componentName);
        releaseToken.setVersion(componentVersion);

        // Get the deep license data
        ComponentReleaseAggregate aggregate = proxy.getDeepLicenseDataApi().getLicensesForKbComponent(releaseToken);

        // Get the KB component release
        KbComponentRelease release = proxy.getColaApi().getKbComponentRelease(releaseToken);

        // Derive the number of occurrences of the declared license
        long declaredOccurenceCount = aggregate.getTotalCount();

        for (LicenseOrFamily licenseOrFamily : aggregate.getLicenses()) {
            if (licenseOrFamily instanceof License) {
                License license = (License) licenseOrFamily;
                declaredOccurenceCount -= license.getOccurrenceCount();
            }
        }

        // Print success information
        System.out.println("Successfully retrieved deep license data for \"" + componentName + "\" version \"" + componentVersion + "\":");
        System.out.println();

        System.out.println("Declared License");
        print(release.getDeclaredLicenses().get(0).getNameToken().getName(), declaredOccurenceCount);

        for (LicenseOrFamily licenseOrFamily : aggregate.getLicenses()) {
            if (licenseOrFamily instanceof LicenseFamily) {
                LicenseFamily licenseFamily = (LicenseFamily) licenseOrFamily;
                System.out.println();
                System.out.println(licenseFamily.getFamilyBean().getFamilyName());
            } else if (licenseOrFamily instanceof License) {
                License license = (License) licenseOrFamily;
                print(license.getLicenseName(), license.getOccurrenceCount());
            }
        }

        System.out.println();
    }

    protected void print(String licenseName, long occurrences) {
        System.out.println("    " + occurrences + " occurence" + (occurrences != 1 ? "s" : "") + " of " + licenseName);
    }

}
