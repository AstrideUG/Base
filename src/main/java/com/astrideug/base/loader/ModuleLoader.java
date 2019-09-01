package com.astrideug.base.loader;

import com.astrideug.base.ProjectBase;
import com.astrideug.base.reflections.transform.ModuleTransformer;
import com.astrideug.base.reflections.transform.impl.chain.ChainTransformer;
import com.astrideug.base.reflections.transform.impl.config.ConfigTransformer;
import com.astrideug.base.reflections.transform.impl.spigotlistener.SubListenerTransformer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleLoader {

    @Getter
    private List<ModuleTransformer> moduleTransformers = Collections.synchronizedList(new ArrayList<>());

    public ModuleLoader() {
        moduleTransformers.add(new SubListenerTransformer());
        moduleTransformers.add(new ChainTransformer());
        moduleTransformers.add(new ConfigTransformer());
    }

    @SneakyThrows
    public void loadModule(ProjectBase module) {
        ResolvedJar resolvedJar = module.resolvedJar;

        for (ModuleTransformer moduleTransformer : moduleTransformers) {
            moduleTransformer.transform(resolvedJar,module);
        }
    }

    @SneakyThrows
    public void unloadModule(ProjectBase module) {

    }

}
