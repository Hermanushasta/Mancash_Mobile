package org.d3if0043.mancashmobile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.d3if0043.mancashmobile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActvity::class.java )
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()

            //validasi email
            if (email.isEmpty()) {
                binding.emailRegister.error = "Email Harus Diisi"
                binding.emailRegister.requestFocus()
                return@setOnClickListener
            }

            //validasi email tidak sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailRegister.error = "Email Tidak Valid"
                binding.emailRegister.requestFocus()
                return@setOnClickListener
            }

            //validasi password
            if (password.isEmpty()) {
                binding.passwordRegister.error = "Password Harus Diisi"
                binding.passwordRegister.requestFocus()
                return@setOnClickListener
            }

            //validasi paanjang password
            if (password.length < 6) {
                binding.passwordRegister.error = "Password Minimal 6 Karakter"
                binding.passwordRegister.requestFocus()
                return@setOnClickListener
            }

            RegisterFirebase(email, password)
        }
    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActvity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}