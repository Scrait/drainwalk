package tech.drainwalk.api.managers;

import tech.drainwalk.api.impl.interfaces.IFind;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.Config;
import tech.drainwalk.client.configs.FriendConfig;
import tech.drainwalk.client.configs.MainConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends ArrayList<Config> implements IManager<Config>, IFind<Config> {

    public ConfigManager() {
        init();
    }

    @Override
    public void init() {
        register(new FriendConfig());
        register(new MainConfig());
    }

    @Override
    public void register(Config file) {
        this.add(file);
    }

    public void loadAll() {
        loadAll("main");
    }

    public void loadAll(String name) {
        this.findByClass(MainConfig.class).setName(name);
        this.forEach(Config::safeLoad);
    }

    public void saveAll() {
       saveAll("main");
    }

    public void saveAll(String name) {
        this.findByClass(MainConfig.class).setName(name);
        this.forEach(Config::safeSave);
    }

    @Override
    public <T extends Config> T findByName(final String name) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }

    @Override
    public <T extends Config> T findByClass(final Class<T> clazz) {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getClass() == clazz)
                .findAny().orElse(null);
    }

    @Deprecated(forRemoval = true, since = "3.0")
    public List<String> getConfigsNamesInDir() {
        final File dir = new File("C:/drainwalk/configs");
        final File[] arrFiles = dir.listFiles();
        assert arrFiles != null;
        final List<String> arrStrings = new ArrayList<>();
        for (File file : arrFiles) {
            if (!file.getName().endsWith(".dw")) continue;
            final String name = file.getName();
            arrStrings.add(name.substring(0, name.length() - 3));
        }
        return arrStrings;
    }

}
