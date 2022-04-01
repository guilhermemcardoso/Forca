package me.gmcardoso.forca.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.gmcardoso.forca.model.ForcaApi
import me.gmcardoso.forca.model.Identifier
import me.gmcardoso.forca.model.Word
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ForcaViewModel(application: Application): AndroidViewModel(application) {
    val identifiersMld: MutableLiveData<Identifier> = MutableLiveData()
    val wordMld: MutableLiveData<Word> = MutableLiveData()
    val totalRoundsMdl: MutableLiveData<Int> = MutableLiveData()
    val currentRoundMdl: MutableLiveData<Int> = MutableLiveData()
    val difficultyMdl: MutableLiveData<Int> = MutableLiveData()

    var gameIdentifiers: MutableList<Int> = ArrayList()

    companion object {
        val BASE_URL = "https://nobile.pro.br/forcaws/"
        val SHARED_PREFERENCES_KEY = "FORCA_SHARED_PREFERENCES_KEY"
        val TOTAL_ROUNDS_KEY = "TOTAL_ROUNDS_KEY"
        val TOTAL_ROUNDS_DEFAULT = 1
        val DIFFICULTY_KEY = "DIFFICULTY_KEY"
        val DIFFICULTY_DEFAULT = 1
    }

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("${BASE_URL}")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val forcaApi: ForcaApi = retrofit.create(ForcaApi::class.java)

    fun startGame() {
        getTotalRounds()
        getDifficulty()
        getIdentifiers(difficultyMdl.value!!)
        generateRoundIdentifiers()
        currentRoundMdl.postValue(1)
        getWord(gameIdentifiers[currentRoundMdl.value!!])
    }

    fun generateRoundIdentifiers() {
        val random = Random()
        gameIdentifiers = ArrayList()
        while (gameIdentifiers.size < totalRoundsMdl.value!!) {
            val randomIndex = random.nextInt(identifiersMld.value!!.words.size - 1)
            val randomIdentifier = identifiersMld.value!!.words[randomIndex]
            if(!gameIdentifiers.contains(randomIdentifier)) {
                gameIdentifiers.add(randomIdentifier)
            }
        }
    }

    fun getTotalRounds() {

        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val rodadas = sharedPref?.getInt(TOTAL_ROUNDS_KEY, TOTAL_ROUNDS_DEFAULT)
        totalRoundsMdl.postValue(rodadas)
    }

    fun setTotalRounds(rodadas: Int) {
        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(TOTAL_ROUNDS_KEY, rodadas)
            apply()
        }

        totalRoundsMdl.postValue(rodadas)
    }

    fun getDifficulty() {

        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val difficulty = sharedPref?.getInt(DIFFICULTY_KEY, DIFFICULTY_DEFAULT)
        difficultyMdl.postValue(difficulty)
    }

    fun setDifficulty(nivel: Int) {
        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(DIFFICULTY_KEY, nivel)
            apply()
        }

        difficultyMdl.postValue(nivel)
    }

    fun getIdentifiers(id: Int) {
        scope.launch {
            forcaApi.retrieveIdentificadores(id).enqueue(object: Callback<Identifier> {
                override fun onResponse(
                    call: Call<Identifier>,
                    response: Response<Identifier>
                ) {
                    identifiersMld.postValue(response.body())
                }

                override fun onFailure(call: Call<Identifier>, t: Throwable) {
                    Log.e("${BASE_URL}", t.message.toString())
                }
            })
        }
    }

    fun getWord(id: Int) {
        scope.launch {
            forcaApi.retrievePalavra(id).enqueue(object: Callback<Word> {
                override fun onResponse(
                    call: Call<Word>,
                    response: Response<Word>
                ) {
                    wordMld.postValue(response.body())
                }

                override fun onFailure(call: Call<Word>, t: Throwable) {
                    Log.e("${BASE_URL}", t.message.toString())
                }
            })
        }
    }


}