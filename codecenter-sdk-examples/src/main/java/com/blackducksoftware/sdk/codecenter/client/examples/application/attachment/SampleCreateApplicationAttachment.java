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

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.kohsuke.args4j.Argument;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationAttachmentCreate;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.client.examples.BDCodeCenterSample;
import com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler.FileDataSourceOptionHandler;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

public class SampleCreateApplicationAttachment extends BDCodeCenterSample {

    public static void main(String[] args) {
        new SampleCreateApplicationAttachment(args).run();
    }

    @Argument(index = 3, required = true, metaVar = "applicationName", usage = "the name of the application to add the attachment to")
    protected String applicationName;

    @Argument(index = 4, required = true, metaVar = "applicationVersion", usage = "the application version")
    protected String applicationVersion;

    @Argument(index = 5, required = true, handler = FileDataSourceOptionHandler.class, metaVar = "path", usage = "the local path of the attachment to add")
    protected FileDataSource ds;

    @Argument(index = 6, required = false, metaVar = "description", usage = "the description to give to the attachment")
    protected String descripion;

    @Argument(index = 7, required = false, metaVar = "attachmentName", usage = "the filename to give to the attachment")
    protected String attachmentName;

    public SampleCreateApplicationAttachment(String[] args) {
        super(args);
    }

    @Override
    public void run(CodeCenterServerProxy proxy) throws Exception {
        // Create the attachment create bean
        String path = ds.getFile().getPath();

        ApplicationNameVersionToken appToken = new ApplicationNameVersionToken();
        appToken.setName(applicationName);
        appToken.setVersion(applicationVersion);

        ApplicationAttachmentCreate attachmentCreateBean = new ApplicationAttachmentCreate();
        attachmentCreateBean.setApplicationId(appToken);
        attachmentCreateBean.setAttachmentContent(new DataHandler(ds));
        attachmentCreateBean.setDescription(descripion);
        attachmentCreateBean.setFileName(path);
        attachmentCreateBean.setName(attachmentName != null ? attachmentName : ds.getName());

        // Add the attachment
        String attachmentId = proxy.getApplicationApi().createApplicationAttachment(attachmentCreateBean);

        // Print success information
        System.out.println("Successfully added attachment \"" + path + "\"" + (attachmentName != null ? " as \"" + attachmentName + "\"" : "")
                + " to application \"" + applicationName + "\" version \"" + applicationVersion + "\" with attachment ID " + attachmentId + ".");
    }

}
