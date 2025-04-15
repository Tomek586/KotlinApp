package pl.wsei.pam.lab03

import android.widget.ImageButton

data class Tile(val button: ImageButton, val tileResource: Int, val deckResource: Int) {
    init {
        button.setImageResource(deckResource)
    }

    private var _revealed: Boolean = false
    var revealed: Boolean
        get() = _revealed
        set(value) {
            _revealed = value
            button.setImageResource(if (value) tileResource else deckResource)
        }
    var matched: Boolean = false

    fun removeOnClickListener() {
        button.setOnClickListener(null)
    }
}
