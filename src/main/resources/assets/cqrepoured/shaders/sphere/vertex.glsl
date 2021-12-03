#version 110

void main() {
  gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * gl_Vertex;
  gl_TexCoord[0].stp = gl_Vertex.xyz;
}