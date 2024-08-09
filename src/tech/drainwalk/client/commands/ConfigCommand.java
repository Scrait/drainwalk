package tech.drainwalk.client.commands;

import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.api.impl.models.Command;
import tech.drainwalk.utils.minecraft.ChatUtils;

import java.io.File;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super(".cfg", ".cfg list",
                ".cfg save " + TextFormatting.RED + "[name]",
                ".cfg load " + TextFormatting.RED + "[name]");
    }

    @Override
    protected void execute(String[] args) {
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("save")) {
                dw.getApiMain().getConfigManager().saveAll(args[1]);
                return;
            } else if (args[0].equalsIgnoreCase("load")) {
                dw.getApiMain().getConfigManager().loadAll(args[1]);
                return;
            }
        } else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                final File dir = new File("C:/drainwalk/configs");
                final File[] arrFiles = dir.listFiles();
                assert arrFiles != null;
                for (File file : arrFiles) {
                    if (!file.getName().endsWith(".dw")) continue;
                    ChatUtils.addChatMessage(file.getName());
                }
                return;
            }
        }
        printUsage();
    }
}
