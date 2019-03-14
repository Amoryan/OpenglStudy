uniform mat4 u_MVPMatrix;
uniform vec4 u_Color;

attribute vec4 a_Position;

varying vec4 v_Color;

void main(){
    gl_Position = u_MVPMatrix * a_Position;
    v_Color = u_Color;
}
