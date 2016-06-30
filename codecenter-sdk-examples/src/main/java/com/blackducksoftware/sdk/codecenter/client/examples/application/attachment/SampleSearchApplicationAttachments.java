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
package com.blackducksoftware.sdk.codecenter.client.examples.application.attachment;

import java.util.Collection;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.springframework.util.StringUtils;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationAttachment;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.SampleSearch;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.AttachmentPageFilter;
import com.blackducksoftware.sdk.codecenter.user.data.User;

public class SampleSearchApplicationAttachments extends SampleSearch<ApplicationAttachment> {

    public static void main(String[] args) {
        new SampleSearchApplicationAttachments(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to search for attachments in")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the version of the application to search for attachments in")
    protected String applicationVersion;

    @Argument(index = 5, required = false, metaVar = "searchString", usage = "part of the attachment name to search for; omit to retrieve all")
    protected String searchString;

    public SampleSearchApplicationAttachments(String[] args) {
        super(args);
    }

    @Override
    protected Collection<ApplicationAttachment> search(CodeCenterServerProxy proxy) throws Exception {
        // We'll need this later; get the log statement out of the way now
        proxy.getUserApi();

        // Create the attachment page filter and application token
        AttachmentPageFilter pageFilter = new AttachmentPageFilter();
        pageFilter.setFirstRowIndex(0);
        pageFilter.setLastRowIndex(limit);

        ApplicationNameVersionToken appToken = new ApplicationNameVersionToken();
        appToken.setName(applicationName);
        appToken.setVersion(applicationVersion);

        // Search for attachments
        List<ApplicationAttachment> attachments = proxy.getApplicationApi().searchApplicationAttachments(searchString != null ? searchString : "", pageFilter,
                appToken);
        return attachments;
    }

    @Override
    protected String getSearchExpression() {
        return "attachments" + (searchString != null ? " matching \"" + searchString + "\"" : "") + " in application \"" + applicationName + "\" version \""
                + applicationVersion + "\"";
    }

    @Override
    protected void print(ApplicationAttachment attachment, CodeCenterServerProxy proxy) throws Exception {
        // Retrieve the uploader's details
        User uploader = proxy.getUserApi().getUser(attachment.getUserUploaded());

        // Print attachment details
        System.out.println("ID:           " + attachment.getId());
        System.out.println("Name:         " + attachment.getFileName());
        System.out.println("Type:         " + attachment.getContentType());
        System.out.println("Size (bytes): " + attachment.getFilesizeBytes());
        System.out.println("Uploaded by:  " + uploader.getFirstName() + " " + uploader.getLastName() + " (" + uploader.getName().getName() + ")");
        System.out.println("Uploaded at:  " + attachment.getTimeUploaded());
        if (StringUtils.hasText(attachment.getDescription())) {
            System.out.println("Description:\n" + attachment.getDescription());
        }
        System.out.println();
        System.out.println();
    }

}
