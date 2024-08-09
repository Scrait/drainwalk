package tech.drainwalk.client.modules.misc;

import by.radioegor146.nativeobfuscator.Native;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPlayerPacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.events.player.EventMove;
import tech.drainwalk.utils.movement.MoveUtils;

@Native
public class Disabler extends Module {

    public Disabler() {
        super("Disabler", Category.MISC);
    }

    @EventTarget
    public void onMotion(EventMove event) {
        mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ(), mc.player.isOnGround()));
        mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY() + 0.00000000000000000000000000000000000000001, mc.player.getPosZ(), false));
        MoveUtils.disabler(MoveUtils.DisablerType.ONCE);
        toggle();
    }
}
