package ilhamelmujib.assignment.camculator.service

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ScanTextFromImageService(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val recognizers = listOf(
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS),
    )

    suspend operator fun invoke(image: InputImage) = withContext(dispatcher) {
        kotlin.runCatching {
            val detectedText = mutableSetOf<String>()
            recognizers.forEach { textRecognizer ->
                val result = textRecognizer.process(image).await()
                if (result.text.isNotBlank()) {
                    detectedText.add(result.text)
                }
            }

            val completeText = buildString {
                detectedText.forEach { text -> append(text) }
            }
            completeText
        }
    }
}