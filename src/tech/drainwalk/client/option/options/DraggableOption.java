package tech.drainwalk.client.option.options;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.math.vector.Vector2f;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScreenService;

import java.util.List;
import java.util.function.BooleanSupplier;

public class DraggableOption extends Option<Vector2f> implements IInstanceAccess {
    @Getter
    @Setter
    private float width;
    @Getter
    @Setter
    private float height;
    @Setter
    private boolean drag;
    private float prevX, prevY;

    private final float gridSize = 100;  // Размер ячейки сетки

    public DraggableOption(String settingName, Vector2f value, float width, float height) {
        super(settingName, value);
        this.width = width;
        this.height = height;
    }

    @Override
    public DraggableOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public DraggableOption addSettingDescription(String settingDescription) {
        this.settingDescription = settingDescription;
        return this;
    }

    public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (drag) {
            setValue(new Vector2f(mouseX - prevX, mouseY - prevY));

            if (!InputMappings.isKeyDown(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                applyBounds(); // Применяем ограничения по границам экрана
                alignToGrid(); // Применяем выравнивание по сетке
                drawGrid(matrixStack); // Рисуем сетку
            }

        }
    }

    public void click(int mouseX, int mouseY) {
        if (ScreenService.isHovered(mouseX, mouseY, getValue().x, getValue().y, width, height)) {
            drag = true;
            prevX = mouseX - getValue().x;
            prevY = mouseY - getValue().y;
        }
    }

    public void release() {
        drag = false;
    }

    private void applyBounds() {
        float maxX = mc.getMainWindow().getWidth() - width;
        float maxY = mc.getMainWindow().getHeight() - height;

        Vector2f pos = getValue();

        pos = new Vector2f(
                Math.max(0, Math.min(pos.x, maxX)),
                Math.max(0, Math.min(pos.y, maxY))
        );

        setValue(pos);
    }

    private void alignToGrid() {
        Vector2f pos = getValue();

        // Calculate grid size dynamically based on screen dimensions
        float screenWidth = mc.getMainWindow().getWidth();
        float screenHeight = mc.getMainWindow().getHeight();

        float divisions = 8; // Number of divisions, can be adjusted for more or less grid lines
        float dynamicGridSizeX = screenWidth / divisions;
        float dynamicGridSizeY = screenHeight / divisions;

        // Calculate potential snap positions for each reference point
        float[] snapX = new float[]{
                Math.round(pos.x / dynamicGridSizeX) * dynamicGridSizeX,                           // Top-left corner
                Math.round((pos.x + width) / dynamicGridSizeX) * dynamicGridSizeX - width,        // Top-right corner
                Math.round((pos.x + width / 2) / dynamicGridSizeX) * dynamicGridSizeX - width / 2 // Center
        };

        float[] snapY = new float[]{
                Math.round(pos.y / dynamicGridSizeY) * dynamicGridSizeY,                           // Top-left corner
                Math.round((pos.y + height) / dynamicGridSizeY) * dynamicGridSizeY - height,      // Bottom-left corner
                Math.round((pos.y + height / 2) / dynamicGridSizeY) * dynamicGridSizeY - height / 2 // Center
        };

        // Find the closest snapping positions for x and y
        float closestX = snapX[0];
        float closestY = snapY[0];
        float minDistance = Float.MAX_VALUE;

        for (float sx : snapX) {
            for (float sy : snapY) {
                float distance = distance(pos.x, pos.y, sx, sy);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestX = sx;
                    closestY = sy;
                }
            }
        }

        // Ensure that the aligned position is within bounds before setting it
        closestX = Math.max(0, Math.min(closestX, mc.getMainWindow().getWidth() - width));
        closestY = Math.max(0, Math.min(closestY, mc.getMainWindow().getHeight() - height));

        // Apply the aligned position
        setValue(new Vector2f(closestX, closestY));
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void drawGrid(MatrixStack matrixStack) {
        GLService.INSTANCE.rescale(1);
        // Get the screen width and height from the Minecraft instance
        float screenWidth = mc.getMainWindow().getWidth() - 1;
        float screenHeight = mc.getMainWindow().getHeight() - 1;

        // Define the default color of the grid lines
        int defaultGridColor = 0x60FFFFFF; // Semi-transparent white
        int snapGridColor = 0xFFFFFF00;    // Yellow for snapping lines

        // Calculate dynamic grid sizes based on screen dimensions
        float divisions = 8; // Number of divisions, can be adjusted for more or fewer grid lines
        float dynamicGridSizeX = screenWidth / divisions;
        float dynamicGridSizeY = screenHeight / divisions;

        // Get the current position of the draggable element
        Vector2f pos = getValue();
        float elementCenterX = pos.x + width / 2;
        float elementCenterY = pos.y + height / 2;

        // Define the snapping threshold (distance within which the line turns yellow)
        float snapThreshold = 5.0f;

        // Draw vertical grid lines
        for (float x = 0; x <= screenWidth; x += dynamicGridSizeX) {
            int gridColor = Math.abs(x - elementCenterX) < snapThreshold ? snapGridColor : defaultGridColor;
            RenderService.drawRect(matrixStack, x, 0, 1, screenHeight, gridColor);
        }

        // Draw horizontal grid lines
        for (float y = 0; y <= screenHeight; y += dynamicGridSizeY) {
            int gridColor = Math.abs(y - elementCenterY) < snapThreshold ? snapGridColor : defaultGridColor;
            RenderService.drawRect(matrixStack, 0, y, screenWidth, 1, gridColor);
        }
        GLService.INSTANCE.rescaleMC();
    }

    public boolean checkOverlap(List<DraggableOption> otherOptions) {
        for (DraggableOption other : otherOptions) {
            if (other != this && isOverlapping(other)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlapping(DraggableOption other) {
        Vector2f pos = getValue();
        Vector2f otherPos = other.getValue();

        return pos.x < otherPos.x + other.getWidth() &&
                pos.x + width > otherPos.x &&
                pos.y < otherPos.y + other.getHeight() &&
                pos.y + height > otherPos.y;
    }
}