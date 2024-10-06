import org.w3c.dom.HTMLCanvasElement
import vision.gears.webglmath.Vec2
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Vec4
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.random.Random

class Scene(
    val gl: WebGL2RenderingContext
) {

    val timeAtFirstFrame = Date().getTime()
    var timeAtLastFrame = timeAtFirstFrame

    val vsIdle = Shader(gl, GL.VERTEX_SHADER, "idle-vs.glsl")
    val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
    val fsStriped = Shader(gl, GL.FRAGMENT_SHADER, "striped-fs.glsl")
    val stripedProgram = Program(gl, vsIdle, fsStriped, arrayOf("vertexPosition", "vertexColor"))
    val solidProgram = Program(gl, vsIdle, fsSolid, arrayOf("vertexPosition", "vertexColor"))

    val triGeometry = TriangleGeometry(gl)
    val heartGeometry = HeartGeometry(gl, 60)

    val stripedMaterialThick = Material(stripedProgram).apply {
        this["stripeWidth"]?.set(0.6f)
    }

    val stripedMaterialThin = Material(stripedProgram).apply {
        this["stripeWidth"]?.set(0.05f)
    }

    val solidMaterialRed = Material(solidProgram).apply {
        this["color"]?.set(0.8f, 0.1f, 0.05f)
    }

    val avatarMesh = Mesh(stripedMaterialThick, triGeometry)
    val stripedHeartMesh = Mesh(stripedMaterialThin, heartGeometry)

    val gameObjects = ArrayList<GameObject>()

    val staticGameObject = GameObject(stripedHeartMesh).apply {
        position.set(-0.5f, -0.5f, 0.0f)
    }

    init {
        gameObjects += staticGameObject

        for (i in 0..20) {
            gameObjects += GameObject(avatarMesh).apply {
                position.randomize(Vec3(-2.0f, -2.0f, 0.0f), Vec3(2.0f, 2.0f, 0.0f))
                scale.randomize(Vec3(0.1f, 0.1f, 1.0f), Vec3(0.3f, 0.3f, 1.0f))
                roll = Random.nextFloat() * (2.0f * 3.14f)
            }
        }
    }

    val camera = OrthoCamera()

    fun resize(canvas: HTMLCanvasElement) {
        gl.viewport(
            0,
            0,
            canvas.width,
            canvas.height
        )//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
    }

    var amtSelected = 0
    @Suppress("UNUSED_PARAMETER")
    fun update(keysPressed: Set<String>, selection: Pair<Vec2, Vec2>?) {
        val timeAtThisFrame = Date().getTime()
        val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
        val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
        timeAtLastFrame = timeAtThisFrame

        gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
        gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

        if("RIGHT" in keysPressed){
            camera.position.x  +=  1.0f * dt
        }

        camera.setAspectRatio(gl.canvas.width.toFloat() / gl.canvas.height)
        camera.updateViewProjMatrix()

        var firstSelected: GameObject? = null
        selection?.let { rect ->
            amtSelected = 0
            gameObjects.forEach {
                it.selected = if (rect.encapsulates(it.position.xy)) {
                    if (amtSelected == 0) {
                        firstSelected = it
                    }
                    ++amtSelected
                } else 0
            }
        }

        gameObjects.forEach {
            if (it.selected > 0) {
                when {
                    "G" in keysPressed -> {
                        val gridN = ceil(sqrt(amtSelected.toDouble())).toInt()
                        val gridPos = Vec2(
                            (((it.selected - 1) % gridN).toFloat() / (gridN - 1)) - 0.5f,
                            (((it.selected - 1) / gridN).toFloat() / (gridN - 1)) - 0.5f
                        )
                        it.position.set(gridPos + camera.position)
                    }
                    "A" in keysPressed -> {
                        it.roll += 1f * dt
                    }
                    "D" in keysPressed -> {
                        it.roll -= 1f * dt
                    }
                    "DELETE" in keysPressed -> {
                        gameObjects.remove(it)
                    }
                }
            }
            it.update()
        }

        gameObjects.forEach {
            if (it.selected > 0) {
                it.using(solidMaterialRed).draw(camera)
            } else it.draw(camera)
        }
    }

    fun clickInScene(canvas : HTMLCanvasElement, screenX: Float, screenY: Float): Vec4 {
        val ndc = Vec4(screenX * 2.0f / canvas.width.toFloat() - 1.0f, 1.0f - screenY * 2.0f / canvas.height.toFloat(), 0.0f, 1.0f)
        return camera.viewProjMatrix.invert() * ndc
    }

    private fun Pair<Vec2, Vec2>.encapsulates(position: Vec2): Boolean {
        val (lowerX, upperX) = this.getLowerAndUpperX()
        val (lowerY, upperY) = this.getLowerAndUpperY()
        val withinX = position.x in lowerX..upperX
        val withinY = position.y in lowerY..upperY

//    console.log("in X: ${withinX}, in Y: ${withinY}")
        return withinX && withinY
    }

    private fun Pair<Vec2, Vec2>.getLowerAndUpperX(): Pair<Float, Float> {
        return if (this.first.x < this.second.x)
            Pair(this.first.x, this.second.x)
        else
            Pair(this.second.x, this.first.x)
    }

    private fun Pair<Vec2, Vec2>.getLowerAndUpperY(): Pair<Float, Float> {
        return if (this.first.y < this.second.y)
            Pair(this.first.y, this.second.y)
        else
            Pair(this.second.y, this.first.y)
    }
}
