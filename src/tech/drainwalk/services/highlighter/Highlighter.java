package tech.drainwalk.services.highlighter;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import java.util.HashSet;
import java.util.Set;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import tech.drainwalk.api.impl.events.player.EntityItemPickupEvent;
import tech.drainwalk.api.impl.events.player.ItemTooltipEvent;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DrainwalkResource;
import tech.drainwalk.services.animation.EasingList;

public class Highlighter implements IInstanceAccess
{

	public static final ResourceLocation NEW_ITEM_MARKS = new DrainwalkResource("textures/gui/newitemmarks.png");

	private static final Set<Integer> markedSlots = new HashSet<>(36);

	@EventTarget
	public void preItemPickup(EntityItemPickupEvent event)
	{
		PlayerEntity player = event.getPlayer();
		ItemStack item = event.getItem().getItem();
		
		handlePreItemPickup(player, item);
	}

	private void handlePreItemPickup(PlayerEntity player, ItemStack item)
	{
		// First see if there is a stack with available space in the player's inventory.
		int slot = player.inventory.getSlotFor(item);

		// If not, check for a free slot.
		if (slot == -1)
		{
			slot = player.inventory.getBestHotbarSlot();
		}

		// If we found a valid slot, that's the slot the item should go into.
		// We will mark that as a "new item" slot.
		if (slot != -1)
		{
			markedSlots.add(slot);
		}
	}

	public static void itemClicked(final int slotIndex)
	{
		markedSlots.remove(slotIndex);
	}

	@EventTarget
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if (mc.currentScreen instanceof ContainerScreen<?> invScreen)
		{
			Slot slot = invScreen.getHoveredSlot();
			if (slot != null && slot.getStack() == event.getItemStack())
			{
				markedSlots.remove(slot.getSlotIndex());
			}
		}
	}

	public static void renderNewItemMark(MatrixStack matrixStack, Slot slot)
	{
		if (!mc.player.isCreative())
		{
			if (markedSlots.contains(slot.getSlotIndex()))
			{
				render(matrixStack, slot.getStack(), slot.xPos, slot.yPos);
			}
		}
	}

	private static void render(MatrixStack matrixStack, ItemStack item, int x, int y)
	{
		if (item.isEmpty())
		{
			return;
		}

		float timeOffset = Math.abs(((Util.milliTime() % 2000) / 1000.0f) - 1.0f);

		int color = mc.getItemColors().getColor(item, 0);

		RenderSystem.disableDepthTest();

		matrixStack.push();
		matrixStack.translate(0, -EasingList.NONE.ease(timeOffset), 390);

		Minecraft.getInstance().getTextureManager().bindTexture(NEW_ITEM_MARKS);
		RenderSystem.color3f((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color & 255) / 255.0f);
		AbstractGui.blit(matrixStack, x, y, false ? 8 : 0, 0, 8, 8, 16, 16);

		matrixStack.pop();
	}

}