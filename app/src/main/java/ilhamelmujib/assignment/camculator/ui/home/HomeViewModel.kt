package ilhamelmujib.assignment.camculator.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import ilhamelmujib.assignment.camculator.persistence.entity.Scan
import ilhamelmujib.assignment.camculator.service.ScanTextFromImageService
import ilhamelmujib.assignment.camculator.utils.getCurrentDateTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val scanTextFromImageService: ScanTextFromImageService,
) : ViewModel() {

    private val _events = Channel<HomeEvents>(capacity = 1)
    val events = _events.receiveAsFlow()

    fun handlePermissionDenied() = viewModelScope.launch {
        _events.send(HomeEvents.ShowPermissionInfo)
    }

    fun handleScan(image: InputImage) {
        viewModelScope.launch {
            _events.send(HomeEvents.ShowScanLoading)
            val completeTextResult = scanTextFromImageService(image)
            completeTextResult.onSuccess { completeText ->
                createScan(completeText)
            }
            completeTextResult.onFailure {
                Log.e("DEBUG", "Error: ${it.localizedMessage}")
                _events.send(HomeEvents.ShowScanError())
            }
        }
    }

    private fun createScan(text: String) = viewModelScope.launch {
        if (text.isNotEmpty() or text.isNotBlank()) {
            var stringOne = ""
            var stringTwo = ""
            var flag= true
            var flagOperand= false
            var operand= 0
            var flagResult= true
            val finalResult = ""

            try {
                for (char: Char in text) {
                    if (char == ' ' || char == '\n') {
                        continue
                    } else if (char in '0'..'9') {
                        if (flag) {
                            stringOne += char
                        } else {
                            stringTwo += char
                        }
                    } else if (char == '+' || char == '-' || char == '_' || char == 'x' || char == 'X' || char == '/') {
                        if (!flagOperand) {
                            flagOperand = true
                            flag = false
                            operand = when (char) {
                                '+' -> {
                                    1
                                }
                                '-', '_' -> {
                                    2
                                }
                                'x', 'X' -> {
                                    3
                                }
                                else -> {
                                    4
                                }
                            }
                        } else {
                            flagResult = false
                            break
                        }
                    } else {
                        flagResult = false
                        break
                    }
                }

                if (!flagResult) {
                    _events.send(HomeEvents.ShowScanError("Invalid"))
                } else {
                    val first = stringOne.toInt()
                    val second = stringTwo.toInt()
                    var result = 0
                    if (operand == 1) {
                        result = first + second
                    } else if (operand == 2) {
                        result = first - second
                    } else if (operand == 3) {
                        result = first * second
                    } else {
                        if (second == 0) {
                            _events.send(HomeEvents.ShowScanError("Divide by Zero"))
                        } else {
                            result = first / second
                            _events.send(HomeEvents.ShowScanSuccess(Scan(input = text, result = result, createdAt = getCurrentDateTime())))
                        }
                    }

                    if (operand != 4) {
                        _events.send(HomeEvents.ShowScanSuccess(Scan(input = text, result = result, createdAt = getCurrentDateTime())))
                    }
                }
            } catch (e: Exception) {
                Log.e("DEBUG", "Error: ${e.localizedMessage}")
               _events.send(HomeEvents.ShowScanError())
            }
        } else {
            _events.send(HomeEvents.ShowScanEmpty)
        }
    }

}