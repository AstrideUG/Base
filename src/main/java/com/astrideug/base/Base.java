package com.astrideug.base;

import com.astrideug.base.chain.ChainManager;
import com.astrideug.base.loader.ClazzLoader;
import com.astrideug.base.loader.InitialLoader;
import com.astrideug.base.loader.JarLoader;
import com.astrideug.base.loader.ModuleLoader;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class Base {

    public static final ExecutorService PublicPool = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    @Getter
    private static Base instance;

    private final ExecutorService POOL = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    private ClazzLoader parentClassLoader = new ClazzLoader();
    private ModuleLoader moduleLoader = new ModuleLoader();
    private JarLoader jarLoader = new JarLoader();
    private ChainManager chainManager = new ChainManager();
    private InitialLoader initialLoader = new InitialLoader();
    private List<ProjectBase> projectBases = Collections.synchronizedList(new ArrayList<>());
    private Plugin plugin;

    public Base(Plugin plugin) {
        this.plugin = plugin;
        instance = this;

        initialLoader.initModules();

    }

    public static void cconstruct(Object object) {

    }

}
