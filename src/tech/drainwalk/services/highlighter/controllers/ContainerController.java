package tech.drainwalk.services.highlighter.controllers;

import lombok.experimental.UtilityClass;
import tech.drainwalk.services.highlighter.Highlighter;

@UtilityClass
public class ContainerController {

    public void doClick(final int slotIndex)
    {
        Highlighter.itemClicked(slotIndex);
    }

}
