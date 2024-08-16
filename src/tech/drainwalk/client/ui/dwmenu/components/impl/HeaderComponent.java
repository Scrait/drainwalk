package tech.drainwalk.client.ui.dwmenu.components.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.client.ui.dwmenu.UIMain;
import tech.drainwalk.client.ui.dwmenu.components.Component;

public class HeaderComponent extends Component {

    public HeaderComponent(float x, float y, float width, float height, UIMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        final float PADDING = 16f;
        SFPD_MEDIUM.drawText(matrixStack, parent.getSelectedCategory().getName(), x + PADDING, y + PADDING + 2, textFirstColor, 18);
        SFPD_MEDIUM.drawText(matrixStack, " (" + dw.getApiMain().getModuleManager().stream()
                .filter(module -> module.getCategory() == parent.getSelectedCategory())
                .toList().size() + ")", x + PADDING + SFPD_MEDIUM.getWidth(parent.getSelectedCategory().getName(), 18), y + PADDING + 5, textFirstColor, 14);
    }

    @Override
    public void tick() {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {

    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double delta) {

    }

}
