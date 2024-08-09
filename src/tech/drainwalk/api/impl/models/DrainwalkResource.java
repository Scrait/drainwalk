package tech.drainwalk.api.impl.models;

import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;

public class DrainwalkResource extends ResourceLocation implements IInstanceAccess
{

    private static final String NAMESPACE = "drainwalk";

    public DrainwalkResource(String pathIn)
    {
        super(NAMESPACE, pathIn);
    }

    public static void registerResources() {
        SimpleReloadableResourceManager resourceManager = (SimpleReloadableResourceManager) mc.getResourceManager();
        IResourcePack customResourcePack = new DrainwalkPack(NAMESPACE);
        resourceManager.addResourcePack(customResourcePack);
    }

}
