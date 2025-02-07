import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import vision.gears.webglmath.*

class QuadGeometry(val gl : WebGL2RenderingContext) : Geometry () {

  val vertexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer) //#ARRAY_BUFFER# OpenGL dictionary:; array buffer means vertex buffer #bind# OpenGL phraseology:; Binding means: select as current. Further operations on the same target affect the bound resource.
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array( arrayOf<Float>(
        -0.5f, -0.5f, 0.5f,
        -0.5f,  0.5f, 0.5f,
         0.5f,  0.0f, 0.5f,
         0.5f,  0.5f, 0.5f         
          //## vertex position data x, y, and z coordinates for 3 vertices
      )),
      GL.STATIC_DRAW)
  }

  val vertexColorBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexColorBuffer) //#ARRAY_BUFFER# OpenGL dictionary:; array buffer means vertex buffer #bind# OpenGL phraseology:; Binding means: select as current. Further operations on the same target affect the bound resource.
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array( arrayOf<Float>(
         1.0f,  1.0f, 0.0f,
         1.0f,  0.0f, 10.0f,
         0.0f,  0.0f, -10.0f,
         1.0f,  1.0f, 0.0f
            //## vertex position data x, y, and z coordinates for 3 vertices
      )),
      GL.STATIC_DRAW)
  }  

  val indexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer) //#ELEMENT_ARRAY_BUFFER# OpenGL dictionary:; element array buffer: index buffer
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,
      Uint16Array( arrayOf<Short>(
        0, 1, 2,
        1, 2, 3
      )),
      GL.STATIC_DRAW)
  }

  val inputLayout = gl.createVertexArray() //#VertexArray# OpenGL dictionary:; vertex array object (VAO) is input layout
  init{
    gl.bindVertexArray(inputLayout)

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0, //#0# this explains how attribute 0 can be found in the vertex buffer
      3, GL.FLOAT, //< three pieces of float
      false, //< do not normalize (make unit length)
      0, //< tightly packed
      0 //< data starts at array start
    )

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexColorBuffer)
    gl.enableVertexAttribArray(1)
    gl.vertexAttribPointer(1, //#0# this explains how attribute 0 can be found in the vertex buffer
      3, GL.FLOAT, //< three pieces of float
      false, //< do not normalize (make unit length)
      0, //< tightly packed
      0 //< data starts at array start
    )    

    gl.bindVertexArray(null)
  }

  override fun draw() {

    gl.bindVertexArray(inputLayout)
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)  

    gl.drawElements(GL.TRIANGLES, 6, GL.UNSIGNED_SHORT, 0) //#3# pipeline is all set up, draw three indices worth of geometry
  }

}
