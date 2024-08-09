package tech.drainwalk.client.commands;

import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TextFormatting;
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
                dw.getApiMain().getModuleManager().findByName(args[2]).addKey(InputMappings.getInputByNamePatched(args[1]).getKeyCode());
                return;
            } else if (args[0].equalsIgnoreCase("del")) {
                dw.getApiMain().getModuleManager().findByName(args[2]).getKey().remove((Integer) InputMappings.getInputByNamePatched(args[1]).getKeyCode());
                return;
            }
        } else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                for (Module module : dw.getApiMain().getModuleManager()) {
                    if (module.getKey().isEmpty()) continue;
                    StringBuilder keys = new StringBuilder();
                    for (int key : module.getKey()) {
                        keys.append(InputMappings.getInputByCode(key, 0).getTranslationKeySplit().toUpperCase()).append(", ");
                    }
                    ChatUtils.addChatMessage(module.getName() + " - " + keys.substring(0, keys.length() - 2));
                }
                return;
            }
        }
        printUsage();
    }
}
