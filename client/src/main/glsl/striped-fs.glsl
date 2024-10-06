#version 300 es

precision highp float;

in vec4 position;
in vec3 color;
out vec4 fragmentColor; //#vec4# A four-element vector [r,g,b,a].; Alpha is opacity, we set it to 1 for opaque.; It will be useful later for transparency.

uniform struct {
  float stripeWidth;
} material;

void main(void) {
  if(fract(position.x / material.stripeWidth) > 0.5)
    fragmentColor = vec4(color, 1);
  else
    fragmentColor = vec4(1, 1, 0, 1); //#1, 1, 0, 1# solid yellow
}
