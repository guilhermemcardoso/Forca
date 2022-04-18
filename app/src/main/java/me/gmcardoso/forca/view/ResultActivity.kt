package me.gmcardoso.forca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import me.gmcardoso.forca.databinding.ActivityResultBinding
import me.gmcardoso.forca.model.Word
import me.gmcardoso.forca.viewmodel.ForcaViewModel

class ResultActivity : AppCompatActivity() {

    private val activityResultBinding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityResultBinding.root)

        val correctAnswers = intent.getStringArrayListExtra("correctAnswers")
        val wrongAnswers = intent.getStringArrayListExtra("wrongAnswers")
        Log.d("WORD - correctAnswers", correctAnswers?.size.toString())
        Log.d("WORD - wrongAnswers", wrongAnswers?.size.toString())

    }
}