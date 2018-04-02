package pl.skript.ipsauth.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlFile extends YamlConfiguration {

    private final File file;

    public YamlFile(File file) {
        super();
        this.file = file;
        try {
            if (file.exists()) {
                load(file);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
