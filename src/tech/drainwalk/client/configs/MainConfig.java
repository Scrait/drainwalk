package tech.drainwalk.client.configs;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tech.drainwalk.api.impl.models.Config;
import tech.drainwalk.api.impl.models.module.Module;

public class MainConfig extends Config {

    public MainConfig() {
        super("main");
    }

    @Override
    protected void load() {
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(readConfig());
        if (object.has("Modules")) {
            JsonObject modulesObject = object.getAsJsonObject("Modules");
            for (Module module : dw.getApiMain().getModuleManager()) {
                if (module.isEnabled()) {
                    module.toggle();
                }
                module.load(modulesObject.getAsJsonObject(module.getName()));
            }
        }
    }

    @Override
    protected void save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Module module : dw.getApiMain().getModuleManager()) {
            modulesObject.add(module.getName(), module.save());
        }

        jsonObject.add("Modules", modulesObject);
        final String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
        writeConfig(contentPrettyPrint);
    }

}
