package tech.drainwalk.client.draggables;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.*;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.font.Icon;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.utils.time.Timer;

public class Watermark extends DraggableComponent {

    private float fps;

    public Watermark() {
        super("Watermark", new Vector2f(10, 10), 257, 26);
    }

    @Override
    public void onUpdate(UpdateEvent event) {

    }

    @Override
    public void onRender2D(EventRender2D event) {
        fps = AnimationService.animation(fps, Minecraft.getDebugFPS(), (float) Timer.deltaTime());
        final float x = getDraggableOption().getValue().x;
        final float y = getDraggableOption().getValue().y;
        final float[] paddings = {8, 6};
        final float paddingFromLogo = ICONS.getWidth(String.valueOf(Icon.LOGO.getSymbol()), 16);
        final Style textFirstStyle = Style.EMPTY.setColor(Color.fromInt(textFirstColor));
        final Style textSecondStyle = Style.EMPTY.setColor(Color.fromInt(textSecondColor));
        final Style dotsStyle = Style.EMPTY.setColor(Color.fromHex("#4B4A4F"));
        final Style squareBracketsStyle = Style.EMPTY.setColor(Color.fromHex("#8D8D8F"));
        final ITextComponent dot = new StringTextComponent("   •   ").setStyle(dotsStyle);
        final ITextComponent text = new StringTextComponent("").
                append(dot).
                append(new StringTextComponent(dw.getProfile().getUSERNAME()).setStyle(textFirstStyle)).
                append(new StringTextComponent(" [").setStyle(squareBracketsStyle)).
                append(new StringTextComponent(dw.getProfile().getROLE()).setStyle(textSecondStyle)).
                append(new StringTextComponent("]").setStyle(squareBracketsStyle)).
                append(dot).
                append(new StringTextComponent(String.valueOf((int) fps)).setStyle(textFirstStyle)).
                append(new StringTextComponent("FPS").setStyle(textSecondStyle)).
                append(dot).
                append(new StringTextComponent(String.valueOf(mc.getCurrentServerData() != null ? mc.getCurrentServerData().pingToServer : 0)).setStyle(textFirstStyle)).
                append(new StringTextComponent("MS").setStyle(textSecondStyle));
        getDraggableOption().setWidth(paddingFromLogo + paddings[0] * 2 + SFPD_REGULAR.getWidth(text, 13));
        final float width = getDraggableOption().getWidth();
        final float height = getDraggableOption().getHeight();
        final int[] backgroundColors = getBackgroundColorsWithAlpha();

        RenderService.drawRoundedDiagonalGradientRect(event.getMatrixStack(), x, y, width, height, getRound().getValue(), backgroundColors[0], backgroundColors[1]);
        ICONS.drawText(event.getMatrixStack(), String.valueOf(Icon.LOGO.getSymbol()), x + paddings[0], y + paddings[1], textSecondColor, 16);
        SFPD_REGULAR.drawText(event.getMatrixStack(), text, x + paddings[0] + paddingFromLogo, y + paddings[1], 13, 1);
    }

}
