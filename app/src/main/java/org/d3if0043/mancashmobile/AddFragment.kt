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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import android.widget.RadioButton
import android.widget.RadioGroup

class AddFragment : Fragment() {
    private lateinit var addDescriptionEditText: EditText
    private lateinit var nominalEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var transactionsCollection: CollectionReference
    private lateinit var datePicker: EditText

    private lateinit var categoryRadioGroup: RadioGroup
    private lateinit var selectedCategory: String

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

        // Inisialisasi Firestore Database dan referensi ke collection "transactions"
        val db = FirebaseFirestore.getInstance()
        transactionsCollection = db.collection("transactions")

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

        categoryRadioGroup = view.findViewById(R.id.radioGroup)
        categoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)
            selectedCategory = radioButton.text.toString()
        }

        // Menambahkan listener untuk tombol save
        saveButton.setOnClickListener {
            val description = addDescriptionEditText.text.toString()
            val amountString = nominalEditText.text.toString()
            val amount = amountString.toFloat()

            // Membuat objek Transaction
            val transaction = Transaction(description, amount, )

            // Menyimpan data ke Firestore Database
            transactionsCollection.add(transaction)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(activity, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
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
