package tech.drainwalk.utils.minecraft;

import net.minecraft.util.text.*;
import tech.drainwalk.utils.Utils;

public class ChatUtils extends Utils {

    public static void addChatMessage(String text) {
        addChatMessage(text, true);
    }

    public static void addChatMessage(String text, boolean withPrefix) {
        if (withPrefix) {
            mc.player.sendMessage(getFormattedWithPrefix(text));
        } else {
            mc.player.sendMessage(new StringTextComponent(text));
        }
    }

    private static ITextComponent getFormattedWithPrefix(String text) {
        ITextComponent itextcomponent = new StringTextComponent("D").setStyle(Style.EMPTY.setColor(Color.fromHex("#8c8c8c"))).
                append(new StringTextComponent("r").setStyle(Style.EMPTY.setColor(Color.fromHex("#999999")))).
                append(new StringTextComponent("a").setStyle(Style.EMPTY.setColor(Color.fromHex("#a6a6a6")))).
                append(new StringTextComponent("i").setStyle(Style.EMPTY.setColor(Color.fromHex("#b2b2b2")))).
                append(new StringTextComponent("n").setStyle(Style.EMPTY.setColor(Color.fromHex("#bfbfbf")))).
                append(new StringTextComponent("W").setStyle(Style.EMPTY.setColor(Color.fromHex("#cccccc")))).
                append(new StringTextComponent("a").setStyle(Style.EMPTY.setColor(Color.fromHex("#d9d9d9")))).
                append(new StringTextComponent("l").setStyle(Style.EMPTY.setColor(Color.fromHex("#e5e5e5")))).
                append(new StringTextComponent("k").setStyle(Style.EMPTY.setColor(Color.fromHex("#f2f2f2")))).
                append(new StringTextComponent(" // ").setStyle(Style.EMPTY.setColor(Color.fromHex("#8c8c8c")))).
                append(new StringTextComponent(TextFormatting.WHITE + text));
        return itextcomponent;
    }
}
