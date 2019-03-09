uniform mat4 u_MVPMatrix;
// 环境光颜色
uniform vec3 u_AmbientLightColor;
// 环境光强度
uniform float u_AmbientLightStrength;

attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color;

vec4 ambientColor(){
    vec3 ambient = u_AmbientLightStrength * u_AmbientLightColor;
    return vec4(ambient, 1.0);
}

void main(){

   gl_Position = u_MVPMatrix * a_Position;

   v_Color = ambientColor() * a_Color;

}

//end

/*
    Ambient light(环境光)的计算将，色值相乘即可
    f -> final color
    o -> origin color
    a -> ambient light color
    Rf = Ro * Ra;
    Gf = Go * Ga;
    Bf = Bo * Ba;
*/