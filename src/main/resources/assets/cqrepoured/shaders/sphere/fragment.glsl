#version 110

uniform samplerCube cubemap;
uniform vec4 color;
uniform bool useTexture;

void main() {
  if (useTexture) {
    gl_FragColor = textureCube(cubemap, gl_TexCoord[0].stp) * color;
  } else {
    gl_FragColor = color;
  }
}