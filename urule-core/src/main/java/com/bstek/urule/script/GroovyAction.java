package com.bstek.urule.script;

/**
 * 类注释
 *
 * @author wpx
 * @since 2021/1/14
 */
public class GroovyAction  extends AbstractScriptAction{
    /**groovy 脚本*/
    private String expression;
    /**groovy 方法名称*/
    private String scriptMethodName;

    @Override
    protected GroovyActionInvoker newActionInvoker() {
        return new GroovyActionInvoker(this);
    }

    @Override
    public ScriptType getActionType() {
        return ScriptType.GROOVY;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getScriptMethodName() {
        return scriptMethodName;
    }

    public void setScriptMethodName(String scriptMethodName) {
        this.scriptMethodName = scriptMethodName;
    }
}
