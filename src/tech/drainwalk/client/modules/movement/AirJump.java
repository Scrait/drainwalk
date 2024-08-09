package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.events.UpdateEvent;

public class AirJump extends Module {

    public AirJump() {
        super("AirJump", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if ((!mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0, -1, 0).expand(0.5, 0, 0.5)) && mc.player.ticksExisted % 2 == 0)) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.setJumpTicks(0);
                mc.player.fallDistance = 0;
                mc.player.setOnGround(true);
            }
        }
    }
}
