package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.utils.movement.MoveUtils;
import tech.drainwalk.utils.time.Timer;

public class Watermark extends Module {

    public final MultiOption widgets = new MultiOption("Widgets", new MultiOptionValue("Coords", false), new MultiOptionValue("Speed", false), new MultiOptionValue("Timer", false), new MultiOptionValue("Potions", false));
    private final DraggableOption dragWt = new DraggableOption(this.getName(), new Vector2f(3, 3), 20, 32).addVisibleCondition(() -> mc.currentScreen instanceof ChatScreen && this.isEnabled());
    private final DraggableOption dragSt = new DraggableOption("SmartTimer", new Vector2f(75, 50), 64, 20).addVisibleCondition(() -> mc.currentScreen instanceof ChatScreen && this.isEnabled() && widgets.isSelected("Timer"));
    private final DraggableOption dragP = new DraggableOption("Potions", new Vector2f(130, 100), 20, 20).addVisibleCondition(() -> mc.currentScreen instanceof ChatScreen && this.isEnabled() && widgets.isSelected("Potions"));
    private final Timer timer = new Timer();
    private float inter = 0;
    private float widthAnim = 0;

    public Watermark() {
        super("Watermark", Category.OVERLAY);
        register(
                widgets,
                dragWt,
                dragSt,
                dragP
        );
    }

    @EventTarget
    public void onEntitySync(EventEntitySync eventPreMotion) {
        if (!dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).isEnabled()) {
            if (dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).smart.getValue()) {
                if (dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks >= 30) {
                    mc.getTimer().timerSpeed = 1;
                }

                if (!MoveUtils.isMovingSprint()) {
                    dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks -= 1;
                } else {
                    if (timer.delay(300, true)) {
                        dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks -= 0.1;
                    }
                }

                if (dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks <= 30) {
                    dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).active = false;
                }
            }
            dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks = MathHelper.clamp(dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks, 0, 100);
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (widgets.isSelected("Potions")) {
            for (EffectInstance effectInstance : mc.player.getActivePotionEffects()) {
                effectInstance.getPotionsAnimation().update(effectInstance.getDuration() > 10);
            }
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D eventRender2D) {
//        final String coords = mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + mc.player.getPosition().getZ();
//        final String bps = String.format("%.2f", Math.hypot(mc.player.getPosX() - mc.player.prevPosX, mc.player.getPosZ() - mc.player.prevPosZ) * (double) mc.getTimer().timerSpeed * 20.0D);
//
//        // Watermark
//        GLUtils.INSTANCE.rescale(2);
//        final float xWt = dragWt.getValue().x;
//        final float yWt = dragWt.getValue().y;
//        final String dateTime = DateTimeFormatter.ofPattern("hh:mm:ss a")
//                .format(LocalDateTime.now());
//        final Profile profile = dw.getProfile();
//        dragWt.setWidth(FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | |  |  " + Minecraft.getDebugFPS() + "fps" + (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost") + dateTime) + FontManager.ICONSWT_12.getStringWidth("sft") + 26f + 17f);
//        RenderUtils.drawRoundedRectWithOutline(xWt, yWt, 17, 17, 5, 2, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f), ColorUtils.rgbaFloat(1, 1, 1, 0.25f));
//        RenderUtils.drawRoundedRectWithOutline(xWt + 17.6f, yWt, dragWt.getWidth() - 17.6f, 17, 5, 2, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f), ColorUtils.rgbaFloat(1, 1, 1, 0.25f));
//        NORMAL_BLUR_RUNNABLES.add(() -> {
//            GLUtils.INSTANCE.rescale(2);
//            RenderUtils.drawRoundedRect(xWt, yWt, 17, 17, 5, ClientColor.panelMain);
//            RenderUtils.drawRoundedRect(xWt + 17.6f, yWt, dragWt.getWidth() - 17.6f, 17,5, ClientColor.panelMain);
//            GLUtils.INSTANCE.rescaleMC();
//        });
//        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
//            GLUtils.INSTANCE.rescale(2);
//            RenderUtils.drawRoundedRect(xWt, yWt, 17, 17, 8, ClientColor.panelMain);
//            RenderUtils.drawRoundedRect(xWt + 17.6f, yWt, dragWt.getWidth() - 17.6f, 17,8, ClientColor.panelMain);
//            GLUtils.INSTANCE.rescaleMC();
//        });
//        FontManager.ICONS_16.drawString("k", xWt + 5.7f, yWt + 7.3f, ClientColor.textMain);
//        FontManager.ICONSWT_12.drawString("u", xWt + 22.6f, yWt + 7.65f, ClientColor.main);
//        FontManager.LIGHT_13.drawString(profile.getUsername() + " [", xWt + 31f, yWt + 6.9f, ClientColor.textMain);
//        FontManager.LIGHT_13.drawString(profile.getRole(), FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [") + xWt + 31.65f, yWt + 7f, ClientColor.main);
//        FontManager.LIGHT_13.drawString("] " + "|", FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole()) + xWt + 32f, yWt + 6.9f, ClientColor.textMain);
//        FontManager.ICONSWT_12.drawString("s", FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | ") + xWt + 32f, yWt + 7.85f, ClientColor.main);
//        FontManager.LIGHT_13.drawString(mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost", FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | ") + FontManager.ICONSWT_12.getStringWidth("s") + xWt + 34f, yWt + 7, ClientColor.textMain);
//        FontManager.LIGHT_13.drawString(" |", FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | " + (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost")) + FontManager.ICONSWT_12.getStringWidth("s") + xWt + 34f, yWt + 6.9f, ClientColor.textMain);
//        FontManager.ICONSWT_12.drawString("f",  FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | |  " + (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost")) + FontManager.ICONSWT_12.getStringWidth("s") + xWt + 34f, yWt + 7.85f, ClientColor.main);
//        FontManager.LIGHT_13.drawString(Minecraft.getDebugFPS() + "fps", FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | |  " + (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost")) + FontManager.ICONSWT_12.getStringWidth("sf") + xWt + 36f, yWt + 7f, ClientColor.textMain);
//        FontManager.LIGHT_13.drawString(" |", FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | |  " + Minecraft.getDebugFPS() + "fps" + (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost")) + FontManager.ICONSWT_12.getStringWidth("sf") + xWt + 36f, yWt + 6.9f, ClientColor.textMain);
//        FontManager.ICONSWT_12.drawString("t", FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | |  |  " + Minecraft.getDebugFPS() + "fps" + (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost")) + FontManager.ICONSWT_12.getStringWidth("sf") + xWt + 36f, yWt + 7.85f, ClientColor.main);
//        FontManager.LIGHT_13.drawString(dateTime, FontManager.LIGHT_13.getStringWidth(profile.getUsername() + " [" + profile.getRole() + "] | |  |  " + Minecraft.getDebugFPS() + "fps" + (mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "localhost")) + FontManager.ICONSWT_12.getStringWidth("sft") + xWt + 38f, yWt + 7, ClientColor.textMain);
//
//        final boolean coordsEnabled = widgets.isSelected("Coords") ;
//        if (coordsEnabled) {
//            //RenderUtils.drawRoundedShadow(dwm.getX(), dwm.getY() + 15, FontManager.LIGHT_12.getStringWidth(coords + 10), 14, 8, 3, ClientColor.panelMain);
//            RenderUtils.drawRoundedRectWithOutline(xWt, yWt + 18, FontManager.LIGHT_12.getStringWidth(coords + 10 + "XYZ: "), 14, 5, 2, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f), ColorUtils.rgbaFloat(1, 1, 1, 0.25f));
//            NORMAL_BLUR_RUNNABLES.add(() -> {
//                GLUtils.INSTANCE.rescale(2);
//                RenderUtils.drawRoundedRect(xWt, yWt + 18, FontManager.LIGHT_12.getStringWidth(coords + 10 + "XYZ: "), 14, 5, ClientColor.panelMain);
//                GLUtils.INSTANCE.rescaleMC();
//            });
//            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
//                GLUtils.INSTANCE.rescale(2);
//                RenderUtils.drawRoundedRect(xWt, yWt + 18, FontManager.LIGHT_12.getStringWidth(coords + 10 + "XYZ: "), 14, 5, ClientColor.panelMain);
//                GLUtils.INSTANCE.rescaleMC();
//            });
//            FontManager.LIGHT_12.drawString("XYZ: ", xWt + 3.5f, yWt + 24, ClientColor.main);
//            FontManager.LIGHT_12.drawString(coords, xWt+ 3.5f + FontManager.LIGHT_12.getStringWidth("XYZ: "), yWt + 24, ClientColor.textMain);
//        }
//
//
//        if (widgets.isSelected("Speed")) {
//            //RenderUtils.drawRoundedShadow(dwm.getX() + FontManager.LIGHT_12.getStringWidth(coordsEnabled ? coords + 100 : ""), dwm.getY() + 15, FontManager.LIGHT_12.getStringWidth(bps + 30) - .5f, 14, 8, 3, ClientColor.panelMain);
//            RenderUtils.drawRoundedRectWithOutline(xWt + FontManager.LIGHT_12.getStringWidth(coordsEnabled ? coords + 100 +  "XYZ: " :  ""), yWt + 18, FontManager.LIGHT_12.getStringWidth(bps + "BPS: " + 30)- .5f, 14, 5, 2, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f), ColorUtils.rgbaFloat(1, 1, 1, 0.25f));
//            NORMAL_BLUR_RUNNABLES.add(() -> {
//                GLUtils.INSTANCE.rescale(2);
//                RenderUtils.drawRoundedRect(xWt + FontManager.LIGHT_12.getStringWidth(coordsEnabled ? coords + 100 +  "XYZ: " :  ""), yWt + 18, FontManager.LIGHT_12.getStringWidth(bps + "BPS: " + 30)- .5f, 14, 5, ClientColor.panelMain);
//                GLUtils.INSTANCE.rescaleMC();
//            });
//            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
//                GLUtils.INSTANCE.rescale(2);
//                RenderUtils.drawRoundedRect(xWt + FontManager.LIGHT_12.getStringWidth(coordsEnabled ? coords + 100 +  "XYZ: " :  ""), yWt + 18, FontManager.LIGHT_12.getStringWidth(bps + "BPS: " + 30)- .5f, 14, 5, ClientColor.panelMain);
//                GLUtils.INSTANCE.rescaleMC();
//            });
//            FontManager.LIGHT_12.drawString("BPS: ", xWt + 3.5f + FontManager.LIGHT_12.getStringWidth(coordsEnabled ? coords + 100 + "XYZ: " :  ""), yWt + 24, ClientColor.main);
//            FontManager.LIGHT_12.drawString(bps, xWt + 3.5f + FontManager.LIGHT_12.getStringWidth(coordsEnabled ? coords + 100 +  "XYZ: " + "BPS: " : "BPS: "), yWt + 24, ClientColor.textMain);
//        }
//
//
//        if (widgets.isSelected("Timer")) {
//            if (dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).smart.getValue()) {
//                final float x = dragSt.getValue().x;
//                final float y = dragSt.getValue().y;
//                final float pos = (100 - (dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks * 3.2f)) / 100;
////                if (mode.getValueByIndex(0)) {
////                    RenderUtils.drawRoundedShadow(x - 2, y, dst.getWidth() + 4, dst.getHeight(), 9, 4,
////                            ClientColor.panelMain);
////                    RenderUtils.drawRoundedRect(x - 2, y, dst.getWidth() + 4, dst.getHeight(), 4,
////                            ClientColor.panelMain);
////                    FontManager.SEMI_BOLD_18.drawCenteredString(MathematicUtils.round(100 - (Drainwalk.getInstance().getModuleManager().timer.ticks * 3.2f), 0.1) + "%", x + dst.getWidth() / 2, y + 3, ClientColor.textMain);
////                    RenderUtils.drawRoundedGradientRect(x + 2, y + dst.getHeight() - 7, (dst.getWidth() - 4) * pos, 3F, 2F,
////                            (int) fadeColor.getSkyRainbow(Drainwalk.getInstance().getModuleManager().moduleListModule.updateTime.getValue(), 0),
////                            (int) fadeColor.getSkyRainbow(Drainwalk.getInstance().getModuleManager().moduleListModule.updateTime.getValue(), 0),
////                            (int) fadeColor.getSkyRainbow(Drainwalk.getInstance().getModuleManager().moduleListModule.updateTime.getValue(), 190),
////                            (int) fadeColor.getSkyRainbow(Drainwalk.getInstance().getModuleManager().moduleListModule.updateTime.getValue(), 190)
////                    );
////                } else if (mode.getValueByIndex(1)) {
//                    //RenderUtils.drawRoundedShadow(x - 2, y - 1, dst.getWidth() + 4, dst.getHeight() + 2, 10, 5, ClientColor.textStay);
//                RenderUtils.drawRoundedRect(x - 2, y, 64, 20, 8,
//                        ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.35f));
//                RenderUtils.drawRoundedRect(x - 2, y, (64) * pos, 20, 8,
//                        ColorUtils.getColorWithAlpha(ClientColor.panelLines, 0.35f));
//                FontManager.LIGHT_14.drawCenteredStringWithShadow("TIMER " + MathematicUtils.round(100 - (dw.getApiMain().getModuleManager().findByClass(tech.drainwalk.client.modules.movement.Timer.class).ticks * 3.2f), 0.1) + "%", x + (float) 60 / 2, y + 8.5f, ClientColor.textStay);
//                RenderUtils.drawRoundedOutlineRect(x - 2, y + 0.5F, 64, 20, 8, 1f, ColorUtils.getColorWithAlpha(ClientColor.textStay, 0.5f));
//                NORMAL_BLUR_RUNNABLES.add(() -> {
//                    GLUtils.INSTANCE.rescale(2);
//                    RenderUtils.drawRoundedRect(x - 2, y, (64), 20, 5,
//                            ClientColor.panelLines);
//                    GLUtils.INSTANCE.rescaleMC();
//                });
//                NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
//                    GLUtils.INSTANCE.rescale(2);
//                    RenderUtils.drawRoundedRect(x - 2, y, (64), 20, 6,
//                            ClientColor.textStay);
//                    GLUtils.INSTANCE.rescaleMC();
//                    });
//                //}
//            }
//        }
//
//        if (widgets.isSelected("Potions")) {
//            final float x = dragP.getValue().x;
//            final float y = dragP.getValue().y;
//            float offset = 0;
//            for (EffectInstance effectInstance : mc.player.getActivePotionEffects()) {
//                if (effectInstance.getPotionsAnimation().getAnimationValue() > 0.1) {
//                    offset += 11f * effectInstance.getPotionsAnimation().getAnimationValue();
//                }
//            }
//            inter = offset + 2;
//            final EffectInstance potionMax = Collections.max(mc.player.getActivePotionEffects(), Comparator.comparing(s -> FontManager.LIGHT_14.getStringWidth(s.getPotion().getDisplayName().getString() + " " + I18n.format("enchantment.level." + (s.getAmplifier() + 1)) + "[" + EffectUtils.getPotionDurationString(s, 1) + "]")));
//            final float width = FontManager.LIGHT_14.getStringWidth(potionMax.getPotion().getDisplayName().getString() + " " + I18n.format("enchantment.level." +  (potionMax.getAmplifier() + 1)) + "[" + EffectUtils.getPotionDurationString(potionMax, 1) + "]") + 15;
//            widthAnim = AnimationUtility.animation(widthAnim, width, (float) (Timer.deltaTime()));
//            widthAnim = widthAnim < 50 ? 50 : widthAnim;
//            dragP.setWidth(widthAnim);
//            dragP.setHeight(inter + 17);
//
//            NORMAL_BLUR_RUNNABLES.add(() -> {
//                GLUtils.INSTANCE.rescale(2);
//                RenderUtils.drawRoundedRect(x, y, widthAnim, (17 + inter), 9, ClientColor.panelMain);
//                GLUtils.INSTANCE.rescaleMC();
//            });
//            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
//                GLUtils.INSTANCE.rescale(2);
//                RenderUtils.drawRoundedRect(x, y, widthAnim, (16 + inter), 9, ClientColor.panelMain);
//                GLUtils.INSTANCE.rescaleMC();
//            });
//
//            StencilUtils.initStencilToWrite();
//            RenderUtils.drawRect(x - 10, y - 10, widthAnim + 20, 27, -1);
//            StencilUtils.readStencilBuffer(1);
//            RenderUtils.drawRoundedRect(x - 1, y, widthAnim + 2, 25, 9, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f));
//            FontManager.REGULAR_18.drawCenteredString("potions", x + widthAnim / 2, (y + 13 / 2f), ClientColor.textMain);
//            StencilUtils.uninitStencilBuffer();
//            double i = 8.5;
//            StencilUtils.initStencilToWrite();
//            RenderUtils.drawRoundedRect(x - 1, y, widthAnim + 2, (17 + inter), 9, -1);
//            StencilUtils.readStencilBuffer(1);
//            RenderUtils.drawGradientRect(x - 1, y + 17.2f, widthAnim + 2, inter, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.17f), ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.17f), ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f), ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.7f));
//            StencilUtils.uninitStencilBuffer();
//            RenderUtils.drawGradientRect(x, y + 17f, widthAnim / 2, 0.7f, ColorUtils.rgba(242, 242, 242, 0), ColorUtils.rgb(242, 242, 242), ColorUtils.rgb(242, 242, 242), ColorUtils.rgba(242, 242, 242, 0));
//            RenderUtils.drawGradientRect(x + widthAnim / 2, y + 17f, widthAnim / 2, 0.7f, ColorUtils.rgb(242, 242, 242), ColorUtils.rgba(242, 242, 242, 0), ColorUtils.rgba(242, 242, 242, 0), ColorUtils.rgb(242, 242, 242));
//            for (EffectInstance effectInstance : mc.player.getActivePotionEffects()) {
//                effectInstance.getPotionsAnimation().animate(0, 1, 0.25f, EasingList.CIRC_OUT, mc.getTimer().renderPartialTicks);
//                if (effectInstance.getPotionsAnimation().getAnimationValue() > 0.1) {
//                    StencilUtils.initStencilToWrite();
//                    RenderUtils.drawRoundedRect(x, y + 17, widthAnim, (1000), 5, -1);
//                    StencilUtils.readStencilBuffer(1);
//                    FontManager.LIGHT_14.drawString(effectInstance.getPotion().getDisplayName().getString() + " " + I18n.format("enchantment.level." +  (effectInstance.getAmplifier() + 1)), x + 4, (float) ((y + 20 / 2f) + i) - 10 + 13 * effectInstance.getPotionsAnimation().getAnimationValue(), ColorUtils.rgbaFloat(242, 242, 247, 1 * effectInstance.getPotionsAnimation().getAnimationValue()));
//                    FontManager.LIGHT_14.drawString("[" + EffectUtils.getPotionDurationString(effectInstance, 1) + "]",
//                            (float) (x + widthAnim - 3.5) - FontManager.LIGHT_14.getStringWidth("[" + EffectUtils.getPotionDurationString(effectInstance, 1) + "]"), (float) ((y + 20 / 2f) + i) - 10 + 13 * effectInstance.getPotionsAnimation().getAnimationValue(), ColorUtils.rgbaFloat(242, 242, 247, 1 * effectInstance.getPotionsAnimation().getAnimationValue()));
//                    StencilUtils.uninitStencilBuffer();
//                    i += 11f * effectInstance.getPotionsAnimation().getAnimationValue();
//                }
//            }
//            RenderUtils.drawRoundedOutlineRect(x - 1.5f, y - 0.5f, widthAnim + 3, (18 + inter), 9, 2.2f, ColorUtils.rgbaFloat(1, 1, 1, 0.25f));
//        }
//        GLUtils.INSTANCE.rescaleMC();
    }
}