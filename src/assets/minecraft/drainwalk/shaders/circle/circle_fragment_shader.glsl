#version 330 core

out vec4 fragColor;
uniform vec2 u_Center; // Центр круга
uniform float u_Radius; // Радиус круга
uniform vec4 u_Color; // Цвет круга

void main() {
    vec2 fragCoord = gl_FragCoord.xy;
    float distance = length(fragCoord - u_Center);

    if (distance <= u_Radius) {
        fragColor = u_Color;
    } else {
        discard;
    }
}
