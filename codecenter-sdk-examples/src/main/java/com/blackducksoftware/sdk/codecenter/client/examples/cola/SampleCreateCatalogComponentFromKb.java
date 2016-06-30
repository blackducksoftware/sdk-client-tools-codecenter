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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.MultiMapOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentReleaseNameVersionToken;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;

public class SampleCreateCatalogComponentFromKb extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateCatalogComponentFromKb(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "kbComponentName", usage = "the name of the KB component to create a catalog component from")
    protected String kbComponentName;

    @Argument(index = 4, required = true, metaVar = "kbComponentVersion", usage = "the version of the KB component to create a catalog component from")
    protected String kbComponentVersion;

    @Argument(index = 5, required = false, multiValued = true, handler = MultiMapOptionHandler.class, metaVar = "attribute=value",
            usage = "set the value of a named attribute for the created component; may repeat a name to set multiple values"
                    + " (ex: type=\"Open Source\" projecturl=http://www.example.com/)")
    protected Map<String, Collection<String>> attributeMap;

    public SampleCreateCatalogComponentFromKb(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the KB component token
        KbComponentReleaseNameVersionToken kbComponentNameVersion = new KbComponentReleaseNameVersionToken();
        kbComponentNameVersion.setName(kbComponentName);
        kbComponentNameVersion.setVersion(kbComponentVersion);

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

        // Create the component from a KB component
        proxy.getColaApi().createCatalogComponentFromKb(kbComponentNameVersion, attributes);

        // Print success information
        System.out
                .println("Successfully created a catalog component from the KB component \"" + kbComponentName + "\" version \"" + kbComponentVersion + "\".");
    }

}
