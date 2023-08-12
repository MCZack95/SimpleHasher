import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.security.MessageDigest
import java.security.Security


@Composable
@Preview
fun App() {
    var mExpanded by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(10.dp, 10.dp, 10.dp, 10.dp),
            horizontalAlignment = Alignment.Start) {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp, 5.dp),
                verticalAlignment = Alignment.Top) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Output") },
                    modifier = Modifier.fillMaxWidth().padding(10.dp, 10.dp, 10.dp, 0.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {

                Column(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    OutlinedTextField(
                        value = mSelectedText,
                        onValueChange = { mSelectedText = it },
                        label = {Text("Select Hash Type")},
                        trailingIcon = {
                            Icon(icon,"contentDescription",
                                Modifier.clickable { mExpanded = !mExpanded })
                        },
                        modifier = Modifier.width(250.dp),
                        readOnly = true
                    )
                    DropdownMenu(
                        expanded = mExpanded,
                        onDismissRequest = { mExpanded = false }
                    ) {
                        val hashList = getHashOptions()
                        hashList.forEach { label ->
                            DropdownMenuItem(onClick = {
                                mSelectedText = label
                                mExpanded = false
                            }) {
                                Text(text = label)
                            }
                        }
                    }
                }
                Column(modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)) {
                    Button(onClick = {
                        text = generateHash(text, mSelectedText)
                    }) {
                        Text("Generate Hash")
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Simple Hash Generator") {
        App()
    }
}

fun getHashOptions(): List<String> {
    return Security.getAlgorithms("MessageDigest").toList()
}

fun generateHash(text: String, hashType: String): String {
    val input = text.toByteArray()
    val md = MessageDigest.getInstance(hashType)
    val bytes = md.digest(input)
    var hash = ""
    bytes.forEach { byte ->
        hash += String.format("%02X", byte)
    }
    return hash
}
