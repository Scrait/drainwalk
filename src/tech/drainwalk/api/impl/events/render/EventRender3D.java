package tech.drainwalk.api.impl.events.render;

import com.darkmagician6.eventapi.events.Event;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventRender3D implements Event {
    private final MatrixStack matrixStack;
    private final float partialTicks;

    public static class PreHand extends EventRender3D
    {
        public PreHand(MatrixStack matrixStack, float partialTicks) {
            super(matrixStack, partialTicks);
        }
    }

    public static class PreEntity extends EventRender3D
    {
        public PreEntity(MatrixStack matrixStack, float partialTicks) {
            super(matrixStack, partialTicks);
        }
    }

    public static class PostEntity extends EventRender3D
    {
        public PostEntity(MatrixStack matrixStack, float partialTicks) {
            super(matrixStack, partialTicks);
        }
    }
}
