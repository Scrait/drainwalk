package tech.drainwalk.client.modules.render;

import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.api.impl.models.module.category.Category;
import tech.drainwalk.api.impl.models.module.category.Type;
import tech.drainwalk.client.modules.combat.Aura;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;

public class ViewModel extends Module {

    private final BooleanOption onlyAura = new BooleanOption("Only Aura", false);
    public final FloatOption right_x = new FloatOption("Right X", 0.5f, -2f,2f).addIncrementValue(0.1f);
    public final FloatOption right_y = new FloatOption("Right Y", 0.5f, -1f,1f).addIncrementValue(0.1f);
    public final FloatOption right_z = new FloatOption("Right Z", 0.5f, -2f,2f).addIncrementValue(0.1f);
    public final FloatOption left_x = new FloatOption("Left X", 0.5f, -2f,2f).addIncrementValue(0.1f);
    public final FloatOption left_y = new FloatOption("Left Y", 0.5f, -1f,1f).addIncrementValue(0.1f);
    public final FloatOption left_z = new FloatOption("Left Z", 0.5f, -2f,2f).addIncrementValue(0.1f);
    public final BooleanOption anim = new BooleanOption("Animation", true);
    public final BooleanOption customHands = new BooleanOption("Custom Hands", false);


    public ViewModel() {
        super("ViewModel", Category.RENDER);
        register(
                onlyAura,
                right_x,
                right_y,
                right_z,
                left_x,
                left_y,
                left_z,
                anim//,
                //customHands
        );
        addType(Type.SECONDARY);
    }

    public boolean isEnabledThis() {
        return !onlyAura.getValue() || dw.getApiMain().getModuleManager().findByClass(Aura.class).getTarget() != null;
    }
}
