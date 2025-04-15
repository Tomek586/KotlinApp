package pl.wsei.pam.lab03

import MemoryBoardView
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoardModel: MemoryBoardView
    lateinit var completionPlayer: MediaPlayer
    lateinit var negativePlayer: MediaPlayer
    private var isSound = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val gridLayout = findViewById<GridLayout>(R.id.memory_grid)

        val size = intent.getIntArrayExtra("size") ?: intArrayOf(3, 2)
        val rows = size[0]
        val cols = size[1]
        gridLayout.columnCount = cols
        gridLayout.rowCount = rows

        val savedIcons = savedInstanceState?.getIntArray("game_icons")
        mBoardModel = MemoryBoardView(this, gridLayout, cols, rows, savedIcons)

        savedInstanceState?.getIntArray("game_state")?.let {
            mBoardModel.setState(it)
        }



        mBoardModel.setOnGameChangeListener { e ->
            runOnUiThread {
                when (e.state) {
                    GameStates.Matching -> {
                        e.tiles.forEach { it.revealed = true }
                    }

                    GameStates.Match -> {
                        if (isSound) completionPlayer.start()
                        e.tiles.forEach { it.revealed = true }

                        var finishedCount = 0
                        e.tiles.forEach { tile ->
                            mBoardModel.animatePairedButton(tile.button, Runnable {
                                finishedCount++
                                if (finishedCount == e.tiles.size) {
                                    mBoardModel.setInteractionEnabled(true)
                                }
                            })
                        }
                    }

                    GameStates.NoMatch -> {
                        if (isSound) negativePlayer.start()
                        e.tiles.forEach { it.revealed = true }

                        var finishedCount = 0
                        e.tiles.forEach { tile ->
                            mBoardModel.animateWrongPair(tile.button, Runnable {
                                tile.revealed = false
                                finishedCount++
                                if (finishedCount == e.tiles.size) {
                                    mBoardModel.setInteractionEnabled(true)
                                }
                            })
                        }
                    }

                    GameStates.Finished -> {
                        if (isSound) completionPlayer.start()
                        e.tiles.forEach { it.revealed = true }

                        var finishedCount = 0
                        e.tiles.forEach { tile ->
                            mBoardModel.animatePairedButton(tile.button, Runnable {
                                finishedCount++
                                if (finishedCount == e.tiles.size) {
                                    Toast.makeText(this@Lab03Activity, "Game finished", Toast.LENGTH_SHORT).show()
                                    mBoardModel.setInteractionEnabled(false)
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("game_state", mBoardModel.getState())
        outState.putIntArray("game_icons", mBoardModel.getIconsLayout())
    }
    override fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePlayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }

    override fun onPause() {
        super.onPause()
        completionPlayer.release()
        negativePlayer.release()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.board_activity_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.board_activity_sound -> {
                if (isSound) {
                    Toast.makeText(this, "Sound turned off", Toast.LENGTH_SHORT).show()
                    item.setIcon(R.drawable.baseline_alarm_24)
                    isSound = false
                } else {
                    Toast.makeText(this, "Sound turned on", Toast.LENGTH_SHORT).show()
                    item.setIcon(R.drawable.baseline_alarm_off_24)
                    isSound = true
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
