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
package com.blackducksoftware.sdk.codecenter.client.examples.util.optionhandler;

import java.lang.reflect.Method;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

public abstract class BDEnumOptionHandler<E extends Enum<E>> extends OptionHandler<E> {

    public static <E extends Enum<E>> String enumToString(E value) {
        return enumToString(value, true);
    }

    public static <E extends Enum<E>> String enumToString(E value, boolean underscoresToSpaces) {
        String toString = value.toString();

        if (!value.name().equals(value.toString())) {
            return toString;
        }

        Method toStringMethod;

        try {
            toStringMethod = value.getClass().getMethod("toString");
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }

        if (toStringMethod.getDeclaringClass().equals(Enum.class)) {
            return enumNameToString(toString, underscoresToSpaces);
        } else {
            return toString;
        }
    }

    public static String enumNameToString(String name) {
        return enumNameToString(name, true);
    }

    public static String enumNameToString(String name, boolean underscoresToSpaces) {
        name = name.toLowerCase();
        return underscoresToSpaces ? name.replaceAll("_+", " ").trim() : name;
    }

    @SafeVarargs
    public static <E extends Enum<E>> E stringToEnum(String str, E... allowedEnumConstants) {
        String alphanumericStr = getAlphanumericOnly(str);

        for (E testEnum : allowedEnumConstants) {
            String alphanumericEnumName = getAlphanumericOnly(testEnum.name());
            if (alphanumericEnumName.equalsIgnoreCase(alphanumericStr)) {
                return testEnum;
            }

            String alphanumericEnumStr = getAlphanumericOnly(testEnum.toString());
            if (alphanumericEnumStr.equalsIgnoreCase(alphanumericStr)) {
                return testEnum;
            }
        }

        return null;
    }

    public static String getAlphanumericOnly(CharSequence cs) {
        String normalized = Normalizer.normalize(cs, Form.NFD);
        String result = normalized.replaceAll("[^A-Za-z0-9]", "");
        return result;
    }

    private final E[] allowedEnumConstants;

    public BDEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super E> setter, Class<E> enumType) {
        this(parser, option, setter, true, enumType);
    }

    public BDEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super E> setter, boolean sortEnumConstants, Class<E> enumType) {
        this(parser, option, setter, sortEnumConstants, enumType.getEnumConstants());
    }

    @SafeVarargs
    public BDEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super E> setter, E... sortedAllowedEnumConstants) {
        this(parser, option, setter, false, sortedAllowedEnumConstants);
    }

    @SafeVarargs
    public BDEnumOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super E> setter, boolean sortEnumConstants, E... allowedEnumConstants) {
        super(parser, option, setter);

        if (allowedEnumConstants.length == 0) {
            throw new IllegalArgumentException("A " + BDEnumOptionHandler.class.getSimpleName() + " must allow at least one enum constant");
        }

        this.allowedEnumConstants = allowedEnumConstants.clone();

        if (sortEnumConstants) {
            Arrays.sort(allowedEnumConstants, new Comparator<E>() {
                @Override
                public int compare(E e1, E e2) {
                    return BDEnumOptionHandler.this.toString(e1).compareToIgnoreCase(BDEnumOptionHandler.this.toString(e2));
                }
            });
        }
    }

    public final E[] getAllowedEnumConstants() {
        return allowedEnumConstants.clone();
    }

    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        String param = params.getParameter(0);
        E value = toEnum(param);

        if (value == null) {
            if (option.isArgument()) {
                throw new CmdLineException(owner, Messages.ILLEGAL_OPERAND, option.toString(), param);
            } else {
                throw new CmdLineException(owner, Messages.ILLEGAL_OPERAND, params.getParameter(-1), param);
            }
        }

        setter.addValue(value);
        return 1;
    }

    @Override
    public String getDefaultMetaVariable() {
        StringBuffer sb = new StringBuffer();

        sb.append("(");

        for (E value : allowedEnumConstants) {
            String enumToString = toString(value);
            String quote = enumToString.indexOf(' ') >= 0 ? "\"" : "";
            sb.append(quote).append(enumToString).append(quote).append(" | ");
        }
        sb.delete(sb.length() - 3, sb.length());

        sb.append(")");

        return sb.toString();
    }

    @Override
    public String getMetaVariable(ResourceBundle rb) {
        return getDefaultMetaVariable();
    }

    @Override
    public final String print(E value) {
        return toString(value);
    }

    protected E toEnum(String str) {
        String alphanumericStr = getAlphanumericOnly(str);

        for (E testEnum : getAllowedEnumConstants()) {
            String alphanumericEnumStr = getAlphanumericOnly(toString(testEnum));
            if (alphanumericEnumStr.equalsIgnoreCase(alphanumericStr)) {
                return testEnum;
            }
        }

        return null;
    }

    protected String toString(E value) {
        return enumToString(value);
    }

}
