package tech.drainwalk.client.ui.dwmenu;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender3D;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.theme.Theme;
import tech.drainwalk.client.theme.ThemeSetting;
import tech.drainwalk.client.ui.dwmenu.components.Component;
import tech.drainwalk.client.ui.dwmenu.components.impl.HeaderComponent;
import tech.drainwalk.client.ui.dwmenu.components.impl.LeftAreaComponent;
import tech.drainwalk.client.ui.dwmenu.components.impl.MainComponent;
import tech.drainwalk.client.ui.dwmenu.components.impl.ModulesComponent;
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
    private Category selectedCategory = Category.COMBAT;

    private final List<Component> components = new ArrayList<>();
    private final float UI_WIDTH = 752;
    private final float UI_HEIGHT = 472;
    @Getter
    private final Animation animation = new Animation();
    private boolean direction;
    private Vector3d onClosePlayerPos;
    private Vector2f rotation;

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

        components.clear();
        EventManager.unregister(this);

        register(new MainComponent(x, y, UI_WIDTH, UI_HEIGHT, this));
        register(new LeftAreaComponent(x, y, 64, UI_HEIGHT, this));
        register(new HeaderComponent(x + 64 + 1, y, UI_WIDTH - (64 + 1), 59, this));
        register(new ModulesComponent(x + 64 + 1, y + 59 + 1, UI_WIDTH - (64 + 1), UI_HEIGHT - (59 + 1), this));
    }

    @Override
    public void render(@NonNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GLService.INSTANCE.rescale(1);
        animation.animate(0, 1, 0.125f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);

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
        direction = true;
        animation.update(true);
        components.forEach(Component::tick);
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
        onClosePlayerPos = mc.gameRenderer.getActiveRenderInfo().getPos();
        Vector3d lookVector = new Vector3d(mc.gameRenderer.getActiveRenderInfo().getViewVector());
        double offsetDistance = 7.5f * mc.getMainWindow().getFramebufferHeight() / 1080.0f;
        onClosePlayerPos = onClosePlayerPos.add(lookVector.scale(offsetDistance));
        rotation = new Vector2f(mc.gameRenderer.getActiveRenderInfo().getYaw(), mc.gameRenderer.getActiveRenderInfo().getPitch());
        components.clear();
        final float x = UI_WIDTH / -2;
        final float y = UI_HEIGHT / -2;
        register(new MainComponent(x, y, UI_WIDTH, UI_HEIGHT, this));
        register(new LeftAreaComponent(x, y, 64, UI_HEIGHT, this));
        register(new HeaderComponent(x + 64 + 1, y, UI_WIDTH - (64 + 1), 59, this));
        register(new ModulesComponent(x + 64 + 1, y + 59 + 1, UI_WIDTH - (64 + 1), UI_HEIGHT - (59 + 1), this));
        EventManager.register(this);
        super.closeScreen();
    }

    // Custom after close render

    @EventTarget
    public void onRender3D(EventRender3D.PostAll event) {
        MatrixStack matrixStack = event.getMatrixStack();
        animation.animate(0, 1, 0.125f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);

        matrixStack.push();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        event.getMatrixStack().translate(onClosePlayerPos.x - mc.getRenderManager().info.getProjectedView().getX(),
                onClosePlayerPos.y - mc.getRenderManager().info.getProjectedView().getY(),
                onClosePlayerPos.z - mc.getRenderManager().info.getProjectedView().getZ());
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-rotation.x + 180));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-rotation.y + 180));

        float scale = 0.01f;
        matrixStack.scale(scale, scale, scale);
        float x = UI_WIDTH / -2;
        float y = UI_HEIGHT / -2;
        GLService.INSTANCE.scaleAnimation(matrixStack, x, y, UI_WIDTH, UI_HEIGHT, animation.getAnimationValue());

        final Vector2f fixedMouseCords = GLService.INSTANCE.normalizeCords(mc.mouseHelper.getMouseX(), mc.mouseHelper.getMouseY(), 1);
        components.forEach(component -> component.render(matrixStack, (int) fixedMouseCords.x, (int) fixedMouseCords.y, event.getPartialTicks()));

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        matrixStack.pop();
    }

    @EventTarget
    public void onUpdate(UpdateEvent ignoredEvent) {
        animation.update(false);
        components.forEach(Component::tick);
        if (animation.getValue() == 0 && animation.getPrevValue() == 0) {
            EventManager.unregister(this);
        }
    }

    @Override
    public void register(Component component) {
        components.add(component);
    }

}