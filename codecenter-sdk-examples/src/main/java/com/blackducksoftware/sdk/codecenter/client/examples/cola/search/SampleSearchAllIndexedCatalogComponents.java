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

import java.util.List;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.CatalogComponentSummary;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentPageFilter;

public class SampleSearchAllIndexedCatalogComponents extends SampleSearchCatalogComponents {

    public static void main(String[] args) {
        new SampleSearchAllIndexedCatalogComponents(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "searchString", usage = "part of the component name or the component version to search for")
    protected String searchString;

    public SampleSearchAllIndexedCatalogComponents(String[] args) {
        super(args);
    }

    @Override
    public List<CatalogComponentSummary> search_(CodeCenterServerProxy proxy) throws Exception {
        // Create the component page filter
        ComponentPageFilter pageFilter = new ComponentPageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(limit - 1);
        pageFilter.setComponentType(searchType);
        pageFilter.setIncludeDeprecated(includeDeprecated);

        // Search for components
        return proxy.getColaApi().searchAllIndexedCatalogComponents(searchString, pageFilter);
    }

    @Override
    protected String getSearchCondition() {
        return "containing \"" + searchString + "\"";
    }

}
