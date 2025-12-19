package com.zjw.compose.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjw.compose.util.Changed
import com.zjw.compose.util.Composer
import com.zjw.compose.util.Composition
import com.zjw.compose.util.MyComposable
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        //自定义compose部分
        var composition = Composition()

        val counterState =  com.zjw.compose.util.mutableStateOf(0) {
            composition.recompose()
        }

        composition.setContent { composer ->
            Counter(counterState.value, composer)
            Text("Tom +${counterState.value}", composer)
        }

        counterState.value = 1
        counterState.value = 1
        counterState.value = 2
    }
}

@MyComposable
fun Text(
    text: String,
    composer: Composer
) {
    var dirty = composer.changed(text)

    composer.startGroup()
    if (dirty) {
        println("🔁 Text recomposed: $text")
    } else {
        println("⏭ Text skipped")
        composer.skipGroup()
    }
    composer.endGroup()
}

@MyComposable
fun Counter(
    count: Int,
    composer: Composer
) {
    composer.startGroup()
    var dirty = composer.changed(count)
    if (dirty) {
        println("🔁 Counter recomposed: $count")
    } else {
        composer.skipGroup()
        println("⏭ Counter skipped")
    }
    composer.endGroup()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val timeStr = timeFormat.format(Date(currentTime))

    Text(
        text = "Hello $name! Current Time: $timeStr",
        modifier = modifier
    )

    Image(
        painter = painterResource(R.drawable.ic_launcher_background),
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}