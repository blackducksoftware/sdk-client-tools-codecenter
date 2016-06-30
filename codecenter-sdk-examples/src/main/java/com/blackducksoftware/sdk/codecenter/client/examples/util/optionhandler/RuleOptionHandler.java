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

import java.lang.reflect.Field;
import java.util.ResourceBundle;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.FieldSetter;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.blackducksoftware.sdk.codecenter.approval.data.Condition;
import com.blackducksoftware.sdk.codecenter.approval.data.Rule;
import com.blackducksoftware.sdk.codecenter.client.examples.util.Pair;
import com.blackducksoftware.sdk.codecenter.client.examples.util.ShiftableParameters;

public class RuleOptionHandler extends OptionHandler<Rule> {

    protected final ConditionOptionHandler dummyConditionOptionHandler;

    public RuleOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Rule> setter) {
        super(parser, option, setter);
        dummyConditionOptionHandler = new ConditionOptionHandler(parser, option, null);
    }

    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        ShiftableParameters shiftableParams = ShiftableParameters.of(params);
        Rule rule = new Rule();

        while ((shiftableParams.size() > 0) && (!shiftableParams.getParameter(0).startsWith("-"))) {
            Pair<Condition, Integer> conditionResult = parseCondition(shiftableParams);
            rule.getConditions().add(conditionResult.getX());
            shiftableParams.shift(conditionResult.getY());
        }

        setter.addValue(rule);
        return shiftableParams.getOffset();
    }

    protected final Pair<Condition, Integer> parseCondition(Parameters params) throws CmdLineException {
        ConditionReceiver conditionReceiver = new ConditionReceiver();
        int ofs = new ConditionOptionHandler(owner, option, conditionReceiver).parseArguments(params);
        return new Pair<>(conditionReceiver.getValue(), ofs);
    }

    @Override
    public String getDefaultMetaVariable() {
        return dummyConditionOptionHandler.getDefaultMetaVariable() + " ...";
    }

    @Override
    public String getMetaVariable(ResourceBundle rb) {
        return getDefaultMetaVariable();
    }

    private static class ConditionReceiver implements Setter<Condition> {

        private Condition condition;

        public ConditionReceiver() {}

        public Condition getValue() {
            return condition;
        }

        @Override
        public void addValue(Condition value) throws CmdLineException {
            condition = value;
        }

        @Override
        public Class<Condition> getType() {
            return Condition.class;
        }

        @Override
        public boolean isMultiValued() {
            return false;
        }

        @Override
        public FieldSetter asFieldSetter() {
            return new FieldSetter(this, asAnnotatedElement());
        }

        @Override
        public Field asAnnotatedElement() {
            try {
                return this.getClass().getField("condition");
            } catch (NoSuchFieldException | SecurityException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
