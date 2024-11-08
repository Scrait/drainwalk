package tech.drainwalk.client.option.options;

import lombok.Getter;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.utils.math.MathematicUtils;

import java.util.function.BooleanSupplier;

@Getter
public class SelectOption extends Option<SelectOptionValue> {
    private final SelectOptionValue[] values;

    private int currentSelected;

    public SelectOption(String settingName, int selected, SelectOptionValue... values) {
        super(settingName, values[(int) MathematicUtils.clamp(selected,0, values.length -1)]);
        this.currentSelected = selected;
        this.values = values;
    }
    public boolean getValueByIndex(final int index) {
        return getValue() == getValues()[index];
    }
    @Override
    public SelectOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public SelectOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }

}
