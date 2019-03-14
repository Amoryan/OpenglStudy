precision mediump float;

// 2d纹理取样器
uniform sampler2D u_Texture;

//varying vec4 v_Color;

varying vec2 v_TexCoord;

void main(){
    gl_FragColor = texture2D(u_Texture, v_TexCoord);
}
