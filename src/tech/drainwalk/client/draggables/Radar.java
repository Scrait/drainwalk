package tech.drainwalk.client.draggables;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.services.render.RenderService;

public class Radar extends DraggableComponent {

    public Radar() {
        super("Radar", new Vector2f(300, 10), 277, 24);
        getDraggableOption().setVisible(() -> false);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        getDraggableOption().setValue(new Vector2f(
                (float) mc.getMainWindow().getWidth() / 2 - getDraggableOption().getWidth() / 2,
                getDraggableOption().getValue().y
        ));
    }

    @Override
    public void onRender2D(EventRender2D event) {
        final float x = getDraggableOption().getValue().x;
        final float y = getDraggableOption().getValue().y;
        final int[] backgroundColors = getBackgroundColorsWithAlpha();
        final MatrixStack matrixStack = event.getMatrixStack();
        final float[] paddings = {8, 6};

        RenderService.drawRoundedLinearGradientRect(matrixStack, x, y, getDraggableOption().getWidth(), getDraggableOption().getHeight(), getRound().getValue(), backgroundColors[0], backgroundColors[1]);
        drawCompass(matrixStack, x + paddings[0], y + paddings[1],
                getDraggableOption().getWidth() - paddings[0] * 2 + 1, getDraggableOption().getHeight() - paddings[1] * 2 - 1,
                mc.gameRenderer.getActiveRenderInfo().getYaw());
    }

    public void drawCompass(MatrixStack matrixStack, float x, float y, float width, float height, float playerYaw) {
        int totalMarks = 180;
        float segmentWidth = width / totalMarks;

        for (int i = -totalMarks / 2; i <= totalMarks / 2; i++) {
            int angle = (int)((playerYaw + i + 360) % 360);

            float markHeight;
            int markColor;

            if (angle % 90 == 0) {
                String direction = getDirectionFromAngle(Math.abs(angle));
                float posX = x + (i + (float) totalMarks / 2) * segmentWidth;

                if (posX >= x && posX <= x + width) {
                    SFPD_REGULAR.drawCenteredText(matrixStack, direction, posX, y + height / 2 - SFPD_REGULAR.getHeight(12) / 2, textSecondColor, 12);
                }

                continue;
            } else if (angle % 45 == 0) {
                markHeight = height;
                markColor = textSecondColor;
            } else if (angle % 9 == 0) {
                markHeight = height * 0.6f;
                markColor = textFirstColor;
            } else {
                continue;
            }

            float posX = x + (i + (float) totalMarks / 2) * segmentWidth;

            if (posX >= x && posX <= x + width) {
                RenderService.drawRect(matrixStack, posX, y + height / 2 - markHeight / 2, 1.0f, markHeight, markColor);
            }
        }
    }

    private String getDirectionFromAngle(int angle) {
        return switch (angle) {
            case 0 -> "S";  // South at 0 degrees
            case 90 -> "W";  // West at 90 degrees
            case 180 -> "N"; // North at 180 degrees
            case 270 -> "E"; // East at 270 degrees
            case 45 -> "SW"; // South-West at 45 degrees
            case 135 -> "NW"; // North-West at 135 degrees
            case 225 -> "NE"; // North-East at 225 degrees
            case 315 -> "SE"; // South-East at 315 degrees
            default -> "";
        };
    }

}
