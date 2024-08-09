package tech.drainwalk.api.impl.interfaces;

import tech.drainwalk.utils.shader.Shader;

public interface IShaders {

    Shader FONT = new Shader("shaders/msdf_font.fsh", true);

}
