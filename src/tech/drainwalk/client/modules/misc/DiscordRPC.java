package tech.drainwalk.client.modules.misc;

import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;

public class DiscordRPC extends Module {

    public DiscordRPC() {
        super("DiscordRPC", Category.MISC);
        addType(Type.SECONDARY);
    }

    @Override
    public synchronized void onEnable() {
        tech.drainwalk.client.profile.DiscordRPC.startRPC();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        tech.drainwalk.client.profile.DiscordRPC.stopRPC();
        super.onDisable();
    }
}
