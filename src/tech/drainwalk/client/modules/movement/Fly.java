package tech.drainwalk.client.modules.movement;

import by.radioegor146.nativeobfuscator.Native;
import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.packet.EventReceivePacket;
import tech.drainwalk.utils.minecraft.ChatUtils;
import tech.drainwalk.utils.movement.MoveUtils;
import tech.drainwalk.utils.time.Timer;
import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.api.impl.models.module.Module;

@Native
public class Fly extends Module {

    public final SelectOption mode = new SelectOption("Modes", 0,
            new SelectOptionValue("NGDisabler"),
            new SelectOptionValue("Nexus Jump"),
            new SelectOptionValue("Damage"),
             new SelectOptionValue("Sunrise Elytra"));
    private final BooleanOption superBow = new BooleanOption("Super Bow",false).addVisibleCondition(() -> mode.getValueByIndex(3));

    private final FloatOption speed = new FloatOption("Speed", 1.7f, 0.1f,4f).addIncrementValue(0.1f).addVisibleCondition(() -> mode.getValueByIndex(3) ||  mode.getValueByIndex(0));
    private final BooleanOption antiKick = new BooleanOption("Anti Kick",false).addVisibleCondition(() -> mode.getValueByIndex(0));
    //private final FloatOption abuz = new FloatOption("Delay", 260f, 0.1f,2000f).addVisibleCondition(() -> mode.getValueByIndex(2));
    private final BooleanOption checkWalls = new BooleanOption("Check Walls",false).addVisibleCondition(() -> mode.getValueByIndex(0));
    private final FloatOption checkWallsRange = new FloatOption("Check Walls Rsnge", 5, 0,25).addVisibleCondition(() -> checkWalls.getValue());

    public int ticks = 0;
    public boolean flyTick = false;
    private final Timer timer = new Timer();
    private float flySpeed;

    public Fly() {
        super("Fly", Category.MOVEMENT);
        register(
                mode,
                superBow,
                antiKick,
                speed,
                //abuz,
                checkWalls,
                checkWallsRange
        );
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
//        if (mode.getValueByIndex(0)) {
//            if (mc.player.collidedHorizontally) {
//                flySpeed = 0;
//            }
//            if (mc.player.collidedVertically) {
//                flySpeed = 0;
//                mc.player.setMotionY(0.41999998688697815);
//            }
//            int elytra = InvenotryUtil.getSlotIDFromItem(Items.ELYTRA);
//
//            if (elytra == -1) {
//                ChatUtils.addChatMessage("Нету элитр в инвентаре!");
//                toggle();
//                return;
//            }
//            if (mc.player.isOnGround()) {
//                mc.player.jump();
//            }
//            if (timer.delay(80, true)) {
//                MoveUtils.disabler(MoveUtils.DisablerType.ONCE);
//            }
//
//            if (mc.player.fallDistance > 0.25f) {
//
//                MoveUtils.setMotion(Math.min((flySpeed = (flySpeed + 11.0F / speed.getValue())) / 100.0F, 0.5));
//
//                if (!mc.gameSettings.keyBindForward.isPressed() && !mc.gameSettings.keyBindLeft.isPressed()
//                        && !mc.gameSettings.keyBindRight.isPressed() && !mc.gameSettings.keyBindBack.isPressed())
//                    flySpeed = 0;
//
//                if (!superBow.getValue() && !mc.player.isOnGround()) {
//                    float bybass = -0.01F - (mc.player.ticksExisted % 2 == 0 ? 1.0E-4F : 0.006F);
//                    mc.player.setMotionY(bybass);
//                }
////                if (superBow.getValue()) {
////                    float bybass = -0.01F - (mc.player.ticksExisted % 2 == 0 ? 1.0E-4F : 0.006F);
////                    mc.player.setMotionY(mc.player.ticksExisted % 2 == 0 ? 0.41999998688697815 : -0.41999998688697815);
////                    MoveUtils.setMotion(0.8f);
////                    flySpeed = 70;
////                }
//
//                if (!mc.player.isSneaking() && mc.gameSettings.keyBindJump.isPressed()) {
//                    mc.player.setMotionY(0.5);
//                }
//                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
//                    mc.player.setMotionY(-0.5);
//                }
//            }
//        } else
        if (mode.getValueByIndex(0)) {
            if (mc.player.ticksExisted % 2f == 0.0f) {
                assert mc.world != null;
                if (checkWalls.getValue() && !mc.player.collidedHorizontally) {
                    if (!mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(checkWallsRange.getValue(), 0, checkWallsRange.getValue()).expand(0, 0.D, 0))) {
                        return;
                    }
                    if (!mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(-checkWallsRange.getValue(), 0, -checkWallsRange.getValue()).expand(0, 0.D, 0))) {
                        return;
                    }
                }
                if (mc.player.isOnGround() && timer.isDelayComplete(100)) {
                    mc.player.jump();
                    timer.reset();
                    return;
                }
                if (!mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.D, -0.5D, 0.D).expand(0, 0.D, 0))) {
                    return;
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.addVelocity(0, 0.1, 0);
                    mc.player.setMotionY(0.1);
                    mc.player.addVelocity(0, 0, 0);
                    mc.player.abilities.isFlying = false;
                    mc.gameSettings.keyBindSpectatorOutlines.setPressed(true);
                    mc.gameSettings.keyBindForward.setPressed(true);
                    MoveUtils.setSpeed((float) MoveUtils.getSpeed());
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.setMotionY(-0.22);
                } else {
                    if (!mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.D, -1D, 0.D).expand(0, 0.D, 0))) {
                        if (!mc.player.collidedHorizontally) {
                            mc.player.setMotionY(0.039);
                        }
                    }
                }
                mc.player.abilities.isFlying = false;
                if (!mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.D, -1D, 0.D).expand(0, 0.D, 0))) {
                    MoveUtils.setMotion(speed.getValue());
                }
//                if ((mc.player.getCooledAttackStrength(0.0f) == 1.0f)) {
//                    flyTick = true;
//                }
            } else if (mc.player.ticksExisted % 8 == 0) {
                // empty if block
                mc.player.setMotionY(-0.4);
            }
        } else if (mode.getValueByIndex(1)) {
            //MoveUtils.setMotion(MoveUtils.getSpeed());
            if (!mc.player.isOnGround() && !mc.player.collidedVertically && !mc.world.hasNoCollisions(mc.player, mc.player.getBoundingBox().offset(0.D, -0.5D, 0.D).expand(0, 0.D, 0)) && mc.player.fallDistance <= 0.6) {
                mc.player.jump();
                //mc.player.addVelocity(0, 4 , 0);
                //MoveUtils.setMotion(3);

            }
            if (timer.delay(1000, true)) {
                mc.gameSettings.keyBindJump.setPressed(true);
                mc.player.setMotionY(1.2);
                mc.player.setMotionWithMultiplication(5, 5);
                MoveUtils.setMotion(MoveUtils.getSpeed());
            }
        } else if (mode.getValueByIndex(2)) {
            if (mc.player.hurtTime > 0) {

                mc.player.setMotionY(mc.player.getMotion().y + 0.3f);
                if (!mc.player.isOnGround()) {
                    mc.player.setMotion(mc.player.getMotion().x - MathHelper.sin((float) Math.toRadians(mc.player.rotationYaw)), mc.player.getMotion().z + MathHelper.cos((float) Math.toRadians(mc.player.rotationYaw)));
                }
            }
        } else if (mode.getValueByIndex(3)) {
            if (mc.player.ticksExisted % 2f == 0.0f) {
                if (mc.player.isOnGround()) {
                    mc.player.jump();
                    return;
                }
                /*if (Math.hypot(mc.player.posX - mc.player.prevPosX, mc.player.posZ - mc.player.prevPosZ) * (double) mc.timer.timerSpeed * 20.0D <= 30) {
                    MoveUtils.setSpeed((float) (MoveUtils.getSpeed() + 0.001));
                }*/
                mc.player.jump();
                //mc.player.motionY = 0.5;
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.setMotionY(mc.player.getMotion().y - 0.12);
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.setMotionY(mc.player.getMotion().y - 0.76);
                } else if (mc.player.getMotion().y > 0.0) {
                    mc.player.setMotionY(mc.player.getMotion().y - 0.36);
                }
                if (MoveUtils.isMovingSprint()) {
//                    if (customSpeed.getValue()) {
//                        MoveUtils.setSpeed(speed.getValue());
//                    } else {
                        MoveUtils.setSpeed((float) MoveUtils.getSpeed());
                    //}

                    MoveUtils.setStrafe();
                }
                //if (timer.delay(speed.getValue(), true)) {
                //if (timer.delay(motion.getValue(), true)) {
                MoveUtils.disabler(MoveUtils.DisablerType.ONCE);
                //}
                //}
            } else if (mc.player.ticksExisted % 8 == 0) {
                // empty if block
                //mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mode.getValueByIndex(1)) {
            if (event.getPacket() instanceof SPlayerPositionLookPacket) {
                mc.player.setPosition(((SPlayerPositionLookPacket) event.getPacket()).getX(), ((SPlayerPositionLookPacket) event.getPacket()).getY(), ((SPlayerPositionLookPacket) event.getPacket()).getZ());
                mc.player.connection.sendPacket(new CConfirmTeleportPacket(((SPlayerPositionLookPacket) event.getPacket()).getTeleportId()));
                //mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(((SPacketPlayerPosLook) event.getPacket()).getX(), ((SPacketPlayerPosLook) event.getPacket()).getY(), ((SPacketPlayerPosLook) event.getPacket()).getZ(), ((SPacketPlayerPosLook) event.getPacket()).getYaw(), ((SPacketPlayerPosLook) event.getPacket()).getPitch(), false));
                event.setCancelled(true);
                //mc.timer.timerSpeed = 1;
            }
        } else if (mode.getValueByIndex(0)) {
            if (event.getPacket() instanceof SPlayerPositionLookPacket) {
                ChatUtils.addChatMessage(TextFormatting.YELLOW + "[" + TextFormatting.GOLD +"drainwalk" + TextFormatting.YELLOW + "]"+ TextFormatting.DARK_GRAY + " << " + TextFormatting.RED + "Flagged!" + TextFormatting.RESET);
                this.toggle();
                if (antiKick.getValue()) {
                    ChatUtils.addChatMessage(TextFormatting.YELLOW + "[" + TextFormatting.GOLD +"drainwalk" + TextFormatting.YELLOW + "]"+ TextFormatting.DARK_GRAY + " << " +TextFormatting.RED + "Enabling FlagResetter!" + TextFormatting.RESET);
//                    Drainwalk.moduleManager.flagResetter.toggle();
//                    Drainwalk.moduleManager.freeCam.toggle();
                }
            }
        }
    }
}