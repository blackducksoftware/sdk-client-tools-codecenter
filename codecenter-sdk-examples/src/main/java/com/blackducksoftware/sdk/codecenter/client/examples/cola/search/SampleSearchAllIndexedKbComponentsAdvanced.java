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

import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.StaticSearchTermOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentAdvanceSearch;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentPageFilter;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentSummary;
import com.blackducksoftware.sdk.codecenter.cola.data.StaticFieldSearchTerm;

public class SampleSearchAllIndexedKbComponentsAdvanced extends SampleSearchKbComponents {

    public static void main(String[] args) {
        new SampleSearchAllIndexedKbComponentsAdvanced(args).run();
    }

    @Argument(index = 3, required = true, multiValued = true, handler = StaticSearchTermOptionHandler.class, metaVar = "searchTerm", usage = "a search term")
    protected List<StaticFieldSearchTerm> searchTerms;

    public SampleSearchAllIndexedKbComponentsAdvanced(String[] args) {
        super(args);
    }

    @Override
    public List<KbComponentSummary> search_(CodeCenterServerProxy proxy) throws Exception {
        // Create the component search and page filter
        KbComponentAdvanceSearch search = new KbComponentAdvanceSearch();
        search.getStaticFields().addAll(searchTerms);

        KbComponentPageFilter pageFilter = new KbComponentPageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(limit - 1);
        pageFilter.setIncludeDeprecated(includeDeprecated);

        // Search for components
        return proxy.getColaApi().searchIndexedKbComponentsAdvanced(search, pageFilter, true);
    }

    @Override
    protected String getSearchCondition() {
        return "";
    }

}
