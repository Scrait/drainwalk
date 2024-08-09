package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.movement.EventNoSlow;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;
import tech.drainwalk.api.impl.events.player.EventEntitySync;

public class NoSlow extends Module {

    private final SelectOption mode = new SelectOption("Mode", 0,
            new SelectOptionValue("Matrix"),
            new SelectOptionValue("Really World"));

    public NoSlow() {
        super("NoSlow", Category.MOVEMENT);
        addType(Type.SECONDARY);
        register(mode);
    }

    @EventTarget
    public void onEating(EventNoSlow event) {
        event.setCancelled(true);
        if (mode.getValueByIndex(0)) {
            if (mc.player.isOnGround()) {
                mc.player.setMotionWithMultiplication(0.5, 0.5);
            } else {
                mc.player.setMotionWithMultiplication(0.95, 0.95);
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mode.getValueByIndex(1)) {
            if (mc.player.getActiveItemStack().getUseAction() == UseAction.NONE || !mc.player.isOnGround() || mc.player.movementInput.jump) return;
            float f3 = (1.0f - ((LivingEntity)mc.player).getLandMovementFactor()) * 0.9f;
            f3 *= 0.3f;
            f3 = (float)((double)f3 + 0.055);
            mc.player.setMotionWithMultiplication(f3, f3);
        }
    }

    @EventTarget
    public void onSync(EventEntitySync event) {
        if (mode.getValueByIndex(1)) {
            if (mc.player.getActiveItemStack().getUseAction() == UseAction.NONE || mc.player.getItemInUseMaxCount() != 2) return;
//            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 8 + 1));
//            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket eventPacket) {
        if (mode.getValueByIndex(1)) return;
        if (eventPacket.getPacket() instanceof SHeldItemChangePacket) {
            SHeldItemChangePacket packetHeldItemChange = (SHeldItemChangePacket) eventPacket.getPacket();
            if (packetHeldItemChange.getHeldItemHotbarIndex() != mc.player.inventory.currentItem) {
                mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                eventPacket.setCancelled(true);
            }
        }
    }
}
