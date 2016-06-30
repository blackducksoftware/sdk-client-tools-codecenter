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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationAttachmentToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;
import com.blackducksoftware.sdk.codecenter.common.data.AttachmentContent;

/**
 * A sample that retrieves an attachment from an application.
 *
 * <p>
 * Requires:
 * </p>
 * <ul>
 * <li>An application which already exists on the server with the attachment to retrieve.</li>
 * </ul>
 *
 * <p>
 * Demonstrates how to:
 * </p>
 * <ul>
 * <li>Retrieve the content of an attachment to an application.</li>
 * </ul>
 */
public class SampleGetApplicationAttachmentContent extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleGetApplicationAttachmentContent(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to retrieve the attachment from")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the application version")
    protected String applicationVersion;

    @Argument(index = 5, required = true, metaVar = "attachmentName", usage = "the name of the attachment to retrieve")
    protected String attachmentName;

    @Argument(index = 6, required = false, metaVar = "saveLocation", usage = "the local path to save the attachment to (including the filename)")
    protected String saveLocation;

    public SampleGetApplicationAttachmentContent(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the attachment token
        ApplicationNameVersionToken appToken = new ApplicationNameVersionToken();
        appToken.setName(applicationName);
        appToken.setVersion(applicationVersion);

        ApplicationAttachmentToken token = new ApplicationAttachmentToken();
        token.setApplicationId(appToken);
        token.setFileName(attachmentName);

        // Retrieve the attachment content
        AttachmentContent attachmentContent = proxy.getApplicationApi().getApplicationAttachmentContent(token);

        // Write the attachment content
        if (saveLocation != null) {
            try (OutputStream outputStream = new FileOutputStream(new File(saveLocation))) {
                attachmentContent.getAttachmentContent().writeTo(outputStream);
            }

            // Print success information
            System.out.println("Successfully retrieved \"" + attachmentName + "\" from application \"" + applicationName + "\" version \"" + applicationVersion
                    + "\" to \"" + saveLocation + "\".");

        } else {
            attachmentContent.getAttachmentContent().writeTo(System.out);
        }
    }

}
