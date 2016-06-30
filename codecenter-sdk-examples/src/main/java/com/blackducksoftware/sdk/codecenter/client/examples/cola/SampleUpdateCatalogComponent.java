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
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentNameVersionToken;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentUpdate;
import com.blackducksoftware.sdk.codecenter.cola.data.CustomComponentRelease;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseNameToken;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;

public class SampleUpdateCatalogComponent extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleUpdateCatalogComponent(args).run();
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Argument(index = 3, required = true, metaVar = "componentName", usage = "the name of the component to update")
    protected String componentName;

    @Argument(index = 4, required = true, metaVar = "componentVersion", usage = "the version of the component to update")
    protected String componentVersion;

    @Argument(index = 5, required = false, multiValued = true, handler = MultiMapOptionHandler.class, metaVar = "attribute=value",
            usage = "set the value of a named attribute or KB metadata for the component; may repeat a name to set multiple values"
                    + " (ex: \"Declared License\"=\"Apache License Version 2.0\" type=Internal projecturl=http://www.example.com/")
    protected Map<String, Collection<String>> attributeMap;

    @Option(name = "-d", aliases = { "--description" }, usage = "the description of the component")
    protected String description;

    @Option(name = "-v", aliases = { "--version" }, usage = "the updated version of the component")
    protected String version;

    public SampleUpdateCatalogComponent(String[] args) {
        super(args);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the component create bean
        ComponentUpdate componentUpdateBean = new ComponentUpdate();
        componentUpdateBean.setName(componentName);
        componentUpdateBean.setVersion(version);
        componentUpdateBean.setDescription(description);

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
                            componentUpdateBean.setReleaseDate(dateFormat.parse(attributeValue));
                        } catch (ParseException e) {
                            throw new CmdLineException(e.getMessage(), e);
                        }
                    } else if (attributeName.equals("Declared License")) {
                        LicenseNameToken licenseNameToken = new LicenseNameToken();
                        licenseNameToken.setName(attributeValue);
                        componentUpdateBean.getDeclaredLicenses().add(licenseNameToken);
                    } else if (attributeName.equals("Homepage")) {
                        componentUpdateBean.setHomepage(attributeValue);
                    } else if (attributeName.equals("Categories")) {
                        componentUpdateBean.setCategories(attributeValue);
                    } else if (attributeName.equals("DB Environments")) {
                        componentUpdateBean.setDbEnvironments(attributeValue);
                    } else if (attributeName.equals("Intended Audiences")) {
                        componentUpdateBean.setIntendedAudiences(attributeValue);
                    } else if (attributeName.equals("Operating Systems")) {
                        componentUpdateBean.setOperatingSystems(attributeValue);
                    } else if (attributeName.equals("Programming Languages")) {
                        componentUpdateBean.setProgrammingLanguages(attributeValue);
                    } else if (attributeName.equals("Project Maturity")) {
                        componentUpdateBean.setProjectMaturity(attributeValue);
                    } else if (attributeName.equals("User Interfaces")) {
                        componentUpdateBean.setUserInterfaces(attributeValue);
                    } else if (attributeName.equals("Number Contributors")) {
                        try {
                            componentUpdateBean.setNumberContributors(Integer.valueOf(attributeValue));
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

        componentUpdateBean.getAttributeValues().addAll(attributes);

        // Get the existing component to set the update ID
        ComponentNameVersionToken componentToken = new ComponentNameVersionToken();
        componentToken.setName(componentName);
        componentToken.setVersion(componentVersion);

        CustomComponentRelease component = proxy.getColaApi().getCatalogComponentRelease(componentToken);

        componentUpdateBean.setId(component.getId());

        // Update the component
        proxy.getColaApi().updateCatalogComponent(componentUpdateBean);

        // Print success information
        System.out.println("Successfully updated catalog component \"" + componentName + "\" version \"" + componentVersion + "\" with component ID "
                + component.getId().getId() + ".");
    }

}
