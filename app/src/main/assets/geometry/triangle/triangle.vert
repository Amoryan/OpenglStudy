uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;

void main(){
    gl_Position = u_MVPMatrix * a_Position;
}

//end
/*
    GL_TRIANGLES，每三个点绘制一个三角形，总共绘制N/3个三角形
    GL_TRIANGLE_STRIDE，会绘制
*/