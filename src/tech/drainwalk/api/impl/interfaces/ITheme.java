package tech.drainwalk.api.impl.interfaces;

import tech.drainwalk.services.render.ColorService;

public interface ITheme {

    int backgroundFirstColor = ColorService.hexToRgb("#222128");
    int backgroundSecondColor = ColorService.hexToRgb("#111111");
    int textFirstColor = ColorService.hexToRgb("#FFFFFF");
    int textSecondColor = ColorService.hexToRgb("#A39BD6");
    int borderColor = ColorService.hexToRgb("#353343");
    int additionalFirstColor = ColorService.hexToRgb("#555170");
    int additionalSecondColor = ColorService.hexToRgb("#A39BD6");
    int additionalThirdColor = ColorService.hexToRgb("#C7BEFF");
    int uiBorderColor = ColorService.hexToRgb("#55526E");
    int moduleEnabledBackgroundColor = ColorService.hexToRgb("#25242D");
    int moduleDisabledBackgroundColor = ColorService.hexToRgb("#1C1C1E");

    default int[] getFadedColors(int amount) {
        int[] colors = new int[amount];
        int speed = 15;
        for (int i = 0; i < amount; i++) {
            colors[i] = ColorService.gradient(speed, 1, backgroundFirstColor, backgroundSecondColor);
            speed += 5;
        }
        return colors;
    }

}
