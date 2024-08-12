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
                mc.getMainWindow().getWidth() / 2 - getDraggableOption().getWidth() / 2,
                getDraggableOption().getValue().y
        ));
    }

    @Override
    public void onRender2D(EventRender2D event) {
        final float x = getDraggableOption().getValue().x;
        final float y = getDraggableOption().getValue().y;
        final int[] backgroundColors = getBackgroundColorsWithAlpha();
        final MatrixStack matrixStack = event.getMatrixStack();

        RenderService.drawRoundedLinearGradientRect(matrixStack, x, y, getDraggableOption().getWidth(), getDraggableOption().getHeight(), getRound().getValue(), backgroundColors[0], backgroundColors[1]);
    }
}
