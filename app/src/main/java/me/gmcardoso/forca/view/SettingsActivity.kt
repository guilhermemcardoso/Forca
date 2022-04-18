package me.gmcardoso.forca.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import me.gmcardoso.forca.databinding.ActivitySettingsBinding
import me.gmcardoso.forca.viewmodel.ForcaViewModel

class SettingsActivity : AppCompatActivity() {

    private val activitySettingsBinding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    private lateinit var forcaViewModel: ForcaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activitySettingsBinding.root)

        forcaViewModel = ViewModelProvider
            .AndroidViewModelFactory(this.application)
            .create(ForcaViewModel::class.java)

        getInitialValues()

        activitySettingsBinding.closeBtn.setOnClickListener {
            finish()
        }

        activitySettingsBinding.difficultySb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                forcaViewModel.setDifficulty(value)
                runOnUiThread {
                    activitySettingsBinding.difficultyLabelTv.text = "Dificuldade: ${value}"
                    activitySettingsBinding.difficultySb.progress = value
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // Nao se aplica
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // Nao se aplica
            }
        })

        activitySettingsBinding.roundsSb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                forcaViewModel.setTotalRounds(value)
                runOnUiThread {
                    activitySettingsBinding.roundsLabelTv.text = "Rodadas: ${value}"
                    activitySettingsBinding.roundsSb.progress = value
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // Nao se aplica
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // Nao se aplica
            }
        })
    }

    fun getInitialValues() {
        val initialDifficulty = forcaViewModel.getDifficulty()
        val initialRounds = forcaViewModel.getRounds()

        activitySettingsBinding.roundsLabelTv.text = "Rodadas: ${initialRounds}"
        activitySettingsBinding.roundsSb.progress = initialRounds!!

        activitySettingsBinding.difficultyLabelTv.text = "Dificuldade: ${initialDifficulty}"
        activitySettingsBinding.difficultySb.progress = initialDifficulty!!

    }
}