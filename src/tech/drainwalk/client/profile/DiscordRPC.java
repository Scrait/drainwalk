package tech.drainwalk.client.profile;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

public class DiscordRPC implements IInstanceAccess {

    private final static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private final static club.minnced.discord.rpc.DiscordRPC discordRPC = club.minnced.discord.rpc.DiscordRPC.INSTANCE;

    public static void startRPC() {
        final DiscordEventHandlers eventHandlers = new DiscordEventHandlers();

        discordRPC.Discord_Initialize("1009857786412269578", eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.largeImageKey = "https://i.imgur.com/eFInOAq.gif";
        discordRichPresence.largeImageText = dw.getClientInfo().getCLIENT_NAME() + " " + dw.getClientInfo().getRELEASE_TYPE() +  " " + dw.getClientInfo().getVERSION();
        discordRichPresence.smallImageKey = "https://i.imgur.com/e3n1aFW.gif";
        discordRichPresence.smallImageText = "UID: " + dw.getProfile().getID() + " | Login: " + dw.getProfile().getUSERNAME();
        discordRichPresence.details = "Role: " + dw.getProfile().getROLE();
        new Thread(() -> {
            while (true) {
                try {
                    discordRichPresence.state =  mc.getCurrentServerData() != null ? "Server: " + mc.getCurrentServerData().serverIP : "localhost";
                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {

                }
            }
        }).start();
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
