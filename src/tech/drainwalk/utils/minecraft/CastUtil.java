package tech.drainwalk.utils.minecraft;

import com.google.common.base.Predicates;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.optifine.reflect.Reflector;
import tech.drainwalk.Drainwalk;
import tech.drainwalk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CastUtil extends Utils {

    private final List<EntityType> casts = new ArrayList<>();
    private static final GameSettings gamesettings = mc.gameSettings;

    public static EntityType isInstanceof(Entity entity, EntityType... types) {
        for (EntityType type : types) {
            if (type == EntityType.VILLAGERS && entity instanceof VillagerEntity) {
                return type;
            }
            if (type == EntityType.PLAYERS && entity instanceof PlayerEntity && entity != mc.player && !dw.getApiMain().getFriendManager().existsByName(entity.getName().getString())) {
                return type;
            }
            if (type == EntityType.MOBS && entity instanceof MobEntity) {
                return type;
            }
            if (type == EntityType.ANIMALS && (entity instanceof AnimalEntity || entity instanceof GolemEntity)) {
                return type;
            }
            if (type == EntityType.FRIENDS && entity instanceof PlayerEntity && dw.getApiMain().getFriendManager().existsByName(entity.getName().getString())) {
                return type;
            }
            if (type == EntityType.SELF && entity == mc.player && gamesettings.getPointOfView() != PointOfView.FIRST_PERSON) {
                return type;
            }if (type == EntityType.SELF_NO_CHECK && entity == mc.player) {
                return type;
            }
            if (type == EntityType.ITEMS && entity instanceof ItemEntity) {
                return type;
            }
        }
        return null;
    }

    public CastUtil apply(EntityType type) {
        this.casts.add(type);
        return this;
    }

    public EntityType[] build() {
        return this.casts.toArray(new EntityType[0]);
    }

    public static Entity getMouseOver(Entity target, float yaw, float pitch, double distance)
    {
        Entity entity = mc.getRenderViewEntity();
        RayTraceResult objectMouseOver = null;

        if (entity != null && mc.world != null)
        {
            double d0 = distance;
            double d1 = d0;
            d1 = d1 * d1;
            Vector3d vector3d = entity.getEyePosition(1);
            Vector3d vector3d1 = getVectorForRotation(pitch, yaw);
            Vector3d vector3d2 = vector3d.add(vector3d1.x * d0, vector3d1.y * d0, vector3d1.z * d0);
            AxisAlignedBB axisalignedbb = target.getBoundingBox().expand(vector3d1.scale(d0)).grow(1.0D, 1.0D, 1.0D);
            EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(entity, vector3d, vector3d2, axisalignedbb, (p_lambda$getMouseOver$0_0_) ->
            {
                return !p_lambda$getMouseOver$0_0_.isSpectator() && p_lambda$getMouseOver$0_0_.canBeCollidedWith();
            }, d1);

            if (entityraytraceresult != null)
            {
                objectMouseOver = entityraytraceresult;
            }

        }
        return ((EntityRayTraceResult) objectMouseOver).getEntity();
    }

//    public static RayTraceResult rayTrace(double blockReachDistance, float yaw, float pitch, boolean walls) {
//        if (!walls) {
//            return null;
//        }
//        Vector3d vec3d = mc.player.getEyePosition(1);
//        Vector3d vec3d1 = getVectorForRotation(pitch, yaw);
//        Vector3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance,
//                vec3d1.z * blockReachDistance);
//        return mc.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d2, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, mc.player));
//    }
//
//    public static RayTraceResult rayTrace(double blockReachDistance, float yaw, float pitch) {
//        Vector3d vec3d = mc.player.getEyePosition(1);
//        Vector3d vec3d1 = getVectorForRotation(pitch, yaw);
//        Vector3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance,
//                vec3d1.z * blockReachDistance);
//        return mc.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d2, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, mc.player));
//    }

    private static Vector3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vector3d(f1 * f2, f3, f * f2);
    }

    private static AxisAlignedBB expandXyz(double value, AxisAlignedBB axisAlignedBB) {
        return axisAlignedBB.expand(value, value, value);
    }

    private static Vector3d getLook(float yaw, float pitch) {
        return getVectorForRotation(pitch, yaw);
    }

    public enum EntityType {
        PLAYERS, MOBS, ANIMALS, VILLAGERS, FRIENDS, SELF, SELF_NO_CHECK, ITEMS;
    }
}
