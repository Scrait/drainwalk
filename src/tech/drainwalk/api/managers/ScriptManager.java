package tech.drainwalk.api.managers;

import lombok.Getter;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.script.Script;

import javax.script.ScriptEngineManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ScriptManager extends ArrayList<Script> implements IManager<Script> {
    public static final File DIRECTORY = new File("C:/drainwalk/scripts");
    @Getter
    private static final ScriptEngineManager engineManager = new ScriptEngineManager();

    public ScriptManager() {
        init();
    }

    @Override
    public void init() {
        System.out.println(DIRECTORY.getAbsolutePath());
        try {
            for (File file : Objects.requireNonNull(DIRECTORY.listFiles())) {
                if (file.getName().endsWith(".js")) {
                    System.out.println(file.getAbsolutePath());
                    this.register(new Script(file.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(Script script) {
        this.add(script);
    }
}