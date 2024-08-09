package tech.drainwalk.client.draggables;

import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.client.option.options.DraggableOption;

public class DraggableComponent implements IInstanceAccess {

    public void draw(int mouseX, int mouseY) {
        for (Module module : dw.getApiMain().getModuleManager()) {
            for (Option<?> option : module.getSettingList()) {
                if (option instanceof DraggableOption draggableOption) {
                    if (draggableOption.getVisible().getAsBoolean()) {
                        draggableOption.draw(mouseX, mouseY);
                    }
                }
            }
        }
    }

    public void click(int mouseX, int mouseY) {
        for (Module module : dw.getApiMain().getModuleManager()) {
            for (Option<?> option : module.getSettingList()) {
                if (option instanceof DraggableOption draggableOption) {
                    if (draggableOption.getVisible().getAsBoolean()) {
                        draggableOption.click(mouseX, mouseY);
                    }
                }
            }
        }
    }

    public void release() {
        for (Module module : dw.getApiMain().getModuleManager()) {
            for (Option<?> option : module.getSettingList()) {
                if (option instanceof DraggableOption draggableOption) {
                    if (draggableOption.getVisible().getAsBoolean()) {
                        draggableOption.setDrag(false);
                    }
                }
            }
        }
    }

}