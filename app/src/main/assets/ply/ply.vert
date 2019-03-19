uniform mat4 u_MVMatrix;
uniform mat4 u_MVPMatrix;

uniform vec4 u_Color;

attribute vec4 a_Position;

varying vec4 v_Color;

void main(){
    gl_Position = u_MVPMatrix * a_Position;

    vec4 lightColor = vec4(1.0, 1.0, 1.0, 1.0);

    float ambientStrength = 0.3;
    vec4 ambientColor = ambientStrength * lightColor;

    v_Color = (ambientColor) * u_Color;
}
