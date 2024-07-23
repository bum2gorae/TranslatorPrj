package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.time.format.TextStyle

class TranslateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {

            }
        }
    }
}

@Composable
fun TranslateScreen() {
    var inputText by remember { mutableStateOf("입력") }
    var tranText by remember { mutableStateOf("") }
    var textCount by remember { mutableIntStateOf(0) }
    val textSize by remember { mutableIntStateOf(20) }
//    val translated = TranslationPart("KOREAN", "ENGLISH")
    var isDownloaded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(80.dp))
        TextField(
            value = inputText,
            onValueChange = { newText ->
                inputText = newText
                textCount = inputText.length
            },
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = textSize.sp)
        )
        Spacer(modifier = Modifier.size(30.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp)
                .background(
                    color = Color(0xfffaf2e6),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(15.dp)
        ) {
            Text(
                text = tranText,
                fontSize = textSize.sp
            )
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                val translated =TranslationPart("KOREAN", "ENGLISH")
                translated.TranslateOnClick(inputText)
                tranText = translated.tranText
            },
            enabled = isDownloaded,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFd69f47)
            )
        ) {
            Text(text = "번역")

        }
    }
}

class TranslationPart(startLang: String, targetLang: String) {
    var tranText = ""
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.KOREAN)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()
    val koenTranslator = Translation.getClient(options)
    var isDownloaded = false

//    @Composable
//    fun TranslateActive() {
//        DownloadModel( onSuccess = { this.isDownloaded = true })
//    }

    fun TranslateOnClick(inputText: String) {
        this.koenTranslator.translate(inputText)
            .addOnSuccessListener { translatedText ->
                this.tranText = translatedText
            }
    }

    fun download() {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        koenTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                isDownloaded = true
            }
    }
    @Composable
    fun DownloadModel() {
        LaunchedEffect(key1 = koenTranslator) {
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            koenTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    isDownloaded = true
                }
        }
    }
}