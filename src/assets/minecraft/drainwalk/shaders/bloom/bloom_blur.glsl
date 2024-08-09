#version 330 core
out vec4 FragColor;
in vec2 TexCoords;

uniform sampler2D bloomBlur;
uniform bool horizontal;
uniform float weight[5] = float[](0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

void main()
{             
    vec2 tex_offset = 1.0 / textureSize(bloomBlur, 0); // Размер текстуры
    vec3 result = texture(bloomBlur, TexCoords).rgb * weight[0];
    if(horizontal) {
        for(int i = 1; i < 5; ++i) {
            result += texture(bloomBlur, TexCoords + vec2(tex_offset.x * float(i), 0.0)).rgb * weight[i];
            result += texture(bloomBlur, TexCoords - vec2(tex_offset.x * float(i), 0.0)).rgb * weight[i];
        }
    } else {
        for(int i = 1; i < 5; ++i) {
            result += texture(bloomBlur, TexCoords + vec2(0.0, tex_offset.y * float(i))).rgb * weight[i];
            result += texture(bloomBlur, TexCoords - vec2(0.0, tex_offset.y * float(i))).rgb * weight[i];
        }
    }
    FragColor = vec4(result, 1.0);
}
