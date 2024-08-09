package tech.drainwalk.client.gui.modernui;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import tech.drainwalk.api.impl.interfaces.IManager;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.client.modules.overlay.Shaders;
import tech.drainwalk.client.theme.Theme;
import tech.drainwalk.client.theme.ThemeSetting;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.client.gui.modernui.component.Component;
import tech.drainwalk.client.gui.modernui.component.components.*;
import tech.drainwalk.services.render.GLService;

import java.util.ArrayList;
import java.util.List;

public class MenuMain extends Screen implements IManager<Component>, IInstanceAccess {

    @Getter
    private static ThemeSetting<Theme> selectedTheme = new ThemeSetting<>("Themes", Theme.BLACK);
    @Getter
    @Setter
    protected Category selectedCategory = Category.COMBAT;
    private final List<Component> components = new ArrayList<>();
    private final float GUI_WIDTH = 440;
    private final float GUI_HEIGHT = 270;
    @Getter
    private final Animation animation = new Animation();
    @Getter
    private final Animation randAnimation = new Animation();
    private boolean direction;


    public MenuMain() {
        super(NarratorChatListener.EMPTY);
    }

    @Override
    public void init() {
        //new Sound().playSound("metal_pipe.wav", 100);
        float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(2);
        float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(2);
        float x = width / 2 - GUI_WIDTH / 2;
        float y = height / 2 - GUI_HEIGHT / 2;
        register(new BackgroundComponent(0, 0, width, height, this));
        register(new MainComponent(x, y, GUI_WIDTH, GUI_HEIGHT, this));
        register(new CategoryComponent(x, y + 60, 115, GUI_HEIGHT - 60 - 30, this));
        register(new ProfileComponent(x, y, 115, 60, this));
        register(new ModuleComponent(x + 115, y, GUI_WIDTH - 115, GUI_HEIGHT, this));
        animation.setAnimationValue(0);
        animation.setPrevValue(0);
        animation.setValue(0);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GLService.INSTANCE.rescale(2);
        animation.animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
        matrixStack.push();
//        RenderSystem.pushMatrix();
        float width = mc.getMainWindow().getScaledWidthWithoutAutisticMojangIssue(2);
        float height = mc.getMainWindow().getScaledHeightWithoutAutisticMojangIssue(2);
        float x = width / 2 - GUI_WIDTH / 2;
        float y = height / 2 - GUI_HEIGHT / 2;
        GLService.INSTANCE.scaleAnimation(matrixStack, x, y, GUI_WIDTH, GUI_HEIGHT, animation.getAnimationValue());
//
//        if (!dw.getApiMain().getModuleManager().findByClass(Shaders.class).isEnabled()) {
//            renderShaders(partialTicks);
//        }
//        float mX = (float) (Minecraft.getInstance().mouseHelper.getMouseX() / 2f);
//        float mY = (float) (Minecraft.getInstance().mouseHelper.getMouseY() / 2f);

        randAnimation.animate(0, 1, 0.01f, EasingList.NONE, mc.getTimer().renderPartialTicks);

        if (randAnimation.getValue() == 1) {
            direction = false;
        } else if (randAnimation.getValue() < 0.4f) {
            direction = true;
        }
        components.forEach(component -> {
            //component.setX(mX);
            //component.setY(mY);
            component.render(matrixStack, mouseX, mouseY, partialTicks);
        });
        matrixStack.pop();

//        GLFW.glfwSetInputMode(minecraft.getMainWindow().getHandle(), 208897, 212995);
//        RenderService.renderImage(matrixStack, new ResourceLocation("drainwalk/images/cursor.png"), mouseX, mouseY, 11, 11, -1);
//
//        RenderSystem.popMatrix();
        GLService.INSTANCE.rescaleMC();
//        matrixStack.clear();
//        renderService.clearMatrixStack();
    }

    @Override
    public void tick() {
        animation.update(true);
        randAnimation.update(direction);
        components.forEach(Component::tick);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        components.forEach(component -> component.charTyped(codePoint, modifiers));
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void register(Component component) {
        components.add(component);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        components.forEach(component -> component.mouseScrolled(mouseX, mouseY, delta));
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void closeScreen() {
        EventManager.register(this);
        super.closeScreen();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        //        GLUtils.INSTANCE.rescale(2);
        animation.update(false);
        animation.animate(0, 1, 0.08f, EasingList.BACK_OUT, Minecraft.getInstance().getTimer().renderPartialTicks);
        event.getMatrixStack().push();
//        RenderSystem.pushMatrix();
        float x = width / 2 - GUI_WIDTH / 2;
        float y = height / 2 - GUI_HEIGHT / 2;
        GLService.INSTANCE.scaleAnimation(event.getMatrixStack(), x, y, GUI_WIDTH, GUI_HEIGHT, animation.getAnimationValue());
//
        if (!dw.getApiMain().getModuleManager().findByClass(Shaders.class).isEnabled()) {
            renderShaders(event.getPartialTicks());
        }
//        float mX = (float) (Minecraft.getInstance().mouseHelper.getMouseX() / 2f);
//        float mY = (float) (Minecraft.getInstance().mouseHelper.getMouseY() / 2f);

        randAnimation.animate(0, 1, 0.005f, EasingList.NONE, Minecraft.getInstance().getTimer().renderPartialTicks);

        if (randAnimation.getValue() == 1) {
            direction = false;
        } else if (randAnimation.getValue() < 0.4f) {
            direction = true;
        }
        randAnimation.update(direction);
        components.forEach(component -> {
            //component.setX(mX);
            //component.setY(mY);
            component.render(event.getMatrixStack(), (int) Minecraft.getInstance().mouseHelper.getMouseX(), (int) Minecraft.getInstance().mouseHelper.getMouseY(), event.getPartialTicks());
        });
        event.getMatrixStack().pop();

//        GLFW.glfwSetInputMode(minecraft.getMainWindow().getHandle(), 208897, 212995);
//        RenderService.renderImage(matrixStack, new ResourceLocation("drainwalk/images/cursor.png"), mouseX, mouseY, 11, 11, -1);
//
//        RenderSystem.popMatrix();
//        GLUtils.INSTANCE.rescaleMC();
//        matrixStack.clear();
//        renderService.clearMatrixStack();
        if (animation.getValue() == 0 && animation.getPrevValue() == 0) {
            EventManager.unregister(this);
        }

//        }
    }

}
