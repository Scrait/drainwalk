package tech.drainwalk.utils.inventory;

import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.AirItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import tech.drainwalk.utils.Utils;

public class InvenotryUtil extends Utils {
    public static int getSlotIDFromItem(Item item) {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack.getItem() == item) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }

    public static int getSlotWithElytra() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.ELYTRA) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int findItemInHotbar(Item itemToFind) {

        // search blocks in hotbar
        int slot = -1;
        for (int i = 0; i < 9; i++) {

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof Item)) {
                continue;
            }

            Item item = (stack.getItem());
            if (item.equals(itemToFind)) {
                slot = i;
                break;
            }

        }

        return slot;

    }

    public static int findNullSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof AirItem) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return 999;
    }

    public static void setItem(int n, boolean bl) {
        mc.playerController.windowClick(0, n, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, n, 0, ClickType.PICKUP, mc.player);
    }
}
