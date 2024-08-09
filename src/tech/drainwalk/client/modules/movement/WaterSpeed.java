package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.api.impl.events.player.EventMove;
import tech.drainwalk.utils.movement.MoveUtils;

public class WaterSpeed extends Module {
    private final FloatOption speed = new FloatOption("Speed", 1.0f, 0.1f,1.1f).addIncrementValue(0.1f);

    public WaterSpeed() {
        super("WaterSpeed", Category.MOVEMENT);
        register(speed);
    }

    @EventTarget
    public void onMove(EventMove event) {
//        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.2, mc.player.posZ)).getBlock() == Blocks.WATER && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() != Blocks.WATER) {
//            MoveUtils.setStrafe();
//            mc.player.motionY = -0.07;
//            return;
//        }
        if (mc.player.ticksExisted % 2f == 0.0f) {
            if (MoveUtils.isInLiquid() && !mc.player.isOnGround()) {
                if (!mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.D, -1D, 0.D).expand(0, 0.D, 0))) {
                    return;
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.jump();
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.setMotionY(-0.30);
                } else {
                    mc.player.setMotionY(0.04);
                }
                MoveUtils.setSpeed(speed.getValue());
                if (MoveUtils.isMovingSprint()) {
                    MoveUtils.setStrafe();
                }
            }
        }
    }
}
