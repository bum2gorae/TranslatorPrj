package com.example.TranslatorPrj

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class LangEnum {
    enum class TranslateTargetLanguage(val targetLanguage: String) {
        Korean(TranslateLanguage.KOREAN),
        English(TranslateLanguage.ENGLISH),
        Japanese(TranslateLanguage.JAPANESE),
        Chinese(TranslateLanguage.CHINESE),
        Spanish(TranslateLanguage.SPANISH),
        Portuguese(TranslateLanguage.PORTUGUESE),
        French(TranslateLanguage.FRENCH)
    }

    enum class RecognizeTargetLanguage(val targetLanguage: TextRecognizer) {
        Korean(TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())),
        English(TextRecognition.getClient(TextRecognizerOptions.Builder().build())),
        Japanese(TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())),
        Chinese(TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build()))
    }
}