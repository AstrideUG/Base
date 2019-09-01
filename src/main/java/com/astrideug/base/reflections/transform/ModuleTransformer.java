package com.astrideug.base.reflections.transform;

import com.astrideug.base.ProjectBase;
import com.astrideug.base.loader.ResolvedJar;

public interface ModuleTransformer {

    void transform(ResolvedJar resolvedJar, ProjectBase projectBase);

}
