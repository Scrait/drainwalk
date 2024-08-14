package tech.drainwalk.client.commands;

import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.models.Command;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.utils.minecraft.ChatUtils;

public class BindCommand extends Command {

    public BindCommand() {
        super(".bind", ".bind list",
                ".bind add " + TextFormatting.RED + "[key] [module]",
                ".bind del " + TextFormatting.RED + "[key] [module]");
    }

    @Override
    protected void execute(String[] args) {
        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("add")) {
                dw.getApiMain().getModuleManager().findByName(args[2]).setCurrentKey(InputMappings.getInputByNamePatched(args[1]).getKeyCode());
                return;
            } else if (args[0].equalsIgnoreCase("del")) {
                dw.getApiMain().getModuleManager().findByName(args[2]).setCurrentKey(GLFW.GLFW_KEY_UNKNOWN);
                return;
            }
        } else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                for (Module module : dw.getApiMain().getModuleManager()) {
                    if (!module.hasBind()) continue;
                    ChatUtils.addChatMessage(module.getName() + " - " + module.getCurrentKey());
                }
                return;
            }
        }
        printUsage();
    }
}
