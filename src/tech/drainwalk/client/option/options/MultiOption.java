package tech.drainwalk.client.option.options;

import lombok.Getter;
import tech.drainwalk.client.option.Option;

import java.util.function.BooleanSupplier;

@Getter
public class MultiOption extends Option<MultiOptionValue> {
    private final MultiOptionValue[] values;

    public MultiOption(final String settingName,
                       final MultiOptionValue... values) {
        super(settingName, values[0]);

        this.values = values;
    }

    public boolean isSelected(String s) {
        for(MultiOptionValue value : values) {
            if(value.getName().equals(s)) {
                return value.isToggle();
            }
        }
        return false;
    }

    public String getSelected() {
        String s = "";
        for(MultiOptionValue value : values) {
            if (value.isToggle()) {
                s += value.getName() + ":";
            }
        }
        return s;
    }

    @Override
    public MultiOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public MultiOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }
}
