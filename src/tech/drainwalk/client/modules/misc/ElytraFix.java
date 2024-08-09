package tech.drainwalk.client.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.api.impl.events.UpdateEvent;
import tech.drainwalk.utils.inventory.InvenotryUtil;

public class ElytraFix extends Module {

    private long delay;

    public ElytraFix() {
        super("ElytraFix", Category.MISC);
        addType(Type.SECONDARY);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        ItemStack stack = mc.player.inventory.getItemStack();
        if (stack != null && stack.getItem() instanceof ArmorItem && System.currentTimeMillis() > delay) {
            ArmorItem ia = (ArmorItem) stack.getItem();
            if (ia.getEquipmentSlot() == EquipmentSlotType.CHEST
                    && mc.player.inventory.armorItemInSlot(2).getItem() == Items.ELYTRA) {
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                int nullSlot = InvenotryUtil.findNullSlot();
                boolean needDrop = nullSlot == 999;
                if (needDrop) {
                    nullSlot = 9;
                }
                mc.playerController.windowClick(0, nullSlot, 1, ClickType.PICKUP, mc.player);
                if (needDrop) {
                    mc.playerController.windowClick(0, -999, 1, ClickType.PICKUP, mc.player);
                }
                delay = System.currentTimeMillis() + 300;
            }
        }
    }
}
