package org.d3if0043.mancashmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.d3if0043.mancashmobile.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityResetPasswordBinding
    lateinit var auth: FirebaseAuth
    private lateinit var emailReset: EditText
    private lateinit var sendEmailButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backToSignLink.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        setContentView(R.layout.activity_reset_password)

        emailReset = findViewById(R.id.enterYourEmailBtn)
        sendEmailButton = findViewById(R.id.sendEmailButtonReset)

        auth = FirebaseAuth.getInstance()

        sendEmailButton.setOnClickListener {
            val sPassword = emailReset.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener {
                    Toast.makeText(this, "Silahkan Cek Email Anda!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}