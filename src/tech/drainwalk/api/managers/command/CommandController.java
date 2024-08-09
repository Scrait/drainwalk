package tech.drainwalk.api.managers.command;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.events.packet.EventMessage;
import tech.drainwalk.api.impl.models.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandController implements IInstanceAccess {

    @EventTarget
    public void onMessage(EventMessage event) {
        if (event.getMessage().startsWith(".")) {
            final List<String> args = new ArrayList<>(Arrays.asList(event.getMessage().split(" ")));
            final String prefix = args.getFirst();
            args.removeFirst();
            final CommandManager commandManager = dw.getApiMain().getCommandManager();
            final List<Command> commands = commandManager.stream()
                    .filter(command -> command.getPrefix().equalsIgnoreCase(prefix)).toList();
            if (commands.isEmpty()) {
                commandManager.getHelpCommand().safeExecute(args.toArray(new String[0]));
                return;
            }
            commands.forEach(command -> command.safeExecute(args.toArray(new String[0])));
            event.setCancelled(true);
        }
    }

}
