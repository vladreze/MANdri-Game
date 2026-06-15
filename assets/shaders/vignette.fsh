#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform vec2 u_resolution;

uniform sampler2D u_texture;
uniform sampler2D u_noise_texture;

uniform float u_time;
uniform float u_rays_intensity;

uniform float u_density;
uniform float u_fog_intensity;
uniform float u_stars_intensity;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0) * v_color;

    vec2 uv = gl_FragCoord.xy / u_resolution.xy;
    vec2 relativePosition = uv - 0.5;

    float brightness = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 shadowColor = vec3(0.54, 0.17, 0.89);
    vec3 lightChunks = vec3(0.99, 0.87, 0.42);
    vec3 gradedColor = mix(shadowColor, lightChunks, brightness);
    color.rgb = mix(color.rgb, gradedColor, 0.5);

    float ray1 = sin(uv.x * 12.0 - uv.y * 7.0 + u_time * 0.2);
    float ray2 = sin(uv.x * 18.0 - uv.y * 9.0 + u_time * 0.3);
    float rays = (ray1 + ray2) * 0.5;

    rays = smoothstep(0.6, 1.0, rays);
    vec3 rayColor = vec3(1.0, 0.95, 0.7) * rays * 0.15 * u_rays_intensity;

    color.rgb += rayColor;

    vec3 fogColor = vec3(0.6, 0.75, 0.85);
    float wave = sin(uv.x * 6.0 + u_time * 1.5) * 0.05;

    float fogHeight = (1.0 - uv.y) + wave;
    fogHeight = clamp(fogHeight, 0.0, 1.0);

    float opacity = pow(fogHeight, u_density);
    opacity *= 0.7 * u_fog_intensity;

    color.rgb = mix(color.rgb, fogColor, opacity);

    vec4 noiseColor = texture2D(u_noise_texture, uv * 3.0);
    float stars = smoothstep(0.9935, 1.0, noiseColor.r);
    float heightMask = smoothstep(0.0, 0.45, uv.y);
    stars *= heightMask;
    stars *= 0.4 + 0.6 * abs(sin(u_time * 3.0 + noiseColor.r * 20.0));
    stars = max(0.0, stars);
    color.rgb += vec3(stars) * u_stars_intensity;

    float len = length(relativePosition);
    float vignette = smoothstep(0.5, 0.4, len);
    vec3 vignetteColor = vec3(0.02, 0.02, 0.08);
    vec3 coloredVignette = mix(vignetteColor, color.rgb, vignette);
    color.rgb = mix(color.rgb, coloredVignette, 0.5);

    gl_FragColor = color;
}
