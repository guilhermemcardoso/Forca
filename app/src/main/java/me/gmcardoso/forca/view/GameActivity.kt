package me.gmcardoso.forca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import me.gmcardoso.forca.databinding.ActivityGameBinding
import me.gmcardoso.forca.viewmodel.ForcaViewModel

class GameActivity : AppCompatActivity() {

    private val activityGameBinding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    var keyboardEnabled = false

    private lateinit var forcaViewModel: ForcaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityGameBinding.root)

        forcaViewModel = ViewModelProvider
            .AndroidViewModelFactory(this.application)
            .create(ForcaViewModel::class.java)

        addKeyboardListeners()

        startGame()
        observeWord()
        observeTotalRounds()
        observeCurrentRound()
    }

    fun startGame() {
        //forcaViewModel.startGame()
        keyboardEnabled = true
    }

    fun observeWord() {
        forcaViewModel.wordMld.observe(this) { word ->

        }
    }

    fun observeTotalRounds() {
        forcaViewModel.totalRoundsMdl.observe(this) { totalRounds ->
            runOnUiThread {
                activityGameBinding.totalRodadasTv.text = totalRounds.toString()
            }
        }
    }

    fun observeCurrentRound() {
        forcaViewModel.currentRoundMdl.observe(this) { currentRound ->
            runOnUiThread {
                activityGameBinding.rodadaAtualTv.text = "Rodada $currentRound de "
            }
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
            Log.d("PRESS", key)
        }
    }
}