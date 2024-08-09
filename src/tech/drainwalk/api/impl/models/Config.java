package tech.drainwalk.api.impl.models;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.utils.minecraft.ChatUtils;

import java.io.File;
import java.nio.file.Files;

public abstract class Config implements IInstanceAccess {

    protected final String DRAINWALK_MAIN_CFG_DIR = "C:/drainwalk/configs/";
    protected final String SUFFIX = ".dw";

    @Setter
    @Getter
    private String name;

    protected Config(String name) {
        this.name = name;
    }

    @SneakyThrows
    protected String readConfig() {
        return Files.readString(new File(DRAINWALK_MAIN_CFG_DIR + name + SUFFIX).toPath());
    }

    @SneakyThrows
    protected void writeConfig(String cfg) {
        Files.writeString(new File(DRAINWALK_MAIN_CFG_DIR + name + SUFFIX).toPath(), cfg);
    }

    protected abstract void load();
    protected abstract void save();

    public synchronized void safeLoad() {
        try {
            load();
            ChatUtils.addChatMessage("Successfully loaded " + name + " config" + TextFormatting.GREEN + " [✔]");
        } catch (Exception exception) {
            ChatUtils.addChatMessage("Exception while loading " + name + " config (" + exception.getClass() + ")" + TextFormatting.RED + " [✖]");
        }
    }

    public void safeSave() {
        try {
            save();
            ChatUtils.addChatMessage("Successfully saved " + name + " config" + TextFormatting.GREEN + " [✔]");
        } catch (Exception exception) {
            ChatUtils.addChatMessage("Exception while saving " + name + " config (" + exception.getClass() + ")" + TextFormatting.RED + " [✖]");
        }
    }

}
