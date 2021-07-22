package com.example.socialize.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialize.R
import com.example.socialize.UserModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_get_user_number.*
import kotlinx.android.synthetic.main.fragment_get_user_number.view.*
import java.util.concurrent.TimeUnit


class GetUserNumber : Fragment(R.layout.fragment_get_user_number) {

    private var number: String? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var code: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_get_user_number, container, false)


        view.btn_sendCode.setOnClickListener {
//            btn_sendCode.text = "Code Sent"
            if (checkNumber()) {
                val phoneNumber = view.countryCodePicker.selectedCountryCodeWithPlus + number
                sendCode(phoneNumber)


            }
        }


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException)
                    Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
                else if (e is FirebaseTooManyRequestsException)
                    Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
                else Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationCode: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                code = verificationCode
                activity!!.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, VerifiyNumber.newInstance(code!!))
                    .commit()


            }

        }
        return view
    }

    private fun sendCode(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            callbacks
        )
        Toast.makeText(context, "Code Sent", Toast.LENGTH_LONG).show()
        btn_sendCode.text = "Code Sent"
    }


    private fun checkNumber(): Boolean {
        number = requireView().enter_phone_number.text.toString().trim()
        if (number!!.isEmpty()) {
            requireView().enter_phone_number.error = "Field is required"
            return false
        } else if (number!!.length < 10) {
            requireView().enter_phone_number.error = "Number should be 10 in length"
            return false
        } else return true
    }
}

class VerifiyNumber : Fragment(R.layout.fragment_get_user_number) {
    private var code: String? = null
    private lateinit var pin: String
    private var firebaseAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            code = it.getString("Code")

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        val view = inflater.inflate(R.layout.fragment_get_user_number, container, false)

        view.btn_continue.setOnClickListener {
            if (checkPin()) {
                val credential = PhoneAuthProvider.getCredential(code!!, pin)
                signInUser(credential)
            }
        }
        return view
    }

    private fun signInUser(credential: PhoneAuthCredential) {
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val userModel = UserModel(
                    "", "", "", firebaseAuth!!.currentUser!!.phoneNumber!!, firebaseAuth!!.uid!!
                )

                databaseReference!!.child(firebaseAuth?.uid!!).setValue(userModel)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, GetUserData())
                    .commit()

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(code: String) =
            VerifiyNumber().apply {
                arguments = Bundle().apply {
                    putString("Code", code)
                }
            }
    }

    private fun checkPin(): Boolean {
        pin = requireView().enter_code.text.toString()
        if (pin.isEmpty()) {
            requireView().enter_code.error =
//                isEmpty
                "Filed is required"
            return false
        } else if (pin.length < 6) {
            requireView().enter_code.error = "Enter valid pin"
            return false
        } else return true
    }
}

