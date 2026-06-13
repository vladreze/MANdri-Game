#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform vec3 damage_color;
uniform sampler2D u_texture;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0) * v_color;
//    color.r += 0.5;
//    color.gb -= .2;
    color.rgb += damage_color;
    gl_FragColor = color;
}
