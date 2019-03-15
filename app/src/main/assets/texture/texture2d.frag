precision mediump float;

uniform sampler2D u_Texture1;
uniform sampler2D u_Texture2;

varying vec2 v_TexCoord;
varying vec4 v_Color;

void main(){
    gl_FragColor = mix(texture2D(u_Texture1, v_TexCoord), texture2D(u_Texture2, v_TexCoord), 0.3f);
}


//end
/*
mix()函数
第三个参数如果是0.0，则使用第一个纹理渲染；如果是1.0，则使用第二个纹理渲染；
如果是之间的值，则使用两个纹理的混合渲染
*/
