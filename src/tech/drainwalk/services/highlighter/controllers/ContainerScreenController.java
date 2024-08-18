package tech.drainwalk.services.highlighter.controllers;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import tech.drainwalk.services.highlighter.Highlighter;

@UtilityClass
public class ContainerScreenController {

    public void renderSlot(MatrixStack matrixStack, Slot slot)
    {
        // Only mark items that are in the player's inventory and hotbar.
        if (slot.inventory instanceof PlayerInventory)
        {
            if (slot.getHasStack())
            {
                Highlighter.renderNewItemMark(matrixStack, slot);
            }
        }
    }

}
