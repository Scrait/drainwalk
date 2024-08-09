package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector2f;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.services.animation.Animation;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.option.options.DraggableOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TargetHUD extends Module {
    private final Animation animation = new Animation();
    private static LivingEntity curTarget = null;
    private double healthBarWidth;
    private final SelectOption mode = new SelectOption("Mode", 0,
            new SelectOptionValue("JebrikBest"));
    private final DraggableOption dragTh = new DraggableOption(this.getName(), new Vector2f(120, 30), 117, 37).addVisibleCondition(() -> mc.currentScreen instanceof ChatScreen && this.isEnabled());
    private boolean direction = true;

    public TargetHUD() {
        super("TargetHUD", Category.OVERLAY);
        register(
                mode,
                dragTh
        );
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        animation.update(direction);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
//        final float x = dragTh.getValue().x;
//        final float y = dragTh.getValue().y;
//        GLUtils.INSTANCE.rescale(2);
//        animation.animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
//        if (dw.getApiMain().getModuleManager().findByClass(Aura.class).getTarget() == null) {
//            if (mc.player != null && mc.currentScreen instanceof ChatScreen) {
//                curTarget = mc.player;
//                direction = true;
//            } else {
//                direction = false;
//            }
//        } else {
//            curTarget = dw.getApiMain().getModuleManager().findByClass(Aura.class).getTarget();
//            direction = true;
//        }
////        if (mode.getValueByIndex(0)) {
////            if (curTarget != null) {
////                try {
////                    dth.setWidth(120);
////                    dth.setHeight(25);
////                    GlStateManager.pushMatrix();
////                    //ChatUtils.addChatMessage(String.valueOf(animation.getAnimationValue()));
////                    AnimationUtility.scaleAnimation(x, y, 120F, 25F, animation.getAnimationValue());
////                    double healthWid = (curTarget.getHealth() / curTarget.getMaxHealth() * 360);
////                    healthWid = MathHelper.clamp(healthWid, 0.0D, 360);
////                    healthBarWidth = AnimationUtility.animation((float) healthBarWidth, (float) healthWid, (float) (160 * Timer.deltaTime()));
////                    String health = "" + MathematicUtils.round(curTarget.getHealth(), 1);
////                    health = health.substring(0, health.length() - 2);
////                    RenderUtils.drawRoundedShadow(x, y, 120, 35 - 8, 8, 3, ClientColor.panelMain);
////                    RenderUtils.drawRoundedRect(x, y + 1, 120, 25, 3, ClientColor.panelMain);
////                    FontManager.SEMI_BOLD_12.drawString("Name: ", x + 28, y + 8, ClientColor.textMain);
////                    FontManager.SEMI_BOLD_12.drawSubstringDefault(curTarget.getName().getString(), x + 28 + FontManager.SEMI_BOLD_12.getStringWidth("Name: "), y + 8, ClientColor.main, 38);
////                    FontManager.SEMI_BOLD_12.drawString("Item:", x + 28, y + 17, ClientColor.textMain);
////                    FontManager.SEMI_BOLD_12.drawSubstringDefault(curTarget.getHeldItemOffhand().getDisplayName().getString().toLowerCase() + " x" + curTarget.getHeldItemOffhand().getMaxStackSize(), x + 28 + FontManager.SEMI_BOLD_12.getStringWidth("Item: "), y + 17, ClientColor.main, 41);
////                    RenderUtils.drawCircle(x + 106, (y + 13.5f), (float) 0, (float) 360, 8F, ColorUtils.rgba(17, 17, 17, 255), (int) 3f);
////                    RenderUtils.drawCircle(x + 106, (y + 13.5f), (float) 0, (float) healthBarWidth, 8F,ClientColor.main, (int) 3f);
////                    FontManager.SEMI_BOLD_12.drawCenteredString(health, x + 106, y + 12.5f, ClientColor.textMain);
////                    healthBarWidth = AnimationUtility.getAnimationState((float) healthBarWidth, (float) healthWid, (float) (10 * Timer.deltaTime()));
////                    RenderUtils.drawFace(255, (x + 5), (y + 5f), 8.0f, 8.0f, 8, 8, 18, 18, 64.0f, 64.0f, (AbstractClientPlayerEntity) curTarget);
////                } finally{
////                    GlStateManager.popMatrix();
////                }
////            }
////        } else if (mode.getValueByIndex(1)) {
////            if (curTarget != null) {
////                try {
////                    dth.setWidth((int) 140.5f);
////                    dth.setHeight(42);
////                    GlStateManager.pushMatrix();
////                    //ChatUtils.addChatMessage(String.valueOf(animation.getAnimationValue()));
////                    AnimationUtility.scaleAnimation(x, y + 1, 140.5f, 42, animation.getAnimationValue());
////                    final double healthWid = MathHelper.clamp(curTarget.getHealth() / curTarget.getMaxHealth() * 70, 0.0D, 70);
////                    final double healthPercent = MathematicUtils.round(MathHelper.clamp((curTarget.getHealth() / curTarget.getMaxHealth() * 100), 0.0D, 100), 0.1);
////                    healthBarWidth = AnimationUtility.animation((float) healthBarWidth, (float) healthWid, (float) (10 * Timer.deltaTime()));
////                    RenderUtils.drawRoundedShadow(x - 0.5f, y - 0.5f, 141.5f, 45, 9, 8, ClientColor.main);
////                    RenderUtils.drawRoundedRect(x, y + 1, 140.5f, 42, 8, ClientColor.panelMain);
////                    FontManager.SEMI_BOLD_18.drawSubstringDefault(curTarget.getName().getString(), x + 47, y + 6, ClientColor.main, 57);
////                    RenderUtils.drawRoundedGradientRect(x + 46, y + 22, (float) healthBarWidth, 17, 9, ClientColor.main, ClientColor.main, ColorUtils.rgb(0, 0, 0), ColorUtils.rgb(0, 0, 0));
////                    RenderUtils.drawRoundedOutlineRect(x + 46, y + 22, (float) healthBarWidth, 17, 9, 1.5f, ClientColor.panelLines);
////                    FontManager.SEMI_BOLD_10.drawString(healthPercent + "%", x + 47, y + 18, ClientColor.main);
////                    /*RenderUtils.drawRoundedRect(x + 118, y + 3.5f, 19, 28, 1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 118, y + 3.5f, 19, 28, 1, 1.5f, ClientColor.panelLines);
////                    RenderUtils.drawRoundedRect(x + 141, y + 3.5f, 8, 8,1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 141, y + 3.5f, 8, 8, 1, 1.5f, ClientColor.panelLines);
////                    RenderUtils.drawRoundedRect(x + 141, y + 13.5f, 8, 8,1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 141, y + 13.5f, 8, 8, 1, 1.5f, ClientColor.panelLines);
////                    RenderUtils.drawRoundedRect(x + 141, y + 23.5f, 8, 8,1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 141, y + 23.5f, 8, 8, 1, 1.5f, ClientColor.panelLines);
////                    RenderUtils.drawRoundedRect(x + 141, y + 33.5f, 8, 8,1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 141, y + 33.5f, 8, 8, 1, 1.5f, ClientColor.panelLines);
////                    RenderUtils.drawRoundedRect(x + 118, y + 33.5f, 8, 8,1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 118, y + 33.5f, 8, 8, 1, 1.5f, ClientColor.panelLines);
////                    RenderUtils.drawRoundedRect(x + 129, y + 33.5f, 8, 8,1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 129, y + 33.5f, 8, 8, 1, 1.5f, ClientColor.panelLines);*/
////                    RenderUtils.drawRoundedRect(x + 118, y + 2f, 20, 20, 1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 118, y + 2f, 20, 20, 1, 1.5f, ClientColor.panelLines);
////                    RenderUtils.drawRoundedRect(x + 118, y + 22f, 20, 20, 1, ClientColor.main);
////                    RenderUtils.drawRoundedOutlineRect(x + 118, y + 22f, 20, 20, 1, 1.5f, ClientColor.panelLines);
////                    if (curTarget instanceof PlayerEntity) {
////                        drawItemTargetHUD((PlayerEntity) curTarget, (int) (x + 120), (int) (y + 4));
////                    }
////                    if (curTarget instanceof PlayerEntity) {
////                        StencilUtils.initStencilToWrite();
////                        RenderUtils.drawRoundedRect(x, y + 1, 140.5f, 42, 8, ClientColor.panelMain);
////                        StencilUtils.readStencilBuffer(1);
////                        RenderUtils.drawFace(255, x, (y + 1), 8.0f, 8.0f, 8, 8, 42, 42, 64.0f, 64.0f, (AbstractClientPlayerEntity) curTarget);
////                        StencilUtils.uninitStencilBuffer();
////                    }
////                    healthBarWidth = AnimationUtility.getAnimationState((float) healthBarWidth, (float) healthWid, (float) (10 * Timer.deltaTime()));
////                } finally{
////                    GlStateManager.popMatrix();
////                }
////            }
////        } else if (mode.getValueByIndex(2)) {
////            if (curTarget != null) {
////                try {
////                    dth.setWidth(132);
////                    dth.setHeight(46);
////                    GlStateManager.pushMatrix();
////                    AnimationUtility.scaleAnimation(x, y + 1, 132, 46, animation.getAnimationValue());
////                    final double healthWid = MathHelper.clamp(curTarget.getHealth() / curTarget.getMaxHealth() * 360, 0.0D, 360);
////                    final double healthPercent = MathematicUtils.round(MathHelper.clamp((curTarget.getHealth() / curTarget.getMaxHealth() * 100), 0.0D, 100), 0.1);
////                    healthBarWidth = AnimationUtility.animation((float) healthBarWidth, (float) healthWid, (float) (10 * Timer.deltaTime()));
////                    RenderUtils.drawRoundedShadow(x - 0.5f, y - 0.5f, 133, 49, 9, 10, ClientColor.textStay);
////                    RenderUtils.drawRoundedRect(x, y + 1, 132, 46, 10, ClientColor.panelMain);
////                    RenderUtils.drawRoundedOutlineRect(x - 1, y, 134, 48, 10, 1f, ClientColor.textStay);
////                    RenderUtils.drawRoundedShadow(x + 46, y + 8f, FontManager.SEMI_BOLD_18.getStringWidth(curTarget.getName().getString()) > 64 ? 63 : FontManager.SEMI_BOLD_18.getStringWidth(curTarget.getName().getString()), FontManager.SEMI_BOLD_18.getFontHeight(), 11, 0, ClientColor.textStay);
////                    FontManager.SEMI_BOLD_18.drawSubstringDefault(curTarget.getName().getString(), x + 46, y + 8f, ClientColor.textStay, 63);
////                    for (NetworkPlayerInfo networkPlayerInfo : mc.player.connection.getPlayerInfoMap()) {
////                        try {
////                            if (mc.world.getPlayerByUuid(networkPlayerInfo.getGameProfile().getId()) == curTarget && curTarget != null) {
////                                RenderUtils.drawRoundedShadow(x + 46, y + 19f, FontManager.REGULAR_16.getStringWidth("Ping: " + networkPlayerInfo.getResponseTime()), FontManager.REGULAR_16.getFontHeight(), 11, 0, ClientColor.textStay);
////                                FontManager.REGULAR_16.drawString("Ping: " + networkPlayerInfo.getResponseTime(), x + 46, y + 19f, ClientColor.textStay);
////                            }
////                        } catch (Exception exception) {
////                        }
////                    }
////                    //final int hitsLeft = (int) (curTarget.getHealth() / mc.player.getHeldItemMainhand().getItemDamage());
////                    RenderUtils.drawRoundedShadow(x + 46, y + 27f, FontManager.REGULAR_16.getStringWidth("Hits: " + curTarget.hits), FontManager.REGULAR_16.getFontHeight(), 11, 0, ClientColor.textStay);
////                    FontManager.REGULAR_16.drawString("Hits: " + curTarget.hits, x + 46, y + 27f, ClientColor.textStay);
////                    if (curTarget instanceof PlayerEntity) {
////                        drawItemTargetHUD((PlayerEntity) curTarget, !curTarget.getHeldItemMainhand().isEmpty() ? x + 17.5f : !curTarget.getHeldItemOffhand().isEmpty() ? x + 6.5f : x - 6.5f, y + 23, !curTarget.getHeldItemMainhand().isEmpty() ? x + 17.5f : !curTarget.getHeldItemOffhand().isEmpty() ? x + 6.5f : x - 6.5f);
////                    }
////                    FontManager.SEMI_BOLD_12.drawCenteredString(String.valueOf(healthPercent), x + 23.5f, y + 42, ClientColor.textStay);
////                    if (curTarget instanceof PlayerEntity) {
////                        StencilUtils.initStencilToWrite();
////                        drawFCircle(x + 23.5f, (y + 21.5f), 0, 360, 17, true);
////                        StencilUtils.readStencilBuffer(1);
////                        RenderUtils.drawFace(255, x + 6, (y + 4), 8.0f, 8.0f, 8, 8, 35, 35, 64.0f, 64.0f, (AbstractClientPlayerEntity) curTarget);
////                        StencilUtils.uninitStencilBuffer();
////                    }
////                    RenderUtils.drawCircle(x + 23.5f, (y + 21.5f), (float) 0, (float) 360, 17F, ClientColor.panelMain, 3);
////                    RenderUtils.drawCircle(x + 23.5f, (y + 21.5f), (float) -90, (float) -((float) 90 + (healthBarWidth)), 17F, ClientColor.textStay, (int) 2f);
////                } finally{
////                    GlStateManager.popMatrix();
////                }
////            }
//        if (mode.getValueByIndex(0)) {
//            if (curTarget != null) {
//                try {
//                    GlStateManager.pushMatrix();
//                    AnimationService.scaleAnimation(x, y + 1, 117, 37, animation.getAnimationValue());
//                    final double healthWid = MathHelper.clamp(curTarget.getHealth() / curTarget.getMaxHealth() * 110, 0.0D, 110);
//                    healthBarWidth = AnimationService.animation((float) healthBarWidth, (float) healthWid, (float) (10 * Timer.deltaTime()));
//                    RenderUtils.drawRoundedShadow(x, y + 1, 117, 37, 2, 5, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.5f));
//                    StencilUtils.initStencilToWrite();
//                    RenderUtils.drawRect(x, y, 117, 7f, -1);
//                    StencilUtils.readStencilBuffer(1);
//                    RenderUtils.drawRoundedRect(x, y + 1, 117, 37, 5, ColorUtils.getColorWithAlpha(ClientColor.panelMain, 0.6f));
//                    NORMAL_BLUR_RUNNABLES.add(() -> {
//                        GLUtils.INSTANCE.rescale(2);
//                        GlStateManager.pushMatrix();
//                        AnimationService.scaleAnimation(x, y + 1, 117, 37, animation.getAnimationValue());
//                        RenderUtils.drawRoundedRect(x, y + 1, 117, 37, 5, ClientColor.panelMain);
//                        GlStateManager.popMatrix();
//                        GLUtils.INSTANCE.rescaleMC();
//                    });
//                    RenderUtils.drawRoundedRect(x + 3.5f, y + 3.5f, 110, 1.5f, 1, ColorUtils.rgba(41, 207, 66, 255 / 2));
//                    RenderUtils.drawRoundedRect(x + 3.5f, y + 3.5f, (float) healthBarWidth, 1.5f, 1, ColorUtils.rgb(41, 207, 66));
//                    StencilUtils.uninitStencilBuffer();
//                    for (NetworkPlayerInfo networkPlayerInfo : mc.player.connection.getPlayerInfoMap()) {
//                        try {
//                            if (mc.world.getPlayerByUuid(networkPlayerInfo.getGameProfile().getId()) == curTarget && curTarget != null) {
//                                FontManager.MEDIUM_16.drawStringWithShadow(event.getMatrixStack(), curTarget.getName().getString(), x + 32f, y + 13f, ClientColor.textMain); String health = "" + MathematicUtils.round(curTarget.getHealth(), 1);
//                                health = health.substring(0, health.length() - 2);
//                                StencilUtils.initStencilToWrite();
//                                RenderUtils.drawRect(x, y + 1, 115, 37, -1);
//                                StencilUtils.readStencilBuffer(1);
//                                FontManager.LIGHT_14.drawStringWithShadow(event.getMatrixStack(), " | HP:" + health + " | " + networkPlayerInfo.getResponseTime() + "ms", x + 32f + FontManager.MEDIUM_16.getStringWidth(curTarget.getName().getString()), y + 13.5f, ClientColor.textMain);
//                                StencilUtils.uninitStencilBuffer();
//                            }
//                        } catch (Exception exception) {
//                        }
//                    }
//                    if (curTarget instanceof PlayerEntity) {
//                        drawItemTargetHUD((PlayerEntity) curTarget, x + 2, y + 13, x + 2);
//                    }
//                    if (curTarget instanceof PlayerEntity) {
//                        StencilUtils.initStencilToWrite();
//                        RenderUtils.drawRoundedRect(x + 3, (y + 10), 25, 25, 6, -1);
//                        StencilUtils.readStencilBuffer(1);
//                        //RenderUtils.drawFace(255, x + 3, (y + 11), 8.0f, 8.0f, 8, 8, 25, 25, 64.0f, 64.0f, (AbstractClientPlayerEntity) curTarget);
//                        RenderUtils.drawRoundedTextureHead( ((AbstractClientPlayerEntity) curTarget).getLocationSkin(), x + 3, (y + 10),  25, 25, 6, 1f);
//                        StencilUtils.uninitStencilBuffer();
//                    }
//                } finally{
//                    GlStateManager.popMatrix();
//                }
//            }
//        }
//        GLUtils.INSTANCE.rescaleMC();
    }

    private static void drawItemTargetHUD(PlayerEntity player, float posX, float posY) {
        List<ItemStack> list = new ArrayList<>(Arrays.asList(player.getHeldItemMainhand(), player.getHeldItemOffhand()));
        for (ItemStack itemStack : list) {
            RenderHelper.enableStandardItemLighting();
            renderItem(itemStack, (int) posX, (int) (posY));
            posY += 20f;
        }
    }

    public static void drawItemTargetHUD(PlayerEntity player, float posX, float posY, float x2) {
        List<ItemStack> list = new ArrayList<>(Arrays.asList(player.getHeldItemMainhand(), player.getHeldItemOffhand()));
        for (int i = 1; i < 5; ++i) {
            ItemStack getEquipmentInSlot = getEquipmentInSlot(i, player);
            list.add(getEquipmentInSlot);
        }
        for (ItemStack itemStack : list) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(posX, posY, 1.0f);
            GlStateManager.scalef(0.65f, 0.65f, 0.65f);
            GlStateManager.translatef(-posX - 7, -posY + 10, 1.0f);
            renderItem(itemStack, (int) x2 + 50, (int) (posY + 6));
            GlStateManager.popMatrix();
            x2 += 18.0f;
        }
    }

    private static ItemStack getEquipmentInSlot(int item, PlayerEntity player) {
        return item == 0 ? player.inventory.getCurrentItem() : player.inventory.armorInventory.get(item - 1);
    }

    private static void renderItem(ItemStack itemStack, float x, float y) {
        GlStateManager.enableTexture();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepthTest();
        GlStateManager.disableAlphaTest();

        mc.getItemRenderer().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();

        mc.getItemRenderer().renderItemAndEffectIntoGUI(itemStack, (int) x, (int) y);
        mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, itemStack, (int) x, (int) y);
        RenderHelper.disableStandardItemLighting();
        mc.getItemRenderer().zLevel = 0.0f;
        GlStateManager.enableAlphaTest();
    }

    private static void drawFCircle(float x, float y, float start, float end, float radius, boolean filled) {
        float sin;
        float cos;
        float i;

        float endOffset;
        if (start > end) {
            endOffset = end;
            end = start;
            start = endOffset;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) (Math.cos(i * Math.PI / 180) * radius * 1);
            sin = (float) (Math.sin(i * Math.PI / 180) * radius * 1);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);
        for (i = end; i >= start; i -= 4) {
            cos = (float) Math.cos(i * Math.PI / 180) * radius;
            sin = (float) Math.sin(i * Math.PI / 180) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }

}
