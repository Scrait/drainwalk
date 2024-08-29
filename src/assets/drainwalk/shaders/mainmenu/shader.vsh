#version 120

varying vec2 fragCoord;

void main() {
    fragCoord = gl_MultiTexCoord0.xy;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
