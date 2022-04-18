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
import kotlin.collections.ArrayList

class ForcaViewModel(application: Application): AndroidViewModel(application) {
    val identifiersMld: MutableLiveData<Identifier> = MutableLiveData()
    val wordMld: MutableLiveData<Word> = MutableLiveData()
    val currentRoundMdl: MutableLiveData<Int> = MutableLiveData()
    val attemptsMdl: MutableLiveData<Int> = MutableLiveData()
    val gameEndedMdl: MutableLiveData<Boolean> = MutableLiveData()

    private var correctAnswerCounter: MutableList<String> = mutableListOf()
    private var wrongAnswerCounter: MutableList<String> = mutableListOf()
    private var currentDifficulty: Int? = getDifficulty()
    private var totalRounds: Int? = getRounds()
    private var gameIdentifiers: MutableList<Int> = ArrayList()


    companion object {
        val BASE_URL = "https://www.nobile.pro.br/forcaws/"
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

    fun guess(key: String) {
        val word: Word = wordMld.value!!
        if(word.word.uppercase().contains(key.uppercase())) {

        } else {
            val attempts: Int = attemptsMdl.value!!
            attemptsMdl.postValue(attempts - 1)
        }
    }

    fun startGame() {
        currentRoundMdl.postValue(0)
        getIdentifiers(currentDifficulty!!)
        correctAnswerCounter = mutableListOf()
        wrongAnswerCounter = mutableListOf()
        gameEndedMdl.postValue(false)
    }

    fun nextRound() {
        val index = gameIdentifiers[currentRoundMdl.value!!]
        attemptsMdl.postValue(6)
        getWord(index)
        currentRoundMdl.postValue(currentRoundMdl.value!! + 1)
    }

    fun finishRound(ganhouRound: Boolean) {
        Log.d("WORD - finishRound() ganhouRound", ganhouRound.toString())
        if(ganhouRound) {
            correctAnswerCounter.add(wordMld.value?.word!!)

            Log.d("WORD - finishRound() correctAnswerCounterMdl", correctAnswerCounter.toString())
        } else {
            wrongAnswerCounter.add(wordMld.value?.word!!)
            Log.d("WORD - finishRound() wrongAnswerCounterMdl", wrongAnswerCounter.toString())
        }

        if(currentRoundMdl.value!! < totalRounds!!) {
            nextRound()
        } else {
            gameEndedMdl.postValue(true)
        }

    }

    fun generateRoundIdentifiers() {
        val random = Random()
        gameIdentifiers = ArrayList()
        while (gameIdentifiers.size < totalRounds!!) {
            val randomIndex = random.nextInt(identifiersMld.value!!.words.size - 1)
            val randomIdentifier = identifiersMld.value!!.words[randomIndex]
            if(!gameIdentifiers.contains(randomIdentifier)) {
                gameIdentifiers.add(randomIdentifier)
            }
        }
    }

    fun getCorrectAnswers(): MutableList<String> {
        return correctAnswerCounter
    }

    fun getWrongAnswers(): MutableList<String> {
        return wrongAnswerCounter
    }

    fun getRounds() : Int? {

        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val rodadas = sharedPref?.getInt(TOTAL_ROUNDS_KEY, TOTAL_ROUNDS_DEFAULT)
        return rodadas
    }

    fun setTotalRounds(rodadas: Int) {
        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(TOTAL_ROUNDS_KEY, rodadas)
            apply()
        }

        totalRounds = rodadas
    }

    fun getDifficulty() : Int? {

        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val difficulty = sharedPref?.getInt(DIFFICULTY_KEY, DIFFICULTY_DEFAULT)
        return difficulty
    }

    fun setDifficulty(nivel: Int) {
        val application = getApplication<Application>()
        val sharedPref = application.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(DIFFICULTY_KEY, nivel)
            apply()
        }

        currentDifficulty = nivel
    }

    fun getIdentifiers(id: Int) {
        scope.launch {
            forcaApi.retrieveIdentificadores(id).enqueue(object: Callback<Array<Int>> {
                override fun onResponse(
                    call: Call<Array<Int>>,
                    response: Response<Array<Int>>
                ) {
                    val list: Array<Int> = response.body()!!
                    val identifier = Identifier(list)
                    identifiersMld.postValue(identifier)
                }

                override fun onFailure(call: Call<Array<Int>>, t: Throwable) {
                    Log.e("${BASE_URL}", t.message.toString())
                }
            })
        }
    }

    fun getWord(id: Int) {
        scope.launch {
            forcaApi.retrievePalavra(id).enqueue(object: Callback<Array<Word>> {
                override fun onResponse(
                    call: Call<Array<Word>>,
                    response: Response<Array<Word>>
                ) {
                    Log.d("WORD AQUI", response.body()!!.get(0).word)
                    wordMld.postValue(response.body()!!.get(0))
                }

                override fun onFailure(call: Call<Array<Word>>, t: Throwable) {
                    Log.e("${BASE_URL}/palavra/${id}", t.message.toString())
                }
            })
        }
    }


}