package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.input.EventMouse;
import tech.drainwalk.utils.inventory.InvenotryUtil;

public class MiddleClickItem extends Module {
    public MiddleClickItem() {
        super("MiddleClickItem", Category.MISC);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2) {
            final int slot = InvenotryUtil.findItemInHotbar(Items.EXPERIENCE_BOTTLE);
            if (slot == -1) return;
            mc.player.connection.sendPacket(new CHeldItemChangePacket(slot));
            mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
        }
    }

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
        super.onDisable();
    }
}
