package net.optifine.reflect;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.optifine.Log;
import net.optifine.util.StrUtils;

public class ReflectorForge
{
    public static InputStream getOptiFineResourceStream(String path)
    {
        if (!Reflector.OptiFineResourceLocator.exists())
        {
            return null;
        }
        else
        {
            path = StrUtils.removePrefix(path, "/");
            return (InputStream)Reflector.call(Reflector.OptiFineResourceLocator_getOptiFineResourceStream, path);
        }
    }

    public static ReflectorClass getReflectorClassOptiFineResourceLocator()
    {
        String s = "optifine.OptiFineResourceLocator";
        Object object = System.getProperties().get(s + ".class");

        if (object instanceof Class)
        {
            Class oclass = (Class)object;
            return new ReflectorClass(oclass);
        }
        else
        {
            return new ReflectorClass(s);
        }
    }

    public static MapData getMapData(ItemStack stack, World world)
    {
        return FilledMapItem.getMapData(stack, world);
    }

    public static String[] getForgeModIds()
    {
        return new String[0];
    }

    public static void fillNormal(int[] faceData, Direction facing)
    {
        Vector3f vector3f = getVertexPos(faceData, 3);
        Vector3f vector3f1 = getVertexPos(faceData, 1);
        Vector3f vector3f2 = getVertexPos(faceData, 2);
        Vector3f vector3f3 = getVertexPos(faceData, 0);
        vector3f.sub(vector3f1);
        vector3f2.sub(vector3f3);
        vector3f2.cross(vector3f);
        vector3f2.normalize();
        int i = (byte)Math.round(vector3f2.getX() * 127.0F) & 255;
        int j = (byte)Math.round(vector3f2.getY() * 127.0F) & 255;
        int k = (byte)Math.round(vector3f2.getZ() * 127.0F) & 255;
        int l = i | j << 8 | k << 16;
        int i1 = faceData.length / 4;

        for (int j1 = 0; j1 < 4; ++j1)
        {
            faceData[j1 * i1 + 7] = l;
        }
    }

    private static Vector3f getVertexPos(int[] data, int vertex)
    {
        int i = data.length / 4;
        int j = vertex * i;
        float f = Float.intBitsToFloat(data[j]);
        float f1 = Float.intBitsToFloat(data[j + 1]);
        float f2 = Float.intBitsToFloat(data[j + 2]);
        return new Vector3f(f, f1, f2);
    }
}
