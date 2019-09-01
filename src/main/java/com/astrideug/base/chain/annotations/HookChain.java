package com.astrideug.base.chain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HookChain {

    /**
     * Names of the Chains after which this is invoked
     */
    String[] value();

}
