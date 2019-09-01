package com.astrideug.base;

import org.bukkit.plugin.java.JavaPlugin;

public class BasePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new Base(this);
    }
}
