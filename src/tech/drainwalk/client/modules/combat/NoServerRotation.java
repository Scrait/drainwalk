package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;

public class NoServerRotation extends Module {
    public NoServerRotation() {
        super("NoServerRotation", Category.COMBAT);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPlayerPositionLookPacket) {
            SPlayerPositionLookPacket packet = (SPlayerPositionLookPacket) event.getPacket();
            packet.setYaw(mc.player.rotationYaw);
            packet.setPitch(mc.player.rotationPitch);
        }
    }
}
