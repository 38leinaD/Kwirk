#ifdef GL_ES
precision mediump float;
#endif
varying vec2 v_tex0;
varying float v_brightness;

uniform float u_textureS, u_textureT;
uniform float u_textureWidth, u_textureHeight;

uniform sampler2D u_sampler0;
void main() {
    /*
    float s = u_textureS + fract(v_tex0.s)*u_textureWidth;
  	float t = u_textureT + fract(v_tex0.t)*u_textureHeight;
  	gl_FragColor = texture2D(u_sampler0, vec2(s, t));
  	*/
  	gl_FragColor = vec4(1, 1, 1, 1) *  texture2D(u_sampler0,  v_tex0) * clamp(v_brightness + 0.5, 0.0, 1.0);
}