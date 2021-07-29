/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.runtime.assertor;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class AssertorEvaluator implements ApplicationContextAware {
    public static final String BEAN_ID = "urule.assertorEvaluator";
    private final static Map<Op, Assertor> ASSERTORS = new HashMap<>();


    public boolean evaluate(Object left, Object right, Datatype datatype, Op op) {
        Assertor targetAssertor = ASSERTORS.get(op);
        if (targetAssertor == null) {
            throw new RuleException("Unsupport op:" + op);
        }
        return targetAssertor.eval(left, right, datatype);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Collection<Assertor> assertors = applicationContext.getBeansOfType(Assertor.class).values();
        for (Assertor assertor : assertors) {
            for (Op op : Op.values()) {
                if (assertor.support(op)) {
                    ASSERTORS.put(op, assertor);
                }
            }
        }
    }
}
