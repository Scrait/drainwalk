package tech.drainwalk.client.modules.overlay;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.client.font.FontManager;
import com.darkmagician6.eventapi.EventTarget;

import java.util.ArrayList;
import java.util.Comparator;

public class ModuleList extends Module {
    public final FloatOption updateTime = new FloatOption("Update", 15f, 1f, 25f);
    private final BooleanOption glow = new BooleanOption("Glow", true);
    private final SelectOption colorType = new SelectOption("Color Type", 1,
            new SelectOptionValue("Astolfo"),
            new SelectOptionValue("Sky Rainbow"),
            new SelectOptionValue("Fade"));
    private final DraggableOption dragMl = new DraggableOption(this.getName(), new Vector2f(3, 40), 50, 50).addVisibleCondition(() -> mc.currentScreen instanceof ChatScreen && this.isEnabled());

    public ModuleList() {
        super("ModuleList", Category.OVERLAY);
        register(
                glow,
                colorType,
                updateTime,
                dragMl
        );
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        final ArrayList<Module> activeModules = dw.getApiMain().getModuleManager();
        activeModules.sort(Comparator.comparingDouble(s -> - FontManager.LIGHT_12.getStringWidth(s.getName())));
        for (Module module : activeModules) {
            module.getListAnimation().update(module.isEnabled());
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
//        final float xDrag = dragMl.getValue().x;
//        float y = dragMl.getValue().y;
//        final ArrayList<Module> activeModules = dw.getApiMain().getModuleManager();
//        activeModules.sort(Comparator.comparingDouble(s -> - FontManager.LIGHT_12.getStringWidth(s.getName())));
//        int yTotal = 0;
//        for (int i = 0; i < dw.getApiMain().getModuleManager().size(); ++i) {
//            yTotal += FontManager.LIGHT_12.getFontHeight() + 3;
//        }
//        GLUtils.INSTANCE.rescale(2);
//        for (Module function : activeModules) {
//            function.getListAnimation().animate(-0.5f, 1, 0.2f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
//            if (function.getListAnimation().getAnimationValue() > 0.01f) {
//                int color = 0;
//                if (colorType.getValueByIndex(0)) {
//                    color = ColorUtils.astolfo(y, yTotal, 0.5f, updateTime.getValue());
//                } else if (colorType.getValueByIndex(1)) {
//                    color = (int) ColorUtils.getSkyRainbow(updateTime.getValue(), (int) y);
//                } else if (colorType.getValueByIndex(2)) {
//                    color = ColorUtils.interpolateColorsBackAndForth(updateTime.getValue().intValue(), (int) y, ClientColor.panelMain, ClientColor.main);
//                }
//                RenderUtils.drawRect(xDrag + 7f * function.getListAnimation().getAnimationValue() - 3, 5 + y, 6 + FontManager.LIGHT_12.getStringWidth(function.getName()), (float)FontManager.LIGHT_12.getFontHeight() + 7, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f));
//                float finalY1 = y;
//                NORMAL_BLUR_RUNNABLES.add(() -> {
//                    GLUtils.INSTANCE.rescale(2);
//                    RenderUtils.drawRect(xDrag + 7f * function.getListAnimation().getAnimationValue() - 3, 5 + finalY1, 6 + FontManager.LIGHT_12.getStringWidth(function.getName()), (float)FontManager.LIGHT_12.getFontHeight() + 7, ClientColor.panelMain);
//                    GLUtils.INSTANCE.rescaleMC();
//                });
//
//                FontManager.LIGHT_12.drawString(event.getMatrixStack(), function.getName(), xDrag + 7f * function.getListAnimation().getAnimationValue(), 9 + y, color);
//                RenderUtils.drawRect(xDrag + 7f * function.getListAnimation().getAnimationValue() - 4f, 5 + y, 1.0f, (float)FontManager.LIGHT_12.getFontHeight() + 7, color);
//                function.getListAnimation().animate(-0.5f, 1, 0.3f, EasingList.NONE, mc.getTimer().renderPartialTicks);
//                GL11.glPushMatrix();
//                GL11.glTranslated(-2 - (1), 2, 0);
//
//                GL11.glPopMatrix();
//                y += (FontManager.LIGHT_12.getFontHeight() + 7) * function.getListAnimation().getAnimationValue();
//            }
//        }
//        if (glow.getValue()) {
//            y = dragMl.getValue().y - 2;
//            for (Module function : activeModules) {
//                function.getListAnimation().animate(-0.5f, 1, 0.2f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
//                if (function.getListAnimation().getAnimationValue() > 0.01f) {
//                    int color = 0;
//                    if (colorType.getValueByIndex(0)) {
//                        color = ColorUtils.astolfo(y, yTotal, 0.5f, updateTime.getValue());
//                    } else if (colorType.getValueByIndex(1)) {
//                        color = (int) ColorUtils.getSkyRainbow(updateTime.getValue(), (int) y);
//                    } else if (colorType.getValueByIndex(2)) {
//                        color = ColorUtils.interpolateColorsBackAndForth(updateTime.getValue().intValue(), (int) y, ClientColor.panelMain, ClientColor.main);
//                    }
//                    RenderUtils.drawGlow(xDrag + 7f * function.getListAnimation().getAnimationValue() - 3, 4 + y, 6 + FontManager.LIGHT_12.getStringWidth(function.getName()), (float) FontManager.LIGHT_12.getFontHeight() + 11, 15, color);
//                    function.getListAnimation().animate(-0.5f, 1, 0.3f, EasingList.NONE, mc.getTimer().renderPartialTicks);
//                    y += (FontManager.LIGHT_12.getFontHeight() + 7) * function.getListAnimation().getAnimationValue();
//                }
//            }
//        }
//        GLUtils.INSTANCE.rescaleMC();
    }
}
