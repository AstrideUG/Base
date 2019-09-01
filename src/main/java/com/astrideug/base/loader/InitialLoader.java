package com.astrideug.base.loader;

import com.astrideug.base.Base;
import com.astrideug.base.ProjectBase;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;

@AllArgsConstructor
public class InitialLoader {

    @SneakyThrows
    public void initModules() {
        File dir = new File("modules/");

        if (!dir.exists()){
            dir.mkdirs();
        }

        File[] files = dir.listFiles();
        if (files == null) return;

        ArrayList<ResolvedJar> jars = new ArrayList<>();

        JarLoader jarLoader = Base.getInstance().getJarLoader();

        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                ResolvedJar resolvedJar = jarLoader.getJar(file);
                jars.add(resolvedJar);
            }
        }


        ArrayList<ProjectBase> modules = new ArrayList<>();
        ArrayList<ResolvedJar> sorted = DependencyResolver.orderedLoad(jars);

        for (ResolvedJar jar : sorted) {
            jar.load();
            ProjectBase module = jar.instance();
            modules.add(module);
        }

        Base.getInstance().getProjectBases().clear();
        Base.getInstance().getProjectBases().addAll(modules);

        for (ProjectBase module : modules) {
            module.initialise();
        }

        for (ProjectBase module : modules) {
            Base.getInstance().getModuleLoader().loadModule(module);
        }

        for (ProjectBase module : modules) {
            Base.getInstance().getPOOL().execute(module::enable);
        }
    }
}
