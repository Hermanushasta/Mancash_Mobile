package org.d3if0043.mancashmobile

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {
    private lateinit var addDescriptionEditText: EditText
    private lateinit var nominalEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var transactionsRef: DatabaseReference
    private lateinit var datePicker: EditText

    // Konstruktor tambahan dengan parameter description dan amount
    private data class Transaction(
        val description: String = "",
        val amount: Float = 0.0f
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        datePicker = view.findViewById(R.id.datePicker)
        datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        // Inisialisasi Firebase Database dan referensi ke child "transactions"
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.reference
        transactionsRef = databaseReference.child("transactions")

        // Inisialisasi view yang diperlukan
        addDescriptionEditText = view.findViewById(R.id.addDescriptionEditText)
        nominalEditText = view.findViewById(R.id.nominal)
        saveButton = view.findViewById(R.id.saveButton)

        // Set input filter untuk nominalEditText
        nominalEditText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            // Mengizinkan hanya angka dan desimal
            val regex = Regex("[0-9]+(\\.[0-9]{0,2})?")
            if (source.isEmpty() || regex.matches(source)) {
                null // Membiarkan input diterima
            } else {
                "" // Mengabaikan input yang tidak sesuai
            }
        })

        // Menambahkan listener untuk tombol save
        saveButton.setOnClickListener {
            val description = addDescriptionEditText.text.toString()
            val amountString = nominalEditText.text.toString()
            val amount = amountString.toFloat()

            // Membuat objek Transaction
            val transaction = Transaction(description, amount)

            // Menyimpan data ke Firebase Database
            transactionsRef.push().setValue(transaction)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            datePicker.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}
