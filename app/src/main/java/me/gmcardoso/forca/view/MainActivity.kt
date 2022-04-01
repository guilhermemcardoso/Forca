package me.gmcardoso.forca.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.gmcardoso.forca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.playBtn.setOnClickListener {
            val intent: Intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        activityMainBinding.settingsBtn.setOnClickListener {
            val intent: Intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}