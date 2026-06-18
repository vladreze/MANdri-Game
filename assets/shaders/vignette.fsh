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
uniform float u_cloud_intensity;
uniform float u_stars_intensity;
uniform float u_lights_intensity;

uniform vec3 u_shadow_color;
uniform vec3 u_light_color;

uniform vec2 u_vignette_range;
uniform vec3 u_flashlight_color;
uniform float u_flashlight_intensity;

float drawCloud(vec2 uv, vec2 center, float scaleX, float scaleY, float speed, float time) {
    vec2 pos = uv;

    pos.x = fract(pos.x - time * speed);
    float dx = abs(pos.x - center.x);
    dx = min(dx, 1.0 - dx);
    float dy = pos.y - center.y;

    float flatBottom = dy < 0.0 ? 3.5 : 1.0;
    float fluff = dy > 0.0 ? sin(dx * 20.0) * 0.04 : 0.0;
    float dist = length(vec2(dx * scaleX, dy * scaleY * flatBottom)) - fluff;
    return smoothstep(0.18, 0.02, dist);
}

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0) * v_color;

    vec2 uv = gl_FragCoord.xy / u_resolution.xy;
    vec2 relativePosition = uv - 0.5;

    // coloring
    float brightness = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 gradedColor = mix(u_shadow_color, u_light_color, brightness);
    color.rgb = mix(color.rgb, gradedColor, 0.5);

    // rays
    float ray1 = sin(uv.x * 12.0 - uv.y * 7.0 + u_time * 0.5);
    float ray2 = sin(uv.x * 16.0 - uv.y * 9.0 + u_time * 0.6);
    float ray3 = sin(uv.x * 14.0 - uv.y * 8.0 + u_time * 0.7);
    float rays = (ray1 + ray2 + ray3) * 0.5;

    rays = smoothstep(0.6, 1.0, rays);
    vec3 rayColor = vec3(1.0, 1.0, 0.75) * rays * 0.2 * u_rays_intensity;
    color.rgb += rayColor;

    // fog
    vec3 fogColor = vec3(0.6, 0.75, 0.95);
    float wave = abs(sin(uv.x * 1.0 + u_time * .15) * .25);

    float fogHeight = (1.0 - uv.y) + wave;
    fogHeight = clamp(fogHeight, 0.0, 1.0);

    float opacity = pow(fogHeight, u_density);
    opacity *= 0.7 * u_fog_intensity;
    color.rgb = mix(color.rgb, fogColor, opacity);

    vec3 cloudTopColor = vec3(0.80, 0.75, 0.68);
    vec3 cloudShadowColor = vec3(0.50, 0.38, 0.55);

    float c1 = drawCloud(uv, vec2(0.20, 0.88), 1.5, 2.0, 0.020, u_time);
    float c2 = drawCloud(uv, vec2(0.70, 0.78), 1.8, 2.5, 0.035, u_time);
    float c3 = drawCloud(uv, vec2(0.50, 0.95), 1.2, 1.8, 0.015, u_time);
    float c4 = drawCloud(uv, vec2(0.90, 0.68), 2.2, 3.0, 0.045, u_time);

    float finalClouds = clamp(c1 + c2 + c3 + c4, 0.0, 1.0);

    vec3 actualCloudColor = mix(cloudShadowColor, cloudTopColor, clamp(finalClouds * 1.5, 0.0, 1.0));

    finalClouds *= 0.6 * u_cloud_intensity;

    color.rgb = mix(color.rgb, actualCloudColor, finalClouds);

    // vignette
    float len = length(relativePosition);
    float vignette = smoothstep(u_vignette_range.x, u_vignette_range.y, len);
    float halo = sin(vignette * 3.14159);
    vec3 baseVignetteColor = vec3(0.01, 0.01, 0.03);
    vec3 dynamicVignetteColor = mix(baseVignetteColor, u_flashlight_color, halo * u_flashlight_intensity);
    vec3 coloredVignette = mix(dynamicVignetteColor, color.rgb, vignette);
    color.rgb = mix(color.rgb, coloredVignette, 0.5);

    // stars
    vec4 noiseColor = texture2D(u_noise_texture, uv * 1.0);
    float stars = smoothstep(0.995, 1.0, noiseColor.r);
    float heightMask = smoothstep(0.3, 0.6, uv.y);
    stars *= heightMask;

    vec3 starBaseColor = vec3(1.0, 1.0, 1.0);
    float starBrightness = 10.0;

    stars *= 0.15 + 0.5 * abs(sin(u_time * 1.5 + noiseColor.r * 10.0));
    stars = max(0.0, stars);

    color.rgb += starBaseColor * stars * starBrightness * u_stars_intensity;

    // caveLights
    vec2 fireflyUV = uv * .7;
    fireflyUV.x += sin(u_time * 0.2) * 0.05;
    fireflyUV.y -= u_time * 0.05;

    vec4 fireflyNoise = texture2D(u_noise_texture, fract(fireflyUV));

    float caveLights = smoothstep(0.995, 1.0, fireflyNoise.r);
    float lightsHeightMask = smoothstep(0.0, 0.45, uv.y) * smoothstep(1.0, 0.45, uv.y);

    caveLights *= lightsHeightMask;
    caveLights *= (1.0 - vignette);

    vec3 lightsBaseColor = vec3(1.0, 0.8, 0.4);
    float lightBrightness = 8.0;

    caveLights *= 0.1 + 0.9 * abs(sin(u_time * .5 + fireflyNoise.r * 10.0));
    caveLights = max(0.0, caveLights);

    color.rgb += lightsBaseColor * caveLights * lightBrightness * u_lights_intensity;

    gl_FragColor = color;
}
