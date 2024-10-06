#version 300 es

in vec4 vertexPosition; //#vec4# A four-element vector [x,y,z,w].; We leave z and w alone.; They will be useful later for 3D graphics and transformations. #vertexPosition# attribute fetched from vertex buffer according to input layout spec
in vec3 vertexColor;
out vec3 color;
out vec4 position;

uniform struct{
  mat4 modelMatrix;
} gameObject;

uniform struct{
  mat4 viewProjMatrix;
} camera;

void main(void) {
  color = vertexColor;

  position = vertexPosition;

  gl_Position = vertexPosition * gameObject.modelMatrix * camera.viewProjMatrix; //#gl_Position# built-in output, required
}