package com.astrideug.base.chain.impl;

import com.astrideug.base.chain.annotations.ChainId;
import com.astrideug.base.chain.type.Validateable;
import com.astrideug.base.chain.type.impl.KeyNotEqualsValidator;
import me.helight.ccom.concurrency.Chain;
import me.helight.ccom.concurrency.Environment;
import me.helight.ccom.concurrency.chain.EnvAdrr;

@ChainId("log")
public class LoggingChain extends Chain {

    private Validateable noLogCheck = new KeyNotEqualsValidator(EnvAdrr.from("doNotLog"), true);

    public LoggingChain() {
        consume(System.out::println, Environment.class).adresses(EnvAdrr.from("env"));
    }

}
