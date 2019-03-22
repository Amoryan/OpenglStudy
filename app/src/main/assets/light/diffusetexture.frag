precision mediump float;

uniform sampler2D u_Texture;

uniform vec3 u_LightInEyeSpace;

varying vec2 v_TexCoord;
varying vec3 v_PosInEyeSpace;
varying vec3 v_NormalInEyeSpace;

void main(){
    float ambientStrength = 0.3;
    float diffuseStrength = 0.7;
    float specularStrength = 0.7;
    float shininess = 32.0;

    vec3 lightColor = vec3(1.0, 1.0, 1.0);

    vec4 ambientColor = vec4(ambientStrength * lightColor, 1.0);

    vec3 lightDirection = normalize(u_LightInEyeSpace - v_PosInEyeSpace);
    float diffuse = max(dot(v_NormalInEyeSpace, lightDirection), 0.0);
    vec4 diffuseColor = vec4(diffuseStrength * diffuse * lightColor, 1.0);

    vec3 reflectLight = -reflect(normalize(v_PosInEyeSpace - u_LightInEyeSpace), v_NormalInEyeSpace);
    float specular = pow(max(dot(reflectLight, normalize(v_PosInEyeSpace)), 0.0), shininess);
    vec4 specularColor = vec4(specularStrength * specular * lightColor, 1.0);

    gl_FragColor = (ambientColor + diffuseColor + specularColor) * texture2D(u_Texture, v_TexCoord);
}