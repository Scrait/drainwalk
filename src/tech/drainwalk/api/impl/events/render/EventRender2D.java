package tech.drainwalk.api.impl.events.render;

import com.darkmagician6.eventapi.events.Event;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import net.minecraft.client.MainWindow;

@Getter
public class EventRender2D implements Event {

    private final MainWindow mainWindow;
    private final MatrixStack matrixStack;
    private final float partialTicks;

    public EventRender2D(MainWindow mainWindow, MatrixStack matrixStack, float partialTicks) {
        this.mainWindow = mainWindow;
        this.matrixStack = matrixStack;
        this.partialTicks = partialTicks;
    }

    public static class Pre extends EventRender2D
    {
        public Pre(MainWindow mainWindow, MatrixStack matrixStack, float partialTicks) {
            super(mainWindow, matrixStack, partialTicks);
        }
    }

}
