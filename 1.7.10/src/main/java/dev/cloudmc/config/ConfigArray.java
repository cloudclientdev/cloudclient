package dev.cloudmc.config;

import java.util.ArrayList;

public class ConfigArray {

    private ArrayList<Config> configList;

    public ConfigArray() {
        configList = new ArrayList<>();
    }

    public void addConfig(Config config) {
        configList.add(config);
    }

    public ArrayList<Config> getConfig() {
        return configList;
    }
}
