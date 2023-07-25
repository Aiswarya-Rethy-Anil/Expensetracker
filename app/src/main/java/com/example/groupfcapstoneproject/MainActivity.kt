//package com.example.groupfcapstoneproject
//
//import android.app.Activity
//import android.content.DialogInterface
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.AppCompatImageButton
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.gson.Gson
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var btnAddExpense: AppCompatImageButton
//    private lateinit var expenseList: MutableList<Expense>
//    private lateinit var expenseAdapter: ExpenseAdapter
//
//    companion object {
//        private const val ADD_EXPENSE_REQUEST_CODE = 1
//        private const val EDIT_EXPENSE_REQUEST_CODE = 2
//        private const val DELETE_EXPENSE_REQUEST_CODE = 3
//        private const val EXTRA_EXPENSE = "expense"
//        private const val EXTRA_POSITION = "position"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        recyclerView = findViewById(R.id.recyclerView)
//        btnAddExpense = findViewById(R.id.btnAddExpense)
//
//        // Initialize the expense list and adapter
//        expenseList = ExpenseDataManager.loadExpenses(this).toMutableList()
//        expenseAdapter = ExpenseAdapter(expenseList)
//
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter = expenseAdapter
//        }
//
//        btnAddExpense.setOnClickListener {
//            startActivityForResult(Intent(this, ExpenseActivity::class.java), ADD_EXPENSE_REQUEST_CODE)
//        }
//    }
//
//    private fun addExpense(expense: Expense) {
//        expenseList.add(expense)
//        expenseAdapter.notifyDataSetChanged()
//        saveExpenses()
//    }
//
//    private fun editExpense(position: Int, expense: Expense) {
//        expenseList[position] = expense
//        expenseAdapter.notifyDataSetChanged()
//        saveExpenses()
//    }
//
//    private fun deleteExpense(position: Int) {
//        expenseList.removeAt(position)
//        expenseAdapter.notifyDataSetChanged()
//        saveExpenses()
//    }
//
//    private fun saveExpenses() {
//        ExpenseDataManager.saveExpenses(this, expenseList)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                ADD_EXPENSE_REQUEST_CODE -> {
//                    data?.getParcelableExtra<Expense>(EXTRA_EXPENSE)?.let { expense ->
//                        addExpense(expense)
//                    }
//                }
//                EDIT_EXPENSE_REQUEST_CODE -> {
//                    data?.getParcelableExtra<Expense>(EXTRA_EXPENSE)?.let { expense ->
//                        val position = data.getIntExtra(EXTRA_POSITION, -1)
//                        if (position != -1) {
//                            editExpense(position, expense)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    inner class ExpenseAdapter(private val expenses: List<Expense>) :
//        RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_expense, parent, false)
//            return ExpenseViewHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
//            val expense = expenses[position]
//            holder.bind(expense)
//        }
//
//        override fun getItemCount(): Int {
//            return expenses.size
//        }
//
//        inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            private val tvExpenseTitle: TextView = itemView.findViewById(R.id.tvExpenseTitle)
//            private val tvExpenseAmount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
//            private val btnDeleteExpense: AppCompatImageButton = itemView.findViewById(R.id.btnDeleteExpense)
//            private val btnEditExpense: AppCompatImageButton = itemView.findViewById(R.id.btnEditExpense)
//
//            fun bind(expense: Expense) {
//                tvExpenseTitle.text = expense.title
//                tvExpenseAmount.text = String.format("%.2f", expense.amount)
//
//                btnDeleteExpense.setOnClickListener {
//                    val position = adapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        showDeleteConfirmationDialog(position)
//                    }
//                }
//
//                btnEditExpense.setOnClickListener {
//                    val position = adapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        val expense = expenseList[position]
//                        val intent = Intent(this@MainActivity, ExpenseActivity::class.java).apply {
//                            putExtra(EXTRA_EXPENSE, expense)
//                            putExtra(EXTRA_POSITION, position)
//                        }
//                        startActivityForResult(intent, EDIT_EXPENSE_REQUEST_CODE)
//                    }
//                }
//            }
//        }
//
//        private fun showDeleteConfirmationDialog(position: Int) {
//            val builder = AlertDialog.Builder(this@MainActivity)
//            builder.setTitle("Confirm Deletion")
//                .setMessage("Are you sure you want to delete this expense?")
//                .setPositiveButton("Confirm") { dialog, _ ->
//                    deleteExpense(position)
//                    dialog.dismiss()
//                }
//                .setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.dismiss()
//                }
//                .create()
//                .show()
//        }
//    }
//
//}



package com.example.groupfcapstoneproject

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddExpense: AppCompatImageButton
    private lateinit var etSearch: EditText
    private lateinit var expenseList: MutableList<Expense>
    private lateinit var expenseAdapter: ExpenseAdapter

    private var filteredExpenseList: MutableList<Expense> = mutableListOf()

    companion object {
        private const val ADD_EXPENSE_REQUEST_CODE = 1
        private const val EDIT_EXPENSE_REQUEST_CODE = 2
        private const val DELETE_EXPENSE_REQUEST_CODE = 3
        private const val EXTRA_EXPENSE = "expense"
        private const val EXTRA_POSITION = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        etSearch = findViewById(R.id.etSearch)

        // Initialize the expense list and adapter
        expenseList = ExpenseDataManager.loadExpenses(this).toMutableList()
        filteredExpenseList.addAll(expenseList)
        expenseAdapter = ExpenseAdapter(filteredExpenseList)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
        }

        btnAddExpense.setOnClickListener {
            startActivityForResult(Intent(this, ExpenseActivity::class.java), ADD_EXPENSE_REQUEST_CODE)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterExpenses(s.toString())
            }
        })
    }

    private fun filterExpenses(searchText: String) {
        filteredExpenseList.clear()
        if (searchText.isBlank()) {
            filteredExpenseList.addAll(expenseList)
        } else {
            val searchQuery = searchText.lowercase()
            expenseList.forEach { expense ->
                if (expense.title.lowercase().contains(searchQuery)) {
                    filteredExpenseList.add(expense)
                }
            }
        }
        expenseAdapter.notifyDataSetChanged()
    }

    private fun addExpense(expense: Expense) {
        expenseList.add(expense)
        filteredExpenseList.add(expense)
        expenseAdapter.notifyDataSetChanged()
        saveExpenses()
    }

    private fun editExpense(position: Int, expense: Expense) {
        expenseList[position] = expense
        filteredExpenseList[position] = expense
        expenseAdapter.notifyDataSetChanged()
        saveExpenses()
    }

    private fun deleteExpense(position: Int) {
        expenseList.removeAt(position)
        filteredExpenseList.removeAt(position)
        expenseAdapter.notifyDataSetChanged()
        saveExpenses()
    }

    private fun saveExpenses() {
        ExpenseDataManager.saveExpenses(this, expenseList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_EXPENSE_REQUEST_CODE -> {
                    data?.getParcelableExtra<Expense>(EXTRA_EXPENSE)?.let { expense ->
                        addExpense(expense)
                    }
                }
                EDIT_EXPENSE_REQUEST_CODE -> {
                    data?.getParcelableExtra<Expense>(EXTRA_EXPENSE)?.let { expense ->
                        val position = data.getIntExtra(EXTRA_POSITION, -1)
                        if (position != -1) {
                            editExpense(position, expense)
                        }
                    }
                }
            }
        }
    }

    inner class ExpenseAdapter(private val expenses: List<Expense>) :
        RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_expense, parent, false)
            return ExpenseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
            val expense = expenses[position]
            holder.bind(expense)
        }

        override fun getItemCount(): Int {
            return expenses.size
        }

        inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvExpenseTitle: TextView = itemView.findViewById(R.id.tvExpenseTitle)
            private val tvExpenseAmount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
            private val btnDeleteExpense: AppCompatImageButton = itemView.findViewById(R.id.btnDeleteExpense)
            private val btnEditExpense: AppCompatImageButton = itemView.findViewById(R.id.btnEditExpense)

            fun bind(expense: Expense) {
                tvExpenseTitle.text = expense.title
                tvExpenseAmount.text = String.format("%.2f", expense.amount)

                btnDeleteExpense.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        showDeleteConfirmationDialog(position)
                    }
                }

                btnEditExpense.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val expense = filteredExpenseList[position]
                        val intent = Intent(this@MainActivity, ExpenseActivity::class.java).apply {
                            putExtra(EXTRA_EXPENSE, expense)
                            putExtra(EXTRA_POSITION, position)
                        }
                        startActivityForResult(intent, EDIT_EXPENSE_REQUEST_CODE)
                    }
                }
            }
        }

        private fun showDeleteConfirmationDialog(position: Int) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Confirm") { dialog, _ ->
                    deleteExpense(position)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }
}
