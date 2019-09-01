package com.astrideug.base.chain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InvokeChain {

    /**
     * Names of the Chains which are invoked afterwards
     */
    String[] value();

}
