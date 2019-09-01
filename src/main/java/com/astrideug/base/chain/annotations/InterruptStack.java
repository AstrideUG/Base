package com.astrideug.base.chain.annotations;

import me.helight.ccom.concurrency.chain.EnvAdrr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InterruptStack {

    /**
     * EnvAddress pointing to a boolean, indicating if the chain should stop at this part of the trace
     */
    String value();

}
