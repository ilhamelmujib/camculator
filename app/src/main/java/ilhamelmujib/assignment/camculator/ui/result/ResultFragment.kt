package ilhamelmujib.assignment.camculator.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ilhamelmujib.assignment.camculator.R
import ilhamelmujib.assignment.camculator.persistence.entity.Scan
import ilhamelmujib.assignment.camculator.utils.getCurrentDateTime

class ScanFragment : Fragment() {

    private val args: ScanFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ResultScreen(scan = args.scan)
            }
        }
    }
}

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    dispatcher: OnBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher,
    scan: Scan
) {
    MaterialTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = modifier
                        .width(200.dp)
                        .height(70.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = 10.dp,
                    backgroundColor = colorResource(id = R.color.light_blue)
                ) {
                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Input: ${scan.input}",
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            color = colorResource(id = R.color.heavy_blue),
                            style = MaterialTheme.typography.subtitle2,
                        )
                        Text(
                            text = "Result: ${scan.result}",
                            fontFamily = FontFamily(Font(R.font.noto_sans_jp_regular)),
                            color = colorResource(id = R.color.heavy_blue),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                        )
                    }
                }
                Button(
                    modifier = modifier.padding(top = 10.dp),
                    onClick = {
                        dispatcher.onBackPressed()
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(
                            id = R.color.white
                        )
                    )
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back button",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(
                        text = "Back",
                        color = colorResource(id = R.color.heavy_blue),
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun ResultScreenPreview() {
    ResultScreen(
        scan = Scan(
            input = "1 + 1",
            result = 2,
            createdAt = getCurrentDateTime(),
        )
    )
}