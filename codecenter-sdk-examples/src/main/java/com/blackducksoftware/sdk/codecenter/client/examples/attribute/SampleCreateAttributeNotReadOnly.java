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
package com.blackducksoftware.sdk.codecenter.client.examples.attribute;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.blackducksoftware.sdk.codecenter.attribute.data.AbstractAttribute;

public abstract class SampleCreateAttributeNotReadOnly<T extends AbstractAttribute> extends SampleCreateAttribute<T> {

    @Argument(index = 5, required = true, metaVar = "question", usage = "the question to present")
    protected String question;

    @Option(name = "-r", aliases = { "--required" }, usage = "make required")
    protected boolean isRequired = false;

    public SampleCreateAttributeNotReadOnly(String[] args) {
        super(args);
    }

    @Override
    protected final T createAttribute() throws Exception {
        T attribute = createAttribute_();
        attribute.setQuestion(question);
        attribute.setRequired(isRequired);

        return attribute;
    }

    protected abstract T createAttribute_() throws Exception;

}
