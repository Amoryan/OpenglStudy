uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;
attribute vec4 a_Color;
// 纹理坐标
attribute vec2 a_TexCoord;

//varying vec4 v_Color;
varying vec2 v_TexCoord;

void main(){
    gl_Position = u_MVPMatrix * a_Position;
    
//    v_Color = a_Color;
    
    v_TexCoord = a_TexCoord;
}


//end

图片的坐标系：
    图片坐标系是以图片左上角为原点，向右为x轴的正方向，向下为y轴正方向；
