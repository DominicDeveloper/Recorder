package com.asadbek.hiddencamerademo

import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.asadbek.hiddencamerademo.databinding.ActivityMainBinding
import java.io.File

const val CODE_AUDIO = 200
class MainActivity : AppCompatActivity() {
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer:MediaPlayer? = null
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaRecorder = MediaRecorder()


        if (isMicrophonePresent()){
            getMicrophonePermission()
        }
        binding.btn.setOnClickListener {
            try {
                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC) // MIC
                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder?.setOutputFile(getRecordingFilePath())
                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                mediaRecorder?.prepare()
                mediaRecorder?.start()
                Toast.makeText(this, "Starting...", Toast.LENGTH_SHORT).show()
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }
        binding.stop.setOnClickListener {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
        }
        binding.play.setOnClickListener {
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(getRecordingFilePath())
                mediaPlayer?.prepare()
                mediaPlayer?.start()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }
    private fun isMicrophonePresent():Boolean{
        return this.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }

    private fun getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO),
                CODE_AUDIO)
        }
    }

    private fun getRecordingFilePath():String{
        val contextWrapper = ContextWrapper(applicationContext)
        val musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val file:File = File(musicDirectory,"testRecording"+".mp3")
        return file.path
    }
}