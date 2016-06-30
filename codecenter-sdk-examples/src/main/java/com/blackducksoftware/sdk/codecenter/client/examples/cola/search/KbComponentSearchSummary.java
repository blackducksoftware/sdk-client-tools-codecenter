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
import java.util.Date;

import com.blackducksoftware.sdk.codecenter.cola.data.ComponentType;
import com.blackducksoftware.sdk.codecenter.cola.data.KbComponentSummary;
import com.blackducksoftware.sdk.codecenter.cola.data.KbLicenseSummary;
import com.blackducksoftware.sdk.codecenter.cola.data.LicenseSummary;
import com.blackducksoftware.sdk.codecenter.common.data.ApprovalStatusEnum;
import com.blackducksoftware.sdk.codecenter.vulnerability.data.VulnerabilityInfo;

public class KbComponentSearchSummary implements IComponentSearchSummary {

    private final KbComponentSummary comp;

    private Collection<LicenseSummary> licenses;

    public KbComponentSearchSummary(KbComponentSummary comp) {
        this.comp = comp;
    }

    @Override
    public String getId() {
        return comp.getId().getId();
    }

    @Override
    public String getName() {
        return comp.getName();
    }

    @Override
    public String getVersion() {
        return comp.getVersionCount() + " Version" + (comp.getVersionCount() != 1 ? "s" : "");
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
        return isDeprecated() ? ComponentType.KB_DEPRECATED : ComponentType.STANDARD;
    }

    @Override
    public Iterable<LicenseSummary> getLicenses() {
        if (licenses == null) {
            licenses = new ArrayList<>();

            if (comp.getDeclaredLicenses() != null) {
                for (KbLicenseSummary kbLicense : comp.getDeclaredLicenses()) {
                    LicenseSummary license = new LicenseSummary();
                    license.setId(kbLicense.getId());
                    license.setNameToken(kbLicense.getNameToken());

                    licenses.add(license);
                }
            }
        }

        return licenses;
    }

    @Override
    public Date getReleaseDate() {
        return null;
    }

    @Override
    public ApprovalStatusEnum getApprovalStatus() {
        return null;
    }

    @Override
    public Date getApprovalTime() {
        return null;
    }

    @Override
    public boolean hasVulnerabilities() {
        return (getVulnerabilityInfo() != null) && (getVulnerabilityInfo().getHighPriorityCount() + getVulnerabilityInfo().getMediumPriorityCount()
                + getVulnerabilityInfo().getLowPriorityCount() > 0);
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
