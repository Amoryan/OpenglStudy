uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;
attribute vec2 a_TexCoord;
attribute vec4 a_Color;

varying vec2 v_TexCoord;
varying vec4 v_Color;

void main(){
    gl_Position = u_MVPMatrix * a_Position;
    v_TexCoord = a_TexCoord;
    v_Color = a_Color;
}

//end

/**
1.纹理环绕方向
    >S方向
    >T方向
    >R方向，三维纹理

    环绕模式
        >GL_REPEAT 对纹理的默认行为。重复纹理图像
        >GL_MIRRORED_REPEAT 和GL_REPEAT一样，但每次重复图片是镜像放置的。
        >GL_CLAMP_TO_EDGE 超出的部分会重复纹理坐标的边缘，产生一种边缘被拉伸的效果
        >GL_CLAMP_TO_BORDER 超出的坐标为用户指定的边缘颜色。(GLES20没有这个了)

2.纹理过滤
    过滤有很多种，用的比较多的是GL_NEAREST(邻近过滤)和GL_LINEAR(线性过滤)
        >GL_NEAREST OpenGL默认的纹理过滤方式，OpenGL会选择中心点最接近纹理坐标的那个像素。
        >GL_LINEAR 它会基于纹理坐标附近的纹理像素，计算出一个插值，近似出这些纹理像素之间的颜色。
                   一个纹理像素的中心距离纹理坐标越近，那么这个纹理像素的颜色对最终的样本颜色的贡献越大。(混合色)
 
    当进行放大(Magnify)和缩小(Minify)操作的时候可以设置纹理过滤的选项，比如你可以在纹理被缩小的时候使用邻近过滤，被放大时使用线性过滤。

我们可以给纹理采样器分配一个位置值，这样的话我们能够在一个片段着色器中设置多个纹理。
一个纹理的位置值通常称为一个纹理单元(Texture Unit)。一个纹理的默认纹理单元是0，
它是默认的激活纹理单元。

OpenGL至少保证有16个纹理单元供你使用，也就是说你可以激活从GL_TEXTURE0到GL_TEXTRUE15。
*/
