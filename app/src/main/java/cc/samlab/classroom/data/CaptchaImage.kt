package cc.samlab.classroom.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import com.caverock.androidsvg.SVG

fun decodeCaptcha(data: String): Bitmap {
    require(data.startsWith("data:image/svg+xml;base64,"))
    val source = String(Base64.decode(data.substringAfter(','), Base64.DEFAULT), Charsets.UTF_8)
    val svg = SVG.getFromString(source)
    val image = Bitmap.createBitmap(450, 150, Bitmap.Config.ARGB_8888)
    svg.documentWidth = 450f
    svg.documentHeight = 150f
    svg.renderToCanvas(Canvas(image))
    return image
}
