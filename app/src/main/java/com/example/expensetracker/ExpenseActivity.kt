package com.example.expensetracker

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
import com.example.expensetracker.Expense

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

        expense = intent.getParcelableExtra(EXTRA_EXPENSE)
        position = intent.getIntExtra(EXTRA_POSITION, -1)

        expense?.let {
            etExpenseTitle.setText(it.title)
            etExpenseAmount.setText(it.amount.toString())
        }

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
            val selectedType = spinnerType.selectedItem.toString()
            val selectedCategory = spinnerCategory.selectedItem.toString()

            val isTitleValid = !TextUtils.isEmpty(title)
            val isAmountValid = amount.toDoubleOrNull() != null
            val isTypeSelected = selectedType == "Income" || selectedType == "Expense"

            btnSave.isEnabled = isTitleValid && isAmountValid && isTypeSelected
        }
    }

    private val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val selectedType = parent.getItemAtPosition(position).toString()

            // Update category options based on selected type
            val categoryOptions = when (selectedType) {
                "Income" -> resources.getStringArray(R.array.income_category_options)
                "Expense" -> resources.getStringArray(R.array.expense_category_options)
                else -> arrayOf("--- Select Category ---")
            }

            val adapter = ArrayAdapter(
                this@ExpenseActivity,
                android.R.layout.simple_spinner_item,
                categoryOptions
            )
            spinnerCategory.adapter = adapter

            val title = etExpenseTitle.text.toString().trim()
            val amount = etExpenseAmount.text.toString().trim()
            val selectedCategory = spinnerCategory.selectedItem.toString()

            val isTitleValid = !TextUtils.isEmpty(title)
            val isAmountValid = amount.toDoubleOrNull() != null
            val isTypeSelected = selectedType == "Income" || selectedType == "Expense"
            val isCategorySelected = selectedCategory != "--- Select Category ---"

            btnSave.isEnabled = isTitleValid && isAmountValid && isTypeSelected && isCategorySelected
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Not used
        }
    }

    private fun saveExpense() {
        val title = etExpenseTitle.text.toString().trim()
        val amount = etExpenseAmount.text.toString().toDouble()
        val selectedType = spinnerType.selectedItem.toString()
        val selectedCategory = spinnerCategory.selectedItem.toString()

        val expense = Expense(title, amount, selectedType, selectedCategory)

        // Save expense to file using ExpenseDataManager
        ExpenseDataManager.addExpense(this, expense)

        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_EXPENSE, expense)
        resultIntent.putExtra(EXTRA_POSITION, position)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun updateExpense() {
        val title = etExpenseTitle.text.toString().trim()
        val amount = etExpenseAmount.text.toString().toDouble()
        val selectedType = spinnerType.selectedItem.toString()
        val selectedCategory = spinnerCategory.selectedItem.toString()

        val expense = Expense(title, amount, selectedType, selectedCategory)

        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_EXPENSE, expense)
        resultIntent.putExtra(EXTRA_POSITION, position)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
