attribute vec4 a_color;
attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_textCoord0;

void main(){
    v_color = a_color;
    v_textCoord0 = a_texCoord0;
    gl_Position = u_projTrans * vec4(vec3(1.0, 1.0, 1.0), 1.0);
}
