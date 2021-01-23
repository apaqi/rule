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
package com.bstek.urule.runtime.rete;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.rule.lhs.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public abstract class AbstractActivity implements Activity {
    private List<Path> paths;

    public List<Path> getPaths() {
        return paths;
    }

    public void addPath(Path path) {
        if (paths == null) {
            paths = new ArrayList<Path>();
        }
        this.paths.add(path);
    }

    protected List<FactTracker> visitPahs(EvaluationContext context, Object obj, FactTracker tracker, Map<String, Object> variableMap) {
        if (paths == null || paths.size() == 0) {
            return null;
        }
        List<FactTracker> trackers = null;
        int size = paths.size();
        Map<Integer,Integer> orExceptionData = new ConcurrentHashMap<>();
        Map<Integer,Integer> orSuccessData = new ConcurrentHashMap<>();
        for (int i = 0; i < size; i++) {
            Path path = paths.get(i);
            Collection<FactTracker> results = null;
            Activity activity = path.getTo();
            Junction parent = null;
            if(activity instanceof CriteriaActivity) {
                parent = ((CriteriaActivity) activity).getCriteria().getParent();
            }
            int parentHashCode = System.identityHashCode(parent);
            path.setPassed(true);
            if (size > 0) {
                Map<String, Object> newVariableMap = new HashMap<String, Object>();
                newVariableMap.putAll(variableMap);
                /**
                 * 解决“或”条件处理，异常导致后续“或”条件不执行的问题。如果不是最后一个“或”条件异常，则捕获掉此异常
                 */
                try {
                    results = activity.enter(context, obj, tracker.newSubFactTracker(), newVariableMap);
                } catch (Exception e) {
                    //Junction parent = ((CriteriaActivity) activity).getCriteria().getParent();
                    if(parent instanceof And) {
                        throw new RuleException("规则校验异常！");
                    }
                    if (parent instanceof Or){
                        orExceptionData.put(parentHashCode,parentHashCode);
                    }
                }
            } else {
                results = activity.enter(context, obj, tracker, variableMap);
            }
            if (results != null) {//match 场景
                if(parent instanceof Or) {
                    orSuccessData.put(parentHashCode,parentHashCode);
                }
                //check
                int nextPathParentHashCode = getNextPathObjAddress(i, size);
                if(null != parent && parent instanceof Or && (null == parent.getParent() || parentHashCode != nextPathParentHashCode) ) {
                    if (orExceptionData.containsKey(parentHashCode) && !orSuccessData.containsKey(parentHashCode)) {
                        throw new RuleException("规则校验异常！");
                    }
                }
                if (trackers == null) {
                    trackers = new ArrayList<FactTracker>();
                }
                trackers.addAll(results);
            }else {//mismatch 场景
                int nextPathParentHashCode = getNextPathObjAddress(i, size);
                //null == parent.getParent() && parent.getCriterions().size() -1 == i 单层或条件组，最后一个条件判断
                //parentHashCode != nextPathParentHashCode 多层或条件组，最后一个条件判断
                if(null != parent && parent instanceof Or && ((null == parent.getParent() && parent.getCriterions().size() -1 == i) || parentHashCode != nextPathParentHashCode) ) {
                    if (orExceptionData.containsKey(parentHashCode) && !orSuccessData.containsKey(parentHashCode)) {
                        throw new RuleException("规则校验异常！");
                    }
                }

            }
        }
        return trackers;
    }

    public abstract boolean orNodeIsPassed();

    public abstract void reset();

    /**
     * 获取下个path的对象地址
     *
     * @param i
     * @return
     */
    private int getNextPathObjAddress(int i, int size) {
        if (i + 1 >= size) {
            return -1;
        }
        Path nextPath = paths.get(i + 1);
        if (null != nextPath && nextPath.getTo() instanceof CriteriaActivity) {
            return System.identityHashCode(((CriteriaActivity) nextPath.getTo()).getCriteria().getParent());
        } else {
            return -1;
        }
    }

    private Junction parsePath(Junction parent) {
        if (parent instanceof Or) {
            Junction superParent = parent.getParent();
            if (null != superParent && superParent instanceof Or) {
                return parsePath(superParent);
            } else {
                return parent;
            }
        } else {
            return parent;
        }
    }

    /**
     * 计算或子节点数量
     *
     * @param criterions
     * @param i
     * @return
     */
    private int countChildNum(List<Criterion> criterions, int i) {
        if (!CollectionUtils.isEmpty(criterions)) {
            for (Criterion criterion : criterions) {
                if (criterion instanceof Or) {
                    List<Criterion> criterions1 = ((Or) criterion).getCriterions();
                    if (!CollectionUtils.isEmpty(criterions1)) {
                        i += criterions1.size();
                        countChildNum(criterions1, i);
                    } else {
                        return i;
                    }
                }
            }
        }
        return i;
    }

}
