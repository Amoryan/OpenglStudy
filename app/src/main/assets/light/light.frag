precision mediump float;

// 光颜色
uniform vec3 u_LightColor;
// 光源位置
uniform vec3 u_LightInEyeSpace;

// 环境光强度
uniform float u_AmbientStrength;
// 漫反射强度
uniform float u_DiffuseStrength;
// 镜面反射强度(物体粗糙度，1表示非常光滑)
uniform float u_SpecularStrength;

varying vec3 v_PosInEyeSpace;
varying vec3 v_NormalInEyeSpace;

vec4 ambientColor(){
    vec3 ambient = u_AmbientStrength * u_LightColor;
    return vec4(ambient, 1.0);
}

vec4 diffuseColor(){
    vec3 lightDirection = normalize(u_LightInEyeSpace - v_PosInEyeSpace);

    float diffuse = max(dot(lightDirection, v_NormalInEyeSpace), 0.0);

    vec3 diffuseColor = u_DiffuseStrength * diffuse * u_LightColor;

    return vec4(diffuseColor, 1.0);
}

vec4 specularColor(){
    vec3 cameraInEyeSpace = vec3(0.0, 0.0, 0.0);

    vec3 cameraDirection = normalize(v_PosInEyeSpace - cameraInEyeSpace);

    vec3 reflectLight = reflect(-normalize(u_LightInEyeSpace - v_PosInEyeSpace), v_NormalInEyeSpace);

    float specular = pow(max(dot(cameraDirection, reflectLight), 0.0), 32.0);

    vec3 specularColor = u_SpecularStrength * specular * u_LightColor;

    return vec4(specularColor, 1.0);
}

void main(){
    gl_FragColor = (ambientColor() + diffuseColor() + specularColor()) * vec4(1.0, 1.0, 0.0, 1.0);
}