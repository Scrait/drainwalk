package tech.drainwalk.client.draggables;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.*;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.utils.time.Timer;

public class Watermark extends DraggableComponent {

    private float fps;

    public Watermark() {
        super("Watermark", new Vector2f(5, 5), 257, 26);
    }

    @Override
    public void onRender2D(EventRender2D event) {
        fps = AnimationService.animation(fps, Minecraft.getDebugFPS(), (float) Timer.deltaTime());
        final float x = getDraggableOption().getValue().x;
        final float y = getDraggableOption().getValue().y;
        final float[] paddings = {8, 6};
        final float paddingFromLogo = ICONS.getWidth("w", 16);
        final int someTextColor = -1;
        final Style textFirstStyle = Style.EMPTY.setColor(Color.fromInt(someTextColor));
        final Style textSecondStyle = Style.EMPTY.setColor(Color.fromInt(mainColor));
        final Style dotsStyle = Style.EMPTY.setColor(Color.fromHex("#4B4A4F"));
        final Style squareBracketsStyle = Style.EMPTY.setColor(Color.fromHex("#8D8D8F"));
        final ITextComponent text = new StringTextComponent("").setStyle(dotsStyle).
                append(new StringTextComponent("   •   ").setStyle(dotsStyle)).
                append(new StringTextComponent(dw.getProfile().getUsername()).setStyle(textFirstStyle)).
                append(new StringTextComponent(" [").setStyle(squareBracketsStyle)).
                append(new StringTextComponent(dw.getProfile().getRole()).setStyle(textSecondStyle)).
                append(new StringTextComponent("]").setStyle(squareBracketsStyle)).
                append(new StringTextComponent("   •   ").setStyle(dotsStyle)).
                append(new StringTextComponent(String.valueOf((int) fps)).setStyle(textFirstStyle)).
                append(new StringTextComponent("FPS").setStyle(textSecondStyle)).
                append(new StringTextComponent("   •   ").setStyle(dotsStyle)).
                append(new StringTextComponent(String.valueOf(mc.getCurrentServerData() != null ? mc.getCurrentServerData().pingToServer : 0)).setStyle(textFirstStyle)).
                append(new StringTextComponent("MS").setStyle(textSecondStyle));
        getDraggableOption().setWidth(paddingFromLogo + paddings[0] * 2 + SFPD_REGULAR.getWidth(text, 13));
        final float width = getDraggableOption().getWidth();
        final float height = getDraggableOption().getHeight();

        RenderService.drawRoundedLinearGradientRect(event.getMatrixStack(), x, y, width, height, 6, backgroundFirstColor, backgroundSecondColor);
        ICONS.drawText(event.getMatrixStack(), "w", x + paddings[0], y + paddings[1], mainColor, 16);
        SFPD_REGULAR.drawText(event.getMatrixStack(), text, x + paddings[0] + paddingFromLogo, y + paddings[1], 13, 1);
    }

}
