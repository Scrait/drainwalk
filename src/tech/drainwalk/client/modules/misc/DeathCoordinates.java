package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.models.Notification;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.utils.minecraft.ChatUtils;

public class DeathCoordinates extends Module {

    private int lastX, lastY, lastZ;

    public DeathCoordinates() {
        super("DeathCoordinates", Category.MISC);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen instanceof DeathScreen) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (lastX != x || lastY != y || lastZ != z) {
                ChatUtils.addChatMessage(TextFormatting.YELLOW + "[" + TextFormatting.GOLD +"drainwalk" + TextFormatting.YELLOW + "]" + TextFormatting.DARK_GRAY + " << " + TextFormatting.GRAY + "Death Coordinates: " + "X: " + x + " Y: " + y + " Z: " + z);
                dw.getApiMain().getNotificationManager().register(new Notification("","X: " + x + " Y: " + y + " Z: " + z, Notification.Type.WARNING));
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }
}
