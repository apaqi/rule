package com.bstek.urule.script;

import com.google.common.collect.ImmutableMap;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.transform.ThreadInterrupt;
import groovy.transform.TimedInterrupt;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.kohsuke.groovy.sandbox.GroovyInterceptor;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 类注释
 *
 * @author wpx
 * @since 2021/1/14
 */
public class GroovyActionInvoker  extends AbstractScriptActionInvoker {
    private final ScriptAction action;
    private GroovyObject groovyObject;
    private String scriptMethodName = "groovyActionHandle";
    private final SandBox sandBox = new SandBox();
    protected GroovyActionInvoker(GroovyAction action) {
        super(action);
        this.action = action;
        this.groovyObject = compile(action.getExpression());
        this.scriptMethodName = action.getScriptMethodName();
    }

    @Override
    public Object invoke(Object context) {
        if (this.groovyObject == null) {
            return null;
        }
        return sandBox.sandBoxRun(() -> (Boolean) InvokerHelper.invokeMethodSafe(groovyObject, scriptMethodName, context));
    }

    @Override
    public ScriptAction getAction() {
        return action;
    }


    private GroovyObject compile(final String groovyScript) {
        try {
            final CompilerConfiguration compilerConfiguration = createCompilerConfiguration();
            final GroovyClassLoader loader = new GroovyClassLoader(GroovyActionInvoker.class.getClassLoader(), compilerConfiguration);
            final Class groovyClass = loader.parseClass(groovyScript);
            return (GroovyObject) groovyClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Compile Groovy Script 失败! scriptContent:" + groovyScript, e);
        }
    }

    private CompilerConfiguration createCompilerConfiguration() {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setTargetBytecode(CompilerConfiguration.JDK8);

        fillImportCustomizer(config);
//        fillTransformationCustomizer(config);
        fillSecureASTCustomizer(config);
        config.addCompilationCustomizers(new SandboxTransformer());
        sandBox.openSandBox();
        return config;
    }

    private void fillTransformationCustomizer(CompilerConfiguration config) {
        config.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("value", 1);
//        builder.put("unit", TimeUnit.SECONDS);
        Map<String, Object> timeoutArgs = builder.build();
        config.addCompilationCustomizers(new ASTTransformationCustomizer(timeoutArgs, TimedInterrupt.class));
    }

    private void fillImportCustomizer(CompilerConfiguration config) {
        ImportCustomizer imports = new ImportCustomizer();
        imports.addStaticStars("java.util.Map");
        config.addCompilationCustomizers(imports);
    }

    private void fillSecureASTCustomizer(CompilerConfiguration config) {
        // 创建SecureASTCustomizer
        final SecureASTCustomizer secure = new SecureASTCustomizer();
        secure.setClosuresAllowed(true);
        secure.setMethodDefinitionAllowed(true);
        config.addCompilationCustomizers(secure);
    }

    public String getScriptMethodName() {
        return scriptMethodName;
    }

    public void setScriptMethodName(String scriptMethodName) {
        this.scriptMethodName = scriptMethodName;
    }

    private static class SandBox {

        private boolean openFlag;
        private NoSystemExitSandbox noSystemExitSandbox;
        private NoRunTimeSandbox noRunTimeSandbox;

        public SandBox() {

        }

        public void openSandBox() {
            this.openFlag = true;
            this.noSystemExitSandbox = new NoSystemExitSandbox();
            this.noRunTimeSandbox = new NoRunTimeSandbox();
        }

        private void beforeRunSandBox() {
            if (this.openFlag) {
                noSystemExitSandbox.register();
                noRunTimeSandbox.register();
            }
        }

        public <T> T sandBoxRun(Callable<T> call) {
            try {
                beforeRunSandBox();
                return call.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                afterRunSandBox();
            }
        }

        private void afterRunSandBox() {
            if (this.openFlag) {
                noSystemExitSandbox.unregister();
                noRunTimeSandbox.unregister();
            }
        }

        private static class NoSystemExitSandbox extends GroovyInterceptor {
            @Override
            public Object onStaticCall(GroovyInterceptor.Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
                if (receiver == System.class && "exit".equals(method)) {
                    throw new SecurityException("No call on System.exit() please");
                }
                return super.onStaticCall(invoker, receiver, method, args);
            }
        }

        private static class NoRunTimeSandbox extends GroovyInterceptor {
            @Override
            public Object onStaticCall(GroovyInterceptor.Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
                if (receiver == Runtime.class) {
                    throw new SecurityException("No call on RunTime please");
                }
                return super.onStaticCall(invoker, receiver, method, args);
            }
        }

    }
}
