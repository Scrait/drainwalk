package tech.drainwalk.services.chatheads.controllers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.IReorderingProcessor;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.services.chatheads.ChatHeadsFields;
import tech.drainwalk.services.chatheads.GuiMessageOwnerAccessor;
import tech.drainwalk.services.render.ColorService;

@UtilityClass
public class ChatComponentController implements IInstanceAccess {

    public float moveTheText(float y, int color) {
        ChatHeadsFields.lastY = (int)y;
        ChatHeadsFields.lastOpacity = ColorService.getRGBAf(color)[3]; // haha yes
        NetworkPlayerInfo owner = ((GuiMessageOwnerAccessor)ChatHeadsFields.lastGuiMessage).chatheads$getOwner();
        return owner == null ? 0 : ChatHeadsFields.CHAT_OFFSET;
    }

    public ChatLine<IReorderingProcessor> captureGuiMessage(ChatLine<IReorderingProcessor> guiMessage) {
        ChatHeadsFields.lastGuiMessage = guiMessage;
        return guiMessage;
    }

    public void render(MatrixStack matrixStack) {
        NetworkPlayerInfo owner = ((GuiMessageOwnerAccessor)ChatHeadsFields.lastGuiMessage).chatheads$getOwner();
        if (owner == null) return;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1, 1, 1, ChatHeadsFields.lastOpacity);
        mc.getTextureManager().bindTexture(owner.getLocationSkin());
        // draw base layer
        AbstractGui.blit(matrixStack, 0, ChatHeadsFields.lastY, 8, 8, 8.0F, 8, 8, 8, 64, 64);
        // draw hat
        AbstractGui.blit(matrixStack, 0, ChatHeadsFields.lastY, 8, 8, 40.0F, 8, 8, 8, 64, 64);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    public int correctClickPosition(int x) {
        return x - ChatHeadsFields.CHAT_OFFSET;
    }

    public int fixTextOverflow() {
        return NewChatGui.calculateChatboxWidth(mc.gameSettings.chatWidth) - ChatHeadsFields.CHAT_OFFSET;
    }

    public void detectNewMessage() {
        ChatHeadsFields.firstLine = true;
    }

}
