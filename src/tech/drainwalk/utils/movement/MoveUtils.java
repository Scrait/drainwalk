package tech.drainwalk.utils.movement;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import tech.drainwalk.api.impl.events.player.EventMove;
import tech.drainwalk.utils.Utils;
import tech.drainwalk.utils.inventory.InvenotryUtil;

import java.util.Objects;

public class MoveUtils extends Utils {
    @Getter
    @Setter
    private static boolean inWeb;

    public static boolean isMoving() {
        return mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindRight.isPressed()
                || mc.gameSettings.keyBindLeft.isPressed()|| mc.gameSettings.keyBindBack.isPressed();
    }

    public static boolean isMovingSprint() {
        return mc.player.movementInput.moveStrafe != 0.0 || mc.player.movementInput.moveForward != 0.0;
    }

    public static boolean isUsingItem() {
        return mc.player.getActiveItemStack().getItem().getUseAction(mc.player.getActiveItemStack()) == UseAction.EAT || mc.player.getActiveItemStack().getItem().getUseAction(mc.player.getActiveItemStack()) == UseAction.BLOCK || mc.player.getActiveItemStack().getItem().getUseAction(mc.player.getActiveItemStack()) == UseAction.BOW || mc.player.getActiveItemStack().getItem().getUseAction(mc.player.getActiveItemStack()) == UseAction.DRINK;
    }

    public static int getSpeedEffect() {
        if (mc.player.isPotionActive(Effects.SPEED)) {
            return Objects.requireNonNull(mc.player.getActivePotionEffect(Effects.SPEED)).getAmplifier() + 1;
        }
        return 0;
    }

    public static double getBaseSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player.isPotionActive(Effects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(Effects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getSpeed() {
        return Math.sqrt(Math.pow(mc.player.getMotion().x, 2) + Math.pow(mc.player.getMotion().z, 2));
    }

    public static float getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        float factor = 0f;

        if (mc.player.movementInput.moveForward > 0)
            factor = 1;
        if (mc.player.movementInput.moveForward < 0)
            factor = -1;

        if (factor == 0) {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 90;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 90;
        } else {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 45 * factor;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 45 * factor;
        }

        if (factor < 0)
            rotationYaw -= 180;

        return (float) Math.toRadians(rotationYaw);
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.getPosX() - 0.3, mc.player.getPosY() + mc.player.getEyeHeight(),
                mc.player.getPosZ() + 0.3, mc.player.getPosX() + 0.3, mc.player.getPosY() + (!mc.player.isOnGround() ? 1.5 : 2.5),
                mc.player.getPosZ() - 0.3);
        return !mc.world.hasNoCollisions(mc.player, axisAlignedBB);
    }

    public static void setSpeed(float speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        mc.player.setMotion((forward * speed * Math.cos(Math.toRadians(yaw + 90))
                + strafe * speed * Math.sin(Math.toRadians(yaw + 90))), (forward * speed * Math.sin(Math.toRadians(yaw + 90))
                - strafe * speed * Math.cos(Math.toRadians(yaw + 90))));
    }

    public static void setMotion(EventMove e, double speed, float pseudoYaw, double aa, double po4) {
        double forward = po4;
        double strafe = aa;
        float yaw = pseudoYaw;
        if (po4 != 0.0) {
            if (aa > 0.0) {
                yaw = pseudoYaw + (float) (po4 > 0.0 ? -45 : 45);
            } else if (aa < 0.0) {
                yaw = pseudoYaw + (float) (po4 > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (po4 > 0.0) {
                forward = 1.0;
            } else if (po4 < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double kak = Math.cos(Math.toRadians(yaw + 90.0f));
        double nety = Math.sin(Math.toRadians(yaw + 90.0f));
        e.setX(forward * speed * kak + strafe * speed * nety);
        e.setZ(forward * speed * nety - strafe * speed * kak);
    }

    public static void setMotion(double speed) {
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            mc.player.setMotion(0, 0);
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            double sin = MathHelper.sin((float) Math.toRadians(yaw + 90));
            double cos = MathHelper.cos((float) Math.toRadians(yaw + 90));
            mc.player.setMotion(forward * speed * cos + strafe * speed * sin, forward * speed * sin - strafe * speed * cos);
        }
    }

    public static double[] getSpeed(double speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        return new double[] {
                (forward * speed * Math.cos(Math.toRadians(yaw + 90))
                        + strafe * speed * Math.sin(Math.toRadians(yaw + 90))),
                (forward * speed * Math.sin(Math.toRadians(yaw + 90))
                        - strafe * speed * Math.cos(Math.toRadians(yaw + 90))),
                yaw };
    }


    public static void setStrafe(double speed) {
        if (!MoveUtils.isMoving()) {
            return;
        }
        double yaw = getDirection();
        mc.player.setMotion(-Math.sin(yaw) * speed, Math.cos(yaw) * speed);
    }

    public static void setStrafe() {
        if (mc.gameSettings.keyBindBack.isKeyDown()) {
            return;
        }
        MoveUtils.setStrafe(MoveUtils.getSpeed());
    }

    public static boolean isInLiquid() {
        return mc.player.isInWater() || mc.player.isInLava();
    }

    public static void disabler(DisablerType disablerType) {
        final int elytra = InvenotryUtil.getSlotIDFromItem(Items.ELYTRA);
        if (elytra != -2) {
            mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
        }
        mc.getConnection().sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_FALL_FLYING));
        if (disablerType == DisablerType.TWICE)
            mc.getConnection().sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_FALL_FLYING));
        if (elytra != -2) {
            mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, mc.player);
        }
    }

    public enum DisablerType {
        ONCE,
        TWICE
    }
}
