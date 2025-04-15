import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.GridLayout
import android.widget.ImageButton
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab03.GameStates
import pl.wsei.pam.lab03.MemoryGameEvent
import pl.wsei.pam.lab03.MemoryGameLogic
import pl.wsei.pam.lab03.Tile
import java.util.*
import java.util.Stack
import kotlin.concurrent.schedule

class MemoryBoardView(
    private val activity: Activity,
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int,
    private val savedIconList: IntArray? = null
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val icons: List<Int> = listOf(
        R.drawable.baseline_adb_24,
        R.drawable.baseline_battery_full_24,
        R.drawable.baseline_airplanemode_active_24,
        R.drawable.baseline_rocket_launch_24,
        R.drawable.baseline_add_shopping_cart_24,
        R.drawable.baseline_access_alarms_24,
        R.drawable.baseline_accessibility_24,
        R.drawable.baseline_assist_walker_24,
        R.drawable.baseline_blender_24,
        R.drawable.baseline_biotech_24,
        R.drawable.baseline_bedroom_baby_24,
        R.drawable.baseline_bakery_dining_24,
        R.drawable.baseline_attach_money_24,
        R.drawable.baseline_audio_file_24,
        R.drawable.baseline_attribution_24,
        R.drawable.baseline_bike_scooter_24,
        R.drawable.baseline_bed_24,
        R.drawable.baseline_av_timer_24,
    )

    private val deckResource: Int = R.drawable.baseline_back_hand_24
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = {}
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)
    private var isInteractionEnabled = true

    init {
        val numberOfTiles = cols * rows
        val numberOfPairs = numberOfTiles / 2

        val selectedIcons = icons.shuffled().take(numberOfPairs)
        val allIcons: MutableList<Int> = savedIconList?.toMutableList()
            ?: (selectedIcons + selectedIcons).shuffled().toMutableList()

        if (numberOfTiles % 2 != 0 && savedIconList == null) {
            allIcons.add(deckResource)
        }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val button = ImageButton(gridLayout.context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = GridLayout.LayoutParams.WRAP_CONTENT
                        columnSpec = GridLayout.spec(j, 1f)
                        rowSpec = GridLayout.spec(i, 1f)
                    }
                    tag = "$i-$j"
                }
                gridLayout.addView(button)
                addTile(button, allIcons.removeAt(0))
            }
        }
    }

    private fun onClickTile(v: View) {
        if (!isInteractionEnabled) return

        val tile = tiles[v.tag]
        if (tile == null || matchedPair.contains(tile) || tile.matched) return

        matchedPair.push(tile)

        if (matchedPair.size == 1) {
            val matchResult = logic.process { matchedPair[0].tileResource }
            onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))
            return
        }

        isInteractionEnabled = false

        matchedPair.forEach { it.revealed = true }

        val matchResult = logic.process { matchedPair[1].tileResource }
        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))

        if (matchResult == GameStates.Finished) {
            Timer().schedule(1000) {
                activity.runOnUiThread {
                    matchedPair.clear()
                    setInteractionEnabled(false)
                    onGameChangeStateListener(MemoryGameEvent(emptyList(), GameStates.Finished))
                }
            }
        } else if (matchResult != GameStates.Matching) {
            matchedPair.clear()
        }
    }

    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }

    fun getState(): IntArray {
        return tiles.values.map {
            when {
                it.matched -> -2
                it.revealed -> it.tileResource
                else -> -1
            }
        }.toIntArray()
    }

    fun setState(state: IntArray) {
        tiles.values.forEachIndexed { index, tile ->
            when (val s = state[index]) {
                -2 -> {
                    tile.revealed = true
                    tile.matched = true
                    tile.button.alpha = 0f
                }
                -1 -> {
                    tile.revealed = false
                    tile.matched = false
                    tile.button.alpha = 1f
                }
                else -> {
                    tile.revealed = true
                    tile.matched = false
                    tile.button.alpha = 1f
                }
            }
        }
    }

    fun getIconsLayout(): IntArray {
        return tiles.values.map { it.tileResource }.toIntArray()
    }

    fun setInteractionEnabled(enabled: Boolean) {
        isInteractionEnabled = enabled
    }

    fun animatePairedButton(button: ImageButton, action: Runnable) {
        val set = AnimatorSet()
        val random = Random()
        button.pivotX = random.nextFloat() * 200f
        button.pivotY = random.nextFloat() * 200f

        val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
        val scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 4f)
        val scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 4f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)

        set.startDelay = 200
        set.duration = 800
        set.interpolator = DecelerateInterpolator()
        set.playTogether(rotation, scaleX, scaleY, fade)

        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                button.scaleX = 1f
                button.scaleY = 1f
                button.alpha = 0f
                tiles[button.tag]?.matched = true
                action.run()
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        set.start()
    }

    fun animateWrongPair(button: ImageButton, action: Runnable) {
        val set = AnimatorSet()

        val shake1 = ObjectAnimator.ofFloat(button, "rotation", 0f, -15f)
        val shake2 = ObjectAnimator.ofFloat(button, "rotation", -15f, 15f)
        val shake3 = ObjectAnimator.ofFloat(button, "rotation", 15f, -10f)
        val shake4 = ObjectAnimator.ofFloat(button, "rotation", -10f, 10f)
        val shake5 = ObjectAnimator.ofFloat(button, "rotation", 10f, 0f)

        set.playSequentially(shake1, shake2, shake3, shake4, shake5)
        set.duration = 400
        set.interpolator = DecelerateInterpolator()

        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                action.run()
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        set.start()
    }
}
