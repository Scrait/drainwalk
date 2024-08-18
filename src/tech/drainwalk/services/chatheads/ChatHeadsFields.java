package tech.drainwalk.services.chatheads;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.network.play.NetworkPlayerInfo;

public class ChatHeadsFields {

    public static NetworkPlayerInfo lastSender;
    public static ChatLine<?> lastGuiMessage;

    public static int lastY = 0;
    public static float lastOpacity = 0;

    public static final int CHAT_OFFSET = 10;

    public static boolean firstLine;

}
