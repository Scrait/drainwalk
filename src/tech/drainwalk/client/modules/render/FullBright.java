package tech.drainwalk.client.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.api.impl.events.UpdateEvent;

public class FullBright extends Module {

    private final SelectOption typeCombo = new SelectOption("Type", 0,
            new SelectOptionValue("Gamma"),
            new SelectOptionValue("Potion"));

    public FullBright() {
        super("FullBright", Category.RENDER);
        addType(Type.SECONDARY);
        register(
                typeCombo
        );
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (typeCombo.getValueByIndex(0)) {
            mc.gameSettings.gamma = 1000;
        } else {
            mc.gameSettings.gamma = 0;
        }
        assert mc.player != null;
        if (typeCombo.getValueByIndex(1)) {
            mc.player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 999999999, 1));
        } else {
            mc.player.removePotionEffect(Effects.NIGHT_VISION);
        }
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gamma = 0;
        mc.player.removePotionEffect(Effects.NIGHT_VISION);
        super.onDisable();
    }
}
