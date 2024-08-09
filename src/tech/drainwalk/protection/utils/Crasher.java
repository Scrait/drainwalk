package tech.drainwalk.protection.utils;

import by.radioegor146.nativeobfuscator.Native;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.util.registry.Bootstrap;
import tech.drainwalk.client.font.FontManager;

@Native
public class Crasher {

    public static void crash() {
        Bootstrap.printToSYSOUT("#@?@# Game crashed! Sosi huyaku blya. #@?@#");
        EventManager.cleanMap(false);
        System.exit(-2);
    }

}
