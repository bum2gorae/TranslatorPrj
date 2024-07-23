package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
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
fun MainScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        val contextAct = LocalContext.current as Activity?
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                val intent = Intent(contextAct, GalleryActivity::class.java)

            }) {
                Text(text = "사진 번역")
            }
            Button(onClick = {
                val intent = Intent(contextAct, GalleryActivity::class.java)

            }) {
                Text(text = "텍스트 번역")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}