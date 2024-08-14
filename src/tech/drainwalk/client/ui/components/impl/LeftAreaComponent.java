package tech.drainwalk.client.ui.components.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.ui.UIMain;
import tech.drainwalk.client.ui.components.Component;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.font.Icon;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScreenService;

public class LeftAreaComponent extends Component {

    private final float PADDING = 16;

    public LeftAreaComponent(float x, float y, float width, float height, UIMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // logo
        RenderService.drawRoundedRect(matrixStack, x + PADDING, y + PADDING, 32, 32, 6, ColorService.getColorWithAlpha(additionalThirdColor, 0.2f));
        RenderService.drawRoundedOutlineVerLinearGradientRect(matrixStack, x + PADDING, y + PADDING, 32, 32, 6, 1, additionalThirdColor, ColorService.getColorWithAlpha(additionalThirdColor, 0));
        ICONS.drawCenteredText(matrixStack, Icon.LOGO.getSymbolString(), x + PADDING + (float) 32 / 2, y + PADDING + 7, textFirstColor, 20);

        drawCategories(matrixStack, mouseX, mouseY, x + PADDING, y + PADDING * 3 + 32, mc.getTimer().renderPartialTicks);
    }

    private void drawCategories(MatrixStack matrixStack, int mouseX, int mouseY, float x, float y, float partialTicks) {
        float offset = 0;
        for (Category category : Category.values()) {
            // animations
            category.getAnimation().animate(0, 1, 0.3f, EasingList.NONE, partialTicks);

            final boolean selected = parent.getSelectedCategory() == category;
            final boolean hovered = ScreenService.isHovered(mouseX, mouseY, x, y + offset, 32, 32);
            int iconColor = hovered ? ColorService.getColorWithAlpha(textSecondColor, 0.5f) : ColorService.getColorWithAlpha(textSecondColor, 0.2f);
            iconColor = selected ?
                    ColorService.getColorWithAlpha(additionalSecondColor, 0.5f + 0.5f * category.getAnimation().getAnimationValue()) :
                    iconColor;
            int rectColor = hovered ? ColorService.getColorWithAlpha(additionalSecondColor, 0.11f) : 0;
            rectColor = selected ?
                    ColorService.getColorWithAlpha(additionalSecondColor, 0.11f + 0.09f * category.getAnimation().getAnimationValue()) :
                    rectColor;

            RenderService.drawRoundedRect(matrixStack, x, y + offset, 32, 32, 6, rectColor);
            ICONS.drawCenteredText(matrixStack, category.getIcon().getSymbolString(), x + (float) 32 / 2, y + offset + 5, iconColor, 24);
            offset += 42;
        }
    }

    @Override
    public void tick() {
        for (Category category : Category.values()) {
            category.getAnimation().update(parent.getSelectedCategory() == category);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float offset = 0;
        for (Category category : Category.values()) {
            final boolean hovered = ScreenService.isHovered((int) mouseX, (int) mouseY, x + PADDING, y + PADDING * 3 + 32 + offset,32, 32);
            if (hovered) {
                parent.setSelectedCategory(category);
            }
            offset += 42;
        }
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {}

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {}

}
