package tech.drainwalk.services.chatheads.controllers;

import lombok.experimental.UtilityClass;
import tech.drainwalk.services.chatheads.ChatHeadsFields;

@UtilityClass
public class KeyboardHandlerController {

    public void debugWarnReset() {
        ChatHeadsFields.lastSender = null;
    }

    public void debugErrorReset() {
        ChatHeadsFields.lastSender = null;
    }

}
