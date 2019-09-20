package rachmanforniandi.com.anotherkotlinv2app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal lateinit var tapButton: Button
    internal lateinit var txtScore: TextView
    internal lateinit var txtTimeLeft: TextView
    internal var score = 0
    internal var beginTheGame =false
    internal  lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal val TAG = MainActivity::class.java.simpleName
    internal var timeLeftOnTimer: Long = 60000

    companion object {
        private val  SCORE_KEY ="SCORE_KEY"
        private val TIME_LEFT_KEY ="TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG,"onCreated callled. score is: $score")

        tapButton = findViewById(R.id.btn_tap)
        txtScore = findViewById(R.id.txt_your_score)
        txtTimeLeft = findViewById(R.id.txt_time_left)
        //resetTheGame()

        if (savedInstanceState != null){
            score =savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }else{
            resetTheGame()
        }


        tapButton.setOnClickListener { 
            view -> incrementScore()
        }
    }

    private fun restoreGame() {
        txtScore.text = getString(R.string.your_score,score.toString())
        val restoredTime = timeLeftOnTimer /1000
        txtTimeLeft.text = getString(R.string.your_time_left,restoredTime.toString())

        countDownTimer = object : CountDownTimer(timeLeftOnTimer,countDownInterval){

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                var timeLeft = millisUntilFinished / 1000
                txtTimeLeft.text = getString(R.string.your_time_left,timeLeft.toString())
            }
            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        beginTheGame = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY,score)
        outState.putLong(TIME_LEFT_KEY,timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG,"onSavedInstanceState: saving score $score & time left: $timeLeftOnTimer")

    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG,"onDestroy called")
    }

    private fun resetTheGame(){
        score = 0
        txtScore.text = getString(R.string.your_score,score.toString())
        val initialTimeLeft = initialCountDown/10000
        txtTimeLeft.text = getString(R.string.your_time_left,initialTimeLeft.toString())

        countDownTimer = object :CountDownTimer(initialCountDown,countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished /1000
                txtTimeLeft.text = getString(R.string.your_time_left,timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
        beginTheGame = false
    }

    private fun initiateGame(){
        countDownTimer.start()
        beginTheGame=true
    }

    private fun endGame(){
        Toast.makeText(this,getString(R.string.game_over_message,score.toString()),Toast.LENGTH_SHORT).show()
        resetTheGame()
    }



    private fun incrementScore() {
        if (!beginTheGame){
            initiateGame()
        }

        score = score+1
        val newScore = getString(R.string.your_score,score.toString())
        txtScore.text = newScore
    }


}
