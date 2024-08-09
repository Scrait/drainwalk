package tech.drainwalk.api.impl.models;

import lombok.Getter;
import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.utils.minecraft.ChatUtils;

public abstract class Command implements IInstanceAccess {

    @Getter
    private final String prefix;
    private final String[] usages;


    public Command(String prefix, String... usages) {
        this.prefix = prefix;
        this.usages = usages;
    }

    protected void printUsage() {
        for (String usage : usages) {
            ChatUtils.addChatMessage(usage);
        }
    }

    protected abstract void execute(String[] args);
    public void safeExecute(String[] args) {
        try {
            execute(args);
            ChatUtils.addChatMessage("Successful operation at " + prefix + " command" + TextFormatting.GREEN + " [✔]");
        } catch (Exception exception) {
            ChatUtils.addChatMessage("Exception at " + prefix + " command (" + exception.getClass().getName() + ") " + TextFormatting.RED + "[✖]");
        }
    }

}
