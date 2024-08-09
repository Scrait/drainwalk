package tech.drainwalk.api.impl.models;

import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.VanillaPack;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.api.impl.interfaces.IFonts;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrainwalkPack extends VanillaPack {

    public DrainwalkPack(String namespace) {
        super(namespace);
    }

    /**
     * @author Дианище
     * @since 30/07/2024
     * <p>
     * Ничего не понятно
     * Но выглядит прикольно
     */
    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn) {
        return Stream.of(IFonts.class.getDeclaredFields()).map(field -> {
            try {
                field.setAccessible(true);
                return field.get(this);
            } catch (IllegalAccessException e) {
                e.fillInStackTrace();
            }
            return null;
        }).filter(field -> field instanceof DrainwalkResource).map(field -> {
            DrainwalkResource resource = (DrainwalkResource) field;
            String modifiedPath = "font/" + resource.getPath() + ".json";
            return new DrainwalkResource(modifiedPath);
        }).collect(Collectors.toList());
    }

}
