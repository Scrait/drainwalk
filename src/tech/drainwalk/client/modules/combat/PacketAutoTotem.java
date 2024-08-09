package tech.drainwalk.client.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.player.EventEntitySync;
import tech.drainwalk.utils.inventory.InvenotryUtil;
import tech.drainwalk.utils.time.Timer;

public class PacketAutoTotem extends Module {

    private final Timer timer = new Timer();
    public PacketAutoTotem() {
        super("PacketAutoTotem", Category.COMBAT);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onSync(EventEntitySync event) {
        final int slot = InvenotryUtil.findItemInHotbar(Items.TOTEM_OF_UNDYING);
        int presSlot = slot;
        presSlot = mc.player.inventory.currentItem;
        if (slot != -1) {

                mc.player.connection.sendPacket(new CHeldItemChangePacket(slot));
                if (mc.player.getActiveItemStack().getUseAction() != UseAction.NONE) {
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(presSlot));
                }
                //mc.playerController.tick();
                //mc.player.connection.sendPacket(new CHeldItemChangePacket(presSlot));
                //mc.playerController.tick();
//            mc.player.connection.sendPacket(new CHeldItemChangePacket(slot));
//            mc.player.inventory.currentItem = slot;
            //mc.playerController.tick();
        }
    }
}
