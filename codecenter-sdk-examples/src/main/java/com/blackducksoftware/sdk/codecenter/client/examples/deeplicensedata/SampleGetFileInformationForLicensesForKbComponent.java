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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentReleaseNameVersionToken;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.ComponentReleaseAggregate;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.DeepLicenseKbComponentReleaseToken;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.DeepLicensePageFilter;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.File;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.FileContent;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.FilesByReleaseLicense;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.License;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.LicenseFamily;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.LicenseOrFamily;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.LicenseOrFamilyIdToken;
import com.blackducksoftware.sdk.codecenter.deeplicense.data.LicenseOrFamilyMatch;
import com.blackducksoftware.sdk.codecenter.fault.ErrorCode;
import com.blackducksoftware.sdk.codecenter.fault.SdkFault;

public class SampleGetFileInformationForLicensesForKbComponent extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleGetFileInformationForLicensesForKbComponent(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "componentName", usage = "the name of the KB component to get deep license file information for")
    protected String componentName;

    @Argument(index = 4, required = true, metaVar = "componentVersion", usage = "the version of the KB component to get deep license file information for")
    protected String componentVersion;

    public SampleGetFileInformationForLicensesForKbComponent(String[] args) {
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

        // Print success information
        System.out.println("Successfully retrieved deep license data for \"" + componentName + "\" version \"" + componentVersion + "\":");
        System.out.println();

        // Create the deep license KB component release token
        DeepLicenseKbComponentReleaseToken deepLicenseToken = new DeepLicenseKbComponentReleaseToken();
        deepLicenseToken.setReleaseId(releaseToken);
        deepLicenseToken.setScanId(aggregate.getReleaseToken().getScanId());

        // Create the page filter
        DeepLicensePageFilter pageFilter = new DeepLicensePageFilter();
        pageFilter.setPageNumber(0);
        pageFilter.setPageSize(Integer.MAX_VALUE);

        // Process every license or family
        for (LicenseOrFamily licenseOrFamily : aggregate.getLicenses()) {
            // Get the license or family name and token
            String name;
            LicenseOrFamilyIdToken token;

            if (licenseOrFamily instanceof LicenseFamily) {
                LicenseFamily licenseFamily = (LicenseFamily) licenseOrFamily;
                name = licenseFamily.getFamilyBean().getFamilyName();
                token = licenseFamily.getId();
            } else if (licenseOrFamily instanceof License) {
                License license = (License) licenseOrFamily;
                name = license.getLicenseName();
                token = license.getId();
            } else {
                throw new AssertionError();
            }

            // Get the files for the license or family
            FilesByReleaseLicense files = null;

            try {
                files = proxy.getDeepLicenseDataApi().getFilesForLicense(deepLicenseToken, token, pageFilter);
            } catch (SdkFault e) {
                if (e.getFaultInfo().getErrorCode() == ErrorCode.NO_DATA_FOUND_EXCEPTION) {
                    continue;
                }
                throw e;
            }

            // Group the files by checksum
            Map<Long, List<File>> filesByChecksum = new HashMap<>();

            for (File file : files.getFileBeans()) {
                Long checksum = file.getUniqueFileMatchCount();
                List<File> filesForChecksum = filesByChecksum.get(checksum);

                if (filesForChecksum == null) {
                    filesForChecksum = new ArrayList<>();
                    filesByChecksum.put(checksum, filesForChecksum);
                }

                filesForChecksum.add(file);
            }

            // Print the license or family name
            System.out.println(name + ":");

            // Process every matched file, grouped by checksum
            for (List<File> filesForChecksum : filesByChecksum.values()) {

                // Print every file path
                for (File file : filesForChecksum) {
                    System.out.println("    " + file.getFilePath());
                }

                // Get the file match information
                FileContent fileContent = proxy.getDeepLicenseDataApi().getFileInformation(filesForChecksum.get(0).getFileToken());

                String fileData = new String(fileContent.getFileData(), "UTF-8");

                // Print the matched file part
                for (LicenseOrFamilyMatch match : fileContent.getOccurrenceBeans()) {
                    printPart(fileData, (int) (long) match.getStart(), (int) (long) match.getEnd());
                }

                System.out.println();
            }

            System.out.println();
        }
    }

    protected void printPart(String str, int start, int end) {
        String indent = "            ";

        int line = 1;
        int lineStart = 0;

        // Find the the line number and start of the line containing start
        for (int i = 0; i < start;) {
            if (str.charAt(i++) == '\n') {
                line++;
                lineStart = i;
            }
        }

        // Print a mark showing where the match starts
        char[] spaces = new char[start - lineStart];
        Arrays.fill(spaces, ' ');
        System.out.println(indent + String.valueOf(spaces) + "[");

        int i = lineStart;
        do {
            // Print the line number prefix
            System.out.printf("%10d  ", line);

            // Print the line
            char c;
            do {
                if (i == str.length()) {
                    c = '\n';
                } else {
                    c = str.charAt(i++);
                    if (c == '\r') {
                        c = '\n';
                        if ((i + 1 < str.length()) && (str.charAt(i + 1) == '\n')) {
                            i++;
                        }
                    }
                }
                System.out.print(c);
            } while (c != '\n');

            // If not past the end of the part to print, continue
            if (i < (end - 1)) {
                line++;
                lineStart = i;
            } else {
                break;
            }
        } while (true);

        // Print a mark showing where the match ends
        spaces = new char[end - 1 - lineStart];
        Arrays.fill(spaces, ' ');
        System.out.println(indent + String.valueOf(spaces) + "]");
    }

}
