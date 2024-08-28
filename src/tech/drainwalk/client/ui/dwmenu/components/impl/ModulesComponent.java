package tech.drainwalk.client.ui.dwmenu.components.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.ui.dwmenu.UIMain;
import tech.drainwalk.client.ui.dwmenu.components.Component;
import tech.drainwalk.client.ui.dwmenu.elements.Element;
import tech.drainwalk.client.ui.dwmenu.elements.impl.CheckboxElement;
import tech.drainwalk.client.ui.dwmenu.elements.impl.DropdownElement;
import tech.drainwalk.client.ui.dwmenu.elements.impl.SliderElement;
import tech.drainwalk.client.ui.dwmenu.elements.impl.TogglerElement;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.font.Icon;
import tech.drainwalk.services.render.*;
import tech.drainwalk.utils.time.Timer;

import java.util.*;

public class ModulesComponent extends Component {

    private float scrollOffset = 0;
    private float targetScrollOffset = 0;
    private final float PADDING = 16;
    private final int COLUMNS = 2;
    private final float MODULE_PADDING = 12;

    private final Multimap<Module, Element<? extends Option<?>>> optionElements = ArrayListMultimap.create();
    private final Map<Module, TogglerElement> moduleToggleElement = new HashMap<>();

    public ModulesComponent(float x, float y, float width, float height, UIMain parent) {
        super(x, y, width, height, parent);
        dw.getApiMain().getModuleManager().forEach(module -> {
            module.getSettingList().forEach(option -> {
                if (option instanceof BooleanOption booleanOption) {
                    optionElements.put(module, new CheckboxElement(booleanOption));
                } else if (option instanceof FloatOption floatOption) {
                    optionElements.put(module, new SliderElement(floatOption));
                } else if (option instanceof SelectOption selectOption) {
                    optionElements.put(module, new DropdownElement(selectOption));
                } // else if...
            });
            moduleToggleElement.put(module, new TogglerElement(new BooleanOption(module.getName(), module.isEnabled())));
        });
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();

        float elementWidth = (width - (COLUMNS + 1) * PADDING) / COLUMNS;

        scrollOffset = AnimationService.animation(scrollOffset, targetScrollOffset, (float) (Timer.deltaTime()));

        float[] columnHeights = new float[COLUMNS];
        for (Module module : modules) {
            module.getAnimation().animate(0, 1, 0.2f, EasingList.NONE, mc.getTimer().renderPartialTicks);
            optionElements.get(module).forEach(element -> element.getOpenAnimation().animate(0, 1, 0.3f, EasingList.NONE, mc.getTimer().renderPartialTicks));

            int i = modules.indexOf(module);
            int column = i % COLUMNS;

            float elementX = x + PADDING + column * (elementWidth + PADDING);
            float elementY = y + PADDING + columnHeights[column] - scrollOffset;

            float elementHeight = getModuleHeight(module);
            final float animationValue = module.getAnimation().getAnimationValue();
            final int whiteWith3Alpha = ColorService.getColorWithAlpha(-1, 0.03f);
            final int whiteWith5Alpha = ColorService.getColorWithAlpha(-1, 0.05f);
            final int whiteWith10Alpha = ColorService.getColorWithAlpha(-1, 0.1f);
            final int whiteWith20Alpha = ColorService.getColorWithAlpha(-1, 0.2f);
            final int whiteWith40Alpha = ColorService.getColorWithAlpha(-1, 0.4f);
            final int whiteWith80Alpha = ColorService.getColorWithAlpha(-1, 0.8f);
            final int borderColor = ColorService.interpolateColor(whiteWith5Alpha, ColorService.getColorWithAlpha(additionalSecondColor, 0.1f), animationValue);
            final int backgroundEnabledColor = ColorService.getColorWithAlpha(moduleBackgroundColor, animationValue);
            final int backgroundDisabledColor = ColorService.getColorWithAlpha(-1, 0.03f * (1 - animationValue));
            final int moduleNameColor = ColorService.interpolateColor(whiteWith40Alpha, textFirstColor, animationValue);
            final int moduleDescColor = ColorService.interpolateColor(whiteWith10Alpha, whiteWith20Alpha, animationValue);
            final int optionIconColor = ColorService.interpolateColor(whiteWith10Alpha, ColorService.getColorWithAlpha(additionalSecondColor, 0.4f), animationValue);
            final int bindColor = ColorService.interpolateColor(whiteWith10Alpha, whiteWith80Alpha, animationValue);
            final int bindBgColor = ColorService.interpolateColor(whiteWith3Alpha, ColorService.getColorWithAlpha(additionalSecondColor, 0.2f), animationValue);

            // stencil
            StencilService.initStencilToWrite();
            RenderService.drawRect(matrixStack, x, y, width, height, -1);
            StencilService.readStencilBuffer(1);

            RenderService.drawRoundedVerLinearGradientRect(matrixStack, elementX, elementY, elementWidth, elementHeight, 8,
                    backgroundDisabledColor, ColorService.getColorWithAlpha(backgroundDisabledColor, 0));
            RenderService.drawRoundedVerLinearGradientRect(matrixStack, elementX, elementY, elementWidth, elementHeight, 8,
                    backgroundEnabledColor, ColorService.getColorWithAlpha(backgroundEnabledColor, 0));
            RenderService.drawRoundedOutlineRect(matrixStack, elementX, elementY, elementWidth, elementHeight, 8, 1,
                    borderColor);

            SFPD_REGULAR.drawText(matrixStack, module.getName(), elementX + MODULE_PADDING, elementY + MODULE_PADDING, moduleNameColor, 16);
            SFPD_REGULAR.drawText(matrixStack, module.getDescription(), elementX + MODULE_PADDING, elementY + MODULE_PADDING * 2 + SFPD_REGULAR.getHeight(16), moduleDescColor, 14);

            cursorLogic(mouseX, mouseY, elementX, elementY, elementWidth, MODULE_PADDING * 3 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14));

            // poloska
            RenderService.drawRect(matrixStack, elementX + 1, elementY + MODULE_PADDING * 3 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14), elementWidth - 2, 1, borderColor);

            float bindPosXOffset = 25;
            if (!optionElements.get(module).isEmpty()) {
                ICONS.drawText(matrixStack, Icon.OPTIONS.getSymbolString(),
                        elementX + MODULE_PADDING,
                        elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) + 1,
                        optionIconColor, 14);
                cursorLogic(mouseX, mouseY, elementX + MODULE_PADDING,
                        elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) + 1,
                        14, 14);
                bindPosXOffset = 0;
            }

            final String keyString = module.hasBind() ? new TranslationTextComponent(InputMappings.getInputByCode(module.getCurrentKey(), GLFW.GLFW_KEY_UNKNOWN).func_237520_d_().getString()).getString().toUpperCase() : "NONE";
            final float keyWidth = SFPD_REGULAR.getWidth(keyString, 12) + 10;
            RenderService.drawRoundedRect(matrixStack,
                    elementX + MODULE_PADDING + 25 - bindPosXOffset,
                    elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) - 1,
                    keyWidth, 18, 4, bindBgColor);

            SFPD_REGULAR.drawCenteredText(matrixStack, keyString,
                    elementX + MODULE_PADDING + 25 + keyWidth / 2 - bindPosXOffset,
                    elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) + 2,
                    bindColor, 12);

            final TogglerElement togglerElement = moduleToggleElement.get(module);
            togglerElement.setX(elementX + elementWidth - MODULE_PADDING - togglerElement.getWidth());
            togglerElement.setY(elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) - 1);
            togglerElement.render(matrixStack, mouseX, mouseY, partialTicks);
            cursorLogic(mouseX, mouseY, togglerElement.getX(), togglerElement.getY(), togglerElement.getWidth(), togglerElement.getHeight());

            float optionY = MODULE_PADDING * 2 + elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) +
                    SFPD_REGULAR.getHeight(14) + 2 + SFPD_REGULAR.getHeight(12);

            StencilService.initStencilToWrite();
            RenderService.drawRect(matrixStack, elementX, elementY, elementWidth, elementHeight, -1);
            StencilService.readStencilBuffer(3);
            // poloska
            final Collection<Element<? extends Option<?>>> elements = optionElements.get(module);
            final Element<? extends Option<?>> firstElement = elements.isEmpty() ? null : new ArrayList<>(elements).getFirst();
            final float poloskaAnim = (firstElement == null ? 0 : firstElement.getOpenAnimation().getAnimationValue());
            final float poloskaWidth = (elementWidth - 2);
            RenderService.drawRect(matrixStack, (elementX + 1) + poloskaWidth / 2 - (poloskaWidth / 2) * poloskaAnim, optionY - PADDING / 2 - 4, (poloskaWidth) * poloskaAnim, 1, borderColor);
            for (Element<? extends Option<?>> element : optionElements.get(module)) {
                matrixStack.push();
                element.getWithModuleEnabledAnimation().animate(0, 1, 0.2f, EasingList.NONE, mc.getTimer().renderPartialTicks);
                SFPD_REGULAR.drawText(matrixStack, element.getOption().getSettingName(), elementX + MODULE_PADDING, optionY, moduleNameColor, 14);
                element.setX(elementX + elementWidth - MODULE_PADDING - element.getWidth());
                element.setY(optionY);
                element.render(matrixStack, mouseX, mouseY, partialTicks);

                cursorLogic(mouseX, mouseY, element.getX(), element.getY(), element.getWidth(), element.getHeight());

                optionY += (MODULE_PADDING + element.getHeight()) * element.getOpenAnimation().getAnimationValue();
                matrixStack.pop();
            }
            StencilService.uninitStencilBuffer();

            columnHeights[column] += elementHeight + PADDING;

            StencilService.uninitStencilBuffer();
        }


        if (parent.getSelectedCategory() == Category.SCRIPTS ||
                parent.getSelectedCategory() == Category.CONFIGS ||
                parent.getSelectedCategory() == Category.THEMES) {
            RenderService.drawRoundedTexture(matrixStack, new DrainwalkResource("textures/gui/cat.png"), x, y, width, height, 8, 0.1f);
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {
        final float SCROLL_SPEED = 50f;
        targetScrollOffset -= (float) (delta * SCROLL_SPEED);

        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();

        float totalHeight = 0;
        float[] columnHeights = new float[COLUMNS];

        for (Module module : modules) {
            int i = modules.indexOf(module);
            int column = i % COLUMNS;
            float elementHeight = getModuleHeight(module);
            columnHeights[column] += elementHeight + PADDING;

//            if (column == COLUMNS - 1 || i == modules.size() - 1) {
            totalHeight = Math.max(totalHeight, columnHeights[column]);
//            }
        }
    }

    private float getModuleHeight(Module module) {
        final List<Element<? extends Option<?>>> elements = (List<Element<? extends Option<?>>>) optionElements.get(module);
        final float additionalHeight = (float) elements.stream()
                .mapToDouble(element -> {
                    boolean isLastElement = elements.indexOf(element) == elements.size() - 1;
                    float padding = isLastElement ? 2 * MODULE_PADDING : MODULE_PADDING;
                    return element.getOpenAnimation().getAnimationValue() * (element.getHeight() + padding);
                })
                .sum();

        return 105 + additionalHeight;
    }

    @Override
    public void tick() {
        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();
        float totalHeight = 0;
        float[] columnHeights = new float[COLUMNS];
        for (Module module : modules) {
            int i = modules.indexOf(module);
            int column = i % COLUMNS;
            float elementHeight = getModuleHeight(module);
            columnHeights[column] += elementHeight + PADDING;
            totalHeight = Math.max(totalHeight, columnHeights[column]);
            module.getAnimation().update(module.isEnabled());
            optionElements.get(module).forEach(element -> element.getOpenAnimation().update(module.isOptionsOpened()));
            optionElements.get(module).forEach(Element::tick);
            moduleToggleElement.get(module).getOption().setValue(module.isEnabled());
            moduleToggleElement.get(module).tick();
            for (Element<? extends Option<?>> element : optionElements.get(module)) {
                element.getWithModuleEnabledAnimation().update(module.isEnabled());
            }
        }

        float maxScroll = Math.max(0, totalHeight - height + PADDING);
        targetScrollOffset = Math.max(0, Math.min(targetScrollOffset, maxScroll));
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();

        float elementWidth = (width - (COLUMNS + 1) * PADDING) / COLUMNS;

        float[] columnHeights = new float[COLUMNS];
        for (Module module : modules) {
            int i = modules.indexOf(module);
            int column = i % COLUMNS;

            float elementX = x + PADDING + column * (elementWidth + PADDING);
            float elementY = y + PADDING + columnHeights[column] - scrollOffset;

            float elementHeight = getModuleHeight(module);

            optionElements.get(module).forEach(element -> element.mouseClicked(mouseX, mouseY, button));
            final TogglerElement togglerElement = moduleToggleElement.get(module);
            togglerElement.mouseClicked(mouseX, mouseY, button);
            if (togglerElement.getOption().getValue() != module.isEnabled()) {
                module.toggle();
            }

            if (ScreenService.isHovered((int) mouseX, (int) mouseY, elementX, elementY, elementWidth, MODULE_PADDING * 3 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14))) {
                if (button == 1) {
                    module.setOptionsOpened(!module.isOptionsOpened());
                } else {
                    module.toggle();
                    togglerElement.getOption().setValue(module.isEnabled());
                }
            }

            if (ScreenService.isHovered((int) mouseX, (int) mouseY, elementX + MODULE_PADDING,
                    elementY + MODULE_PADDING * 4 + SFPD_REGULAR.getHeight(16) + SFPD_REGULAR.getHeight(14) + 1, 14, 14))
                module.setOptionsOpened(!module.isOptionsOpened());

            columnHeights[column] += elementHeight + PADDING;
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        optionElements.forEach((module, element) -> element.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {}

}
