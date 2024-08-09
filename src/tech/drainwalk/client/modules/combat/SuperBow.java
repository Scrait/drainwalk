package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.packet.EventSendPacket;

public class SuperBow extends Module {

    private final FloatOption strength = new FloatOption("Strength", 5, 0, 10).addIncrementValue(0.1f);
    public SuperBow() {
        super("SuperBow", Category.COMBAT);
        addType(Type.SECONDARY);
        register(strength);
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPlayerDiggingPacket && ((CPlayerDiggingPacket) event.getPacket()).getAction() == CPlayerDiggingPacket.Action.RELEASE_USE_ITEM && (mc.player.getActiveItemStack().getItem() == Items.BOW)) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
            for (int i = 0; i < (int) (30f * strength.getValue()); i++) {
                spoof(mc.player.getPosX(), mc.player.getPosY() - 1e-10, mc.player.getPosZ(), true);
                spoof(mc.player.getPosX(), mc.player.getPosY() + 1e-10, mc.player.getPosZ(), false);
            }
        }
    }

    private void spoof(double x, double y, double z, boolean ground) {
        //if (rotation.getValue()) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(x, y, z, mc.player.rotationYaw, mc.player.rotationPitch, ground));
        //} else {
            //mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, ground));
        //}
    }
}
