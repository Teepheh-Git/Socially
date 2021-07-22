package com.example.socialize.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.socialize.R
import com.example.socialize.Util.AppUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations

class SplashScreenActivity: AppCompatActivity() {


    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var appUtil: AppUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = FirebaseAuth.getInstance()
        appUtil = AppUtil()

//        splash.alpha = 0f
//        splash.animate().setDuration(3500).alpha(1f).withEndAction {
//            val i = Intent(this, MainActivity::class.java)
//            startActivity(i)
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            finish()
//        }


        Handler(Looper.getMainLooper()).postDelayed({
//            i changed Handler cos it is deprecated

            if (firebaseAuth!!.currentUser == null) {


                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                FirebaseInstallations.getInstance().getToken(true)
                    .addOnCompleteListener(OnCompleteListener {
                        if (it.isSuccessful) {
                            val token = it.result?.token
                            val databaseReference =
                                FirebaseDatabase.getInstance().getReference("Users")
                                    .child(appUtil.getUID()!!)

                            val map: MutableMap<String, Any> = HashMap()
                            map["token"] = token!!
                            databaseReference.updateChildren(map)
                        }
                    })
                startActivity(Intent(this, DashBoard::class.java))
                finish()
            }
        }, 3000)

    }



}