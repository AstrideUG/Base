package com.astrideug.base.chain.type.impl;

import com.astrideug.base.chain.type.Validateable;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.helight.ccom.concurrency.Environment;
import me.helight.ccom.concurrency.chain.EnvAdrr;

import java.util.Objects;

@Data
@AllArgsConstructor
public class KeyExistsValidator implements Validateable {

    private EnvAdrr key;

    @Override
    public boolean isValid(Environment environment) {
        return environment.containsKey(key.getValue());
    }

}
