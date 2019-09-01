package com.astrideug.base.chain;

import com.astrideug.base.chain.type.Validateable;
import lombok.Data;
import me.helight.ccom.concurrency.Chain;

import java.util.List;

@Data
public class ChainData {

    private String id;
    private List<String> hooks;
    private List<String> invokes;
    private Chain chain;
    private String interrupt;
    private List<Validateable> validateables;

}
