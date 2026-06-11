#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);

    vec2 relativePosition = v_texCoords - vec2(0.5, 0.5);

    float distance = length(relativePosition);

    float vignette = 1.0 - (distance * 1.2);

    vignette = clamp(vignette, 0.0, 1.0);

    gl_FragColor = vec4(texColor.rgb * vignette, texColor.a) * v_color;
}
