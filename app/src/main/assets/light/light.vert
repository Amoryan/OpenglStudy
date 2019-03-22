uniform mat4 u_ModelMatrix;
uniform mat4 u_MVMatrix;
uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;
attribute vec3 a_Normal;

varying vec3 v_PosInWorldSpace;
varying vec3 v_NormalInWorldSpace;

varying vec3 v_PosInEyeSpace;
varying vec3 v_NormalInEyeSpace;

void main(){
    gl_Position = u_MVPMatrix * a_Position;

    v_PosInWorldSpace = (u_ModelMatrix * a_Position).xyz;
    v_NormalInWorldSpace = normalize(mat3(u_ModelMatrix) * a_Normal);

    v_PosInEyeSpace = (u_MVMatrix * a_Position).xyz;
    v_NormalInEyeSpace = normalize(mat3(u_MVMatrix) * a_Normal);
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

    3.Calculate the final color 计算vertex 的最终颜色
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
    理解了漫反射，再来计算镜面反射就非常简单了，我们在视觉空间中进行计算，所以将光源，点，和向量都转换到视觉空间中。
    1.某点的入射光的反射光
        > 某点的入射光
            normalize(v_PosInEyeSpace - u_LightInEyeSpace)
            这里要特别注意一下，我们计算漫射光的时候，是光照方向的向量，是u_LihgtInEyeSpace - v_PosInEyeSpace，和这个方向是相反的
        > 入射光相对向量的反射光
            这里使用reflect()函数计算入射光的反射光
            vec3 reflectLight = reflect(normalize(v_PosInEyeSpace - u_LightInEyeSpace), v_NormalInEyeSpace)
    2.点在视觉空间的向量(也就是眼睛和点朝向点方向的向量)
            vec3 cameraDirection = normalize(v_PosInEyeSpace - cameraInEyeSpace)
            因为在视觉空间中，所以，cameraInEyeSpace始终都在原点(0, 0, 0)的位置
            所以实际上就是
            vec3 = cameraDirection = normalize(v_PosInEyeSpace);
    3.计算反射因子
            这里还需要注意一点，反射光的方向和点在视觉空间的向量的方向是不一致的，需要将反射光方向调整
            float specular = pow(max(dot(-reflectLight, cameraDirection), 0.0), 32.0)
            这里的32表示的是反光度，这个值越大，表示反光度越强，这样入射光反射后被散色的光就越少。
    4.计算反射光
            u_SpecularStrength表示物体的粗糙度，表面越光滑的物体反射效果越好
            vec4 specularColor = u_SpecularStrength * specular * u_LightColor;
 */