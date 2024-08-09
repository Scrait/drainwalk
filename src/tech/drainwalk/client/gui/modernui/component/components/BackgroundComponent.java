package tech.drainwalk.client.gui.modernui.component.components;

import com.mojang.blaze3d.matrix.MatrixStack;

import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.client.service.services.impl.render.GifPlayer;
import tech.drainwalk.services.render.RenderService;
import tech.drainwalk.client.gui.modernui.MenuMain;
import tech.drainwalk.client.gui.modernui.component.Component;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.shader.Shaders;

public class BackgroundComponent extends Component {

    private final Animation upAnimation = new Animation();
    private final Animation downAnimation = new Animation();
    private final GifPlayer gifPlayer = new GifPlayer("snow", 49, 60);

    public BackgroundComponent(float x, float y, float width, float height, MenuMain parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        upAnimation.animate(0, 1, 0.0012f, EasingList.BACK_OUT, partialTicks);
        upAnimation.update(true);
        Shaders.POST_BLOOM_SHADER.setRadius(parent.getAnimation().getAnimationValue() < 1 ? 0 : 25 * upAnimation.getAnimationValue());
        RenderService.drawRect(matrixStack, -1000, -1000, width + 2000, height + 2000, ColorService.rgbaFloat(0, 0, 0, parent.getAnimation().getAnimationValue() - 0.4f));
        NORMAL_BLUR_RUNNABLES.add(() -> RenderService.drawRect(matrixStack, -1000, -1000, width + 2000, height + 2000, ColorService.rgbaFloat(0, 0, 0, parent.getAnimation().getAnimationValue() - 0.2f)));
        //gifPlayer.play(matrixStack, 0, 0, width, height);
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
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

}
