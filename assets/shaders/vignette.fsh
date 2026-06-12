#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform vec2 u_resolution;
uniform sampler2D u_texture;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0) * v_color;
    vec2 relativePosition = gl_FragCoord.xy / u_resolution - .5;
    float len = length(relativePosition);
    float vignette = smoothstep(.4, .3, len);
    color.rgb = mix(color.rgb, color.rgb * vignette, .6);
    gl_FragColor = color;
}
