attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

varying vec2 v_tex0;
varying float v_brightness;
void main() {
   gl_Position = u_projectionMatrix * u_viewMatrix * u_modelMatrix * a_position;
   v_tex0 = a_texCoord0;
   vec3 normalWorldSpace = (u_modelMatrix * vec4(a_normal, 0.0)).xyz;
   v_brightness = dot(normalWorldSpace, normalize(vec3(0.0, -0.5, 1.0)));
   gl_PointSize = 1.0;
}