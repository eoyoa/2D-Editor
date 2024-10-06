import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.math.*
import vision.gears.webglmath.*

class HeartGeometry(val gl : WebGL2RenderingContext, val tes : Int) : Geometry () {

  val vertexBuffer = gl.createBuffer()
  val colorBuffer = gl.createBuffer()
  init{
    val vertices : ArrayList<Float> = ArrayList<Float> ()
    val colors : ArrayList<Float> = ArrayList<Float> ()

    vertices.add (0.0f)
    vertices.add (0.0f)
    vertices.add (0.5f)

    colors.add (1.0f)
    colors.add (0.0f)
    colors.add (0.0f)

    for (i in 1..tes)
    {
      val t : Float = i.toFloat () * 2.0f * PI.toFloat () / tes.toFloat ()
      vertices.add ((16.0f * sin(t).pow (3.0f)) * 0.01f)
      vertices.add ((16.0f * cos(t) - 5.0f * cos( 2.0f * t) - 2.0f * cos(3.0f * t) - cos(4.0f * t)) * 0.01f)
      vertices.add (0.5f)

      colors.add (sin(t * 20.0f))
      colors.add (0.0f)
      colors.add (0.0f)
    }

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer) //#ARRAY_BUFFER# OpenGL dictionary:; array buffer means vertex buffer #bind# OpenGL phraseology:; Binding means: select as current. Further operations on the same target affect the bound resource.
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array(/* arrayOf<Float>(
        
        -0.5f, -0.5f, 0.5f,
        -0.5f,  0.5f, 0.5f,
         0.5f,  -0.5f, 0.5f, //## vertex position data x, y, and z coordinates for 3 vertices
         0.5f,  0.5f, 0.5f
         
         vertices
      )*/vertices.toTypedArray ()),
      GL.STATIC_DRAW)

    gl.bindBuffer(GL.ARRAY_BUFFER, colorBuffer) //#ARRAY_BUFFER# OpenGL dictionary:; array buffer means vertex buffer #bind# OpenGL phraseology:; Binding means: select as current. Further operations on the same target affect the bound resource.
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array(/* arrayOf<Float>(
        
        -0.5f, -0.5f, 0.5f,
        -0.5f,  0.5f, 0.5f,
         0.5f,  -0.5f, 0.5f, //## vertex position data x, y, and z coordinates for 3 vertices
         0.5f,  0.5f, 0.5f
         
         vertices
      )*/colors.toTypedArray ()),
      GL.STATIC_DRAW)
  }


  val indexBuffer = gl.createBuffer()
  init{
    val indices : ArrayList<Short> = ArrayList<Short> ()
    val lastIdx = tes-1
    for (i in 1..lastIdx)
    {
      indices.add (0.toShort ())
      indices.add (i.toShort ())
      indices.add ((i+1).toShort ())
    }
    indices.add (0.toShort ())
    indices.add (tes.toShort ())
    indices.add (1.toShort ())

    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer) //#ELEMENT_ARRAY_BUFFER# OpenGL dictionary:; element array buffer: index buffer
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,
      Uint16Array( /*arrayOf<Short>(
        0, 1, 2,
        0, 2, 3
      )*/indices.toTypedArray ()),
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

    gl.bindBuffer(GL.ARRAY_BUFFER, colorBuffer)
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

    gl.drawElements(GL.TRIANGLES, (tes) * 3, GL.UNSIGNED_SHORT, 0) //#3# pipeline is all set up, draw three indices worth of geometry
  }

}
