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

import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentNameVersionToken;

public class SampleDeleteCatalogComponent extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleDeleteCatalogComponent(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "componentName", usage = "the name of the component to delete")
    protected String componentName;

    @Argument(index = 4, required = true, metaVar = "componentVersion", usage = "the version of the component to delete")
    protected String componentVersion;

    public SampleDeleteCatalogComponent(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the component token
        ComponentNameVersionToken componentNameVersion = new ComponentNameVersionToken();
        componentNameVersion.setName(componentName);
        componentNameVersion.setVersion(componentVersion);

        // Delete the component
        proxy.getColaApi().deleteCatalogComponent(componentNameVersion);

        // Print success information
        System.out.println("Successfully deleted catalog component \"" + componentName + "\" version \"" + componentVersion + "\".");
    }

}
