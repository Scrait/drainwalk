package tech.drainwalk.protection;

import by.radioegor146.nativeobfuscator.Native;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;
import com.mojang.blaze3d.systems.RenderSystem;
import me.dreamix.NativeGetter;
import net.minecraft.client.GameConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenSize;
import net.minecraft.client.util.UndeclaredException;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Bootstrap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.drainwalk.protection.utils.Crasher;
import tech.drainwalk.protection.utils.CryptUtils;
import tech.drainwalk.protection.utils.HttpUtils;

import java.io.File;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.OptionalInt;

@Native
public class ScraitProtect {
    //public static String userData[] = null;
    public static String userData[] = {"1", "Scrait", "0", "Penis", "0", "0", "20.12.2049"};
    private static final Logger LOGGER = LogManager.getLogger();

    public static void pleaseStartMe(String hash)
    {
//        System.out.println("чо это я тут");
//
//        if (userData != null) {
//            HttpUtils.ban();
//            Crasher.crash();
//            return;
//        }
////        if (!System.getProperty("java.vm.name").equals("Sk3dVM")) {
////            HttpUtils.ban();
////            Crasher.crash();
////        }
////        if (!System.getProperty("java.version").equals("Sk3dVM")) {
////            HttpUtils.ban();
////            Crasher.crash();
////        }
//        if (!HttpUtils.isHashExists(hash)) {
//            HttpUtils.ban();
//            Crasher.crash();
//            return;
//        }
//
//        userData = CryptUtils.decryptString(hash.substring(0, hash.length() - 32)).split(":");
//        final String userDataServer[] = CryptUtils.decryptString(HttpUtils.getHash(userData[0])).split(":");
//        if (!(userDataServer[0].equals(userData[0])) || !(userDataServer[1].equals(userData[1])) ||
//                !(userDataServer[2].equals(userData[2])) || !(userDataServer[3].equals(userData[3])) ||
//                !(userDataServer[4].equals(userData[4])) || !(userDataServer[5].equals(userData[5])) ||
//                !(userDataServer[6].equals(userData[6]))) {
//            HttpUtils.ban();
//            Crasher.crash();
//            return;
//        }
//
//        final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//        Date expiryDate = null;
//        try {
//            expiryDate = df.parse(userData[6]);
//        } catch (ParseException e) {
//            HttpUtils.ban();
//            Crasher.crash();
//            return;
//        }
//
//        if (new Date().after(expiryDate)) {
//            Crasher.crash();
//            return;
//        }
//
//        System.out.println("Пытаюсь запустить");
        Thread.currentThread().setName("DrainWalk");
        int i = 1280;
        int j = 720;

        boolean flag = false;
        boolean flag2 = false;
        boolean flag3 = false;

        final String name = "drainwalk" + RandomStringUtils.randomAlphabetic(4);

        Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new Serializer()).create();

        File file2 = new File(new File("C:\\drainwalk\\client"), "assets/");
        File file3 = new File(new File("C:\\drainwalk\\client"), "resourcepacks/");
        String s5 = PlayerEntity.getOfflineUUID(name).toString();

        CrashReport.crash();
        Bootstrap.register();
        Bootstrap.checkTranslations();
        Util.func_240994_l_();
        Session session = new Session(name, s5, "", "legacy");

        GameConfiguration gameconfiguration = new GameConfiguration(
                new GameConfiguration.UserInformation(session, JSONUtils.fromJson(gson, "{}", PropertyMap.class), JSONUtils.fromJson(gson, "{}", PropertyMap.class), Proxy.NO_PROXY), new ScreenSize(i, j, OptionalInt.of(i), OptionalInt.of(j), flag),
                new GameConfiguration.FolderInformation(new File("C:\\drainwalk\\client"), file3, file2, "1.16.5"),
                new GameConfiguration.GameInformation(false, "1.16.5", "vanilla", flag2, flag3),
                new GameConfiguration.ServerInformation(null, 25565)
        );

        Thread thread = new Thread("DrainWalk disable")
        {
            public void run()
            {
                Minecraft minecraft1 = Minecraft.getInstance();

                IntegratedServer integratedserver = minecraft1.getIntegratedServer();

                if (integratedserver != null)
                {
                    integratedserver.initiateShutdown(true);
                }
            }
        };

        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        Runtime.getRuntime().addShutdownHook(thread);
        final Minecraft minecraft;

        try
        {
            Thread.currentThread().setName("Render thread");
            RenderSystem.initRenderThread();
            RenderSystem.beginInitialization();
            minecraft = new Minecraft(gameconfiguration);
            RenderSystem.finishInitialization();
        }
        catch (UndeclaredException undeclaredexception)
        {
            LOGGER.warn("Failed to create window: ", undeclaredexception);
            return;
        }
        catch (Throwable throwable1)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Initializing game");
            crashreport.makeCategory("Initialization");
            Minecraft.fillCrashReport(null, gameconfiguration.gameInfo.version, null, crashreport);
            Minecraft.displayCrashReport(crashreport);
            return;
        }

        if (minecraft.isRenderOnThread())
        {
            new Thread("Game thread")
            {
                public void run()
                {
                    try
                    {
                        RenderSystem.initGameThread(true);
                        minecraft.run();
                    }
                    catch (Throwable throwable2)
                    {
                        LOGGER.error("Exception in client thread", throwable2);
                    }
                }
            }.start();
        }
        else
        {
            try
            {
                RenderSystem.initGameThread(false);
                minecraft.run();
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Unhandled game exception", throwable);
            }
        }

        try
        {
            minecraft.shutdown();
        } finally {
            minecraft.shutdownMinecraftApplet();
        }
        startMinecraft();
    }

    private static void startMinecraft() {
        Crasher.crash();
    }

    static
    {
        System.setProperty("java.awt.headless", "true");
    }
}
