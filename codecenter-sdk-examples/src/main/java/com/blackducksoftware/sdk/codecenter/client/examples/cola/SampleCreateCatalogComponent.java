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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.MultiMapOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentCreate;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentIdToken;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseNameToken;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;

public class SampleCreateCatalogComponent extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateCatalogComponent(args).run();
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Argument(index = 3, required = true, metaVar = "componentName", usage = "the name of the component to create")
    protected String componentName;

    @Argument(index = 4, required = true, metaVar = "componentVersion", usage = "the version of the component to create")
    protected String componentVersion;

    @Argument(index = 5, required = false, multiValued = true, handler = MultiMapOptionHandler.class, metaVar = "attribute=value",
            usage = "set the value of a named attribute or KB metadata for the component; may repeat a name to set multiple values"
                    + " (ex: \"Declared License\"=\"Apache License Version 2.0\" type=Internal projecturl=http://www.example.com/")
    protected Map<String, Collection<String>> attributeMap;

    @Option(name = "-d", aliases = { "--description" }, usage = "the description of the component")
    protected String description;

    public SampleCreateCatalogComponent(String[] args) {
        super(args);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the component create bean
        ComponentCreate componentBean = new ComponentCreate();
        componentBean.setName(componentName);
        componentBean.setVersion(componentVersion);
        componentBean.setDescription(description);

        // Parse the attribute values
        List<AttributeValue> attributes = new ArrayList<>();

        if (attributeMap != null) {
            for (Entry<String, Collection<String>> attributeEntry : attributeMap.entrySet()) {
                String attributeName = attributeEntry.getKey();
                Collection<String> attributeValues = attributeEntry.getValue();

                AttributeValue attribute = null;

                for (String attributeValue : attributeValues) {
                    if (attributeName.equals("Release Date")) {
                        try {
                            componentBean.setReleaseDate(dateFormat.parse(attributeValue));
                        } catch (ParseException e) {
                            throw new CmdLineException(e.getMessage(), e);
                        }
                    } else if (attributeName.equals("Declared License")) {
                        LicenseNameToken licenseNameToken = new LicenseNameToken();
                        licenseNameToken.setName(attributeValue);
                        componentBean.getDeclaredLicenses().add(licenseNameToken);
                    } else if (attributeName.equals("Homepage")) {
                        componentBean.setHomepage(attributeValue);
                    } else if (attributeName.equals("Categories")) {
                        componentBean.setCategories(attributeValue);
                    } else if (attributeName.equals("DB Environments")) {
                        componentBean.setDbEnvironments(attributeValue);
                    } else if (attributeName.equals("Intended Audiences")) {
                        componentBean.setIntendedAudiences(attributeValue);
                    } else if (attributeName.equals("Operating Systems")) {
                        componentBean.setOperatingSystems(attributeValue);
                    } else if (attributeName.equals("Programming Languages")) {
                        componentBean.setProgrammingLanguages(attributeValue);
                    } else if (attributeName.equals("Project Maturity")) {
                        componentBean.setProjectMaturity(attributeValue);
                    } else if (attributeName.equals("User Interfaces")) {
                        componentBean.setUserInterfaces(attributeValue);
                    } else if (attributeName.equals("Number Contributors")) {
                        try {
                            componentBean.setNumberContributors(Integer.valueOf(attributeValue));
                        } catch (NumberFormatException e) {
                            throw new CmdLineException(e.getMessage(), e);
                        }
                    } else {
                        if (attribute == null) {
                            AttributeNameToken nameToken = new AttributeNameToken();
                            nameToken.setName(attributeName);

                            attribute = new AttributeValue();
                            attribute.setAttributeId(nameToken);

                            attributes.add(attribute);
                        }

                        attribute.getValues().add(attributeValue);
                    }
                }
            }
        }

        componentBean.getAttributeValues().addAll(attributes);

        // Create the component
        ComponentIdToken componentId = proxy.getColaApi().createCatalogComponent(componentBean);

        // Print success information
        System.out.println("Successfully created catalog component \"" + componentName + "\" version \"" + componentVersion + "\" with component ID "
                + componentId.getId() + ".");
    }

}
