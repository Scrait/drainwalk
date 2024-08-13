package tech.drainwalk.client.ui.components.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.ui.UIMain;
import tech.drainwalk.client.ui.components.Component;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.font.Icon;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScreenService;

import java.awt.*;

public class LeftAreaComponent extends Component {

    public LeftAreaComponent(float x, float y, float width, float height, UIMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // logo
        final float PADDING = 16;
        RenderService.drawRoundedRect(matrixStack, x + PADDING, y + PADDING, 32, 32, 6, ColorService.getColorWithAlpha(additionalThirdColor, 0.2f));
        RenderService.drawRoundedOutlineVerLinearGradientRect(matrixStack, x + PADDING, y + PADDING, 32, 32, 6, 1, additionalThirdColor, ColorService.getColorWithAlpha(additionalThirdColor, 0));
        ICONS.drawCenteredText(matrixStack, Icon.LOGO.getSymbolString(), x + PADDING + (float) 32 / 2, y + PADDING + 7, textFirstColor, 20);

        drawCategories(matrixStack, mouseX, mouseY, x + PADDING, y + PADDING * 3 + 32, partialTicks);
    }

    private void drawCategories(MatrixStack matrixStack, int mouseX, int mouseY, float x, float y, float partialTicks) {
        final Vector2f fixedMouseCords = GLService.INSTANCE.normalizeCords(mouseX, mouseY, 1);
        float offset = 0;
        for (Category category : Category.values()) {
            // animations
            category.getHoveredAnimation().animate(0, 1, 0.007f, EasingList.NONE, partialTicks);

            final boolean selected = parent.getSelectedCategory() == category;
            final int iconColor = selected ? textSecondColor : ColorService.getColorWithAlpha(textSecondColor, 0.2f);
            final boolean hovered = ScreenService.isHovered((int) fixedMouseCords.x, (int) fixedMouseCords.y, x, y + offset, 32, 32);
            final int rectColor = hovered ? ColorService.getColorWithAlpha(additionalSecondColor, 0.15f) : 0;

            category.getHoveredAnimation().update(hovered);
            RenderService.drawRoundedRect(matrixStack, x, y + offset, 32, 32, 6, rectColor);
            ICONS.drawCenteredText(matrixStack, category.getIcon().getSymbolString(), x + (float) 32 / 2, y + offset + 6, iconColor, 21);
            offset += 45;
        }
    }

    @Override
    public void tick() {
        for (Category category : Category.values()) {

        }
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {}

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {}

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {}

}
