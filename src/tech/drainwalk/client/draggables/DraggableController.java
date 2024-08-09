package tech.drainwalk.client.draggables;

import com.mojang.blaze3d.matrix.MatrixStack;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DraggableComponent;
import tech.drainwalk.api.impl.models.module.Module;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.client.option.options.DraggableOption;

public class DraggableController implements IInstanceAccess {

    public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
        for (DraggableComponent module : dw.getDraggableManager()) {
            for (Option<?> option : module.getSettingList()) {
                if (option instanceof DraggableOption draggableOption) {
                    if (draggableOption.getVisible().getAsBoolean()) {
                        draggableOption.draw(matrixStack, mouseX, mouseY);
                    }
                }
            }
        }
    }

    public void click(int mouseX, int mouseY) {
        for (DraggableComponent module : dw.getDraggableManager()) {
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
        for (DraggableComponent module : dw.getDraggableManager()) {
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