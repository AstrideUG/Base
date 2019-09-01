package com.astrideug.base.loader;

import com.astrideug.base.reflections.AdvancedReflections;
import com.astrideug.base.Base;
import com.astrideug.base.ProjectBase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

@AllArgsConstructor
public class JarLoader {

    @SneakyThrows
    public ResolvedJar getJar(File jar) {

        JarFile jarFile = new JarFile(jar);
        URL[] urls = { new URL("jar:file:" + jar+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls , Base.class.getClassLoader());

        System.out.println("=> Loading Module " + jar);

        ArrayList<String> classNames = new ArrayList<>();
        ArrayList<JsonObject> jsonObjects = new ArrayList<>();
        JsonObject projectJson = null;

        JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jar));

        ZipEntry je;
        while ((je = jarInputStream.getNextEntry()) != null) {

            if(je.isDirectory()){
                continue;
            }

            String jarEntryName = je.getName();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = jarInputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            if (je.getName().endsWith("project.json")) {
                Gson gson = new Gson();
                projectJson = gson.fromJson(new JsonReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray()))), JsonObject.class);
                continue;
            } else if (je.getName().endsWith(".json")) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(new JsonReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray()))), JsonObject.class);
                jsonObjects.add(jsonObject);
                continue;
            }

            if (!je.getName().endsWith(".class")) {
                continue;
            }

            String clazzName = jarEntryName.replace(".class", "");
            clazzName = clazzName.replace("/", ".");

            byte[] classbytes = buffer.toByteArray();
            Base.getInstance().getParentClassLoader().setClassContent(classbytes);
            Base.getInstance().getParentClassLoader().setClassContent(clazzName, classbytes);

            try {
                String className = je.getName().substring(0,je.getName().length()-6);
                classNames.add(className.replace('/', '.'));

            } catch (Exception ignore) { }


        }

        return new ResolvedJar(classNames, jsonObjects, projectJson, jarFile, jar);
    }

    @SneakyThrows
    public ProjectBase constructModule(File jar) {
        return (ProjectBase) AdvancedReflections.getClassesImplementingType(getJar(jar).load(), ProjectBase.class).get(0).newInstance();
    }

}
