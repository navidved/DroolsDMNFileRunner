package com.fanavaran.main.lib;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.impl.DMNRuntimeImpl;
import org.kie.internal.builder.InternalKieBuilder;
import org.kie.api.io.Resource;
import org.kie.api.builder.Message;
import java.math.BigDecimal;
import java.util.UUID;
import org.kie.dmn.core.compiler.RuntimeTypeCheckOption;
import org.kie.dmn.core.api.DMNFactory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Assert;

public class DMNHelper {

    static String DMNModelName;
    static String DMNFilePath;

    public DMNHelper(String DMNName){
        DMNModelName = "../dmn/" + DMNName;
        DMNFilePath = "../dmn/" + DMNName + ".dmn";
    }

    public void executeTest(final int age, final int yearsService, final int expectedVacationDays) {
        final DMNRuntime runtime = createRuntime(DMNFilePath, this.getClass());
        final DMNModel dmnModel = runtime.getModel("https://www.drools.org/kie-dmn", DMNModelName);
        assertThat(dmnModel, notNullValue());
        final DMNContext context = DMNFactory.newContext();
        context.set("Age", age);
        context.set("Years of Service", yearsService);
        final DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        final DMNContext result = dmnResult.getContext();
        System.out.println("Total Vacation Days: " + result.get("Total Vacation Days"));
        assertThat(result.get("Total Vacation Days"), is(BigDecimal.valueOf(expectedVacationDays)));
    }

    public static DMNRuntime createRuntime(final String resourceName, final Class<?> testClass) {
        final KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId("org.kie", "dmn-test-" + UUID.randomUUID(), "1.0");
        Resource resource = ks.getResources().newClassPathResource(resourceName, testClass);
        final KieContainer kieContainer = getKieContainer(releaseId, resource);
        final DMNRuntime runtime = typeSafeGetKieRuntime(kieContainer);
        Assert.assertNotNull(runtime);
        return runtime;
    }

    public static DMNRuntime typeSafeGetKieRuntime(final KieContainer kieContainer) {
        DMNRuntime dmnRuntime = kieContainer.newKieSession().getKieRuntime(DMNRuntime.class);
        ((DMNRuntimeImpl) dmnRuntime).setOption(new RuntimeTypeCheckOption(true));
        return dmnRuntime;
    }

    public static KieContainer getKieContainer(ReleaseId releaseId, Resource... resources) {
        KieServices ks = KieServices.Factory.get();
        createJar(ks, releaseId, resources);
        return ks.newKieContainer(releaseId);
    }

    public static byte[] createJar(KieServices ks, ReleaseId releaseId, Resource... resources) {
        KieFileSystem kfs = ks.newKieFileSystem().generateAndWritePomXML(releaseId);
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] != null) {
                kfs.write(resources[i]);
            }
        }
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        ((InternalKieBuilder) kieBuilder).buildAll(o -> true);
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException(results.getMessages(Message.Level.ERROR).toString());
        }
        InternalKieModule kieModule = (InternalKieModule) ks.getRepository().getKieModule(releaseId);
        byte[] jar = kieModule.getBytes();
        return jar;
    }
}
