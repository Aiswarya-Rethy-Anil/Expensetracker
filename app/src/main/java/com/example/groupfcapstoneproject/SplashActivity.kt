package com.example.groupfcapstoneproject
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import androidx.appcompat.app.AppCompatActivity
//
//class SplashActivity : AppCompatActivity() {
//
//    private val SPLASH_DELAY: Long = 9000 // Splash screen delay in milliseconds
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//        // Delay the start of the MainActivity
//        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, SPLASH_DELAY)
//    }
//}


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000 // Splash screen delay in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay the start of the MainActivity
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}
