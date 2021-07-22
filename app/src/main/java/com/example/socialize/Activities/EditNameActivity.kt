package com.example.socialize.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socialize.Util.AppUtil
import com.example.socialize.databinding.ActivityEditNameBinding

class EditNameActivity : AppCompatActivity() {

    private lateinit var editNameBinding: ActivityEditNameBinding
    private lateinit var Name: String
    private lateinit var appUtil: AppUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editNameBinding = ActivityEditNameBinding.inflate(layoutInflater)
        setContentView(editNameBinding.root)

        appUtil= AppUtil()


//        if (intent.hasExtra("name")) {
//            val name = intent.getStringExtra("name")
//            if (name!!.contains(" ")) {
//                val split = name.split(" ")
//                editNameBinding.edtFName.setText(split[0])
//                editNameBinding.edtLName.setText(split[1])
//
//            }
//        }


        if (intent.hasExtra("name")) {
            val name = intent.getStringExtra("name")
            editNameBinding.editProfileName.text
        }

        editNameBinding.btnEditName.setOnClickListener {
            if (areFieldEmpty()) {
                val intent = Intent()

                intent.putExtra("name", "$Name ")
                setResult(100, intent)
                finish()
            }
        }

    }

//    private fun areFieldEmpty(): Boolean {
//        fName = editNameBinding.edtFName.text.toString()
//        lName = editNameBinding.edtLName.text.toString()
//        var required: Boolean = false
//        var view: View? = null
//
//        if (fName.isEmpty()) {
//            editNameBinding.edtFName.error = "Field is required"
//            required = true
//            view = editNameBinding.edtFName
//
//        } else if (lName.isEmpty()) {
//            editNameBinding.edtLName.error = "Field is required"
//            required = true
//            view = editNameBinding.edtLName
//
//        }
//
//        return if (required) {
//            view?.requestFocus()
//            false
//        } else true
//    }

    private fun areFieldEmpty(): Boolean {
        Name = editNameBinding.editProfileName.text.toString()
        var required: Boolean = false
        var view: View? = null

        if (Name.isEmpty()) {
            editNameBinding.editProfileName.error = "Field is required"
            required = true
            view = editNameBinding.editProfileName

//        } else if (lName.isEmpty()) {
//            editNameBinding.edtLName.error = "Field is required"
//            required = true
//            view = editNameBinding.edtLName

        }

        return if (required) {
            view?.requestFocus()
            false
        } else true
    }


    override fun onPause() {
        super.onPause()
        appUtil.updateOnlineStatus("offline")
    }

    override fun onResume() {
        super.onResume()
        appUtil.updateOnlineStatus("online")
    }
}