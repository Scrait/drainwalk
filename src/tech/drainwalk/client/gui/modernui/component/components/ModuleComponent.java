package tech.drainwalk.client.gui.modernui.component.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.client.gui.modernui.component.Component;
import tech.drainwalk.services.render.ScreenService;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.math.MathematicUtils;
import tech.drainwalk.services.render.StencilService;
import tech.drainwalk.utils.time.Timer;

import java.util.List;

public class ModuleComponent extends Component {

    //private final Animation scrollAnimation = new Animation();
    private final Animation upAnimation = new Animation();
    private final Animation changeAnimation = new Animation();
    private boolean direction;
    private float scrollValue = 0;
    private float scrollAnim = 0;
    private Category prevCategory = parent.getSelectedCategory();
    private Category currentCategory = parent.getSelectedCategory();

    public ModuleComponent(float x, float y, float width, float height, MenuMain parent) {
        // padding
        super(x + 14, y + 12, width - 28, height - 24, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        scrollAnim = AnimationService.animation(scrollAnim, scrollValue, (float) (Timer.deltaTime() * 360));
//        scrollAnimation.animate(scrollValue, 0, 2, EasingList.NONE, partialTicks);
//        scrollAnimation.update(false);
//        RenderService.drawRoundedGradientRect(matrixStack, x, y, width, height, 17, ClientColor.panel, ClientColor.panelMain, ClientColor.panel, ClientColor.panelMain);
        StencilService.initStencilToWrite();
        RenderService.drawRoundedRect(matrixStack, x + 1, y + 1, width - 2, height - 2, 17, -1);
        StencilService.readStencilBuffer(1);
        //RenderUtils.drawRect(x, y, width, height, -1);
        float offset = 11 + scrollAnim;
        for (Module module : dw.getApiMain().getModuleManager().stream().filter(module -> module.getCategory() == currentCategory).toList()) {
            module.getHoveredAnimation().animate(0, 1, 0.007f, EasingList.NONE, partialTicks);
            module.getAnimation().animate(0, 1, 0.007f, EasingList.NONE, partialTicks);
            //RenderService.renderRect(matrixStack, x, y + offset - 11, width,  FontManager.REGULAR_20.getFontHeight() + 22   , -1);
            final boolean hovered = ScreenService.isHovered(mouseX, mouseY, x + 1, y + offset - 11, width - 2,FontManager.REGULAR_20.getFontHeight() + 22);
            module.getHoveredAnimation().update(hovered);
            module.getAnimation().update(module.isEnabled());
            float hoverAnimationValue = module.getHoveredAnimation().getAnimationValue();
            float selectAnimationValue = module.getAnimation().getAnimationValue();
            float[] c = ColorService.getRGBAf(ColorService.interpolateColor(ClientColor.textStay, ClientColor.textMain, selectAnimationValue));
            final int color = ColorService.rgbFloat(
                    c[0] + hoverAnimationValue,
                    c[1] + hoverAnimationValue,
                    c[2] + hoverAnimationValue
            );
            final float yPosition = y + offset + FontManager.REGULAR_20.getFontHeight() + 10;
            final float xPosition = x + width;
            matrixStack.translate(xPosition, yPosition, 0);
            matrixStack.translate(-xPosition, -yPosition, 0);
            StencilService.initStencilToWrite();
            RenderService.drawRect(matrixStack, x + 1, y + offset - 11, width - 2, FontManager.REGULAR_20.getFontHeight() + 22, ColorService.getColorWithAlpha(-1, hoverAnimationValue));
            StencilService.readStencilBuffer(1);
//            RenderService.drawRoundedGradientRect(matrixStack, x, y, width, height, 17, ColorService.getColorWithAlpha(ClientColor.panel, hoverAnimationValue), ColorService.rgbaFloat(0, 0, 0, hoverAnimationValue - 1), ColorService.rgbaFloat(0, 0, 0, hoverAnimationValue - 1), ColorService.getColorWithAlpha(ClientColor.panel, hoverAnimationValue));
//            RenderService.drawRoundedGradientRect(matrixStack, x, y, width, height, 17, ColorService.rgbaFloat(0, 0, 0, hoverAnimationValue - 1), ColorService.getColorWithAlpha(ClientColor.panel, hoverAnimationValue),  ColorService.getColorWithAlpha(ClientColor.panel, hoverAnimationValue), ColorService.rgbaFloat(0, 0, 0, hoverAnimationValue - 1));
            StencilService.uninitStencilBuffer();
            StencilService.initStencilToWrite();
            RenderService.drawRoundedRect(matrixStack, x + 1, y + 1, width - 2, height - 2, 17, -1);
            StencilService.readStencilBuffer(1);
            FontManager.REGULAR_20.drawString(matrixStack, module.getName(), x + 10, y + offset + 0.5f, color);

            // icons
            RenderService.drawRoundedRectWithOutline(matrixStack, x + width - 35, y + offset - 1, 10, 10, 3, 2f, ColorService.getColorWithAlpha(ClientColor.panelLines, 0.1f), color);
            FontManager.ICONS_12.drawString(matrixStack, "k", x + width - 32.8f, y + offset + 3.7f, color);
            RenderService.drawRoundedRectWithOutline(matrixStack, x + width - 23, y + offset - 1, 10, 10, 3, 2f, ColorService.getColorWithAlpha(ClientColor.panelLines, 0.1f), color);
            RenderService.drawRoundedRect(matrixStack, x + width - 20, y + offset + 2.2f, 4, 4, 1, color);

            RenderService.drawRect(matrixStack, x + 1, y + offset + FontManager.REGULAR_20.getFontHeight() + 10, width - 2, 1f, ColorService.getColorWithAlpha(ClientColor.panelLines, 0.3f));


            offset += 30;
            //ChatUtils.addChatMessage("d");
        }
        if (changeAnimation.getValue() == 1) {
            currentCategory = parent.getSelectedCategory();
            direction = false;
        } else if (prevCategory != parent.getSelectedCategory()) {
            direction = true;
        }
        changeAnimation.animate(0, 1, 0.015f, EasingList.NONE, partialTicks);
        changeAnimation.update(direction);
        final float changeAnimationValue = changeAnimation.getAnimationValue();
//        RenderService.drawRoundedGradientRect(matrixStack, x, y, width, height, 17, ColorService.getColorWithAlpha(ClientColor.panel, changeAnimationValue), ColorService.getColorWithAlpha(ClientColor.panelMain, changeAnimationValue), ColorService.getColorWithAlpha(ClientColor.panelMain, changeAnimationValue), ColorService.getColorWithAlpha(ClientColor.panel, changeAnimationValue));
//        RenderService.drawRoundedGradientRect(matrixStack, x, y, width, height, 17, ColorService.getColorWithAlpha(ClientColor.panelMain, changeAnimationValue), ColorService.getColorWithAlpha(ClientColor.panel, changeAnimationValue),  ColorService.getColorWithAlpha(ClientColor.panel, changeAnimationValue), ColorService.getColorWithAlpha(ClientColor.panelMain, changeAnimationValue));

        upAnimation.animate(0, 1, 0.012f, EasingList.BACK_OUT, partialTicks);
        upAnimation.update(true);
        RenderService.drawRoundedShadow(matrixStack, x, y + height - 5, width, 50, 15 * upAnimation.getAnimationValue(), 0, ClientColor.panelMain);

        StencilService.uninitStencilBuffer();
        RenderService.drawRoundedOutlineRect(matrixStack, x, y, width, height, 17, 1.5f, ColorService.getColorWithAlpha(ClientColor.panelLines, 0.3f));


        //NORMAL_POST_BLOOM_RUNNABLES.add(() -> RenderUtils.drawRoundedOutlineRect(x, y, width, height, 17, 1.5f, -1));
        prevCategory = parent.getSelectedCategory();
    }

    @Override
    public void tick() {

        final List<Module> list = dw.getApiMain().getModuleManager().stream().filter(module -> module.getCategory() == parent.getSelectedCategory()).toList();
        //scrollValue = MathematicUtils.clamp(scrollValue, -(list.size() * 7.4f), 0);
        //scrollValue = MathematicUtils.clamp(scrollValue, -(GuiAltManager.altList.size() * (28 + 8)) + MathematicUtils.clamp(GuiAltManager.altList.size() * (28 + 8), 0, 252), 0);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrollValue += (int) delta * 23;

        //  System.out.println(scrollValue);
        final List<Module> list = dw.getApiMain().getModuleManager().stream().filter(module -> module.getCategory() == parent.getSelectedCategory()).toList();
        //scrollValue = MathematicUtils.clamp(scrollValue, -(list.size() * (28 + 8)) + MathematicUtils.clamp(list.size() * (28 + 8), 0, 252), 0);
        //scrollValue = MathematicUtils.clamp(scrollValue, -(list.size() * 22) + MathematicUtils.clamp(list.size() * 22, 0, 200), 0);
        scrollValue = MathematicUtils.clamp(scrollValue, -(list.size() * 30) + height, 0);
        return false;
    }

}
