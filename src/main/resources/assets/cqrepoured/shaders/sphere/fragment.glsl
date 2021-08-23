#version 130

in vec3 v_tex;

out vec4 f_color;

uniform samplerCube cubemap;
uniform vec4 color;
uniform bool useTexture;

void main() {
  if (useTexture) {
    f_color = texture(cubemap, v_tex) * color;
  } else {
    f_color = color;
  }
}