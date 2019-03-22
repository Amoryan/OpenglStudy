precision mediump float;

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

varying vec3 v_PosInWorldSpace;
varying vec3 v_NormalInWorldSpace;

varying vec3 v_PosInEyeSpace;
varying vec3 v_NormalInEyeSpace;

vec4 ambientColor(){
    vec3 ambient = u_AmbientStrength * u_LightColor;
    return vec4(ambient, 1.0);
}

vec4 diffuseColorInWorldSpace(){
    vec3 lightDirection = normalize(u_LightInWorldSpace - v_PosInWorldSpace);

    float diffuse = max(dot(lightDirection, v_PosInWorldSpace), 0.0);

    vec3 diffuseColor = u_DiffuseStrength * diffuse * u_LightColor;

    return vec4(diffuseColor, 1.0);
}

vec4 diffuseColorInEyeSpace(){
    vec3 lightDirection = normalize(u_LightInEyeSpace - v_PosInEyeSpace);

    float diffuse = max(dot(lightDirection, v_NormalInEyeSpace), 0.0);

    vec3 diffuseColor = u_DiffuseStrength * diffuse * u_LightColor;

    return vec4(diffuseColor, 1.0);
}

vec4 specularColorInWorldSpace(){
    vec3 cameraDirection = normalize(v_PosInWorldSpace - u_CameraInWorldSpace);

    vec3 reflectLight = -reflect(normalize(v_PosInWorldSpace - u_LightInWorldSpace), v_NormalInWorldSpace);

    float specular = pow(max(dot(cameraDirection, reflectLight), 0.0), u_ShininessStrength);

    vec3 specularColor = u_SpecularStrength * specular * u_LightColor;

    return vec4(specularColor, 1.0);
}

vec4 specularColorInEyeSpace(){
    vec3 cameraInEyeSpace = vec3(0.0, 0.0, 0.0);

    vec3 cameraDirection = normalize(v_PosInEyeSpace - cameraInEyeSpace);

    vec3 reflectLight = -reflect(normalize(v_PosInEyeSpace - u_LightInEyeSpace), v_NormalInEyeSpace);

    float specular = pow(max(dot(cameraDirection, reflectLight), 0.0), u_ShininessStrength);

    vec3 specularColor = u_SpecularStrength * specular * u_LightColor;

    return vec4(specularColor, 1.0);
}

void main(){
    //    gl_FragColor = (ambientColor() + diffuseColorInWorldSpace()+ specularColorInWorldSpace()) * vec4(1.0, 1.0, 0.0, 1.0);
    gl_FragColor = (ambientColor() + diffuseColorInEyeSpace() + specularColorInEyeSpace()) * vec4(u_Color, 1.0);
}