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

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Setter;
import org.springframework.util.StringUtils;

import com.blackducksoftware.sdk.codecenter.client.examples.SampleSearch;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Printer;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentSearchTypeEnum;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseSummary;

public abstract class SampleSearchComponents<T extends IComponentSearchSummary> extends SampleSearch<T> {

    @Option(name = "-dep", aliases = { "--include-deprecated" }, usage = "include deprecated components in the search")
    protected boolean includeDeprecated = false;

    public SampleSearchComponents(String[] args) {
        super(args);
    }

    @Override
    public final void print(T component, CodeCenterServerProxy proxy) {
        // Print component details
        print("Name", component.getName());
        print("Version", component.getVersion());
        print("Declared Licenses", component.getLicenses(), new Printer<LicenseSummary>() {
            @Override
            public String print(LicenseSummary license) {
                return license.getNameToken().getName();
            }
        });
        print("Type", component.getType());
        print("Release Date", component.getReleaseDate());
        if (component.getApprovalStatus() != null) {
            print("Approval Status", BDEnumOptionHandler.enumToString(component.getApprovalStatus())
                    + (component.getApprovalTime() != null ? " on " + getDateFormat().format(component.getApprovalTime()) : ""));
        }
        print("Vulnerabilities",
                (component.hasVulnerabilities() ? component.getVulnerabilityInfo() != null
                        ? component.getVulnerabilityInfo().getHighPriorityCount() + " high, " + component.getVulnerabilityInfo().getMediumPriorityCount()
                                + " medium, " + component.getVulnerabilityInfo().getLowPriorityCount() + " low"
                        : "yes" : "none"));
        print("Programming Language", component.getProgrammingLanguage());
        print("Homepage", component.getHomepage());
        print("Description", component.getDescription());
        System.out.println();
    }

    @Override
    protected final String getSearchExpression() {
        String searchCondition = getSearchCondition();
        return getComponentSearchType() + " components" + (StringUtils.hasText(searchCondition) ? " " + searchCondition : "");
    }

    protected abstract String getComponentSearchType();

    protected abstract String getSearchCondition();

    public static class ComponentSearchTypeEnumOptionHandler extends BDEnumOptionHandler<ComponentSearchTypeEnum> {

        public ComponentSearchTypeEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super ComponentSearchTypeEnum> setter) {
            super(parser, option, setter, false, ComponentSearchTypeEnum.class);
        }

    }

}
