package com.astrideug.base.reflections.transform.impl.config;

import com.astrideug.base.ProjectBase;
import com.astrideug.base.configuration.Config;
import com.astrideug.base.configuration.Configuration;
import com.astrideug.base.loader.ResolvedJar;
import com.astrideug.base.reflections.AdvancedReflections;
import com.astrideug.base.reflections.transform.ModuleTransformer;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ConfigTransformer implements ModuleTransformer {

    @Override
    @SneakyThrows
    public void transform(ResolvedJar resolvedJar, ProjectBase projectBase) {
        for (Field field : AdvancedReflections.getFieldsAnnotatedWith(projectBase.getClass(), Config.class)) {
            Config config = field.getAnnotation(Config.class);
            Configuration configuration = new Configuration(config.value());
            configuration.init(resolvedJar, config.value());

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(projectBase, configuration);
        }
    }
}
