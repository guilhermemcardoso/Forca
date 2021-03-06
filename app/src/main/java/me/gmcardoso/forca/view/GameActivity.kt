package me.gmcardoso.forca.view

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import me.gmcardoso.forca.databinding.ActivityGameBinding
import me.gmcardoso.forca.viewmodel.ForcaViewModel
import java.text.Normalizer

class GameActivity : AppCompatActivity() {

    private val activityGameBinding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private var keyboardEnabled = false
    private var word: String = ""
    private var ganhouRound = false

    private lateinit var forcaViewModel: ForcaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityGameBinding.root)

        forcaViewModel = ViewModelProvider
            .AndroidViewModelFactory(this.application)
            .create(ForcaViewModel::class.java)

        addKeyboardListeners()

        activityGameBinding.nextBtn.setOnClickListener {
            forcaViewModel.finishRound(ganhouRound)
        }

        startGame()
        observeWord()
        observeIdentifiers()
        getTotalRounds()
        observeCurrentRound()
        observeAttempts()
        observeGameEnded()
    }

    fun startGame() {
        forcaViewModel.startGame()
    }

    fun CharSequence.unaccent(): String {
        val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(temp, "")
    }

    fun guess(key: String) {
        forcaViewModel.guess(key)
        val stringBuilder = StringBuilder()
        stringBuilder.append(activityGameBinding.letrasTv.text)
        if(activityGameBinding.letrasTv.text.length > 0) {
            stringBuilder.append(" - ")
        }
        stringBuilder.append(key)



        if(word.uppercase().contains(key.uppercase())) {
            Toast.makeText(this, "Alternativa correta", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Alternativa incorreta", Toast.LENGTH_SHORT).show()
        }

        for(index in 0 until word.length) {
            var guessingWord = activityGameBinding.palavraTv.text.toString()
            if(word[index].toString().unaccent().uppercase() == key.uppercase()) {
                var before = " "
                var after = ""
                if(index > 0) {
                    before = guessingWord.substring(0, index * 2 + 1)
                }
                if(index < word.length - 1) {
                    after = guessingWord.substring((index + 1) * 2)
                }

                activityGameBinding.palavraTv.text = "${before}${key}${after}"
                if(!activityGameBinding.palavraTv.text.contains("_")) {
                    activityGameBinding.botoesLL.visibility = View.VISIBLE
                    activityGameBinding.tecladoCl.visibility = View.GONE
                    ganhouRound = true
                    Toast.makeText(this, "Voc?? ganhou esta rodada", Toast.LENGTH_SHORT).show()
                }
            }
        }

        activityGameBinding.letrasTv.text = stringBuilder.toString()
    }

    fun observeWord() {
        forcaViewModel.wordMld.observe(this) { updatedWord ->
            keyboardEnabled = true
            word = updatedWord.word
            val stringBuilder = StringBuilder()

            for(index in 0 until updatedWord.letters) {
                stringBuilder.append(" _")
            }
            runOnUiThread {
                activityGameBinding.palavraTv.text = stringBuilder.toString()
                activityGameBinding.letrasTv.text = ""
                activityGameBinding.botoesLL.visibility = View.GONE
                activityGameBinding.tecladoCl.visibility = View.VISIBLE
                ganhouRound = false
                enabledAllKeyboardKeys()
            }
        }
    }

    fun observeIdentifiers() {
        forcaViewModel.identifiersMld.observe(this) {identifiers ->
            forcaViewModel.generateRoundIdentifiers()
            forcaViewModel.nextRound()
        }
    }

    fun getTotalRounds() {
        val total = forcaViewModel.getRounds()
        activityGameBinding.totalRodadasTv.text = total.toString()
    }

    fun observeCurrentRound() {
        forcaViewModel.currentRoundMdl.observe(this) { currentRound ->
            runOnUiThread {
                activityGameBinding.rodadaAtualTv.text = "Rodada $currentRound de "
                val total = activityGameBinding.totalRodadasTv.text.toString().toInt()
                if(currentRound < total) {
                    activityGameBinding.nextBtn.text = "Pr??xima rodada"
                } else {
                    activityGameBinding.nextBtn.text = "Ver resultados"
                }
            }
        }
    }

    fun observeAttempts() {
        forcaViewModel.attemptsMdl.observe(this) {attempts ->
            updateAttempts(attempts)
        }
    }

    fun updateAttempts(remainingAttempts: Int) {

        activityGameBinding.cabecaTv.paintFlags = 0
        activityGameBinding.troncoTv.paintFlags = 0
        activityGameBinding.bracoDireitoTv.paintFlags = 0
        activityGameBinding.bracoEsquerdoTv.paintFlags = 0
        activityGameBinding.pernaDireitaTv.paintFlags = 0
        activityGameBinding.pernaEsquerdaTv.paintFlags = 0

        if(remainingAttempts < 6) {
            activityGameBinding.cabecaTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        if(remainingAttempts < 5) {
            activityGameBinding.troncoTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        if(remainingAttempts < 4) {
            activityGameBinding.bracoDireitoTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        if(remainingAttempts < 3) {
            activityGameBinding.bracoEsquerdoTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        if(remainingAttempts < 2) {
            activityGameBinding.pernaDireitaTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        if(remainingAttempts < 1) {
            activityGameBinding.pernaEsquerdaTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            activityGameBinding.botoesLL.visibility = View.VISIBLE
            activityGameBinding.tecladoCl.visibility = View.GONE
            Toast.makeText(this, "Voc?? perdeu esta rodada", Toast.LENGTH_SHORT).show()
        }
    }

    fun addKeyboardListeners() {
        with(activityGameBinding) {
            letterABtn.setOnClickListener { pressKey("A") }
            letterBBtn.setOnClickListener { pressKey("B") }
            letterCBtn.setOnClickListener { pressKey("C") }
            letterDBtn.setOnClickListener { pressKey("D") }
            letterEBtn.setOnClickListener { pressKey("E") }
            letterFBtn.setOnClickListener { pressKey("F") }
            letterGBtn.setOnClickListener { pressKey("G") }
            letterHBtn.setOnClickListener { pressKey("H") }
            letterIBtn.setOnClickListener { pressKey("I") }
            letterJBtn.setOnClickListener { pressKey("J") }
            letterKBtn.setOnClickListener { pressKey("K") }
            letterLBtn.setOnClickListener { pressKey("L") }
            letterMBtn.setOnClickListener { pressKey("M") }
            letterNBtn.setOnClickListener { pressKey("N") }
            letterOBtn.setOnClickListener { pressKey("O") }
            letterPBtn.setOnClickListener { pressKey("P") }
            letterQBtn.setOnClickListener { pressKey("Q") }
            letterRBtn.setOnClickListener { pressKey("R") }
            letterSBtn.setOnClickListener { pressKey("S") }
            letterTBtn.setOnClickListener { pressKey("T") }
            letterUBtn.setOnClickListener { pressKey("U") }
            letterVBtn.setOnClickListener { pressKey("V") }
            letterWBtn.setOnClickListener { pressKey("W") }
            letterXBtn.setOnClickListener { pressKey("X") }
            letterYBtn.setOnClickListener { pressKey("Y") }
            letterZBtn.setOnClickListener { pressKey("Z") }
        }
    }

    fun enabledAllKeyboardKeys() {
        with(activityGameBinding) {
            letterABtn.isEnabled = true
            letterBBtn.isEnabled = true
            letterCBtn.isEnabled = true
            letterDBtn.isEnabled = true
            letterEBtn.isEnabled = true
            letterFBtn.isEnabled = true
            letterGBtn.isEnabled = true
            letterHBtn.isEnabled = true
            letterIBtn.isEnabled = true
            letterJBtn.isEnabled = true
            letterKBtn.isEnabled = true
            letterLBtn.isEnabled = true
            letterMBtn.isEnabled = true
            letterNBtn.isEnabled = true
            letterOBtn.isEnabled = true
            letterPBtn.isEnabled = true
            letterQBtn.isEnabled = true
            letterRBtn.isEnabled = true
            letterSBtn.isEnabled = true
            letterTBtn.isEnabled = true
            letterUBtn.isEnabled = true
            letterVBtn.isEnabled = true
            letterWBtn.isEnabled = true
            letterXBtn.isEnabled = true
            letterYBtn.isEnabled = true
            letterZBtn.isEnabled = true
        }
    }

    fun disableKey(key: String) {
        with(activityGameBinding) {
            when(key) {
                "A" -> letterABtn.isEnabled = false
                "B" -> letterBBtn.isEnabled = false
                "C" -> letterCBtn.isEnabled = false
                "D" -> letterDBtn.isEnabled = false
                "E" -> letterEBtn.isEnabled = false
                "F" -> letterFBtn.isEnabled = false
                "G" -> letterGBtn.isEnabled = false
                "H" -> letterHBtn.isEnabled = false
                "I" -> letterIBtn.isEnabled = false
                "J" -> letterJBtn.isEnabled = false
                "K" -> letterKBtn.isEnabled = false
                "L" -> letterLBtn.isEnabled = false
                "M" -> letterMBtn.isEnabled = false
                "N" -> letterNBtn.isEnabled = false
                "O" -> letterOBtn.isEnabled = false
                "P" -> letterPBtn.isEnabled = false
                "Q" -> letterQBtn.isEnabled = false
                "R" -> letterRBtn.isEnabled = false
                "S" -> letterSBtn.isEnabled = false
                "T" -> letterTBtn.isEnabled = false
                "U" -> letterUBtn.isEnabled = false
                "V" -> letterVBtn.isEnabled = false
                "W" -> letterWBtn.isEnabled = false
                "X" -> letterXBtn.isEnabled = false
                "Y" -> letterYBtn.isEnabled = false
                "Z" -> letterZBtn.isEnabled = false
            }
        }
    }

    fun pressKey(key: String) {
        if(keyboardEnabled) {
            disableKey(key)
            guess(key)
        }
    }

    fun observeGameEnded() {
        forcaViewModel.gameEndedMdl.observe(this) { gameEnded ->
            if(gameEnded) {
                val intent = Intent(this, ResultActivity::class.java)
                val wrongAnswers = forcaViewModel.getWrongAnswers()
                val correctAnswers = forcaViewModel.getCorrectAnswers()

                intent.putStringArrayListExtra("correctAnswers", ArrayList(correctAnswers))
                intent.putStringArrayListExtra("wrongAnswers", ArrayList(wrongAnswers))
                startActivity(intent)
                finish()
            }
        }
    }
}