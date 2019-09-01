package com.astrideug.base.loader;

import com.astrideug.base.Base;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;

public class ClazzLoader extends ClassLoader {

    private Hashtable<String, byte[]> classes = new Hashtable<>();
    private Hashtable<String, byte[]> resources = new Hashtable<>();
    private byte[] classdata = null;

    public ClazzLoader(){
        super(Base.class.getClassLoader());
    }

    public void newResource(String name, byte[] data) {
        resources.put(name,data);
    }

    @Override
    public InputStream getResourceAsStream(String name) {

        if (resources.containsKey(name)) {
            return new ByteArrayInputStream(resources.get(name));
        }

        return super.getResourceAsStream(name);
    }

    public void setClassContent(byte[] data){
        classdata = new byte[data.length];
        System.arraycopy(data, 0, classdata, 0, data.length);
    }

    @SneakyThrows
    public void setClassContent(String name, byte[] data){
        classdata = new byte[data.length];
        System.arraycopy(data, 0, classdata, 0, data.length);
        classes.put(name, classdata);
    }

    public Class findClass(String name) throws ClassNotFoundException {
        Class result = null;
        try {
            this.classdata = classes.get(name);
            result = defineClass(name, this.classdata,0,this.classdata.length,null);
            if (result == null) {
                return super.loadClass(name, true);
            }
            return result;
        } catch(SecurityException se){
            result = super.loadClass(name, true);
        } catch(Exception e){
            System.out.println("Class " + name + " was not found: " + e.getMessage());
            return null;
        }
        return result;
    }

}

