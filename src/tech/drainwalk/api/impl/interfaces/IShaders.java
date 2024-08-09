package tech.drainwalk.api.impl.interfaces;

import tech.drainwalk.utils.shader.Shader;

public interface IShaders {

    Shader FONT = new Shader("shaders/msdf_font.fsh", true);
    Shader ROUNDED_TEXTURE = new Shader("shaders/rounded_texture.fsh", true);
    Shader ROUNDED_GRADIENT = new Shader("shaders/rounded_gradient.fsh", true);
    Shader SHADOW = new Shader("shaders/shadow.fsh", true);
    Shader ROUNDED_OUTLINE = new Shader("shaders/rounded_outline.fsh", true);
    Shader PROGRESS_BAR = new Shader("shaders/progress_bar.fsh", true);

}
