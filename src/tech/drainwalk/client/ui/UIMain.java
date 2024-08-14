package tech.drainwalk.client.ui;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.theme.Theme;
import tech.drainwalk.client.theme.ThemeSetting;
import tech.drainwalk.client.ui.components.Component;
import tech.drainwalk.client.ui.components.impl.LeftAreaComponent;
import tech.drainwalk.client.ui.components.impl.MainComponent;
import tech.drainwalk.client.ui.components.impl.ModulesComponent;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.services.render.GLService;

import java.util.ArrayList;
import java.util.List;

public class UIMain extends Screen implements IInstanceAccess, IManager<Component> {

    @Getter
    @Setter
    private static ThemeSetting<Theme> selectedTheme = new ThemeSetting<>("Themes", Theme.BLACK);
    @Getter
    @Setter
    protected Category selectedCategory = Category.COMBAT;

    private final List<Component> components = new ArrayList<>();
    private final float UI_WIDTH = 752;
    private final float UI_HEIGHT = 472;
    private final Animation animation = new Animation();
    private boolean direction;

    @Getter
    private final float ROUND = 8;

    public UIMain() {
        super(new StringTextComponent("DrainUI"));
        direction = true;

    }

    @Override
    public void init() {
        final float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(1);
        final float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(1);
        final float x = width / 2 - UI_WIDTH / 2;
        final float y = height / 2 - UI_HEIGHT / 2;

        register(new MainComponent(x, y, UI_WIDTH, UI_HEIGHT, this));
        register(new LeftAreaComponent(x, y, 64, UI_HEIGHT, this));
        register(new ModulesComponent(x + 64 + 1, y + 59 + 1, UI_WIDTH - (64 + 1), UI_HEIGHT - (59 + 1), this));
    }

    @Override
    public void render(@NonNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GLService.INSTANCE.rescale(1);
        animation.animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);

        matrixStack.push();
        final float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(1);
        final float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(1);
        final float x = width / 2 - UI_WIDTH / 2;
        final float y = height / 2 - UI_HEIGHT / 2;
        GLService.INSTANCE.scaleAnimation(matrixStack, x, y, UI_WIDTH, UI_HEIGHT, animation.getAnimationValue());

        final Vector2f fixedMouseCords = GLService.INSTANCE.normalizeCords(mouseX, mouseY, 1);
        components.forEach(component -> component.render(matrixStack, (int) fixedMouseCords.x, (int) fixedMouseCords.y, partialTicks));

        matrixStack.pop();

        GLService.INSTANCE.rescaleMC();
    }

    @Override
    public void tick() {
        animation.update(direction);
        components.forEach(Component::tick);
        if (animation.getValue() == 0 && animation.getPrevValue() == 0) {
            super.closeScreen();
            direction = true;
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        components.forEach(component -> component.charTyped(codePoint, modifiers));
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final Vector2f fixedMouseCords = GLService.INSTANCE.normalizeCords(mouseX, mouseY, 1);
        components.forEach(component -> component.mouseClicked(fixedMouseCords.x, fixedMouseCords.y, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        final Vector2f fixedMouseCords = GLService.INSTANCE.normalizeCords(mouseX, mouseY, 1);
        components.forEach(component -> component.mouseScrolled(fixedMouseCords.x, fixedMouseCords.y, delta));
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void closeScreen() {
        direction = false;
    }

    @Override
    public void register(Component component) {
        components.add(component);
    }

}
