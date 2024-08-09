package tech.drainwalk.api;

import lombok.Getter;
import tech.drainwalk.api.managers.*;
import tech.drainwalk.api.managers.command.CommandManager;

/**
 * @author Scrait
 * @since 14/08/2023
 */
@Getter
public class ApiMain {

    private final NotificationManager notificationManager;
    private final ConfigManager configManager;
    private final FriendManager friendManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    public ApiMain() {
        notificationManager = new NotificationManager();
        configManager = new ConfigManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
    }

}