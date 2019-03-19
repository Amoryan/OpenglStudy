uniform mat4 u_MVMatrix;
uniform mat4 u_MVPMatrix;

uniform vec3 u_LightInEyeSpace;

uniform vec4 u_Color;

attribute vec4 a_Position;
attribute vec3 a_Normal;

varying vec4 v_Color;

void main(){
    gl_Position = u_MVPMatrix * a_Position;

    vec4 lightColor = vec4(1.0, 1.0, 1.0, 1.0);

    float ambientStrength = 0.3;
    vec4 ambientColor = ambientStrength * lightColor;

    float diffuseStrength = 0.7;
    vec3 posInEyeSpace = (u_MVMatrix * a_Position).xyz;
    vec3 lightDirection = normalize(u_LightInEyeSpace - posInEyeSpace);
    vec3 normal = normalize(mat3(u_MVMatrix) * a_Normal);
    float diffuse = max(dot(lightDirection, normal), 0.0);
    vec4 diffuseColor = diffuseStrength * diffuse * lightColor;

    v_Color = (ambientColor + diffuseStrength) * u_Color;
}
