package tech.drainwalk.client.option.options;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.client.option.Option;

import java.util.function.BooleanSupplier;

@Getter
public class BooleanOption extends Option<Boolean> {
    @Setter
    private boolean keyBindVisible;
    @Setter
    private boolean key;
    public BooleanOption(String settingName, Boolean value) {
        super(settingName, value);
    }

    @Override
    public BooleanOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public BooleanOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }
}
