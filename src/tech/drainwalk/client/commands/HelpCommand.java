package tech.drainwalk.client.commands;

import tech.drainwalk.api.impl.models.Command;

public class HelpCommand extends Command {

    public HelpCommand() {
        super(".help", ".help/bind/macro/friend/vclip/hclip");
    }

    @Override
    protected void execute(String[] args) {
        printUsage();
    }

}
