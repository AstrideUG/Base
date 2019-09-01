package com.astrideug.base.reflections.transform.impl.chain;

import com.astrideug.base.Base;
import com.astrideug.base.ProjectBase;
import com.astrideug.base.chain.ChainManager;
import com.astrideug.base.loader.ResolvedJar;
import com.astrideug.base.reflections.AdvancedReflections;
import com.astrideug.base.reflections.transform.ModuleTransformer;
import me.helight.ccom.concurrency.Chain;

import java.util.ArrayList;

public class ChainTransformer implements ModuleTransformer {

    @Override
    public void transform(ResolvedJar resolvedJar, ProjectBase projectBase) {
        ArrayList<Class> chains = AdvancedReflections.getClassesImplementingType(resolvedJar.getClazzSave(), Chain.class);
        ChainManager chainManager = Base.getInstance().getChainManager();

        for (Class chain : chains) {
            chainManager.scan(chain);
        }
    }
}
