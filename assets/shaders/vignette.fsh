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

uniform vec3 u_shadow_color;
uniform vec3 u_light_color;

uniform vec2 u_vignette_range;
uniform vec3 u_flashlight_color;
uniform float u_flashlight_intensity;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0) * v_color;

    vec2 uv = gl_FragCoord.xy / u_resolution.xy;
    vec2 relativePosition = uv - 0.5;

    float brightness = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 gradedColor = mix(u_shadow_color, u_light_color, brightness);
    color.rgb = mix(color.rgb, gradedColor, 0.5);

    float ray1 = sin(uv.x * 12.0 - uv.y * 7.0 + u_time * 0.5);
    float ray2 = sin(uv.x * 16.0 - uv.y * 9.0 + u_time * 0.6);
    float ray3 = sin(uv.x * 14.0 - uv.y * 8.0 + u_time * 0.7);
    float rays = (ray1 + ray2 + ray3) * 0.5;

    rays = smoothstep(0.6, 1.0, rays);
    vec3 rayColor = vec3(1.0, 1.0, 0.75) * rays * 0.2 * u_rays_intensity;
    color.rgb += rayColor;

    vec3 fogColor = vec3(0.6, 0.75, 0.95);
    float wave = sin(uv.x * 6.0 + u_time * 1.5) * 0.05;

    float fogHeight = (1.0 - uv.y) + wave;
    fogHeight = clamp(fogHeight, 0.0, 1.0);

    float opacity = pow(fogHeight, u_density);
    opacity *= 0.7 * u_fog_intensity;
    color.rgb = mix(color.rgb, fogColor, opacity);

    float len = length(relativePosition);
    float vignette = smoothstep(u_vignette_range.x, u_vignette_range.y, len);
    float halo = sin(vignette * 3.14159);
    vec3 baseVignetteColor = vec3(0.01, 0.01, 0.03);
    vec3 dynamicVignetteColor = mix(baseVignetteColor, u_flashlight_color, halo * u_flashlight_intensity);
    vec3 coloredVignette = mix(dynamicVignetteColor, color.rgb, vignette);
    color.rgb = mix(color.rgb, coloredVignette, 0.5);

    vec4 noiseColor = texture2D(u_noise_texture, uv * 1.0);
    float stars = smoothstep(0.995, 1.0, noiseColor.r);
    float heightMask = smoothstep(0.3, 0.6, uv.y);
    stars *= heightMask;

    vec3 starBaseColor = vec3(1.0, 1.0, 1.0);
    float starBrightness = 10.0;

    stars *= 0.15 + 0.5 * abs(sin(u_time * 1.5 + noiseColor.r * 10.0));
    stars = max(0.0, stars);

    color.rgb += starBaseColor * stars * starBrightness * u_stars_intensity;

    gl_FragColor = color;
}
