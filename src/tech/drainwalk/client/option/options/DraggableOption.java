package tech.drainwalk.client.option.options;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.vector.Vector2f;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.services.render.ScreenService;

import java.util.function.BooleanSupplier;

public class DraggableOption extends Option<Vector2f> {
    @Getter
    @Setter
    private float width;
    @Getter
    @Setter
    private float height;
    @Setter
    private boolean drag;
    private float prevX, prevY;

    public DraggableOption(String settingName, Vector2f value, float width, float height) {
        super(settingName, value);
        this.width = width;
        this.height = height;
    }

    @Override
    public DraggableOption addVisibleCondition(BooleanSupplier visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public DraggableOption addSettingDescription(String settingDescription) {
        this.settingDescription =settingDescription;
        return this;
    }

    public void draw(int mouseX, int mouseY) {
        if (drag) {
            setValue(new Vector2f(mouseX - prevX, mouseY - prevY));
        }
    }

    public void click(int mouseX, int mouseY) {
        if(ScreenService.isHovered(mouseX, mouseY, getValue().x, getValue().y, width, height)) {
            drag = true;
            prevX = mouseX - getValue().x;
            prevY = mouseY - getValue().y;
        }
    }
}
