package com.astrideug.base.reflections.transform.impl.spigotlistener;

import com.astrideug.base.Base;
import com.astrideug.base.ProjectBase;
import com.astrideug.base.loader.ResolvedJar;
import com.astrideug.base.reflections.AdvancedReflections;
import com.astrideug.base.reflections.transform.ModuleTransformer;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class SubListenerTransformer implements ModuleTransformer {

    @Override
    @SneakyThrows
    public void transform(ResolvedJar resolvedJar, ProjectBase projectBase) {
        ArrayList<Class> listeners = AdvancedReflections.getClassesImplementingType(resolvedJar.getClazzSave(), Listener.class);
        ArrayList<Class> toSub = AdvancedReflections.getClassesAnnotated(listeners, SubListener.class);

        for (Class aClass : toSub) {
            Listener listener = (Listener) aClass.newInstance();
            Bukkit.getPluginManager().registerEvents(listener, Base.getInstance().getPlugin());
        }

    }


}
