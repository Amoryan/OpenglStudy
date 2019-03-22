precision mediump float;

uniform sampler2D u_Texture;

uniform vec3 u_LightInEyeSpace;

varying vec3 v_PosInEyeSpace;
varying vec3 v_NormalInEyeSpace;

void main(){
    gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);
}