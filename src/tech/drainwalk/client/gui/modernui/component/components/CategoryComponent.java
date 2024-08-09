package tech.drainwalk.client.gui.modernui.component.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.client.gui.modernui.component.Component;
import tech.drainwalk.services.render.ScreenService;
import tech.drainwalk.services.render.ColorService;

public class CategoryComponent extends Component {

    //private float selectAnim;

    public CategoryComponent(float x, float y, float width, float height, MenuMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderService.drawRoundedRect(matrixStack, x + 8, y + 2, 1.5f, height - 15, 2, ColorService.rgb(137, 137, 137));
        float offset = 6;
        for (Category category : Category.values()) {
            category.getHoveredAnimation().animate(0, 1, 0.007f, EasingList.NONE, partialTicks);
            category.getAnimation().animate(0, 1, 0.007f, EasingList.NONE, partialTicks);
            //RenderUtils.drawRect(x + 15, y + offset - (height / Category.values().length) / 3, width - 30, (height - 4) / Category.values().length, -1);
            final boolean hovered = ScreenService.isHovered(mouseX, mouseY, x + 15, y + offset - (height / Category.values().length) / 3, width - 30, (height - 4) / Category.values().length);
            category.getHoveredAnimation().update(hovered);
            final boolean selected = parent.getSelectedCategory() == category;
            category.getAnimation().update(selected);
            float hoverAnimationValue = category.getHoveredAnimation().getAnimationValue();
            float selectAnimationValue = category.getAnimation().getAnimationValue();
            float[] c = ColorService.getRGBAf(ColorService.interpolateColor(ClientColor.textStay, ClientColor.textMain, selectAnimationValue));

            RenderService.drawRoundedRect(matrixStack, x + 8, (y + 2 - 6 + offset), 1.5f, (height - 80) / Category.values().length, 2, ColorService.getColorWithAlpha(ClientColor.textMain, selectAnimationValue));

            RenderService.drawRoundedRect(matrixStack, x + 8, y + 2 - 6 + offset, 1.5f, (height - 80) / Category.values().length, 2,
                    ColorService.getColorWithAlpha(
                            ClientColor.textMain,
                            0.4f * hoverAnimationValue
                    )
            );
            FontManager.ICONS_26.drawString(matrixStack, String.valueOf(category.getIcon()), x + width / 2 - 38, y + offset - 0.7f,
                    ColorService.rgbFloat(
                            c[0] + hoverAnimationValue,
                            c[1] + hoverAnimationValue,
                            c[2] + hoverAnimationValue
                    )
            );
            FontManager.REGULAR_20.drawString(matrixStack, category.getName(), x + width / 2 - 18, y + offset,
                        ColorService.rgbFloat(
                                c[0] + hoverAnimationValue,
                                c[1] + hoverAnimationValue,
                                c[2] + hoverAnimationValue
                        )
                    );
            offset += (height - 4) / Category.values().length;
        }
        //RenderUtils.drawRect(x, y, width, height, -1);
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float offset = 6;
        for (Category category : Category.values()) {
            final boolean hovered = ScreenService.isHovered((int) mouseX, (int) mouseY, x + 15, y + offset - (height / Category.values().length) / 3, width - 30, (height - 4) / Category.values().length);
            if (hovered) {
                parent.setSelectedCategory(category);
            }
            offset += (height - 4) / Category.values().length;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

}
