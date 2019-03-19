uniform mat4 u_MVMatrix;
uniform mat4 u_MVPMatrix;
uniform mat4 u_TMVPMatrix;

attribute vec4 a_Position;
attribute vec3 a_Normal;

varying vec2 v_TexCoord;
varying vec3 v_PosInEyeSpace;
varying vec3 v_NormalInEyeSpace;

void main(){
    gl_Position = u_MVPMatrix * a_Position;

    // [-1,1]映射到[0,1]
    v_TexCoord = ((normalize(mat3(u_MVPMatrix) * a_Normal) + 1.0) / 2.0).xy;

    v_PosInEyeSpace = (u_MVMatrix * a_Position).xyz;

    v_NormalInEyeSpace = normalize(mat3(u_MVMatrix) * a_Normal);
}
