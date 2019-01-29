package ca.warp7.android.scouting.v5

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import ca.warp7.android.scouting.R
import ca.warp7.android.scouting.SettingsActivity
import ca.warp7.android.scouting.v5.entry.Alliance
import ca.warp7.android.scouting.v5.entry.Board


class V5MainActivity : AppCompatActivity() {

    private lateinit var boardTextView: TextView
    private lateinit var scoutTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v5_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = "Humber College Event"
        boardTextView = findViewById(R.id.board)
        scoutTextView = findViewById(R.id.scout_name)

        val context = this

        boardTextView.setOnClickListener {
            android.app.AlertDialog.Builder(this)
                .setTitle("Select board")
                .setItems(R.array.board_choices_v5) { _, which ->
                    Board.values()[which].also {
                        boardTextView.setTextColor(
                            ContextCompat.getColor(
                                context, when (it.alliance) {
                                    Alliance.Red -> R.color.colorRed
                                    Alliance.Blue -> R.color.colorBlue
                                }
                            )
                        )
                        boardTextView.text = it.name
                    }
                }
                .show()
        }
        boardTextView.text = kotlin.run { "R1" }
        boardTextView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))

        scoutTextView.setOnClickListener {
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val layout = LinearLayout(this)
            layout.addView(input)
            layout.setPadding(10, 0, 10, 0)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Enter Name")
                .setMessage("Format: First-Name Space Last-Initial")
                .setView(layout)
                .setPositiveButton("OK") { _, _ -> }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .create()

            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.show()

            val ok = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            ok.setOnClickListener {
                scoutTextView.text = input.text.toString()
            }

            ok.isEnabled = validateName(input.text.toString())

            input.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    ok.isEnabled = validateName(input.text.toString())
                }
            })
        }
    }

    private fun validateName(str: String): Boolean {
        val name = str.trim()
        if (name.isEmpty()) return false
        val split = name.split(" ")
        return split.size == 2 && split[0][0].isUpperCase() && split[1].length == 1 && split[1][0].isUpperCase()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.v5_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.itemId?.let {
            when (it) {
                R.id.menu_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.menu_new_entry -> {
                    startActivity(Intent(this, V5ScoutingActivity::class.java))
                    true
                }
                else -> false
            }
        } ?: false
    }
}