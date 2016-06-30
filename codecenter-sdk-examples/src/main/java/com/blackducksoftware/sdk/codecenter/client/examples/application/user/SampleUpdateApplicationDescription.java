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
package com.blackducksoftware.sdk.codecenter.client.examples.application.user;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationUpdate;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public class SampleUpdateApplicationDescription extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleUpdateApplicationDescription(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to update")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application to update")
    protected String applicationVersion;

    @Argument(index = 5, required = true, metaVar = "description", usage = "the description to set for the application")
    protected String description;

    public SampleUpdateApplicationDescription(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the application update bean
        ApplicationNameVersionToken appNameVersion = new ApplicationNameVersionToken();
        appNameVersion.setName(applicationName);
        appNameVersion.setVersion(applicationVersion);

        ApplicationUpdate updateBean = new ApplicationUpdate();
        updateBean.setId(appNameVersion);
        updateBean.setDescription(description);

        // Update the application
        proxy.getApplicationApi().updateApplication(updateBean);

        // Print success information
        System.out.println(
                "Successfully updated the description of application \"" + applicationName + "\" version \"" + applicationVersion + "\" to:\n" + description);
    }

}
