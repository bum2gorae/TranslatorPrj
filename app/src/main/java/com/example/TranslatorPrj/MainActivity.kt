package com.example.TranslatorPrj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.TranslatorPrj.ui.theme.TranslatorPrjTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranslatorPrjTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 250.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val contextAct = LocalContext.current as Activity?
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xffd69f47)
            ),
            onClick = {
                val intent = Intent(contextAct, GalleryActivity::class.java)
                contextAct?.startActivity(intent)
            },
            modifier = Modifier.size(width = 120.dp, height = 60.dp)
        ) {
            Text(text = "사진 번역")
        }
        Spacer(modifier = Modifier.size(20.dp))
//        Button(
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xffd69f47)
//            ),
//            onClick = {
//                val intent = Intent(contextAct, CameraActivity::class.java)
//                contextAct?.startActivity(intent)
//            },
//            modifier = Modifier.size(width = 120.dp, height = 60.dp)
//        ) {
//            Text(text = "카메라 번역")
//        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xffd69f47)
            ),
            onClick = {
                val intent = Intent(contextAct, TranslateActivity::class.java)
                contextAct?.startActivity(intent)
            },
            modifier = Modifier.size(width = 120.dp, height = 60.dp)
        ) {
            Text(text = "텍스트 번역")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TranslatorPrjTheme {
        MainScreen()
    }
}