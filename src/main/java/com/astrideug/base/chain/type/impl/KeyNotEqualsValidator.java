package com.astrideug.base.chain.type.impl;

import com.astrideug.base.chain.type.Validateable;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.helight.ccom.concurrency.Environment;
import me.helight.ccom.concurrency.chain.EnvAdrr;

import java.util.Objects;

@Data
@AllArgsConstructor
public class KeyNotEqualsValidator implements Validateable {

    private EnvAdrr key;
    private Object value;

    @Override
    public boolean isValid(Environment environment) {
        return !Objects.equals(environment.getOrDefault(key.getValue(),null), value);
    }

}
