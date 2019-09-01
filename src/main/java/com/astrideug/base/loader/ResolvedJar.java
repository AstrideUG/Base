package com.astrideug.base.loader;

import com.astrideug.base.Base;
import com.astrideug.base.ProjectBase;
import com.astrideug.base.reflections.AdvancedReflections;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

@Getter
public class ResolvedJar {

    private ArrayList<String> classNames;
    private ArrayList<JsonObject> jsonObjects;
    private JsonObject projectJson;
    private JarFile jarFile;
    private File file;

    private ArrayList<Class> clazzSave = new ArrayList<>();

    private String name;
    private ArrayList<String> dependencies;

    public ResolvedJar(ArrayList<String> classNames, ArrayList<JsonObject> jsonObjects, JsonObject projectJson, JarFile jarFile, File file) {
        this.classNames = classNames;
        this.jsonObjects = jsonObjects;
        this.projectJson = projectJson;
        this.jarFile = jarFile;
        this.file = file;

         name = projectJson.get("name").getAsString();
         dependencies = new ArrayList<>();
         for (JsonElement element : projectJson.getAsJsonArray("dependencies")) {
             dependencies.add(element.getAsString());
         }
    }

    @SneakyThrows
    public ArrayList<Class> load() {
        ArrayList<Class> classes = new ArrayList<>();
        URL[] urls = { new URL("jar:file:" + file+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls, Base.getInstance().getParentClassLoader());
        for (String className : classNames) {
            classes.add(cl.loadClass(className));
        }
        clazzSave.addAll(classes);
        return classes;
    }

    @SneakyThrows
    public ProjectBase instance() {
        ProjectBase module = (ProjectBase) AdvancedReflections.getClassesImplementingType(clazzSave, ProjectBase.class).get(0).newInstance();
        module.name = name;
        module.resolvedJar = this;
        return module;
    }

}
