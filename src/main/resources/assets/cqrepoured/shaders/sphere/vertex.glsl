#version 130

in vec3 a_pos;

out vec3 v_tex;

uniform mat4 projection;
uniform mat4 modelView;

void main() {
  gl_Position = projection * modelView * vec4(a_pos, 1);
  v_tex = a_pos;
}