#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord0;
uniform sampler2D u_texture;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0);
    if (color.a > 0.1) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.5);
    } else {
        gl_FragColor = vec4(0.0);
    }
}
