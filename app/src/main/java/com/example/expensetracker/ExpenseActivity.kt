package com.example.groupfcapstoneproject

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.groupfcapstoneproject.Expense

class ExpenseActivity : AppCompatActivity() {
    private lateinit var etExpenseTitle: EditText
    private lateinit var etExpenseAmount: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSave: Button

    private var expense: Expense? = null
    private var position: Int = -1

    companion object {
        const val EXTRA_EXPENSE = "expense"
        const val EXTRA_POSITION = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        etExpenseTitle = findViewById(R.id.etExpenseTitle)
        etExpenseAmount = findViewById(R.id.etExpenseAmount)
        spinnerType = findViewById(R.id.spinnerType)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSave = findViewById(R.id.btnSave)

        // Initialize the spinners
        val typeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.type_options,
            android.R.layout.simple_spinner_item
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = typeAdapter

        val categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.income_category_options,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        // Get the Expense data if it exists (for edit mode)
        expense = intent.getParcelableExtra(EXTRA_EXPENSE)
        position = intent.getIntExtra(EXTRA_POSITION, -1)

        expense?.let {
            etExpenseTitle.setText(it.title)
            etExpenseAmount.setText(it.amount.toString())

            // Set selected type and category in spinners
            val typePosition = typeAdapter.getPosition(it.type)
            if (typePosition >= 0) {
                spinnerType.setSelection(typePosition)
            }

            val categoryPosition = categoryAdapter.getPosition(it.category)
            if (categoryPosition >= 0) {
                spinnerCategory.setSelection(categoryPosition)
            }
        }

        // Add listeners
        etExpenseTitle.addTextChangedListener(textWatcher)
        etExpenseAmount.addTextChangedListener(textWatcher)
        spinnerType.onItemSelectedListener = spinnerItemSelectedListener

        btnSave.setOnClickListener {
            if (position == -1) {
                saveExpense()
            } else {
                updateExpense()
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not used
        }

        override fun afterTextChanged(s: Editable?) {
            val title = etExpenseTitle.text.toString().trim()
            val amount = etExpenseAmount.text.toString().trim()
            btnSave.isEnabled = !TextUtils.isEmpty(title) && !TextUtils.isEmpty(amount)
        }
    }

    private val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            // Handle spinner item selection here
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Not used
        }
    }

    private fun saveExpense() {
        val title = etExpenseTitle.text.toString().trim()
        val amount = etExpenseAmount.text.toString().toDouble()
        val type = spinnerType.selectedItem.toString()
        val category = spinnerCategory.selectedItem.toString()

        val newExpense = Expense(title, amount, type, category)
        val intent = Intent().apply {
            putExtra(EXTRA_EXPENSE, newExpense)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun updateExpense() {
        val title = etExpenseTitle.text.toString().trim()
        val amount = etExpenseAmount.text.toString().toDouble()
        val type = spinnerType.selectedItem.toString()
        val category = spinnerCategory.selectedItem.toString()

        val updatedExpense = Expense(title, amount, type, category)
        val intent = Intent().apply {
            putExtra(EXTRA_EXPENSE, updatedExpense)
            putExtra(EXTRA_POSITION, position)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
