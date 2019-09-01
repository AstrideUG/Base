package com.astrideug.base.chain;

import com.astrideug.base.chain.annotations.ChainId;
import com.astrideug.base.chain.annotations.HookChain;
import com.astrideug.base.chain.annotations.InterruptStack;
import com.astrideug.base.chain.annotations.InvokeChain;
import com.astrideug.base.chain.impl.LoggingChain;
import com.astrideug.base.chain.type.Validateable;
import com.astrideug.base.reflections.AdvancedReflections;
import lombok.SneakyThrows;
import me.helight.ccom.concurrency.Chain;
import me.helight.ccom.concurrency.Environment;
import me.helight.ccom.concurrency.chain.EnvAdrr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChainManager {

    public static void main(String[] args) {
        ChainManager chainManager = new ChainManager();
        chainManager.analyse(LoggingChain.class);
    }

    private List<ChainData> chains = Collections.synchronizedList(new ArrayList<>());

    public ChainManager() {
        scan(LoggingChain.class);

        invoke("log", Environment.create().env("doNotLog", true));
        invoke("log", Environment.create().env("doNotLog", false));
    }

    public void invoke(String chainId, Environment environment) {
        ChainData data = get(chainId);

        if (data == null) {
            throw new IllegalArgumentException("Given ChainID is not valid or was not found");
        }

        for (Validateable validateable : data.getValidateables()) {
            if (!validateable.isValid(environment)) {
                return;
            }
        }

        data.getChain().run(environment);

        boolean block = false;

        if (data.getInterrupt() != null) {
            Object o = environment.resolve(EnvAdrr.from(data.getInterrupt()));
            if (o != null) {
                Boolean b = (Boolean) o;
                if (b) block = true;
            }
        }

        for (ChainData chain : chains) {
            if (chain.getHooks().contains(data.getId())) {
                invoke(chain.getId(), environment);
            }
        }

        if (block) return;

        for (String string : data.getInvokes()) {
            invoke(string, environment);
        }
    }

    public ChainData get(String id) {
        for (ChainData chain : chains) {
            if (chain.getId().equals(id)) {
                return chain;
            }
        }
        return null;
    }

    public void scan(Class clazz) {
        ChainData data = analyse(clazz);
        if (data != null) {
            chains.add(data);
            System.out.println("~~Registered Chain " + data.getId());
        }
    }

    @SneakyThrows
    public ChainData analyse(Class clazz) {
        if (!clazz.isAnnotationPresent(ChainId.class)) {
            return null;
        }

        ChainId id = (ChainId) clazz.getAnnotationsByType(ChainId.class)[0];

        HookChain hook = AdvancedReflections.getAnnotationOrDefault(clazz, new HookChain(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return HookChain.class;
            }

            @Override
            public String[] value() {
                return new String[0];
            }
        }, HookChain.class);

        InvokeChain invoke = AdvancedReflections.getAnnotationOrDefault(clazz, new InvokeChain(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return InvokeChain.class;
            }

            @Override
            public String[] value() {
                return new String[0];
            }
        }, InvokeChain.class);

        InterruptStack interruptStack = AdvancedReflections.getAnnotationOrDefault(clazz, new InterruptStack(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return InterruptStack.class;
            }

            @Override
            public String value() {
                return null;
            }
        }, InterruptStack.class);

        Chain chain = (Chain) clazz.newInstance();

        List<Validateable> validateables = new ArrayList<>();
        for (Field field : AdvancedReflections.getFieldsImplementingType(clazz, Validateable.class)) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            validateables.add((Validateable)field.get(chain));
        }

        ChainData data = new ChainData();
        data.setChain(chain);
        data.setId(id.value());
        data.setHooks(Arrays.asList(hook.value()));
        data.setInvokes(Arrays.asList(invoke.value()));
        data.setInterrupt(interruptStack.value());
        data.setValidateables(validateables);

        return data;
    }

}
