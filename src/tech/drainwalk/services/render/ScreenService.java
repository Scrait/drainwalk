package tech.drainwalk.services.render;

import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

@UtilityClass
public class ScreenService implements IInstanceAccess {

    public Vector3d screenToWorld(float screenX, float screenY) {
        // Получаем текущее положение камеры
        Vector3d camera_pos = mc.getRenderManager().info.getProjectedView();
        Quaternion cameraRotation = mc.getRenderManager().getCameraOrientation().copy();

        // Нормализуем экранные координаты
        float halfWidth = mc.getMainWindow().getScaledWidth() / 2.0F;
        float halfHeight = mc.getMainWindow().getScaledHeight() / 2.0F;
        float normalizedX = (screenX - halfWidth) / halfWidth;
        float normalizedY = (halfHeight - screenY) / halfHeight;

        // Преобразуем экранные координаты обратно в мировые
        double fov = mc.gameRenderer.getFOVModifier(mc.getRenderManager().info, mc.getRenderPartialTicks(), true);
        float scaleFactor = (float) (mc.getMainWindow().getScaledHeight() / (2.0F * Math.tan(Math.toRadians(fov / 2.0F))));

        // Вектор из нормализованных координат экрана
        Vector3f screenVector = new Vector3f(normalizedX * scaleFactor, normalizedY * scaleFactor, 1.0F);

        // Применяем поворот камеры
        screenVector.transform(cameraRotation);

        // Возвращаем мировые координаты
        return new Vector3d(camera_pos.x - screenVector.getX(), camera_pos.y - screenVector.getY(), camera_pos.z - screenVector.getZ());
    }

    public Vector2f worldToScreen(Vector3d pos) {
        return worldToScreen(pos.x, pos.y, pos.z);
    }

    public Vector2f worldToScreen(double x, double y, double z) {
        Vector3d camera_pos = mc.getRenderManager().info.getProjectedView();
        Quaternion cameraRotation = mc.getRenderManager().getCameraOrientation().copy();
        cameraRotation.conjugate();

        Vector3f result3f = new Vector3f((float) (camera_pos.x - x), (float) (camera_pos.y - y), (float) (camera_pos.z - z));
        result3f.transform(cameraRotation);

        if (mc.gameSettings.viewBobbing) {
            Entity renderViewEntity = mc.getRenderViewEntity();
            if (renderViewEntity instanceof PlayerEntity playerentity) {
                calculateViewBobbing(playerentity, result3f);
            }
        }

        double fov = mc.gameRenderer.getFOVModifier(mc.getRenderManager().info, mc.getRenderPartialTicks(), true);

        return calculateScreenPosition(result3f, fov);
    }

    private void calculateViewBobbing(PlayerEntity playerentity, Vector3f result3f) {
        float walked = playerentity.distanceWalkedModified;
        float f = walked - playerentity.prevDistanceWalkedModified;
        float f1 = -(walked + f * mc.getRenderPartialTicks());
        float f2 = MathHelper.lerp(mc.getRenderPartialTicks(), playerentity.prevCameraYaw, playerentity.cameraYaw);

        Quaternion quaternion = new Quaternion(Vector3f.XP, Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, true);
        quaternion.conjugate();
        result3f.transform(quaternion);

        Quaternion quaternion1 = new Quaternion(Vector3f.ZP, MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, true);
        quaternion1.conjugate();
        result3f.transform(quaternion1);

        Vector3f bobTranslation = new Vector3f((MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F), (-Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2)), 0.0f);
        bobTranslation.setY(-bobTranslation.getY());
        result3f.add(bobTranslation);
    }

    private Vector2f calculateScreenPosition(Vector3f result3f, double fov) {
        float halfHeight = mc.getMainWindow().getScaledHeight() / 2.0F;
        float scaleFactor = halfHeight / (result3f.getZ() * (float) Math.tan(Math.toRadians(fov / 2.0F)));
        if (result3f.getZ() < 0.0F) {
            return new Vector2f(-result3f.getX() * scaleFactor + mc.getMainWindow().getScaledWidth() / 2.0F, mc.getMainWindow().getScaledHeight() / 2.0F - result3f.getY() * scaleFactor);
        }
        return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    public boolean isHovered(int mX, int mY, float x, float y, float width, float height) {
        return (mX > x && mX < (x + width)) && (mY > y && mY < (y + height));
    }

}
