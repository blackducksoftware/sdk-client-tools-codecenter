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
package com.blackducksoftware.sdk.codecenter.client.examples.cola.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.CatalogComponentSummary;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentSearchTypeEnum;

public abstract class SampleSearchCatalogComponents extends SampleSearchComponents<CatalogComponentSearchSummary> {

    @Option(name = "-t", aliases = { "--type" }, handler = ComponentSearchTypeEnumOptionHandler.class,
            usage = "the type of components to include in the search")
    protected ComponentSearchTypeEnum searchType = ComponentSearchTypeEnum.ALL;

    public SampleSearchCatalogComponents(String[] args) {
        super(args);
    }

    @Override
    public final List<CatalogComponentSearchSummary> search(CodeCenterServerProxy proxy) throws Exception {
        // Search for components
        Collection<CatalogComponentSummary> components = search_(proxy);

        // Convert the components
        List<CatalogComponentSearchSummary> convertedComponents = new ArrayList<>(components.size());

        for (CatalogComponentSummary component : components) {
            convertedComponents.add(new CatalogComponentSearchSummary(component));
        }

        return convertedComponents;
    }

    protected abstract Collection<CatalogComponentSummary> search_(CodeCenterServerProxy proxy) throws Exception;

    @Override
    protected String getComponentSearchType() {
        return BDEnumOptionHandler.enumToString(searchType) + " catalog";
    }

}
