uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;
attribute vec2 a_TexCoord;

varying vec2 v_TexCoord;

void main(){
    gl_Position = u_MVPMatrix * a_Position;

    v_TexCoord = a_TexCoord;
}

//end

/*
    GL_TEXTURE_MIN_FILTER
        表示在绘制小于纹理原始大小的时候使用哪种过滤；
    GL_TEXTURE_MAG_FILTER
        表示在绘制大于纹理原始大小的时候使用哪种过滤

        >GL_NEAREST
            这种方式是最粗糙，
        >GL_LINEAR

*/