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
    纹理的映射就是让物体的每个像素都找到对应的纹理纹素点，在这个映射的过程中势必会出现以下两种情况
        >   拥有大量纹素的纹理被映射到只有少量像素的物体中；
        >   拥有少量纹素的纹理被映射到含有很多像素的物体中；

    GL_TEXTURE_MIN_FILTER对应的就是第一种情况；
    GL_TEXTURE_MAG_FILTER对应的就是第二种情况；

        >   GL_NEAREST
            获取最近的像素点的颜色值作为最终的颜色值，会有明显的锯齿感
        >   GL_LINEAR
            获取最近的4个像素点的颜色值做加权平均，将值作为最终的颜色值，会模糊
*/