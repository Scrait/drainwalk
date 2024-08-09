package tech.drainwalk.api.managers.command;

import com.darkmagician6.eventapi.EventManager;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.Command;
import tech.drainwalk.client.commands.BindCommand;
import tech.drainwalk.client.commands.ConfigCommand;
import tech.drainwalk.client.commands.HelpCommand;

import java.util.ArrayList;

public class CommandManager extends ArrayList<Command> implements IManager<Command> {

    public CommandManager() {
        EventManager.register(new CommandController());
        init();
    }

    @Override
    public void init() {
        register(new BindCommand());
        register(new HelpCommand());
        register(new ConfigCommand());
    }

    @Override
    public void register(Command command) {
        this.add(command);
    }

    public <T extends Command> T getHelpCommand() {
        // noinspection unchecked
        return (T) this.stream()
                .filter(module -> module.getClass() == HelpCommand.class)
                .findAny().orElse(null);
    }

}
