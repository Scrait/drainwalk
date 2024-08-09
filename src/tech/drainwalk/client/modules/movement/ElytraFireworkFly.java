package tech.drainwalk.client.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.utils.inventory.InvenotryUtil;
import tech.drainwalk.utils.movement.MoveUtils;
import tech.drainwalk.utils.time.Timer;

public class ElytraFireworkFly extends Module {
    private int lastItem;
    private final Timer timer = new Timer();

    public ElytraFireworkFly() {
        super("ElytraFireworkFly", Category.MOVEMENT);
        addType(Type.SECONDARY);
    }

    @Override
    public void onEnable() {
        final int elytra = InvenotryUtil.getSlotIDFromItem(Items.ELYTRA);
        if (elytra != -1) {

            lastItem = elytra;

            mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, lastItem, 1, ClickType.PICKUP, mc.player);

        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!mc.player.isOnGround()) {
            if (mc.player.fallDistance > 0 && !mc.player.isElytraFlying()) {
                //final int elytra = InvenotryUtil.getSlotIDFromItem(Items.ELYTRA);
                //if (elytra != -2) {
                    mc.getConnection().sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_FALL_FLYING));
                //}
            }
            if (mc.player.isElytraFlying()) {
                mc.player.setMotionY(0.4F);
                final int fireworks = InvenotryUtil.findItemInHotbar(Items.FIREWORK_ROCKET);
                if (fireworks >= 0 && timer.delay(1200, true)) {
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(fireworks));
                    mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.setMotionY(1.4);
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.setMotionY(-1);
                }
                MoveUtils.setMotion(2);
            }
        } else {
            mc.player.jump();
        }
    }

    @Override
    public void onDisable() {
        final int elytra = InvenotryUtil.getSlotIDFromItem(Items.ELYTRA);
        if (elytra != -1) {

            if (elytra == -2) {
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, this.lastItem, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
            }
        }
    }
}
