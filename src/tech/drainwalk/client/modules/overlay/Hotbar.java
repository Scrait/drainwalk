package tech.drainwalk.client.modules.overlay;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.settings.AttackIndicatorStatus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.optifine.CustomItems;
import tech.drainwalk.services.animation.AnimationService;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.api.impl.events.modules.HotbarEvent;
import tech.drainwalk.api.impl.events.render.EventRender2D;
import tech.drainwalk.services.render.ColorService;
import tech.drainwalk.utils.render.RenderUtils;
import tech.drainwalk.utils.time.Timer;

public class Hotbar extends Module {

    private float anim;

    public Hotbar() {
        super("Hotbar", Category.OVERLAY);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        PlayerEntity playerentity = !(this.mc.getRenderViewEntity() instanceof PlayerEntity) ? null : (PlayerEntity)this.mc.getRenderViewEntity();;

        if (playerentity != null)
        {
            ItemStack itemstack = playerentity.getHeldItemOffhand();
            HandSide handside = playerentity.getPrimaryHand().opposite();
            int i = event.getMainWindow().getScaledWidth() / 2;
            RenderUtils.drawRoundedShadow(i - 91, event.getMainWindow().getScaledHeight() - 25, 182, 22, 2, 5, ColorService.getColorWithAlpha(ClientColor.panelMain, 0.7f));
            anim = AnimationService.animation(anim, (float) (i - 91 + playerentity.inventory.currentItem * 20), (float) (Timer.deltaTime() * 360));
            RenderUtils.drawRoundedRect(anim, event.getMainWindow().getScaledHeight() - 24, 22, 20, 5, ColorService.getColorWithAlpha(ClientColor.panelMain, 0.3f));
            NORMAL_BLUR_RUNNABLES.add(() -> {
                RenderUtils.drawRoundedRect(i - 91, event.getMainWindow().getScaledHeight() - 25, 182, 22, 5, ClientColor.panelMain);
            });

            RenderSystem.enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            CustomItems.setRenderOffHand(false);

            for (int i1 = 0; i1 < 9; ++i1)
            {
                int j1 = i - 90 + i1 * 20 + 2;
                int k1 = event.getMainWindow().getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j1, k1, mc.getRenderPartialTicks(), playerentity, playerentity.inventory.mainInventory.get(i1));
            }

            if (!itemstack.isEmpty())
            {
                CustomItems.setRenderOffHand(true);
                int i2 = event.getMainWindow().getScaledHeight() - 16 - 3;

                if (handside == HandSide.LEFT)
                {
                    this.renderHotbarItem(i - 91 - 26, i2, mc.getRenderPartialTicks(), playerentity, itemstack);
                }
                else
                {
                    this.renderHotbarItem(i + 91 + 10, i2, mc.getRenderPartialTicks(), playerentity, itemstack);
                }

                CustomItems.setRenderOffHand(false);
            }

            if (this.mc.gameSettings.attackIndicator == AttackIndicatorStatus.HOTBAR)
            {
                float f = this.mc.player.getCooledAttackStrength(0.0F);

                if (f < 1.0F)
                {
                    int j2 = event.getMainWindow().getScaledHeight() - 20;
                    int k2 = i + 91 + 6;

                    if (handside == HandSide.RIGHT)
                    {
                        k2 = i - 91 - 22;
                    }

                    this.mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
                    int l1 = (int)(f * 19.0F);
//                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//                    this.blit(matrixStack, k2, j2, 0, 94, 18, 18);
//                    this.blit(matrixStack, k2, j2 + 18 - l1, 18, 112 - l1, 18, l1);
                }
            }

            RenderSystem.disableRescaleNormal();
            RenderSystem.disableBlend();
        }
    }

    @EventTarget
    public void onHotbar(HotbarEvent event) {
        event.setCancelled(true);
    }

    private void renderHotbarItem(int x, int y, float partialTicks, PlayerEntity player, ItemStack stack)
    {
        y -= 3;
        if (!stack.isEmpty())
        {
            float f = (float)stack.getAnimationsToGo() - partialTicks;

            if (f > 0.0F)
            {
                RenderSystem.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                RenderSystem.translatef((float)(x + 8), (float)(y + 12), 0.0F);
                RenderSystem.scalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                RenderSystem.translatef((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
            }

            mc.getItemRenderer().renderItemAndEffectIntoGUI(player, stack, x, y);

            if (f > 0.0F)
            {
                RenderSystem.popMatrix();
            }

            mc.getItemRenderer().renderItemOverlays(this.mc.fontRenderer, stack, x, y);
        }
    }
}
