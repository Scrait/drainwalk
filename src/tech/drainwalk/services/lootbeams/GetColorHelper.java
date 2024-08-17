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
//        if(LootBeams.CRASH_BLACKLIST.contains(item.getItem())) {
//            return Color.WHITE;
//        }
//
//        try {
//
//            //From Config Overrides
//            Color override = Configuration.getColorFromItemOverrides(item.getItem().getItem());
//            if (override != null) {
//                return override;
//            }
//
//            //From NBT
//            if (item.getItem().hasTag() && item.getItem().getTag().contains("lootbeams.color")) {
//                return Color.decode(item.getItem().getTag().getString("lootbeams.color"));
//            }
//
//            //From Name
//            if (true) {
//                Color nameColor = getRawColor(item.getItem().getTextComponent());
//                if (!nameColor.equals(Color.WHITE)) {
//                    return nameColor;
//                }
//            }
//
//            //From Rarity
//            if (Configuration.RENDER_RARITY_COLOR.get() && item.getItem().getRarity().color != null) {
//                return new Color(item.getItem().getRarity().color.getColor());
//            } else {
//                return Color.WHITE;
//            }
//        } catch (Exception e) {
//            LootBeams.LOGGER.error("Failed to get color for ("+ item.getItem().getDisplayName() + "), added to temporary blacklist");
//            LootBeams.CRASH_BLACKLIST.add(item.getItem());
//            LootBeams.LOGGER.info("Temporary blacklist is now : " );
//            for(ItemStack s : LootBeams.CRASH_BLACKLIST){
//                LootBeams.LOGGER.info(s.getDisplayName());
//            }
//            return Color.WHITE;
//        }
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

        if (!list.isEmpty() && list.get(0).getColor() != null) {
            return new Color(list.get(0).getColor().getColor());
        }
        return Color.WHITE;
    }

}