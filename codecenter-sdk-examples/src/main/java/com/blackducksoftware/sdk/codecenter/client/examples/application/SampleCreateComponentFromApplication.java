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
package com.blackducksoftware.sdk.codecenter.client.examples.application;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.application.data.Application;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationComponentCreate;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public class SampleCreateComponentFromApplication extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateComponentFromApplication(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to publish")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application to publish")
    protected String applicationVersion;

    public SampleCreateComponentFromApplication(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the application token
        ApplicationNameVersionToken appNameVersion = new ApplicationNameVersionToken();
        appNameVersion.setName(applicationName);
        appNameVersion.setVersion(applicationVersion);

        // Get the application information
        Application app = proxy.getApplicationApi().getApplication(appNameVersion);

        // Create the application component create bean
        ApplicationComponentCreate comp = new ApplicationComponentCreate();
        comp.setApplicationId(appNameVersion);
        comp.getAttributeValues().addAll(app.getAttributeValues());

        // Create the component
        proxy.getApplicationApi().createComponentFromApplication(comp);

        // Print success information
        System.out.println("Successfully created a component from application \"" + applicationName + "\" version \"" + applicationVersion + "\".");
    }

}
