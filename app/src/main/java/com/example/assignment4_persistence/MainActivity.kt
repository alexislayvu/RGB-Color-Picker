// Name: Alexis Vu
// CWID: 888697067
// Email: alexislayvu@csu.fullerton.edu

package com.example.assignment4_persistence

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt

const val RED_KEY = "redProgress"
const val GREEN_KEY = "greenProgress"
const val BLUE_KEY = "blueProgress"

class MainActivity : AppCompatActivity() {
    // textViewColor + reset button
    private lateinit var textViewColor: TextView
    private lateinit var buttonReset: Button

    // red color controllers
    private lateinit var switchCompatRed: SwitchCompat
    private lateinit var seekBarRed: SeekBar
    private lateinit var editTextRed: EditText

    // green color controllers
    private lateinit var switchCompatGreen: SwitchCompat
    private lateinit var seekBarGreen: SeekBar
    private lateinit var editTextGreen: EditText

    // blue color controllers
    private lateinit var switchCompatBlue: SwitchCompat
    private lateinit var seekBarBlue: SeekBar
    private lateinit var editTextBlue: EditText

    // starting RGB values RGB(0, 0, 0)
    var red = 0
    var green = 0
    var blue = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // textViewColor + reset button
        textViewColor = findViewById(R.id.textViewColor)
        buttonReset = findViewById(R.id.buttonReset)

        // red color controllers
        switchCompatRed = findViewById(R.id.switchRed)
        seekBarRed = findViewById(R.id.seekBarRed)
        editTextRed = findViewById(R.id.editTextRed)

        // green color controllers
        switchCompatGreen = findViewById(R.id.switchGreen)
        seekBarGreen = findViewById(R.id.seekBarGreen)
        editTextGreen = findViewById(R.id.editTextGreen)

        // blue color controllers
        switchCompatBlue = findViewById(R.id.switchBlue)
        seekBarBlue = findViewById(R.id.seekBarBlue)
        editTextBlue = findViewById(R.id.editTextBlue)

        // set max seekBar values to 255
        seekBarRed.max = 255
        seekBarGreen.max = 255
        seekBarBlue.max = 255

        // if the editText box is empty (since the seekBar progress is 0).. display 0.000
        if (editTextRed.text.toString() == "") {
            editTextRed.setText("0.000")
        }
        if (editTextGreen.text.toString() == "") {
            editTextGreen.setText("0.000")
        }
        if (editTextBlue.text.toString() == "") {
            editTextBlue.setText("0.000")
        }

        // only accept user inputs from 0 - 1
        editTextRed.filters = arrayOf(InputFilterMinMax(0f, 1f))
        editTextGreen.filters = arrayOf(InputFilterMinMax(0f, 1f))
        editTextBlue.filters = arrayOf(InputFilterMinMax(0f, 1f))

        // create sharedPreferences instance called "save" and retrieve switch state boolean values
        val sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        val switchStateRed = sharedPreferences.getBoolean("switchCompatRed", false)
        val switchStateGreen = sharedPreferences.getBoolean("switchCompatGreen", false)
        val switchStateBlue = sharedPreferences.getBoolean("switchCompatBlue", false)

        // set the state of the switches to the saved state retrieved from sharedPreferences
        switchCompatRed.isChecked = switchStateRed
        switchCompatGreen.isChecked = switchStateGreen
        switchCompatBlue.isChecked = switchStateBlue

        // if the switch is not checked, save and load the values for persistence
        if (!switchCompatRed.isChecked) {
            seekBarRed.isEnabled = false
            editTextRed.isEnabled = false

            viewModel.loadRedProgress()
            seekBarRed.progress = viewModel.getRedProgress()
            editTextRed.setText(String.format("%.3f", (viewModel.getRedProgress() / 255.0)))
                .toString()
            editTextRed.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.disabled_seekbar_red
                )
            )
        } else if (switchCompatRed.isChecked) {
            editTextRed.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.enabled_switch_red
                )
            )
        }

        // if the switch is not checked, save and load the values for persistence
        if (!switchCompatGreen.isChecked) {
            seekBarGreen.isEnabled = false
            editTextGreen.isEnabled = false

            viewModel.loadGreenProgress()
            seekBarGreen.progress = viewModel.getGreenProgress()
            editTextGreen.setText(String.format("%.3f", (viewModel.getGreenProgress() / 255.0)))
                .toString()
            editTextGreen.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.disabled_seekbar_green
                )
            )
        } else if (switchCompatGreen.isChecked) {
            editTextGreen.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.enabled_switch_green
                )
            )
        }

        // if the switch is not checked, save and load the values for persistence
        if (!switchCompatBlue.isChecked) {
            seekBarBlue.isEnabled = false
            editTextBlue.isEnabled = false

            viewModel.loadBlueProgress()
            seekBarBlue.progress = viewModel.getBlueProgress()
            editTextBlue.setText(String.format("%.3f", (viewModel.getBlueProgress() / 255.0)))
                .toString()
            editTextBlue.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.disabled_seekbar_blue
                )
            )
        } else if (switchCompatBlue.isChecked) {
            editTextBlue.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.enabled_switch_blue
                )
            )
        }

        buttonReset.setOnClickListener {
            // set textViewColor to black
            textViewColor.setBackgroundColor(Color.argb(255, 0, 0, 0))

            switchCompatRed.isChecked = false
            switchCompatGreen.isChecked = false
            switchCompatBlue.isChecked = false

            // set all seekBar progresses to 0
            seekBarRed.progress = 0
            seekBarGreen.progress = 0
            seekBarBlue.progress = 0

            // set all editTexts to display 0.000
            editTextRed.setText(String.format("%.3f", (0 / 255.0)))
            editTextGreen.setText(String.format("%.3f", (0 / 255.0)))
            editTextBlue.setText(String.format("%.3f", (0 / 255.0)))
        }

        seekBarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                red = p1
                viewModel.setRedBar(red)

                // update textViewColor with red value
                textViewColor.setBackgroundColor(Color.argb(255, red, green, blue))

                // sync seekbar with editText
                editTextRed.setText(String.format("%.3f", (red / 255.0)))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        editTextRed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                editTextRed.setSelection(editTextRed.length())
                try {
                    seekBarRed.progress = (p0.toString().toFloat() * 255).roundToInt()
                } catch (_: Exception) {
                }
            }
        })

        switchCompatRed.setOnCheckedChangeListener { _, isChecked ->
            // if the user turns off the switch...
            if (!isChecked) {
                // remove red from textViewColor
                red = 0
                textViewColor.setBackgroundColor(Color.argb(255, 0, green, blue))

                // disabled seekBarRed and editTextRed
                seekBarRed.isEnabled = false
                editTextRed.isEnabled = false

                // save state of switch to be off
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("switchCompatRed", false)
                editor.apply()
                switchCompatRed.isChecked = false

                // make these attributes faded
                editTextRed.setTextColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_switch_edittext
                    )
                )
                editTextRed.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_seekbar_red
                    )
                )
                seekBarRed.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.white
                    )
                )
                seekBarRed.progressDrawable.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_seekbar_red
                    )
                )
            }

            // if the user turns on the switch...
            if (isChecked) {
                // add red to textViewColor
                red = seekBarRed.progress
                textViewColor.setBackgroundColor(Color.argb(255, red, green, blue))

                // enabled seekBarRed and editTextRed
                seekBarRed.isEnabled = true
                editTextRed.isEnabled = true

                // save state of switch to be on
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("switchCompatRed", true)
                editor.apply()
                switchCompatRed.isChecked = true

                // make these attributes brighter
                editTextRed.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                editTextRed.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_red
                    )
                )
                seekBarRed.thumb.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_red
                    )
                )
                seekBarRed.progressDrawable.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_red
                    )
                )
            }
        }


        seekBarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                green = p1
                viewModel.setGreenBar(green)

                // update textViewColor with green value
                textViewColor.setBackgroundColor(Color.argb(255, red, green, blue))

                // sync seekbar with editText
                editTextGreen.setText(String.format("%.3f", (green / 255.0)))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        editTextGreen.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                editTextGreen.setSelection(editTextGreen.length())
                try {
                    seekBarGreen.progress = (p0.toString().toFloat() * 255).roundToInt()
                } catch (_: Exception) {
                }
            }
        })

        switchCompatGreen.setOnCheckedChangeListener { _, isChecked ->
            // if the user turns off the switch...
            if (!isChecked) {
                // remove green from textViewColor
                green = 0
                textViewColor.setBackgroundColor(Color.argb(255, red, 0, blue))

                // disable seekBarGreen and editTextGreen
                seekBarGreen.isEnabled = false
                editTextGreen.isEnabled = false

                // save state of switch to be off
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("switchCompatGreen", false)
                editor.apply()
                switchCompatGreen.isChecked = false

                // make these attributes faded
                editTextGreen.setTextColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_switch_edittext
                    )
                )
                editTextGreen.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_seekbar_green
                    )
                )
                seekBarGreen.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.white
                    )
                )
                seekBarGreen.progressDrawable.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_seekbar_green
                    )
                )
            }

            // if the user turns on the switch...
            if (isChecked) {
                // add green to textViewColor
                green = seekBarGreen.progress
                textViewColor.setBackgroundColor(Color.argb(255, red, green, blue))

                // enable seekBarGreen and editTextGreen
                seekBarGreen.isEnabled = true
                editTextGreen.isEnabled = true

                // save state of switch to be on
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("switchCompatGreen", true)
                editor.apply()
                switchCompatGreen.isChecked = true

                // make these attributes brighter
                editTextGreen.setTextColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.black
                    )
                )
                editTextGreen.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_green
                    )
                )
                seekBarGreen.thumb.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_green
                    )
                )
                seekBarGreen.progressDrawable.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_green
                    )
                )
            }
        }


        seekBarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                blue = p1
                viewModel.setBlueBar(blue)

                // update textViewColor with blue
                textViewColor.setBackgroundColor(Color.argb(255, red, green, blue))

                // sync seekbar with editText
                editTextBlue.setText(String.format("%.3f", (blue / 255.0)))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        editTextBlue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                editTextBlue.setSelection(editTextBlue.length())

                try {
                    seekBarBlue.progress = (p0.toString().toFloat() * 255).roundToInt()
                } catch (_: Exception) {
                }
            }
        })

        switchCompatBlue.setOnCheckedChangeListener { _, isChecked ->
            // if the user turns off the switch...
            if (!isChecked) {
                // remove blue from textViewColor
                blue = 0
                textViewColor.setBackgroundColor(Color.argb(255, red, green, 0))

                // disable seekBarBlue and editTextBlue
                seekBarBlue.isEnabled = false
                editTextBlue.isEnabled = false

                // save state of switch to be off
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("switchCompatBlue", false)
                editor.apply()
                switchCompatBlue.isChecked = false

                // make these attributes faded
                editTextBlue.setTextColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_switch_edittext
                    )
                )
                editTextBlue.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_seekbar_blue
                    )
                )
                seekBarBlue.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.white
                    )
                )
                seekBarBlue.progressDrawable.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.disabled_seekbar_blue
                    )
                )
            }

            // if the user turns on the switch...
            if (isChecked) {
                // add blue to textViewColor
                blue = seekBarBlue.progress
                textViewColor.setBackgroundColor(Color.argb(255, red, green, blue))

                // enable seekBarBlue and editTextBlue
                seekBarBlue.isEnabled = true
                editTextBlue.isEnabled = true

                // save state of switch to be on
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("switchCompatBlue", true)
                editor.apply()
                switchCompatBlue.isChecked = true

                // make these attributes brighter
                editTextBlue.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                editTextBlue.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_blue
                    )
                )
                seekBarBlue.thumb.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_blue
                    )
                )
                seekBarBlue.progressDrawable.setTint(
                    ContextCompat.getColor(
                        applicationContext, R.color.enabled_switch_blue
                    )
                )
            }
        }

        // load red progress
        viewModel.loadRedProgress()
        seekBarRed.progress = viewModel.getRedProgress()

        // load green progress
        viewModel.loadGreenProgress()
        seekBarGreen.progress = viewModel.getGreenProgress()

        // load blue progress
        viewModel.loadBlueProgress()
        seekBarBlue.progress = viewModel.getBlueProgress()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(RED_KEY, viewModel.getRedProgress())
        outState.putInt(GREEN_KEY, viewModel.getGreenProgress())
        outState.putInt(BLUE_KEY, viewModel.getBlueProgress())

    }

    private val viewModel: ViewModel by lazy {
        PreferencesRepository.initialize(this)
        ViewModelProvider(this)[ViewModel::class.java]
    }
}

// max input value for editText
class InputFilterMinMax(private val min: Float, private val max: Float) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toFloat()
            if (isInRange(input)) {
                return null
            }
        } catch (nfe: NumberFormatException) {
            // do nothing
        }
        return ""
    }

    private fun isInRange(value: Float): Boolean {
        return value in min..max
    }
}