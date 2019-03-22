precision mediump float;

uniform sampler2D u_Texture;

uniform vec3 u_LightInEyeSpace;

varying vec2 v_TexCoord;
varying vec3 v_PosInEyeSpace;
varying vec3 v_NormalInEyeSpace;

void main(){
    vec4 lightColor = vec4(1.0, 1.0, 1.0, 1.0);
    vec3 lightDirection = normalize(u_LightInEyeSpace - v_PosInEyeSpace);
    float diffuse = max(dot(v_NormalInEyeSpace, lightDirection), 0.0);
    vec4 diffuseColor = diffuse * lightColor;

    gl_FragColor = diffuseColor * texture2D(u_Texture, v_TexCoord);
}