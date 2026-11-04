package cc.samlab.classroom.ui.login

import android.text.InputType
import android.view.inputmethod.EditorInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.PlatformTextInputMethodRequest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CaptchaInputPrivacy(content: @Composable () -> Unit) {
    InterceptPlatformTextInput(
        interceptor = { request, next ->
            next.startInputMethod(PlatformTextInputMethodRequest { editorInfo ->
                val connection = request.createInputConnection(editorInfo)
                editorInfo.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                editorInfo.imeOptions = editorInfo.imeOptions or
                    EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
                connection
            })
        },
        content = content,
    )
}
