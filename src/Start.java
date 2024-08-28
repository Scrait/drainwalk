import by.radioegor146.nativeobfuscator.Native;
import net.minecraft.client.main.Main;
import tech.drainwalk.protection.ScraitProtect;

import java.util.Arrays;

@Native
public class Start
{
    public static void main(String[] args)
    {
        //ScraitProtect.pleaseStartMe(args[0]);
//        ScraitProtect.pleaseStartMe("61cf974f25d254e9bd925437208cae9e65d893f4c481f47e9f06dfcc102de6786364b3451ef75a75e08f10782866a9de5fa5f8ba532c163f7a9f48b587bb1a5e60e2c10e1f597de2780f221f7855774547ed395da726e47558da853a788779e324c4c9ebf608b69c3d413d389b76bbeaf6e8337c1b2db23c");
        String assets = System.getenv().containsKey("assetDirectory") ? System.getenv("assetDirectory") : "assets";
        Main.main(concat(new String[] {"--version", "mcp", "--assetsDir", assets, "--accessToken", "0", "--assetIndex", "1.16", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
