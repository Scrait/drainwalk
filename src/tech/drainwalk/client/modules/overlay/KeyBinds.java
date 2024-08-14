package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.services.animation.EasingList;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.DraggableOption;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.client.font.FontManager;
import tech.drainwalk.services.render.GLService;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.render.RenderUtils;
import tech.drainwalk.services.render.StencilService;
import tech.drainwalk.utils.time.Timer;

import java.util.Collections;
import java.util.Comparator;

public class KeyBinds extends Module {
    private float inter = 0;
    private float widthAnim = 0;
    private final DraggableOption dragKb = new DraggableOption(this.getName(), new Vector2f(75, 100), 20, 20).addVisibleCondition(() -> mc.currentScreen instanceof ChatScreen && this.isEnabled());

    public KeyBinds() {
        super("KeyBinds", Category.OVERLAY);
        register(dragKb);
    }

    @Override
    public void onEnable() {
        dw.getApiMain().getModuleManager().forEach(Module::resetAnimationKeybinds);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        for (Module module : dw.getApiMain().getModuleManager().stream().filter(Module::hasBind).toList()) {
            module.getKeyBindsAnimation().update(module.isEnabled());
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        final float x = dragKb.getValue().x;
        final float y = dragKb.getValue().y;
        float offset = 0;
        for (Module module : dw.getApiMain().getModuleManager().stream().filter(Module::hasBind).toList()) {
            if (module.getKeyBindsAnimation().getAnimationValue() > 0.1) {
                offset += 11f * module.getKeyBindsAnimation().getAnimationValue();
            }
        }
        inter = offset + 2;
        final Module moduleMax = Collections.max(dw.getApiMain().getModuleManager().stream().
                filter(Module::isEnabled).filter(Module::hasBind).toList(), Comparator.comparing(s -> FontManager.LIGHT_14.getStringWidth(s.getName() + InputMappings.getInputByCode(s.getCurrentKey(), 0).getTranslationKeySplit().toUpperCase())));
        final float width = FontManager.LIGHT_14.getStringWidth(moduleMax.getName() + "[" + InputMappings.getInputByCode(moduleMax.getCurrentKey(), 0).getTranslationKeySplit().toUpperCase() + "]") + 15;
        widthAnim = AnimationService.animation(widthAnim, width, (float) (Timer.deltaTime()));
        widthAnim = widthAnim < 50 ? 50 : widthAnim;
        dragKb.setWidth(widthAnim);
        dragKb.setHeight(inter + 17);
        GLService.INSTANCE.rescale(2);

        NORMAL_BLUR_RUNNABLES.add(() -> {
            GLService.INSTANCE.rescale(2);
            RenderUtils.drawRoundedRect(x, y, widthAnim, (17 + inter), 9, ClientColor.panelMain);
            GLService.INSTANCE.rescaleMC();
        });
        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            GLService.INSTANCE.rescale(2);
            RenderUtils.drawRoundedRect(x, y, widthAnim, (17 + inter), 9, ClientColor.panelMain);
            GLService.INSTANCE.rescaleMC();
        });

        StencilService.initStencilToWrite();
        RenderUtils.drawRect(x - 10, y - 10, widthAnim + 20, 27, -1);
        StencilService.readStencilBuffer(1);
        RenderUtils.drawRoundedRect(x - 1, y, widthAnim + 2, 25, 9, ColorService.getColorWithAlpha(ClientColor.panelMain, 0.7f));
        FontManager.REGULAR_18.drawCenteredString(event.getMatrixStack(), "keybinds", x + widthAnim / 2, (y + 13 / 2f), ClientColor.textMain);
        StencilService.uninitStencilBuffer();
        double i = 8.5;
        StencilService.initStencilToWrite();
        RenderUtils.drawRoundedRect(x - 1, y, widthAnim + 2, (17 + inter), 9, -1);
        StencilService.readStencilBuffer(1);
        RenderUtils.drawGradientRect(x - 1, y + 17.2f, widthAnim + 2, inter, ColorService.getColorWithAlpha(ClientColor.panelMain, 0.17f), ColorService.getColorWithAlpha(ClientColor.panelMain, 0.17f), ColorService.getColorWithAlpha(ClientColor.panelMain, 0.7f), ColorService.getColorWithAlpha(ClientColor.panelMain, 0.7f));
        StencilService.uninitStencilBuffer();
        RenderUtils.drawGradientRect(x, y + 17f, widthAnim / 2, 0.7f, ColorService.rgba(242, 242, 242, 0), ColorService.rgb(242, 242, 242), ColorService.rgb(242, 242, 242), ColorService.rgba(242, 242, 242, 0));
        RenderUtils.drawGradientRect(x + widthAnim / 2, y + 17f, widthAnim / 2, 0.7f, ColorService.rgb(242, 242, 242), ColorService.rgba(242, 242, 242, 0), ColorService.rgba(242, 242, 242, 0), ColorService.rgb(242, 242, 242));
        for (Module module : dw.getApiMain().getModuleManager().stream().filter(Module::hasBind).toList()) {
            module.getKeyBindsAnimation().animate(0, 1, 0.25f, EasingList.CIRC_OUT, mc.getTimer().renderPartialTicks);
            if (module.getKeyBindsAnimation().getAnimationValue() > 0.1) {
                StencilService.initStencilToWrite();
                RenderUtils.drawRoundedRect(x, y + 17, widthAnim, (1000), 5, -1);
                StencilService.readStencilBuffer(1);
                FontManager.LIGHT_14.drawString(event.getMatrixStack(), module.getName(), x + 4, (float) ((y + 20 / 2f) + i) - 10 + 13 * module.getKeyBindsAnimation().getAnimationValue(), ColorService.rgbaFloat(242, 242, 247, 1 * module.getKeyBindsAnimation().getAnimationValue()));
                FontManager.LIGHT_14.drawString(event.getMatrixStack(), "[" + InputMappings.getInputByCode(module.getCurrentKey(), 0).getTranslationKeySplit().toUpperCase() + "]",
                        (float) (x + widthAnim - 3.5) - FontManager.LIGHT_14.getStringWidth("[" + InputMappings.getInputByCode(module.getCurrentKey(), 0).getTranslationKeySplit().toUpperCase() + "]"), (float) ((y + 20 / 2f) + i) - 10 + 13 * module.getKeyBindsAnimation().getAnimationValue(), ColorService.rgbaFloat(242, 242, 247, 1 * module.getKeyBindsAnimation().getAnimationValue()));
                StencilService.uninitStencilBuffer();
                i += 11f * module.getKeyBindsAnimation().getAnimationValue();
            }
        }
        RenderUtils.drawRoundedOutlineRect(x - 1.5f, y - 0.5f, widthAnim + 3, (18 + inter), 9, 2.2f, ColorService.rgbaFloat(1, 1, 1, 0.25f));
        GLService.INSTANCE.rescaleMC();
    }
}
