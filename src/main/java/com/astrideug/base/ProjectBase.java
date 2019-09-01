package com.astrideug.base;

import com.astrideug.base.loader.ResolvedJar;

public abstract class ProjectBase {

    public String name;
    public ResolvedJar resolvedJar;

    public abstract void initialise();

    public abstract void enable();

}
