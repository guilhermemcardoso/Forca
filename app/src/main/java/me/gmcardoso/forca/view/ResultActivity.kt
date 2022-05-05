package me.gmcardoso.forca.view

import android.content.Intent
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

        val acertosStringBuffer = StringBuffer()
        for(index in 0 until (correctAnswers?.size ?: 0)) {
            acertosStringBuffer.append("- " + (correctAnswers?.get(index) ?: "") + "\n")
        }

        val errosStringBuffer = StringBuffer()
        for(index in 0 until (wrongAnswers?.size ?: 0)) {
            errosStringBuffer.append("- " + (wrongAnswers?.get(index) ?: "") + "\n")
        }

        activityResultBinding.acertosLabelTv.text = "${correctAnswers?.size} acertos:"
        activityResultBinding.errosLabelTv.text = "${wrongAnswers?.size} erros:"
        activityResultBinding.totalPalavrasTv.text = "${(correctAnswers?.size ?: 0) + (wrongAnswers?.size ?: 0)} palavras"
        activityResultBinding.acertosTv.text = acertosStringBuffer.toString()
        activityResultBinding.errosTv.text = errosStringBuffer.toString()
        activityResultBinding.backBtn.setOnClickListener {
            finish()
        }
        activityResultBinding.nextBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}