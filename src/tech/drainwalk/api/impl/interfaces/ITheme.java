package tech.drainwalk.api.impl.interfaces;

import tech.drainwalk.services.render.ColorService;

public interface ITheme {

    int backgroundFirstColor = ColorService.hexToRgb("#222128");
    int backgroundSecondColor = ColorService.hexToRgb("#111111");
    int textFirstColor = ColorService.hexToRgb("#FFFFFF");
    int textSecondColor = ColorService.hexToRgb("#A39BD6");
    int borderColor = ColorService.hexToRgb("#353343");

}
