package tech.drainwalk.client.ui.components.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.client.ui.UIMain;
import tech.drainwalk.client.ui.components.Component;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.services.render.ScissorService;

import java.util.List;

public class ModulesComponent extends Component {

    private float scrollOffset = 0;
    private float targetScrollOffset = 0;
    private final float PADDING = 16;
    private final int COLUMNS = 2;

    public ModulesComponent(float x, float y, float width, float height, UIMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        final float MODULE_PADDING = 12;

        List<Module> modules = dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList();

        float elementWidth = (width - (COLUMNS + 1) * PADDING) / COLUMNS;

        scrollOffset += (targetScrollOffset - scrollOffset) * 0.2f;

        ScissorService.enableScissor((int) x, (int) y, (int) width, (int) height);

        float[] columnHeights = new float[COLUMNS]; // Высоты текущих колонок
        for (Module module : modules) {
            int i = modules.indexOf(module);
            int column = i % COLUMNS;

            // Определяем координаты для текущего элемента
            float elementX = x + PADDING + column * (elementWidth + PADDING);
            float elementY = y + PADDING + columnHeights[column] - scrollOffset;

            // Определяем высоту текущего элемента
            float elementHeight = getModuleHeight(module);

            // Рендерим фон модуля
            RenderService.drawRoundedVerLinearGradientRect(matrixStack, elementX, elementY, elementWidth, elementHeight, 8,
                    moduleBackgroundColor, ColorService.getColorWithAlpha(moduleBackgroundColor, 0.1f));
            RenderService.drawRoundedOutlineRect(matrixStack, elementX, elementY, elementWidth + 0.5f, elementHeight, 8, 1,
                    ColorService.getColorWithAlpha(uiBorderColor, 0.5f));

            // Рендерим текст модуля
            SFPD_REGULAR.drawText(matrixStack, module.getName(), elementX + MODULE_PADDING, elementY + MODULE_PADDING, textFirstColor, 20);

            // Обновляем высоту для колонки
            columnHeights[column] += elementHeight + PADDING;
        }

        ScissorService.disableScissor();
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {
        final float SCROLL_SPEED = 30f;
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

            if (column == COLUMNS - 1 || i == modules.size() - 1) {
                totalHeight = Math.max(totalHeight, columnHeights[column]);
            }
        }

        float maxScroll = Math.max(0, totalHeight - height + PADDING);
        targetScrollOffset = Math.max(0, Math.min(targetScrollOffset, maxScroll));
    }

    private float getModuleHeight(Module module) {
        // Определение высоты элемента на основе его контента.
        // Например, можно использовать фиксированное значение, или рассчитывать в зависимости от количества строк текста, изображения и т.д.
        return 129;
    }

    @Override
    public void tick() {}

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {}

    @Override
    public void charTyped(char codePoint, int modifiers) {}

}
