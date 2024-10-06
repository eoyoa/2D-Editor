import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.math.*

class CircleGeometry(val gl : WebGL2RenderingContext) {

  val vertexBuffer = gl.createBuffer()
  init{
    val vertexCoords = ArrayList<Float>()
    val radius = 0.3f
    vertexCoords.add( 0.0f)
    vertexCoords.add( 0.0f )
    vertexCoords.add( 0.5f  )
    for(i in 0..55) {
      val phi = i * 2.0f * PI.toFloat() / 54.0f;
      vertexCoords.add( cos(phi) * radius )
      vertexCoords.add( sin(phi) * radius )
      vertexCoords.add( 0.5f )
    }

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer) //#ARRAY_BUFFER# OpenGL dictionary:; array buffer means vertex buffer #bind# OpenGL phraseology:; Binding means: select as current. Further operations on the same target affect the bound resource.
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array( vertexCoords.toTypedArray() ),
      GL.STATIC_DRAW)
  }

  val vertexColorBuffer = gl.createBuffer()
  init{
    val vertexColors = ArrayList<Float>()
    vertexColors.add( 0.0f)
    vertexColors.add( 0.0f )
    vertexColors.add( 0.5f  )
    for(i in 0..55) {
      val phi = i * 2.0f * PI.toFloat() / 54.0f;
      vertexColors.add( cos(phi))
      vertexColors.add( sin(phi))
      vertexColors.add( 0.5f  )
    }

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexColorBuffer) //#ARRAY_BUFFER# OpenGL dictionary:; array buffer means vertex buffer #bind# OpenGL phraseology:; Binding means: select as current. Further operations on the same target affect the bound resource.
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array( vertexColors.toTypedArray()),
      GL.STATIC_DRAW)
  }  

  val indexBuffer = gl.createBuffer()
  init{
    val indices = ArrayList<Short>()
    for(i in 0..180) {
      indices.add(0.toShort()) 
      indices.add((i+1).toShort()) 
      indices.add((i+2).toShort())
    } 

    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer) //#ELEMENT_ARRAY_BUFFER# OpenGL dictionary:; element array buffer: index buffer
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,
      Uint16Array( indices.toTypedArray() ),
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

  fun draw() {

    gl.bindVertexArray(inputLayout)
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)  

    gl.drawElements(GL.TRIANGLES, 180, GL.UNSIGNED_SHORT, 0) //#3# pipeline is all set up, draw three indices worth of geometry
  }

}
