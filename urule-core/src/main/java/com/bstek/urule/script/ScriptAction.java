package com.bstek.urule.script;

/**
 * 类注释
 *
 * @author wpx
 * @since 2021/1/11
 */
public interface ScriptAction {
    ScriptType getActionType();

    ScriptActionInvoker actionInvoker();
}
