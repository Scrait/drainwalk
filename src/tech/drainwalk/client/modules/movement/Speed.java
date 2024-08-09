package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Blocks;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.math.BlockPos;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;
import tech.drainwalk.api.impl.events.player.EventMove;
import tech.drainwalk.utils.movement.MoveUtils;

public class Speed extends Module {
    private int boostTicks;
    private long lastVelocityTime;
    private double velocityXZ;
    private int ticks = 0;

    private final SelectOption mode = new SelectOption("Modes", 0,
            new SelectOptionValue("LongJump"),
            new SelectOptionValue("Damage"),
            new SelectOptionValue("Matrix"),
            new SelectOptionValue("Grim"));
    private final FloatOption speed = new FloatOption("Speed", 2, 0, 10).addIncrementValue(0.1f).addVisibleCondition(() -> mode.getValueByIndex(3));

    public Speed() {
        super("Speed", Category.MOVEMENT);
        register(
                mode,
                speed
        );
    }

    @EventTarget
    public void onMotion(EventMove event) {
        if (mode.getValueByIndex(0)) {
            if (mc.world.getBlockState(new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 1.1D, mc.player.getPosZ())).getBlock() != Blocks.AIR) {
                if (mc.player.getMotion().y == -0.4448259643949201D) {
                    MoveUtils.disabler(MoveUtils.DisablerType.ONCE);
                    MoveUtils.setMotion(10);
                    MoveUtils.disabler(MoveUtils.DisablerType.ONCE);
                }
            }
        } else if (mode.getValueByIndex(1)) {
            if (mc.player.isOnGround()) {
                if (System.currentTimeMillis() - lastVelocityTime < 1350) {
                    final double speed = Math.hypot(event.getX(), event.getZ()) + velocityXZ - 0.25;
                    if (mc.player.ticksExisted % 2 == 0) {
                        mc.player.setMotionWithMultiplication(1.5D, 1.5D);
                        mc.player.abilities.isFlying = false;
                    }

                    if (true) {
                        ++this.boostTicks;
                        mc.player.abilities.isFlying = false;
                        mc.player.abilities.allowFlying = false;
                        if (this.boostTicks > 0) {
                            mc.player.setMotionWithMultiplication(2.5, 2.5);
                            MoveUtils.setMotion(speed * 1.5);
                            if (!MoveUtils.isMovingSprint()) {
                                this.boostTicks = 0;
                            }
                        }
                    }
                }
            }
        } else if (mode.getValueByIndex(2)) {
            float timerValue = mc.player.fallDistance <= 0.25f ? 2.5f : (float) (mc.player.fallDistance != Math.ceil(mc.player.fallDistance) ? 0.45f : 1f);
            if (!mc.player.isOnGround() && MoveUtils.isMovingSprint()) {
                mc.getTimer().timerSpeed = timerValue;
                if (mc.player.isOnGround()) {
                    mc.player.jump();
                }
            } else {
                mc.getTimer().timerSpeed = 1.0f;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        ticks++;

        if (ticks == 20) {
            mc.getTimer().timerSpeed = speed.getValue();
            ticks = 0;
        } else if (ticks == 3) {
            mc.getTimer().timerSpeed = 0.9f;
        }
    }

    @Override
    public void onDisable() {
        mc.getTimer().timerSpeed = 1.0f;
        if (mode.getValueByIndex(3)) {
            ticks = 0;
        }
    }

    @EventTarget
    public void onReceive(EventReceivePacket event) {
        if (mode.getValueByIndex(1)) {
            if (((EventReceivePacket) event).getPacket() instanceof SEntityVelocityPacket) {
                final SEntityVelocityPacket packet = (SEntityVelocityPacket) ((EventReceivePacket) event).getPacket();
                if (packet.getEntityID() == mc.player.getEntityId()
                        && System.currentTimeMillis() - lastVelocityTime > 1350) {
                    double vX = Math.abs(packet.getMotionX() / 8000d), vY = packet.getMotionY() / 8000d,
                            vZ = Math.abs(packet.getMotionZ() / 8000d);
                    if (vX + vZ > 0.3) {
                        velocityXZ = vX + vZ;
                        lastVelocityTime = System.currentTimeMillis();
                    } else {
                        velocityXZ = 0;
                    }
                }
            }
        }
    }
}
