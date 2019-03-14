uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;
// 纹理坐标
attribute vec2 a_TexCoord;

varying vec2 v_TexCoord;

void main(){
    gl_Position = u_MVPMatrix * a_Position;
    
    v_TexCoord = a_TexCoord;
}


//end

图片坐标系是以图片左上角为原点，向右为x轴的正方向，向下为y轴正方向；[0,1]

模型所在的坐标系是[-1,1]，以中心点为原点的

