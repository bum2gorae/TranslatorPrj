package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions

class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GalleryScreen()
            }
        }
    }
}


@Composable
fun GalleryScreen() {
    Column(modifier = Modifier.fillMaxSize()
        .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom) {
        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        var output by remember {
            mutableStateOf("")
        }
        val context = LocalContext.current
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        val pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    val image = InputImage.fromFilePath(context, uri)
                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            output = visionText.text
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "텍스트 인식에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // uri가 비어 있는 경우 처리
                }
            }
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "imagei",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(50.dp))
                .border(
                    width = 5.dp,
                    color = Color.Black
                )
        )
        Button(onClick = {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text(text = "사진 변경")
        }
        Text(text = output)
    }
}