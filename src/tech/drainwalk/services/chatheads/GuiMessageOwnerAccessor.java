package tech.drainwalk.services.chatheads;

import net.minecraft.client.network.play.NetworkPlayerInfo;

public interface GuiMessageOwnerAccessor {
    NetworkPlayerInfo chatheads$getOwner();
}