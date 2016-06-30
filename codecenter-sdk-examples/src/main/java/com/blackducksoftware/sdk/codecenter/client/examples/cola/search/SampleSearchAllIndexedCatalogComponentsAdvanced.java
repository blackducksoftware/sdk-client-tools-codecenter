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

import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.DynamicOrStaticSearchTermOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.CatalogComponentSummary;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentAdvanceSearch;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentPageFilter;
import com.blackducksoftware.sdk.codecenter.cola.data.DynamicFieldSearchTerm;
import com.blackducksoftware.sdk.codecenter.cola.data.SearchTerm;
import com.blackducksoftware.sdk.codecenter.cola.data.StaticFieldSearchTerm;

public class SampleSearchAllIndexedCatalogComponentsAdvanced extends SampleSearchCatalogComponents {

    public static void main(String[] args) {
        new SampleSearchAllIndexedCatalogComponentsAdvanced(args).run();
    }

    @Argument(index = 3, required = true, multiValued = true, handler = DynamicOrStaticSearchTermOptionHandler.class, metaVar = "searchTerm",
            usage = "a search term")
    protected List<SearchTerm> searchTerms;

    public SampleSearchAllIndexedCatalogComponentsAdvanced(String[] args) {
        super(args);
    }

    @Override
    public List<CatalogComponentSummary> search_(CodeCenterServerProxy proxy) throws Exception {
        // Create the component search and page filter
        ComponentAdvanceSearch search = new ComponentAdvanceSearch();

        for (SearchTerm searchTerm : searchTerms) {
            if (searchTerm instanceof StaticFieldSearchTerm) {
                search.getStaticFields().add((StaticFieldSearchTerm) searchTerm);
            } else {
                search.getDynamicFields().add((DynamicFieldSearchTerm) searchTerm);
            }
        }

        ComponentPageFilter pageFilter = new ComponentPageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(limit - 1);
        pageFilter.setComponentType(searchType);
        pageFilter.setIncludeDeprecated(includeDeprecated);

        // Search for components
        return proxy.getColaApi().searchAllIndexedCatalogComponentsAdvanced(search, pageFilter);
    }

    @Override
    protected String getSearchCondition() {
        return "";
    }

}
