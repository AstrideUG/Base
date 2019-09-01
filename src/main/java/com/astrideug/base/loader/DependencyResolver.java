package com.astrideug.base.loader;

import com.astrideug.base.loader.exception.NestedDependencyException;
import com.google.common.base.Joiner;

import java.util.ArrayList;

public class DependencyResolver {

    public static ArrayList<ResolvedJar> orderedLoad(ArrayList<ResolvedJar> deps) throws NestedDependencyException {
        ArrayList<String> resolved = new ArrayList<>();

        int nested = 30;
        while (deps.size() != resolved.size() && nested > 0) {
            nested--;
            for (ResolvedJar dep : deps) {
                if (resolved.contains(dep.getName())) {
                    continue;
                }

                if (dep.getDependencies().size() == 0) {
                    resolved.add(dep.getName());
                    System.out.println("###" + dep.getName()+" has no dependencies###");
                } else {
                    boolean depsResolved = true;
                    for (String string : dep.getDependencies()) {
                        if (!resolved.contains(string)) {
                            depsResolved = false;
                        }
                    }

                    if (depsResolved) {
                        resolved.add(dep.getName());
                        System.out.println("###Successfull Resolved Dependencies of " + dep.getName()+"###");
                    }
                }
            }
        }

        if (nested == 0) {
            ArrayList<String> absent = new ArrayList<>();
            for (ResolvedJar d : deps) {
                if (!resolved.contains(d.getName())) {
                    absent.add(d.getName());
                }
            }
            throw new NestedDependencyException("Dependencies are too nested or a absent. Following dependencies haven't been resolved: " + Joiner.on(", ").join(absent));
        }

        System.out.println(resolved.toString());

        ArrayList<ResolvedJar> ret = new ArrayList<>();
        for (String res : resolved) {
            for (ResolvedJar resolvedJar : deps) {
                if (resolvedJar.getName().equals(res)) {
                    ret.add(resolvedJar);
                }
            }
        }

        return ret;
    }

}
