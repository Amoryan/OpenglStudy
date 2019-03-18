uniform mat4 u_MVMatrix;
uniform mat4 u_MVPMatrix;

// 光颜色
uniform vec3 u_LightColor;
// 光源位置
uniform vec3 u_LightPosition;

// 环境光强度
uniform float u_AmbientStrength;
// 漫反射强度
uniform float u_DiffuseStrength;

attribute vec4 a_Position;
attribute vec3 a_Normal;

varying vec4 v_Color;

vec4 ambientColor(){
    vec3 ambient = u_AmbientStrength * u_LightColor;
    return vec4(ambient, 1.0);
}

vec4 diffuseColor(){
    vec3 posInEyeSpace = (u_MVMatrix * a_Position).xyz;

    vec3 lightDirection = normalize(u_LightPosition - posInEyeSpace);

    vec3 normal = normalize(mat3(u_MVMatrix) * a_Normal);

    float diffuse = max(dot(lightDirection, normal), 0.0);

    vec3 diffuseColor = u_DiffuseStrength * diffuse * u_LightColor;

    return vec4(diffuseColor, 1.0);
}

void main(){
    gl_Position = u_MVPMatrix * a_Position;

    v_Color = (ambientColor() + diffuseColor()) * vec4(1.0, 1.0, 0.0, 1.0);
}

//end

/*
    Ambient light(环境光)的计算，将色值相乘即可

    R(final) = R(vertex color) * R(ambient color);
    G(final) = G(vertex color) * G(ambient color);
    B(final) = B(vertex color) * B(ambient color);

    final color = {1, 0, 0} * {0.1, 0.1, 0.1} = {0.1, 0.0, 0.0}
*/

/*
    Diffuse light(漫射光)
    对于漫射光，我们需要添加衰减和光的位置，光的位置将用来计算光与表面之间的角度，
    这将影响到表面的整体照明水平。它也被用来计算光和表面之间的距离，这决定了光在那一点的强度。

    1. caculate lambert factor [0,1](朗伯因子)
    我们需要做的第一个主要计算是算出表面和光之间的夹角。正对着光的表面应全力照明，而倾斜的表面应获得较少的照明。
    正确的计算方法是利用朗伯余弦定律。如果我们有两个向量,一个是光的一个点从表面上看,第二个是一个曲面法线
    (如果表面是平面,那么曲面法线向量垂直向上,或正交于表面),然后我们可以计算第一个规范每个向量的余弦,它有一个长度,然后通过计算两个向量的点积。
        > 1.计算顶点到光源方向的单位向量
            光源向量 = diffuse light position - vertex position
            光源向量方向上的单位向量 = 光源向量 / 光源向量的模
        > 2.计算顶点处的曲面向量和单位向量的点积
            dot product = dot product((x1,y1,z1), (x2,y2,z2)) = x1*x2 + y1*y2 + z1*z2;
        > 3.朗伯因子
            朗伯因子 = max(dot product, 0)

        举个栗子：
        一个垂直于y轴的平面上的一个顶点(0,0,0)，光源位置在(0,10,-10)，该点的曲面向量是(0,1,0)
        > 1.
            光源向量 = (0,10,-10) - (0,0,0) = (0,10,-10);
            光源向量的模 = √(0*0 + 10*10 + -10*-10) = √200 = 14.14
            光源向量方向上的单位向量 = (0 / 14.14,10 / 14.14,-10 / 14.14) = (0, 0.707, -0.707)
            单位向量与曲面向量的点积 = dot product((0, 1, 0), (0, 0.707, -0.707)) = 0 * 0 + 1 * 0.707 + 0 * -0.707 = 0.707
            朗伯因子 = max(0.707, 0) = 0.707

    2.caculate attenuation factor(衰减因子)
    接下来，我们需要计算衰减因子。点光源的真实光衰减遵循平方反比定律
        > 1. luminosity = 1 / (光源向量的模 * 光源向量的模)

    3: Calculate the final color 计算vertex 的最终颜色
        > 1. final color =vertex color  * (diffuse color * lambert factor * attenuation factor)

        举个栗子：
        漫射光颜色 = (1,0,0);红色
        顶点颜色 = (1,1,1);白色

        经过漫射光处理，该顶点的最终颜色计算是
        final color = {1, 0, 0} * ({1, 1, 1} * 0.707 * 0.005}) = {1, 0, 0} * {0.0035, 0.0035, 0.0035} = {0.0035, 0, 0}

        R(final) = R(diffuse color) * R(vertex color) * lambert factor * attenuation factor
        G(final) = G(diffuse color) * G(vertex color) * lambert factor * attenuation factor
        B(final) = B(diffuse color) * B(vertex color) * lambert factor * attenuation factor
*/

/*
    Specular light(镜面反射)的计算
*/