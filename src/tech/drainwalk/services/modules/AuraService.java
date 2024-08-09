package tech.drainwalk.services.modules;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.events.input.MoveInputEvent;
import tech.drainwalk.services.math.GCDService;
import tech.drainwalk.services.math.MathService;
import tech.drainwalk.services.math.UniqueRandomNumberGenerator;
import tech.drainwalk.utils.movement.MoveUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AuraService implements IInstanceAccess {

//    public final UniqueRandomNumberGenerator randValX = new UniqueRandomNumberGenerator(-1, 1, 0.1f);
//    public final UniqueRandomNumberGenerator randValY = new UniqueRandomNumberGenerator(-2, 2, 0.1f);
    public final UniqueRandomNumberGenerator randValX = new UniqueRandomNumberGenerator(1, 2, 0.1f);
    public final UniqueRandomNumberGenerator randValY = new UniqueRandomNumberGenerator(1, 3, 0.1f);

    public boolean attackCondition(Vector2f rotation, LivingEntity target) {
        return canAttack();
    }

    public LivingEntity smartSortEntities(LivingEntity target) {
        return getValidTargets().stream()
                .min(Comparator.comparing((Entity e) -> e != target)
                        .thenComparingDouble(e -> e.getDistanceSq(mc.player))
                        .thenComparingInt(Entity::getEntityId))
                .orElse(null);
    }

    public Vector2f applyRotationPatch(Vector2f rotation) {
        return new Vector2f(GCDService.getFixedRotation(rotation.x),
                GCDService.getFixedRotation(rotation.y));
    }

    private float getRandomValue(float randVal) {
        return (new Random().nextFloat() * 2 - 1) * randVal;
    }

    public Vector2f calculateLookAngles(LivingEntity target) {
//        Vec3d playerEyes = mc.player.getEyePos(); // Player's eye position
//        Vec3d targetPos = target.getLerpedPos(mc.getTickDelta()).add(0, target.getHeight() / 2.0, 0); // Target's center position
//        Vec3d direction = targetPos.subtract(playerEyes);
//
//        float yaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
//        float pitch = (float) Math.toDegrees(Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)));
//
//        return new Vec2f(yaw + getRandomValue(randValX.getCurrent()), -pitch + getRandomValue(randValY.getCurrent()));
        Vector3d playerEyes = mc.player.getEyePosition(1); // Позиция глаз игрока
        Vector3d closestPoint = findClosestPointOnBoundingBox(playerEyes, getSmoothedBoundingBox(target, mc.getRenderPartialTicks()));
        Vector3d direction = closestPoint.subtract(playerEyes).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
        float pitch = (float) Math.toDegrees(Math.asin(direction.y));
        return new Vector2f(yaw + getRandomValue(randValX.getCurrent()), -pitch + getRandomValue(randValY.getCurrent()));
    }

    public Vector2f smoothRotation(Vector2f currentRotation, Vector2f targetRotation, float lerpSpeed) {
        float yaw = targetRotation.x;
        float pitch = targetRotation.y;
        final float lastYaw = currentRotation.x;
        final float lastPitch = currentRotation.y;

        if (lerpSpeed != 0) {

            final double deltaYaw = MathHelper.wrapDegrees(targetRotation.x - currentRotation.x);
            final double deltaPitch = pitch - lastPitch;

            final double distance = Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
            final double distributionYaw = Math.abs(deltaYaw / distance);
            final double distributionPitch = Math.abs(deltaPitch / distance);

            final double maxYaw = lerpSpeed * distributionYaw;
            final double maxPitch = lerpSpeed / (lerpSpeed / 5) * distributionPitch;

            final float moveYaw = (float) Math.max(Math.min(deltaYaw, maxYaw), -maxYaw);
            final float movePitch = (float) Math.max(Math.min(deltaPitch, maxPitch), -maxPitch);

            yaw = lastYaw + moveYaw;
            pitch = lastPitch + movePitch;

            for (int i = 1; i <= (int) (Minecraft.getDebugFPS() / 20f + Math.random() * 10); ++i) {

                if (Math.abs(moveYaw) + Math.abs(movePitch) > 1) {
                    yaw += (float) ((Math.random() - 0.5) / 1000);
                    pitch -= (float) (Math.random() / 200);
                }
                final Vector2f fixedRotations = new Vector2f(yaw, pitch);
                yaw = fixedRotations.x;
                pitch = Math.max(-90, Math.min(90, fixedRotations.y));
            }
        }

        return new Vector2f(yaw, pitch);
    }

    public void fixMovement(final MoveInputEvent event, final float yaw) {
        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        final double angle = MathHelper.wrapDegrees(Math.toDegrees(direction(mc.player.rotationYaw, forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }

    private double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    private AxisAlignedBB getSmoothedBoundingBox(LivingEntity player, float delta) {
        Vector3d lerpedPos = player.getLerpedPos(delta);
        AxisAlignedBB originalBoundingBox = player.getBoundingBox();
        Vector3d offset = lerpedPos.subtract(player.getPosX(), player.getPosY(), player.getPosZ());
        return originalBoundingBox.offset(offset);
    }

    private boolean canAttack() {
        boolean flag2 = mc.player.getBlockState().isIn(Blocks.COBWEB) || MoveUtils.isInLiquid() || mc.player.isRidingHorse() || (mc.player.fallDistance > 0.0F && !mc.player.isOnGround() && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isPotionActive(Effects.BLINDNESS) && !mc.player.isPassenger());
        return mc.player.getCooledAttackStrength(0.5F) > 0.9F && flag2;
    }

    private Vector3d findClosestPointOnBoundingBox(Vector3d point, AxisAlignedBB boundingBox) {
        double x = MathService.clamp(point.x, boundingBox.minX, boundingBox.maxX);
        double y = MathService.clamp(point.y, boundingBox.minY, boundingBox.maxY);
        double z = MathService.clamp(point.z, boundingBox.minZ, boundingBox.maxZ);
        return new Vector3d(x, y, z);
    }

    private List<LivingEntity> getValidTargets() {
        List<LivingEntity> validTargets = new ArrayList<>();
        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof LivingEntity livingEntity) {
                if (isTargetValid(livingEntity)) {
                    validTargets.add(livingEntity);
                }
            }
        }
        return validTargets;
    }

    private boolean isTargetValid(LivingEntity target) {
//        if (!checkDistance(true, target)) {
//            return false;
//        }
//        if(target instanceof ArmorStandEntity){
//            return false;
//        }
//        if (ignoreNaked.getValue() && target.getTotalArmorValue() == 0) {
//            return false;
//        }
        if (target == mc.player) {
            return false;
        }
        if (mc.player.getDistance(target) > 3.2f) {
            return false;
        }
//        if (target.getHealth() <= 0) {
//            return false;
//        }
//        if (!targetsCheck(target)) {
//            return false;
//        }
//        if (target instanceof PlayerEntity
//                && !(dw.getApiMain().getModuleManager().findByClass(AntiBot.class).isValid((PlayerEntity) target)))
//            return false;
        return true;
    }

}
