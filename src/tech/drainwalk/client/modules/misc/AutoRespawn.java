package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.screen.DeathScreen;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.events.UpdateEvent;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", Category.MISC);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen instanceof DeathScreen) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }
}
