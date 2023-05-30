package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var visor: TextView? = null
    var op: Operation? = null

    enum class Operation(val text: String) {
        DIVISION("/"), MULTIPLICATION("*"), ADDITION("+"),
        SUBTRACTION("-")
    }

    private fun getOperation(op: String): Operation {
        return when (op) {
            "/" -> Operation.DIVISION
            "*" -> Operation.MULTIPLICATION
            "+" -> Operation.ADDITION
            else -> Operation.SUBTRACTION
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        visor = findViewById(R.id.view)
    }

    fun onDigitClick(view: View) {
        val digitButton: Button = view as Button
        val visorText = visor?.text
        if (digitButton.text.equals(".") &&
            ((visorText?.contains(".")!! && op == null)
            ||  (visorText.count {it == '.'} == 2 && op != null)
            || visorText.isBlank()))
                return
        val newVisorText = visor?.text.toString() + digitButton.text.toString()
        visor?.text = newVisorText
    }

    fun onClearVisorCLick(view: View) {
        visor?.text = ""
        op = null
    }

    fun onOpClick(view: View) {
        val opButton = view as Button
        if (op == null && visor?.text?.isNotBlank()!!) {
            val newVisorText = visor?.text.toString() + opButton.text.toString()
            visor?.text = newVisorText
            op = getOperation(opButton.text.toString())
        } else if (opButton.text == "-" && visor?.text?.isBlank()!!)
            visor?.text = Operation.SUBTRACTION.text
    }

    fun onEqualClick(view: View) {
        if (op != null) {
            val result: Double
            val elements: List<String>?
            val strOperation = visor?.text?.toString()
            elements = strOperation?.split(op!!.text)
            result = if (op == Operation.SUBTRACTION && strOperation?.count { it == '-' } == 2) {
                doOperation(elements?.get(1)!!.toDouble() * -1, elements[2].toDouble(), op!!)
            } else {
                if (elements?.size != 2 || elements[1].isBlank())
                    return
                if (elements[1].toDouble() == 0.0 && op == Operation.DIVISION) {
                    Toast.makeText(this, "Erro: impossivel fazer divisao por zero", Toast.LENGTH_SHORT)
                        .show()
                    onClearVisorCLick(view)
                    return
                }
                doOperation(elements[0].toDouble(), elements[1].toDouble(), op!!)
            }
            var newValue = result.toString()
            while (newValue.length > 1 && newValue.substring(newValue.length - 2,
                    newValue.length) == ".0"
            ) {
                newValue = newValue.removeSuffix(".0")
            }
            visor?.text = newValue
            op = null
        }
    }

    fun doOperation(x: Double, y: Double, operation: Operation): Double {
        return when (op) {
            Operation.ADDITION -> x + y
            Operation.SUBTRACTION -> x - y
            Operation.MULTIPLICATION -> x * y
            Operation.DIVISION -> x / y
            else -> {
                0.0
            }
        }
    }
}