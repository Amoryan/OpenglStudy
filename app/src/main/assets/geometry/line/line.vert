uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;

void main(){
    gl_Position = u_MVPMatrix * a_Position;
}

//end

/*
    比如，定义了4个点(px1,py1),(px2,py2),(px3,py3),(px4,py4)
    GL_LINES，会绘制两条线，第一个点和第二个点组成一条线，第三个点和第四个点组成一条线， N/2条线
    GL_LINE_STRIP，会绘制3条线，第一个点和第二个点，第二个点和第三个点，第三个点和第四个点； N-1条线
    GL_LINE_LOOP，会绘制4条线，在GL_LINE_STRIDE的基础上，会将最后一个点和第一个点连接； N条线
*/