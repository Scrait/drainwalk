package net.minecraft.client.gui;

import net.minecraft.client.network.play.NetworkPlayerInfo;
import tech.drainwalk.services.chatheads.ChatHeadsFields;
import tech.drainwalk.services.chatheads.GuiMessageOwnerAccessor;

public class ChatLine<T> implements GuiMessageOwnerAccessor
{
    private final int updateCounterCreated;
    private final T lineString;
    private final int chatLineID;
    private NetworkPlayerInfo chatheads$owner;

    public ChatLine(int updatedCounterCreated, T lineString, int chatLineID)
    {
        this.lineString = lineString;
        this.updateCounterCreated = updatedCounterCreated;
        this.chatLineID = chatLineID;
        if (!ChatHeadsFields.firstLine) return;
        chatheads$owner = ChatHeadsFields.lastSender;
        ChatHeadsFields.firstLine = false;
    }

    @Override
    public NetworkPlayerInfo chatheads$getOwner() {
        return chatheads$owner;
    }

    public T getLineString()
    {
        return this.lineString;
    }

    public int getUpdatedCounter()
    {
        return this.updateCounterCreated;
    }

    public int getChatLineID()
    {
        return this.chatLineID;
    }
}
