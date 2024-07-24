package com.example.TranslatorPrj

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TranslatorPrj.ui.theme.TranslatorPrjTheme
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TranslateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranslatorPrjTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    val clipboardManager: ClipboardManager = LocalClipboardManager.current
                    val contextAct = LocalContext.current as Activity?
                    val context = LocalContext.current
                    val bgColor = Color(0xfff9defa)
                    var inputText by remember { mutableStateOf("") }
                    var tranText by remember { mutableStateOf("") }
                    var isDownloaded by remember {
                        mutableStateOf(false)
                    }
                    var selectedStartOption by remember { mutableStateOf("Korean") }
                    var selectedGoalOption by remember { mutableStateOf("English") }
                    var translator by remember {
                        mutableStateOf(
                            getTranslator(
                                selectedStartOption,
                                selectedGoalOption
                            )
                        )
                    }

                    LaunchedEffect(translator) {
                        downloadTranslator(translator,
                            onSuccessListener = {
                                Log.d("translator_test", "Down Success")
                                isDownloaded = true
                            })
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(top = 50.dp)
                    ) {
                        ChooseLanguageDropDown(selectedStartOption,
                            bgColor,
                            onClickSuccess = {
                                isDownloaded = false
                                selectedStartOption = it
                                translator = getTranslator(selectedStartOption, selectedGoalOption)
                            }
                        )
                    }

                    InputField(inputText, onValueChangeListener = { inputText = it })
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(top = 30.dp)
                    ) {
                        ChooseLanguageDropDown(selectedGoalOption,
                            bgColor,

                            onClickSuccess = {
                                isDownloaded = false
                                selectedGoalOption = it
                                translator = getTranslator(selectedStartOption, selectedGoalOption)
                            }
                        )
                    }
                    TransField(tranText,
                        onTapSuccess = {
                            clipboardManager.setText(AnnotatedString(tranText))
                            Toast.makeText(context, "클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show()
                        })
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TranslationButton(isDownloaded = isDownloaded,
                            onClickSuccess = {
                                translator.translate(inputText)
                                    .addOnSuccessListener { tranText = it }
                            },
                            Modifier.size(height = 50.dp, width = 100.dp)
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        FinishButton(onClickSuccess = { contextAct?.finish() },
                            Modifier.size(height = 50.dp, width = 100.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseLanguageDropDown(
    selectedOption: String,
    bgColor: Color,
    onClickSuccess: (String) -> Unit,
    optionPick: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val options = if (optionPick) LangEnum.TranslateTargetLanguage.values() else LangEnum.RecognizeTargetLanguage.values()

    Column(modifier = Modifier.size(width = 150.dp, height = 60.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = bgColor,
                    focusedContainerColor = bgColor
                )

            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(bgColor)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = {
                            onClickSuccess(option.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InputField(inputText: String, onValueChangeListener: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(
            value = inputText,
            onValueChange = { newText ->
                onValueChangeListener(newText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
            label = { Text(text = "입력") }
        )
    }
}

@Composable
fun TransField(
    tranText: String,
    onTapSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .verticalScroll(rememberScrollState())
            .background(
                color = Color(0xfffaf2e6),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(15.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onTapSuccess()
                    }
                )
            }
    ) {
        Text(
            text = tranText,
            fontSize = 20.sp
        )
    }
}

@Composable
fun TranslationButton(
    isDownloaded: Boolean,
    onClickSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = {
            onClickSuccess()
        },
        enabled = isDownloaded,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFd69f47)
        )
    ) {
        Text(text = "번역")
    }
}

@Composable
fun FinishButton(
    onClickSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = {
            onClickSuccess()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFd69f47)
        )
    ) {
        Text(text = "돌아가기")
    }
}

fun getTranslator(startLang: String, targetLang: String): Translator {
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(LangEnum.TranslateTargetLanguage.valueOf(startLang).targetLanguage)
        .setTargetLanguage(LangEnum.TranslateTargetLanguage.valueOf(targetLang).targetLanguage)
        .build()
    return Translation.getClient(options)
}

fun downloadTranslator(
    translator: Translator,
    onSuccessListener: () -> Unit
) {
    val conditions = DownloadConditions.Builder()
        .build()
    translator.downloadModelIfNeeded(conditions)
        .addOnSuccessListener {
            onSuccessListener()
        }
        .addOnFailureListener { e ->
            Log.e("translator_test", "Download failed", e)
        }
}
