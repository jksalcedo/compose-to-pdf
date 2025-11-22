package com.jksalcedo.composetopdf

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jksalcedo.composetopdf.ui.theme.ComposeToPDFTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val content = rememberTextFieldState()
            Layout(this, content)
        }

    }
}

@Composable
fun Layout(context: Context, content: TextFieldState) {
    ComposeToPDFTheme {
        val scope = rememberCoroutineScope()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    state = content,
                    label = { Text("Content to write") },
                    modifier = Modifier.padding(innerPadding)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    if (content.text.isNotEmpty()) {
                        scope.launch(Dispatchers.IO) {
                            val pdfGenerator = PdfGenerator(context = context)
                            val path = context.getExternalFilesDir("PDF")

                            path?.let {
                                val file = File(it, "test.pdf")
                                pdfGenerator.generate(
                                    destination = file,
                                    pages = listOf({
                                        Text(
                                            text = content.text.toString()
                                        )
                                    }
                                    )
                                )
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "PDF generated at ${file.absolutePath}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Content cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }) {
                    Text("Generate PDF")
                }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    ComposeToPDFTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    state = TextFieldState(),
                    label = { Text("Content to write") },
                    modifier = Modifier.padding(innerPadding)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                }) {
                    Text("Generate PDF")
                }
            }

        }
    }
}