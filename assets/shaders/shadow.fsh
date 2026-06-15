#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;
uniform sampler2D u_texture;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0) * v_color;
    float brightness = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 shadowColor = vec3(0.54, 0.17, 0.89);
    vec3 lightChunks = vec3(0.99, 0.87, 0.42);
    vec3 gradedColor = mix(shadowColor, lightChunks, brightness);
    color.rgb = mix(color.rgb, gradedColor, 0.5);
    gl_FragColor = color;
}
