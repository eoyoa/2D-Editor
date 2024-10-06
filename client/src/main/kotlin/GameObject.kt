import vision.gears.webglmath.*

open class GameObject(vararg meshes : Mesh) 
   : UniformProvider("gameObject") {

  val position = Vec3()
  var roll = 0.0f 
  val scale = Vec3(1.0f, 1.0f, 1.0f) 

  val modelMatrix by Mat4()

  var selected = 0

  init { 
    addComponentsAndGatherUniforms(*meshes)
  }
  fun update() {
    modelMatrix.set().scale(scale).rotate(roll).translate(position)
  }

  open fun move(dt : Float) {
  }
}
