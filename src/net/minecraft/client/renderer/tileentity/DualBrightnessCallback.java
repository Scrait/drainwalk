package net.minecraft.client.renderer.tileentity;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.optifine.EmissiveTextures;

public class DualBrightnessCallback<S extends TileEntity> implements TileEntityMerger.ICallback<S, Int2IntFunction>
{
    public Int2IntFunction func_225539_a_(S p_225539_1_, S p_225539_2_)
    {
        return (p_lambda$func_225539_a_$0_2_) ->
        {
            if (EmissiveTextures.isRenderEmissive())
            {
                return LightTexture.MAX_BRIGHTNESS;
            }
            else {
                int i = WorldRenderer.getCombinedLight(p_225539_1_.getWorld(), p_225539_1_.getPos());
                int j = WorldRenderer.getCombinedLight(p_225539_2_.getWorld(), p_225539_2_.getPos());
                int k = LightTexture.getLightBlock(i);
                int l = LightTexture.getLightBlock(j);
                int i1 = LightTexture.getLightSky(i);
                int j1 = LightTexture.getLightSky(j);
                return LightTexture.packLight(Math.max(k, l), Math.max(i1, j1));
            }
        };
    }

    public Int2IntFunction func_225538_a_(S p_225538_1_)
    {
        return (p_lambda$func_225538_a_$1_0_) ->
        {
            return p_lambda$func_225538_a_$1_0_;
        };
    }

    public Int2IntFunction func_225537_b_()
    {
        return (p_lambda$func_225537_b_$2_0_) ->
        {
            return p_lambda$func_225537_b_$2_0_;
        };
    }
}
