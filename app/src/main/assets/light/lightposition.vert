uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

attribute vec4 a_Position;

void main(){

    mat4 viewModelMatrix = u_ViewMatrix * u_ModelMatrix;
    mat4 mvpMatrix = u_ProjectionMatrix * viewModelMatrix;
    gl_Position = mvpMatrix * a_Position;

    gl_PointSize = 10.f;

}