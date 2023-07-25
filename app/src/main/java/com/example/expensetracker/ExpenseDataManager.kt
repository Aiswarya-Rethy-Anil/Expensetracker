package com.example.groupfcapstoneproject

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.nio.charset.Charset

object ExpenseDataManager {
    private const val FILE_NAME = "activity_expense.json"

    fun saveExpenses(context: Context, expenses: List<Expense>) {
        val file = getFile(context)
        val gson = Gson()
        val json = gson.toJson(expenses)
        file.writeText(json, Charset.defaultCharset())
    }

    fun loadExpenses(context: Context): List<Expense> {
        val file = getFile(context)
        if (!file.exists()) {
            return emptyList()
        }

        return try {
            val gson = Gson()
            val json = file.readText(Charset.defaultCharset())
            val type = object : TypeToken<List<Expense>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addExpense(context: Context, expense: Expense) {
        val expenses = loadExpenses(context).toMutableList()
        expenses.add(expense)
        saveExpenses(context, expenses)
    }

    fun updateExpense(context: Context, position: Int, updatedExpense: Expense) {
        val expenses = loadExpenses(context).toMutableList()
        if (position in 0 until expenses.size) {
            expenses[position] = updatedExpense
            saveExpenses(context, expenses)
        }
    }

    fun deleteExpense(context: Context, position: Int) {
        val expenses = loadExpenses(context).toMutableList()
        if (position in 0 until expenses.size) {
            expenses.removeAt(position)
            saveExpenses(context, expenses)
        }
    }

    private fun getFile(context: Context): File {
        return File(context.filesDir, FILE_NAME)
    }
}
