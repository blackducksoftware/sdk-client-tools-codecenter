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
package com.blackducksoftware.sdk.codecenter.client.examples.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.client.examples.SamplePrinter;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Pair;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.BDEnumOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.MultiMapOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.PairOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.report.data.AndOrEnum;
import com.blackducksoftware.sdk.codecenter.report.data.RiskProfileOptions;
import com.blackducksoftware.sdk.codecenter.report.data.RiskProfileSummary;

public class SampleGetRiskProfileSummary extends SamplePrinter {

    public static void main(String[] args) {
        new SampleGetRiskProfileSummary(args).run();
    }

    private static final String[] PRIORITY_DESCRIPTIONS = { "", "Multiple high risk elements", "One high risk element", "One low risk element", "Ok to use" };

    @Argument(index = 3, required = false, multiValued = true, handler = PairOptionHandler.class, metaVar = "<applicationName> <applicationVersion>",
            usage = "the name and version of an application to include; includes all if none explicitly specified")
    protected List<Pair<String, String>> appNameVersionPairs;

    @Option(name = "-a", aliases = { "--attribute" }, handler = MultiMapOptionHandler.class, metaVar = "<attribute>=<value>",
            usage = "set the value of a custom attribute; may repeat a name to set multiple values")
    protected Map<String, Collection<String>> attributeMap;

    @Option(name = "--all", aliases = { "--all-components" }, usage = "report on all components, not just those in use")
    protected boolean allComponents = false;

    @Option(name = "-c", aliases = { "--category" }, usage = "only include components whose category contains this")
    protected String categorySearch;

    @Option(name = "-n", aliases = { "--name" }, usage = "only include components whose name contains this")
    protected String nameSearch;

    @Option(name = "-j", aliases = { "--join" }, handler = AndOrEnumOptionHandler.class, usage = "join logic between different risk category filters")
    private AndOrEnum joinLogic = AndOrEnum.AND;

    @Option(name = "-lh", aliases = { "--license-high", "--license-reciprocal" }, usage = "include components with high license risk (reciprocal license)")
    protected boolean licenseHigh;

    @Option(name = "-lm", aliases = { "--license-medium", "--license-weak-reciprocal" },
            usage = "include components with medium license risk (weak reciprocal license)")
    protected boolean licenseMedium;

    @Option(name = "-lo", aliases = { "--license-ok", "--license-permissive" }, usage = "include components with no license risk (permissive license)")
    protected boolean licenseLow;

    @Option(name = "-lu", aliases = { "--license-unknown" }, usage = "include components with unknown license risk")
    protected boolean licenseUnknown;

    @Option(name = "-sh", aliases = { "--security-high" }, usage = "include components with high security risk")
    protected boolean securityHigh;

    @Option(name = "-sm", aliases = { "--security-medium" }, usage = "include components with medium security risk")
    protected boolean securityMedium;

    @Option(name = "-sl", aliases = { "--security-low" }, usage = "include components with low security risk")
    protected boolean securityLow;

    @Option(name = "-so", aliases = { "--security-ok" }, usage = "include components with no security risk")
    protected boolean securityOk;

    @Option(name = "-dh", aliases = { "--diversity-high" }, usage = "include components with high version diversity risk (3+ versions) ")
    protected boolean diversityHigh;

    @Option(name = "-dm", aliases = { "--diversity-medium" }, usage = "include components with medium version diversity risk (2 versions) ")
    protected boolean diversityMedium;

    @Option(name = "-do", aliases = { "--diversity-ok", "-dl", "--diversity--low" }, usage = "include components with no version diversity risk (1 version)")
    protected boolean diversityOk;

    @Option(name = "-vh", aliases = { "--version-high" }, usage = "include components with high version age risk")
    protected boolean versionHigh;

    @Option(name = "-vm", aliases = { "--version-medium" }, usage = "include components with medium version age risk")
    protected boolean versionMedium;

    @Option(name = "-vl", aliases = { "--version-low" }, usage = "include components with low version age risk")
    protected boolean versionLow;

    @Option(name = "-vo", aliases = { "--version-ok" }, usage = "include components with no version age risk")
    protected boolean versionOk;

    @Option(name = "-vu", aliases = { "--version-unknown" }, usage = "include components with unknown version risk")
    protected boolean versionUnknown;

    @Option(name = "-r", aliases = { "--refresh", "--refresh-cache" }, usage = "refresh the monitor cache")
    protected boolean refresh = false;

    public SampleGetRiskProfileSummary(String[] args) {
        super(args);
    }

    @Override
    public final void run(CodeCenterServerProxy proxy) throws Exception {
        // Parse the attribute values
        List<AttributeValue> attributes = new ArrayList<>();

        if (attributeMap != null) {
            for (Entry<String, Collection<String>> attributeEntry : attributeMap.entrySet()) {
                String attributeName = attributeEntry.getKey();
                Collection<String> attributeValues = attributeEntry.getValue();

                AttributeNameToken nameToken = new AttributeNameToken();
                nameToken.setName(attributeName);

                AttributeValue attribute = new AttributeValue();
                attribute.setAttributeId(nameToken);
                attribute.getValues().addAll(attributeValues);

                attributes.add(attribute);
            }
        }

        // Create the risk profile options
        RiskProfileOptions options = new RiskProfileOptions();

        if (appNameVersionPairs != null) {
            for (Pair<String, String> appNameVersionPair : appNameVersionPairs) {
                ApplicationNameVersionToken appNameVersion = new ApplicationNameVersionToken();
                appNameVersion.setName(appNameVersionPair.getX());
                appNameVersion.setVersion(appNameVersionPair.getY());

                options.getApplicationNameVersionOrIdTokens().add(appNameVersion);
            }
        }

        options.getCustomAttributes().addAll(attributes);
        options.setShowOnlyComponentsInUse(!allComponents);
        options.setReleaseCategory(categorySearch);
        options.setReleaseName(nameSearch);

        options.setAndOrEnum(joinLogic);
        options.setLicenseRiskReciprocalLicense(licenseHigh);
        options.setLicenseRiskWeakReciprocalLicense(licenseMedium);
        options.setLicenseRiskPermissiveLicense(licenseLow);
        options.setLicenseRiskNonStandardOrUnknownLicense(licenseUnknown);
        options.setSecurityRiskHighSeverity(securityHigh);
        options.setSecurityRiskMediumSeverity(securityMedium);
        options.setSecurityRiskLowSeverity(securityLow);
        options.setSecurityRiskNoVulnerability(securityOk);
        options.setVersionsInUseThreeOrMoreVersions(diversityHigh);
        options.setVersionsInUseBetweenTwoAndThreeversions(diversityMedium);
        options.setVersionsInUseLessThanTwoVersions(diversityOk);
        options.setVersionStatusHigh(versionHigh);
        options.setVersionStatusMedium(versionMedium);
        options.setVersionStatusLow(versionLow);
        options.setVersionStatusOK(versionOk);
        options.setVersionStatusUnknown(versionUnknown);

        options.setRefreshMonitorCache(refresh);

        // Retrieve the risk profile summaries
        List<RiskProfileSummary> summaries = proxy.getReportApi().getRiskProfileSummary(options);

        // Print success information
        System.out.println((summaries.size() != 0 ? "Successfully r" : "R") + "etrieved " + summaries.size() + " summar" + (summaries.size() != 1 ? "ies" : "y")
                + (summaries.size() != 0 ? ":" : "."));
        System.out.println();

        // Print the risk profile summaries
        for (RiskProfileSummary summary : summaries) {
            print("Component", summary.getComponent());
            print("Priority", summary.getPriority() + " (" + PRIORITY_DESCRIPTIONS[summary.getPriority()] + ")");
            print("License Risk", summary.getLicenseRisk() + " (" + summary.getLicenseType() + ")");
            print("Security Risk", summary.getSecurityRisk());
            print("Versions Existing", summary.getVersionsInUse() + " ("
                    + (summary.getVersionsInUse() <= 1 ? "Ok" : summary.getVersionsInUse() == 2 ? "Medium risk" : "High risk") + ")");
            print("Version Status", summary.getVersionStatus() + " (" + summary.getUpToDate() + " version" + (summary.getUpToDate() != 1 ? "s" : "") + " behind"
                    + (summary.getVersionAge() >= 0 ? " and " + summary.getVersionAge() + " month" + (summary.getVersionAge() != 1 ? "s" : "") + " old" : "")
                    + ")");
            print("Oldest Version", summary.getOldestVersion());
            print("Oldest Release Date", summary.getOldestReleaseDate());
            print("Used By", summary.getInUseAnyStatusInBOM());
            if (!"unknown".equalsIgnoreCase(summary.getActivityStatus())) {
                print("Activity Status", summary.getActivityStatus());
            }
            if (summary.getContributors() > 0) {
                print("Contributors", summary.getContributors());
            }
            if (summary.getCommits() > 0) {
                print("Commits", summary.getCommits());
            }
            print("Last Commit", summary.getLastCommit());
            if (summary.getRating() != 0) {
                print("Rating", summary.getRating());
            }
            print("Maturity", summary.getMaturity());
            System.out.println();
        }
    }

    public static class AndOrEnumOptionHandler extends BDEnumOptionHandler<AndOrEnum> {

        public AndOrEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super AndOrEnum> setter) {
            super(parser, option, setter, false, AndOrEnum.class);
        }

    }

}
