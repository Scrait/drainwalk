package tech.drainwalk.client.service.services.impl.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;
import tech.drainwalk.api.impl.models.DrainwalkResource;

import java.util.Map;

public class CustomFontRenderer extends FontRenderer {

    public CustomFontRenderer() {
        super((p_238552_1_) ->
        {
            final Map<ResourceLocation, net.minecraft.client.gui.fonts.Font> field_238546_d_ = Maps.newHashMap();
            ResourceLocation resourceLocation = new DrainwalkResource("fonts/arial/alt.json");
            Font font = new Font(Minecraft.getInstance().getTextureManager(), resourceLocation);
            field_238546_d_.put(resourceLocation, font);
            return field_238546_d_.getOrDefault(resourceLocation, null);
        });
    }

}
