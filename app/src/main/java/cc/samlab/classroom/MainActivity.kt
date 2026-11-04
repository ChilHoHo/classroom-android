package cc.samlab.classroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cc.samlab.classroom.ui.LocalNetworkAccess
import cc.samlab.classroom.ui.ClassroomApp
import cc.samlab.classroom.ui.theme.ClassroomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClassroomTheme {
                LocalNetworkAccess { ClassroomApp() }
            }
        }
    }
}
