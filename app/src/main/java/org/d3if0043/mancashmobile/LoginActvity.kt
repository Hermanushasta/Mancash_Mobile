package org.d3if0043.mancashmobile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.d3if0043.mancashmobile.databinding.ActivityLoginBinding

class LoginActvity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.signUpLink.setOnClickListener {
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEdtLogin.text.toString()
            val password = binding.passwordEdtLogin.text.toString()

            //validasi email
            if (email.isEmpty()) {
                binding.emailEdtLogin.error = "Email Harus Diisi"
                binding.emailEdtLogin.requestFocus()
                return@setOnClickListener
            }

            //validasi email tidak sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEdtLogin.error = "Email Tidak Valid"
                binding.emailEdtLogin.requestFocus()
                return@setOnClickListener
            }

            //validasi password
            if (password.isEmpty()) {
                binding.passwordEdtLogin.error = "Password Harus Diisi"
                binding.passwordEdtLogin.requestFocus()
                return@setOnClickListener
            }

            LoginFirebase(email, password)
        }
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Selamat Datang", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}