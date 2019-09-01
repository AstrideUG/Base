package com.astrideug.base.configuration;

import com.astrideug.base.loader.ResolvedJar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@RequiredArgsConstructor
public class Configuration {
    
    @NonNull
    private String file;

    private JsonObject idefault;

    public void init(ResolvedJar jar, String file) {
        this.file = file;

        for (JsonObject jsonObject : jar.getJsonObjects()) {
            if (jsonObject.get("iJsonFileName").getAsString().equals(file)) {
                idefault = jsonObject;
            }
        }
    }

    @SneakyThrows
    public File onFileSystem() {
        File f = new File("modules" + File.separator + "configuration", file);

        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(f);
            gson.toJson(idefault, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

        return new File(new java.io.File( "." ), f.toString());
    }

    @SneakyThrows
    public JsonObject read() {
        File f = onFileSystem();
        @Cleanup FileReader fileReader = new FileReader(f);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
        fileReader.close();
        return jsonObject;
    }

    @SneakyThrows
    public <K> K read(Class<K> clazz) {
        File f = onFileSystem();
        @Cleanup FileReader fileReader = new FileReader(f);
        Gson gson = new Gson();
        K k = gson.fromJson(fileReader, clazz);
        fileReader.close();
        return k;
    }
}
