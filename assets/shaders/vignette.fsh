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
    float vignette = smoothstep(.45, .35, len);
    vec3 vignetteColor = vec3(0.02, 0.02, 0.08);
    vec3 coloredVignette = mix(vignetteColor, color.rgb, vignette);
    color.rgb = mix(color.rgb, coloredVignette, .5);
    gl_FragColor = color;
}
