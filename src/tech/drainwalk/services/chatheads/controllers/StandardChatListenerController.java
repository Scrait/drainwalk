package tech.drainwalk.services.chatheads.controllers;

import lombok.experimental.UtilityClass;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.ITextComponent;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.services.chatheads.ChatHeadsFields;

import java.util.UUID;

@UtilityClass
public class StandardChatListenerController implements IInstanceAccess {

    public void onChatMessage(ITextComponent message, UUID senderUuid) {
        ChatHeadsFields.lastSender = mc.getConnection().getPlayerInfo(senderUuid);
        String textString = message.getString();
        if (ChatHeadsFields.lastSender == null) {
            for (String part : textString.split("(§.)|[^\\w]")) {
                if (part.isEmpty()) continue;
                NetworkPlayerInfo p = mc.getConnection().getPlayerInfo(part);
                if (p != null) {
                    ChatHeadsFields.lastSender = p;
                    return;
                }
            }
        }
        for (NetworkPlayerInfo p: mc.getConnection().getPlayerInfoMap()) {
            ITextComponent displayName = p.getDisplayName();
            if (displayName != null && textString.contains(displayName.getString())) {
                ChatHeadsFields.lastSender = p;
                return;
            }
        }
    }

}
