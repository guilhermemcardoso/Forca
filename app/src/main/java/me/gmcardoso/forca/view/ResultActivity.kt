package me.gmcardoso.forca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.gmcardoso.forca.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private val activityResultBinding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityResultBinding.root)
    }
}