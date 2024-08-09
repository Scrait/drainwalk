package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.input.EventMouse;

public class MiddleClickPearl extends Module {

    public MiddleClickPearl() {
        super("MiddleClickPearl", Category.MISC);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2) {
            for (int i = 0; i < 9; i++) {
                final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(i));
                    mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
        super.onDisable();
    }
}
