uniform mat4 u_ModelMatrix;
uniform mat4 u_MVMatrix;
uniform mat4 u_MVPMatrix;

// 物体颜色
uniform vec3 u_Color;
// 光颜色
uniform vec3 u_LightColor;
// 光源位置
uniform vec3 u_LightInWorldSpace;
uniform vec3 u_LightInEyeSpace;
// 相机位置
uniform vec3 u_CameraInWorldSpace;
// 环境光强度
uniform float u_AmbientStrength;
// 漫反射强度
uniform float u_DiffuseStrength;
// 镜面反射强度(物体粗糙度，1表示非常光滑)
uniform float u_SpecularStrength;
// 反光度
uniform float u_ShininessStrength;

attribute vec4 a_Position;
attribute vec3 a_Normal;

varying vec4 v_Color;

vec4 ambientColor(){
    vec3 ambient = u_AmbientStrength * u_LightColor;
    return vec4(ambient, 1.0);
}

vec4 diffuseColorInWorldSpace(){
    vec3 posInWorldSpace = (u_ModelMatrix * a_Position).xyz;
    vec3 normalInWorldSpace = normalize(mat3(u_ModelMatrix) * a_Normal);

    vec3 lightDirection = normalize(u_LightInWorldSpace - posInWorldSpace);

    float diffuse = max(dot(lightDirection, normalInWorldSpace), 0.0);

    vec3 diffuseColor = u_DiffuseStrength * diffuse * u_LightColor;

    return vec4(diffuseColor, 1.0);
}

vec4 diffuseColorInEyeSpace(){
    vec3 posInEyeSpace = (u_MVMatrix * a_Position).xyz;
    vec3 normalInEyeSpace = mat3(u_MVMatrix) * a_Normal;

    vec3 lightDirection = normalize(u_LightInEyeSpace - posInEyeSpace);

    float diffuse = max(dot(lightDirection, normalInEyeSpace), 0.0);

    vec3 diffuseColor = u_DiffuseStrength * diffuse * u_LightColor;

    return vec4(diffuseColor, 1.0);
}

vec4 specularColorInWorldSpace(){
    vec3 posInWorldSpace = (u_ModelMatrix * a_Position).xyz;
    vec3 normalInWorldSpace = normalize(mat3(u_ModelMatrix) * a_Normal);

    vec3 cameraDirection = normalize(posInWorldSpace - u_CameraInWorldSpace);

    vec3 reflectLight = -reflect(normalize(posInWorldSpace - u_LightInWorldSpace), normalInWorldSpace);

    float specular = pow(max(dot(cameraDirection, reflectLight), 0.0), u_ShininessStrength);

    vec3 specularColor = u_SpecularStrength * specular * u_LightColor;

    return vec4(specularColor, 1.0);
}

vec4 specularColorInEyeSpace(){
    vec3 cameraInEyeSpace = vec3(0.0, 0.0, 0.0);
    vec3 posInEyeSpace = (u_MVMatrix * a_Position).xyz;
    vec3 normalInEyeSpace = mat3(u_MVMatrix) * a_Normal;

    vec3 cameraDirection = normalize(posInEyeSpace - cameraInEyeSpace);

    vec3 reflectLight = -reflect(normalize(posInEyeSpace - u_LightInEyeSpace), normalInEyeSpace);

    float specular = pow(max(dot(cameraDirection, reflectLight), 0.0), u_ShininessStrength);

    vec3 specularColor = u_SpecularStrength * specular * u_LightColor;

    return vec4(specularColor, 1.0);
}

void main(){
    gl_Position = u_MVPMatrix * a_Position;

    //    v_Color = (ambientColor() + diffuseColorInWorldSpace()+ specularColorInWorldSpace()) * vec4(u_Color, 1.0);
    v_Color = (ambientColor() + diffuseColorInEyeSpace() + specularColorInEyeSpace()) * vec4(u_Color, 1.0);
}