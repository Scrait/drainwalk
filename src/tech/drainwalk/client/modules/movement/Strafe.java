package tech.drainwalk.client.modules.movement;

import by.radioegor146.nativeobfuscator.Native;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.api.impl.events.player.EventMove;
import tech.drainwalk.utils.minecraft.ChatUtils;
import tech.drainwalk.utils.movement.MoveUtils;
import com.darkmagician6.eventapi.EventTarget;

@Native
public class Strafe extends Module {

    private static int noSlowTicks, counter;
    public final SelectOption typeStrafe = new SelectOption("Mode", 1,
            new SelectOptionValue("Default"),
            new SelectOptionValue("Damage"),
            new SelectOptionValue("ForDisabler"));
    //private final FloatOption speed = new FloatOption("Speed", 1.5f, 0.1f, 6.0f);
    public final BooleanOption autoDisabler = new BooleanOption("Auto disabler", true).addVisibleCondition(() -> typeStrafe.getValueByIndex(2));
    private final BooleanOption damageBoost = new BooleanOption("Damage Boost", false);
    public final BooleanOption elytraBoost = new BooleanOption("Elytra Boost", false).addVisibleCondition(() -> typeStrafe.getValueByIndex(0) && !damageBoost.getValue());
    private final FloatOption speed = new FloatOption("Speed", 1.5f, 0, 3).addIncrementValue(0.1f).addVisibleCondition(() -> typeStrafe.getValueByIndex(2) || (typeStrafe.getValueByIndex(0) && elytraBoost.getValue()));
    private int boostTicks;
    private long lastVelocityTime;
    private double velocityXZ;
    private double oldSpeed, contextFriction;
    private final tech.drainwalk.utils.time.Timer timer = new tech.drainwalk.utils.time.Timer();

    public Strafe() {
        super("Strafe", Category.MOVEMENT);
        register(
                typeStrafe,
                autoDisabler,
                speed,
                damageBoost,
                elytraBoost
        );
        damageBoost.addVisibleCondition(() -> typeStrafe.getValueByIndex(0) && !elytraBoost.getValue());
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (elytraBoost.getValue() && typeStrafe.getValueByIndex(0)) {
            //if (mc.world.getBlockState(new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 1.1D, mc.player.getPosZ())).getBlock() != Blocks.AIR) {
                if (mc.player.fallDistance > 0 && mc.player.fallDistance < 0.1) {
                    MoveUtils.disabler(MoveUtils.DisablerType.TWICE);
                }
            //}
        }
    }

    @EventTarget
    public void onMotion(EventMove event) {
       if (typeStrafe.getValueByIndex(0)) {
           if (damageBoost.getValue()) {
               if (System.currentTimeMillis() - lastVelocityTime < 1350 && !mc.player.isOnGround() && mc.player.fallDistance > 0) {
                    final double speed = velocityXZ * 1.3;
                    MoveUtils.setMotion(speed);
                    oldSpeed = speed / 1.06;
               }
           } else if (elytraBoost.getValue()) {
              // if (!mc.player.isOnGround() && !mc.player.collidedVertically && !mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.D, -0.5D, 0.D).expand(0, 0.D, 0)) && mc.player.fallDistance <= 0.6) {
               if (!mc.player.isOnGround()) {
                   if (mc.player.fallDistance > 0) {
                       MoveUtils.setMotion(MoveUtils.getSpeed() * speed.getValue());
                       oldSpeed = speed.getValue() / 1.06;
                   }
                   MoveUtils.setMotion(MoveUtils.getSpeed());
               }

               return;
           }
            if (!mc.player.isSneaking() && !mc.player.abilities.isFlying) {
                double forward = mc.player.movementInput.moveForward;
                double strafe = mc.player.movementInput.moveStrafe;
                float yaw = mc.player.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    oldSpeed = 0;
                    event.setX(0);
                    event.setZ(0);
                } else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            yaw += ((forward > 0.0) ? -45 : 45);
                        } else if (strafe < 0.0) {
                            yaw += ((forward > 0.0) ? 45 : -45);
                        }

                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        } else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    double speed = calculateSpeed(event);
                    double cos = Math.cos(Math.toRadians(yaw + 90.0f)), sin = Math.sin(Math.toRadians(yaw + 90.0f));
                    event.setX(forward * speed * cos + strafe * speed * sin);
                    event.setZ(forward * speed * sin - strafe * speed * cos);
                }
            } else {
                oldSpeed = 0;
            }
        } else if (typeStrafe.getValueByIndex(1)) {
            if (System.currentTimeMillis() - lastVelocityTime < 1350) {
                final double speed = Math.hypot(event.getX(), event.getZ()) + velocityXZ - 0.25;
                if (mc.player.ticksExisted % 2 == 0) {
                    mc.player.abilities.isFlying = false;
                }

                if (true) {
                    ++this.boostTicks;
                    mc.player.abilities.isFlying = false;
                    mc.player.abilities.allowFlying = false;
                    if (this.boostTicks > 0) {
                        mc.player.setMotionWithMultiplication(1.5, 1.5);
                        MoveUtils.setMotion(speed);
                        if (!MoveUtils.isMovingSprint()) {
                            this.boostTicks = 0;
                        }
                    }
                }
            }
        } else if (typeStrafe.getValueByIndex(2)) {
            MoveUtils.setMotion(MoveUtils.getSpeed());
            if (!mc.player.isOnGround() && !mc.player.collidedVertically && !mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.D, -0.5D, 0.D).expand(0, 0.D, 0)) && mc.player.fallDistance <= 0.6) {
                MoveUtils.setMotion(speed.getValue());
                if (autoDisabler.getValue()) {
                    if (mc.player.fallDistance > 0) {
                        MoveUtils.disabler(MoveUtils.DisablerType.ONCE);
                    }
                }
            }
        }
    }

    @EventTarget
    public void onReceive(EventReceivePacket event) {
        if (typeStrafe.getValueByIndex(2)) {
            if (event.getPacket() instanceof SPlayerPositionLookPacket) {
                ChatUtils.addChatMessage(TextFormatting.YELLOW + "[" + TextFormatting.GOLD +"drainwalk" + TextFormatting.YELLOW + "]"+ TextFormatting.DARK_GRAY + " << " + TextFormatting.RED + "Flagged!" + TextFormatting.RESET);
                this.toggle();
            }
        } else if (typeStrafe.getValueByIndex(1) || (typeStrafe.getValueByIndex(0) && damageBoost.getValue())) {
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

   @EventTarget
   public void onSync(EventEntitySync event) {
       double distTraveledLastTickX = mc.player.getPosX() - mc.player.prevPosX;
       double distTraveledLastTickZ = mc.player.getPosZ() - mc.player.prevPosZ;
       oldSpeed = (Math.sqrt(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) * contextFriction;
   }

   @Override
   public void onEnable() {
        if (typeStrafe.getValueByIndex(0)) {
            oldSpeed = 0;
        }
   }

    private double calculateSpeed(EventMove move) {
        final boolean fromGround = mc.player.isOnGround();
        final boolean jump = move.getY() > 0;
        final float speedAttributes = getAIMoveSpeed(mc.player);
        final float frictionFactor = getFrictionFactor(mc.player);
        float n6 = mc.player.isPotionActive(Effects.JUMP_BOOST) && mc.player.isHandActive() ? 0.88f : (float) (oldSpeed > 0.32 && mc.player.isHandActive() ? 0.88 : 0.91F);
        if (fromGround) {
            n6 = frictionFactor;
        }
        final float n7 = (float) (0.16277135908603668 / Math.pow(n6, 3.01));
        float n8;
        if (fromGround) {
            n8 = speedAttributes * n7;
            if (jump) {
                n8 += 0.2f;
            }
        } else {
            n8 = 0.0255f;
        }
        boolean noslow = false;
        double max2 = oldSpeed + n8;
        double max = 0.0;
        if (mc.player.isHandActive() && !jump) {
            double n10 = oldSpeed + n8 * 0.25;
            final double motionY2 = move.getY();
            if (motionY2 != 0.0 && Math.abs(motionY2) < 0.08) {
                n10 += 0.055;
            }
            if (max2 > (max = Math.max(0.043, n10))) {
                noslow = true;
                ++noSlowTicks;
            } else {
                noSlowTicks = Math.max(noSlowTicks - 1, 0);
            }
        } else {
            noSlowTicks = 0;
        }
        if (noSlowTicks > 3) {
            max2 = max - 0.019;
        } else {
            max2 = Math.max(noslow ? 0 : 0.25, max2) - (counter++ % 2 == 0 ? 0.001 : 0.002);
        }
        contextFriction = n6;
        return max2;
    }

    private float getAIMoveSpeed(PlayerEntity contextPlayer) {
        final boolean prevSprinting = contextPlayer.isSprinting();
        contextPlayer.setSprinting(false);
        final float speed = contextPlayer.getAIMoveSpeed() * 1.3f;
        contextPlayer.setSprinting(prevSprinting);
        return speed;
    }

    private float getFrictionFactor(PlayerEntity contextPlayer) {
        final BlockPos blockpos$pooledmutableblockpos = new BlockPos(mc.player.getPosX(),  mc.player.getBoundingBox().minY - 1.0D, mc.player.getPosZ());
        return contextPlayer.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().getSlipperiness() * 0.91F;
    }
}
