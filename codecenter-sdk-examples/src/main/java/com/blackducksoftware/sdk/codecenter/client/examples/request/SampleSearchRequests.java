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
package com.blackducksoftware.sdk.codecenter.client.examples.request;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.application.data.Application;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionOrIdToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.SampleSearch;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.ApprovalStatusEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.RequestColumnOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.Component;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentNameVersionOrIdToken;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentNameVersionToken;
import com.blackducksoftware.sdk.codecenter.common.data.ApprovalStatusEnum;
import com.blackducksoftware.sdk.codecenter.request.data.RequestColumn;
import com.blackducksoftware.sdk.codecenter.request.data.RequestPageFilter;
import com.blackducksoftware.sdk.codecenter.request.data.RequestSummary;

public class SampleSearchRequests extends SampleSearch<RequestSummary> {

    public static void main(String[] args) {
        new SampleSearchRequests(args).run();
    }

    // TODO: determine what this actually searches against
    @Argument(index = 3, required = false, metaVar = "searchString", usage = "part of the component name to search for; omit/make blank to retrieve all")
    protected String searchString;

    @Argument(index = 4, required = false, multiValued = true, handler = ApprovalStatusEnumOptionHandler.class, metaVar = "approvalStatus", usage = "TODO")
    protected List<ApprovalStatusEnum> approvalStatuses;

    @Option(name = "-s", aliases = { "--sort-by" }, handler = RequestColumnOptionHandler.class, usage = "sort by this property (ascending)")
    protected RequestColumn sortColumn = RequestColumn.REQUEST_COMPONENT_NAME;

    @Option(name = "-r", aliases = { "--reverse" }, usage = "reverse sort order (descending)")
    protected boolean reverse = false;

    private Set<ApprovalStatusEnum> approvalStatusesSet;

    public SampleSearchRequests(String[] args) {
        super(args);
    }

    @Override
    protected void preprocessArgs() {
        // Reduce the approval statuses to a set
        if ((approvalStatuses == null) || (approvalStatuses.contains(ApprovalStatusEnum.ALL))) {
            approvalStatusesSet = EnumSet.of(ApprovalStatusEnum.ALL);
        } else {
            approvalStatusesSet = EnumSet.copyOf(approvalStatuses);
        }
    }

    @Override
    protected Collection<RequestSummary> search(CodeCenterServerProxy proxy) throws Exception {
        // We may need these later; get the log statements out of the way now
        proxy.getApplicationApi();
        proxy.getColaApi();

        // Create the page filter
        RequestPageFilter pageFilter = new RequestPageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(limit);
        pageFilter.setSortAscending(!reverse);
        pageFilter.setSortedColumn(sortColumn);
        pageFilter.getApprovalStatuses().addAll(approvalStatusesSet);

        // Search for requests
        return proxy.getRequestApi().searchRequests(searchString != null ? searchString : "", pageFilter);
    }

    @Override
    protected String getSearchExpression() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (ApprovalStatusEnum approvalStatus : approvalStatusesSet) {
            if (!first) {
                sb.append("/");
            }
            sb.append(ApprovalStatusEnumOptionHandler.enumToString(approvalStatus));
            first = false;
        }

        return sb.toString() + " requests" + (searchString != null ? " matching \"" + searchString + "\"" : "");
    }

    @Override
    protected void print(RequestSummary request, CodeCenterServerProxy proxy) throws Exception {
        // Get the application name and version token
        ApplicationNameVersionToken appNameVersion;

        ApplicationNameVersionOrIdToken appToken = request.getApplicationComponentToken().getApplicationId();
        if (appToken instanceof ApplicationNameVersionToken) {
            appNameVersion = (ApplicationNameVersionToken) appToken;
        } else {
            Application app = proxy.getApplicationApi().getApplication(appToken);
            appNameVersion = app.getNameVersion();
        }

        // Get the component name and version token
        ComponentNameVersionToken compNameVersion;

        ComponentNameVersionOrIdToken compToken = request.getComponentId();
        if (compToken instanceof ComponentNameVersionToken) {
            compNameVersion = (ComponentNameVersionToken) compToken;
        } else {
            Component comp = proxy.getColaApi().getCatalogComponent(compToken);
            compNameVersion = comp.getNameVersion();
        }

        // Print request details
        print("Approval Status", request.getApprovalStatus());
        print("Component Name", compNameVersion.getName());
        print("Component Version", compNameVersion.getVersion());
        print("License", request.getLicenseInfo().getNameToken().getName());
        print("Usage", request.getUsage());
        print("Application Name", appNameVersion.getName());
        print("Application Version", appNameVersion.getVersion());
        print("Created", request.getTimeCreated());
        print("Submitted", request.getTimeSubmitted());
        System.out.println();
    }

}
