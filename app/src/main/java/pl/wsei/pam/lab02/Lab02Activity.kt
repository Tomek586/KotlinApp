package pl.wsei.pam.lab02

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab03.Lab03Activity

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab02)
    }

    fun onBoardSizeSelected(view: View) {
        val button = view as Button
        val tag: String? = button.tag as String?
        val tokens = tag?.split(" ")
        val rows = tokens?.get(0)?.toInt() ?: 3
        val columns = tokens?.get(1)?.toInt() ?: 3

        val intent = Intent(this, Lab03Activity::class.java)
        intent.putExtra("size", intArrayOf(rows, columns))
        startActivity(intent)
    }
}
