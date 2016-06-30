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

import java.util.Date;

import org.springframework.util.CollectionUtils;

import com.blackducksoftware.sdk.codecenter.cola.data.CatalogComponentSummary;
import com.blackducksoftware.sdk.codecenter.cola.data.ComponentType;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseSummary;
import com.blackducksoftware.sdk.codecenter.common.data.ApprovalStatusEnum;
import com.blackducksoftware.sdk.codecenter.vulnerability.data.VulnerabilityInfo;

public class CatalogComponentSearchSummary implements IComponentSearchSummary {

    private final CatalogComponentSummary comp;

    public CatalogComponentSearchSummary(CatalogComponentSummary comp) {
        this.comp = comp;
    }

    @Override
    public String getId() {
        return comp.getGenericCustomComponentIdToken().getGenericCustomComponentId();
    }

    @Override
    public String getName() {
        return comp.getNameVersion().getName();
    }

    @Override
    public String getVersion() {
        return comp.getNameVersion().getVersion();
    }

    @Override
    public String getDescription() {
        return comp.getDescription();
    }

    @Override
    public boolean isDeprecated() {
        return comp.isDeprecated();
    }

    @Override
    public ComponentType getType() {
        return comp.getCatalogComponentType();
    }

    @Override
    public Iterable<LicenseSummary> getLicenses() {
        return comp.getDeclaredLicenseList();
    }

    @Override
    public Date getReleaseDate() {
        return CollectionUtils.isEmpty(comp.getKbReleaseDate()) ? null : comp.getKbReleaseDate().get(0);
    }

    @Override
    public ApprovalStatusEnum getApprovalStatus() {
        return comp.getApprovalStatusValue() != null ? comp.getApprovalStatusValue() : ApprovalStatusEnum.PENDING;
    }

    @Override
    public Date getApprovalTime() {
        return comp.getApprovalTime();
    }

    @Override
    public boolean hasVulnerabilities() {
        return comp.isHasVulnerablity();
    }

    @Override
    public VulnerabilityInfo getVulnerabilityInfo() {
        return comp.getVulnerabilityInfo();
    }

    @Override
    public String getProgrammingLanguage() {
        return comp.getProgrammingLanguage();
    }

    @Override
    public String getHomepage() {
        return comp.getHomepage();
    }

    @Override
    public int getScore() {
        return comp.getScore();
    }

}
