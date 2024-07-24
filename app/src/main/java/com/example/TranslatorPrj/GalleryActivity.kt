package com.example.TranslatorPrj

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.TranslatorPrj.ui.theme.TranslatorPrjTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranslatorPrjTheme {
                GalleryScreen()
            }
        }
    }
}


@Composable
fun GalleryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val contextAct = LocalContext.current as Activity?
        var visible by remember { mutableStateOf(false) }
        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        var output by remember {
            mutableStateOf("")
        }
        val context = LocalContext.current
        var recognizer by remember {
            mutableStateOf(TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()))
        }
        val pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    val image = InputImage.fromFilePath(context, uri)
                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            visible = true
                            output = visionText.text
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "텍스트 인식에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            visible = true
                        }
                } else {
                    visible = false
                }
            }
        var selectedStartOption by remember { mutableStateOf("English") }
        var isTranslated by remember { mutableStateOf(false) }
        var outputTrans by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(10.dp)
        ) {
            if (visible) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "imagei",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 5.dp,
                            color = Color.Black
                        )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .verticalScroll(rememberScrollState())
                .background(
                    color = Color(0xfffaf2e6),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp)
        ) {
            Text(
                text = if (isTranslated) outputTrans else output
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val bgColor = Color(0xfff9defa)
            ChooseLanguageDropDown(selectedStartOption,
                bgColor,
                onClickSuccess = {
                    selectedStartOption = it
                    recognizer = LangEnum.RecognizeTargetLanguage.valueOf(selectedStartOption).targetLanguage
                },
                optionPick = false
            )
            Spacer(modifier = Modifier.size(30.dp))
            TranslationButton(isDownloaded = true,
                onClickSuccess = {
                    val translator = getTranslator(selectedStartOption, "Korean")
                    downloadTranslator(translator, onSuccessListener = {
                        Log.d("test","success1")
                        translator.translate(output)
                            .addOnSuccessListener {
                                Log.d("test","success2")
                                isTranslated = !isTranslated
                                outputTrans = it
                            }
                    })
                },
                Modifier.size(height = 50.dp, width = 100.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.size(width = 110.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffd69f47)
                ),
                onClick = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                Text(text = "사진 변경")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                modifier = Modifier.size(width = 110.dp, height = 50.dp),
                onClick = {
                    contextAct?.finish()
                }) {
                Text(text = "돌아가기")
            }
        }

    }
}
