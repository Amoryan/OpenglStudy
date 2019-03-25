uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;

void main(){
    gl_Position = u_MVPMatrix * a_Position;
}

//end
/*
    GL_TRIANGLES，每三个点绘制一个三角形，总共绘制N/3个三角形
    GL_TRIANGLE_STRIDE，(保证起点是偶数)
        绘制当前三角形依赖于当前顶点的奇偶性，以及前面的两个顶点，我们按照索引从0开始，如果当前点是奇数，
        则构成三角形的点是[n-1,n-2,n]；如果当前点是偶数，则构成三角形的点是[n-2,n-1,n]。
    GL_TRIANGLE_FAN，
        以第一个顶点作为每个三角形的起始点，比如有5个点，那么绘制的三角形分别为012，023，034，这三个三角形，
        我们可以通过这种方式绘制扇形
*/