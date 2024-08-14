package tech.drainwalk.services.render;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.experimental.UtilityClass;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

@UtilityClass
public class ScissorService implements IInstanceAccess {

    /**
     * Включает режим обрезки с заданными координатами и размерами.
     *
     * @param x      X-координата нижнего левого угла области обрезки.
     * @param y      Y-координата нижнего левого угла области обрезки.
     * @param width  Ширина области обрезки.
     * @param height Высота области обрезки.
     */
    public void enableScissor(int x, int y, int width, int height) {
        int windowHeight = mc.getMainWindow().getHeight();
        int adjustedY = windowHeight - (y + height);

        RenderSystem.enableScissor(x, adjustedY, width, height);
    }

    /**
     * Отключает режим обрезки и восстанавливает предыдущее состояние.
     */
    public void disableScissor() {
        RenderSystem.disableScissor();
    }

}
