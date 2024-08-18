package tech.drainwalk.services.lootbeams;

import com.google.common.collect.Lists;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextProcessing;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class GetColorHelper {

    /**
     * Returns the color from the item's name, rarity, tag, or override.
     */
    public static Color getItemColor(ItemEntity item) {
        return getRawColor(item.getItem().getTextComponent());
    }

    /**
     * Gets color from the first letter in the text component.
     */
    public static Color getRawColor(ITextComponent text) {
        List<Style> list = Lists.newArrayList();
        text.getComponentWithStyle((acceptor, styleIn) -> {
            TextProcessing.func_238341_a_(styleIn, acceptor, (string, style, consumer) -> {
                list.add(style);
                return true;
            });
            return Optional.empty();
        }, Style.EMPTY);

        if (!list.isEmpty() && list.getFirst().getColor() != null) {
            return new Color(list.getFirst().getColor().getColor());
        }
        return Color.WHITE;
    }

}