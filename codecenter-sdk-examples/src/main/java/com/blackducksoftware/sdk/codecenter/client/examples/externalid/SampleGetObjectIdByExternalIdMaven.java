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
package com.blackducksoftware.sdk.codecenter.client.examples.externalid;

import java.util.ArrayList;
import java.util.Collection;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.application.data.Application;
import com.blackducksoftware.sdk.codecenter.client.examples.SamplePrinter;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.Component;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponent;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentRelease;
import com.blackducksoftware.sdk.codecenter.externalid.data.ApplicationObjectKey;
import com.blackducksoftware.sdk.codecenter.externalid.data.CatalogComponentVersionObjectKey;
import com.blackducksoftware.sdk.codecenter.externalid.data.CodeCenterObjectKey;
import com.blackducksoftware.sdk.codecenter.externalid.data.CodeCenterObjectType;
import com.blackducksoftware.sdk.codecenter.externalid.data.KbComponentObjectKey;
import com.blackducksoftware.sdk.codecenter.externalid.data.KbComponentReleaseObjectKey;
import com.blackducksoftware.sdk.codecenter.fault.ErrorCode;
import com.blackducksoftware.sdk.codecenter.fault.SdkFault;

public class SampleGetObjectIdByExternalIdMaven extends SamplePrinter {

    public static void main(String[] args) {
        new SampleGetObjectIdByExternalIdMaven(args).run();
    }

    private static final String MAVEN_NAME_SPACE_KEY = "org.apache.maven";

    @Argument(index = 3, required = true, metaVar = "mavenGav", usage = "a Maven group:artifact:version or group:artifact identifier (ex: log4j:log4j:1.2.15)")
    protected String mavenGav;

    public SampleGetObjectIdByExternalIdMaven(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // We may need these later; get the log statements out of the way now
        proxy.getApplicationApi();
        proxy.getColaApi();

        // Query for object mappings of every type
        Collection<CodeCenterObjectKey> objectKeys = new ArrayList<>();

        for (CodeCenterObjectType type : CodeCenterObjectType.values()) {
            try {
                objectKeys.add(proxy.getExternalIdApi().getObjectIdByExternalId(MAVEN_NAME_SPACE_KEY, mavenGav, type));
            } catch (SdkFault e) {
                if (e.getFaultInfo().getErrorCode() != ErrorCode.EXTERNAL_ID_MAPPING_FOR_EXTERNAL_ID_NOT_FOUND) {
                    throw e;
                }
            }
        }

        // Print success information
        System.out.println(objectKeys.size() + " mapping" + (objectKeys.size() != 1 ? "s" : "") + " found for Maven artifact \"" + mavenGav + "\""
                + (objectKeys.size() != 0 ? ":" : "."));
        System.out.println();

        // Print basic information about each object
        for (CodeCenterObjectKey key : objectKeys) {
            switch (key.getObjectType()) {
            case APPLICATION:
                ApplicationObjectKey appKey = (ApplicationObjectKey) key;
                print("Application ID", appKey.getObjectId().getId());

                Application app = proxy.getApplicationApi().getApplication(appKey.getObjectId());
                print("Application Name", app.getName());
                print("Application Version", app.getVersion());
                break;

            case KB_COMPONENT:
                KbComponentObjectKey kbCompKey = (KbComponentObjectKey) key;
                print("KB Component ID", kbCompKey.getObjectId().getId());

                KbComponent kbComp = proxy.getColaApi().getKbComponent(kbCompKey.getObjectId());
                print("Component Name", kbComp.getName());
                break;

            case KB_COMPONENT_RELEASE:
                KbComponentReleaseObjectKey kbReleaseKey = (KbComponentReleaseObjectKey) key;
                print("KB Release ID", kbReleaseKey.getReleaseId().getId());

                KbComponentRelease kbRelease = proxy.getColaApi().getKbComponentRelease(kbReleaseKey.getReleaseId());
                print("Component Name", kbRelease.getName());
                print("Component Version", kbRelease.getVersion());
                break;

            case CATALOG_COMPONENT_VERSION:
                CatalogComponentVersionObjectKey compKey = (CatalogComponentVersionObjectKey) key;
                print("Component ID", compKey.getObjectId().getId());

                Component comp = proxy.getColaApi().getCatalogComponent(compKey.getObjectId());
                print("Component Name", comp.getName());
                print("Component Version", comp.getVersion());
                break;

            default:
                throw new UnsupportedOperationException("Unknown object type: " + key.getObjectType());
            }

            System.out.println();
        }
    }

}
