package tech.drainwalk.utils.shader;

import tech.drainwalk.utils.shader.impl.BlackRectShader;
import tech.drainwalk.utils.shader.impl.BloomShader;
import tech.drainwalk.utils.shader.impl.GaussianBlurShader;
import tech.drainwalk.utils.shader.impl.WhiteRectShader;

public interface Shaders {
    AbstractShader GAUSSIAN_BLUR_SHADER = new GaussianBlurShader();
    AbstractShader POST_BLOOM_SHADER = new BloomShader();
    AbstractShader WHITE_RECT_SHADER = new WhiteRectShader();
    AbstractShader BLACK_RECT_SHADER = new BlackRectShader();
}
