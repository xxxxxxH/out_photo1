package net.basicmodel

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.activity_font.*
import net.adapter.FontAdapter
import net.entity.FontModel
import net.utils.Share
import net.widget.DrawableStickerPixel
import java.util.*

class FontPixelActivity : AppCompatActivity() {
    private var fontAdapter: FontAdapter? = null
    private val list: ArrayList<FontModel> = ArrayList<FontModel>()
    private val font_array = arrayOf(
        "1",
        "6",
        "ardina_script",
        "beyondwonderland",
        "C",
        "coventry_garden_nf",
        "font3",
        "font6",
        "font10",
        "font16",
        "font20",
        "g",
        "h",
        "h2",
        "h3",
        "h6",
        "h7",
        "h8",
        "h15",
        "h18",
        "h19",
        "h20",
        "m",
        "o",
        "saman",
        "variane",
        "youmurdererbb"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font)
        rv_font.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )
        rv_font.layoutParams.height = Share.screenHeight - toolbar_top.height - ll_font_color.height

        et_text.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    v!!.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
        }

        et_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et_text.text.toString() == "") {
                    et_text.setText("")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        initView()
    }

    private fun initView() {
        for (i in font_array.indices) {
            val spinnerModel = FontModel()
            spinnerModel.font_name = font_array[i]
            list.add(spinnerModel)
        }


        fontAdapter = FontAdapter(this@FontPixelActivity, list)
        rv_font.adapter = fontAdapter

        fontAdapter!!.eventListener = object : FontAdapter.EventListener {
            override fun onItemViewClicked(position: Int) {
                Share.FONT_EFFECT = font_array[position].toLowerCase()

                val face = Typeface.createFromAsset(
                    this@FontPixelActivity.assets,
                    font_array[position].toLowerCase() + ".ttf"
                )
                et_text.typeface = face
            }

            override fun onDeleteMember(position: Int) {
            }

        }
    }

    private fun onclick() {
        iv_color.setOnClickListener {
            val builder = ColorPickerDialog.Builder(
                this@FontPixelActivity,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK
            )
                .setTitle("Choose Color")
                .setPreferenceName("Test")
                .setPositiveButton(
                    getString(R.string.confirm),
                    ColorEnvelopeListener { envelope, fromUser ->
                        et_text.setTextColor(Color.parseColor("#" + envelope.hexCode))
                        Share.COLOR = Color.parseColor("#" + envelope.hexCode)
                        et_text.setTextColor(Color.parseColor("#" + envelope.hexCode))
                        Share.COLOR =
                            Color.parseColor("#" + envelope.hexCode)
                    })
                .setNegativeButton(
                    "cancel"
                ) { dialogInterface, i -> dialogInterface.dismiss() }

            builder.show()
        }
        iv_close.setOnClickListener {
            onBackPressed()
        }
        iv_done.setOnClickListener {
            val str = et_text.text.toString()

            val b2: Bitmap = createBitmapFromLayoutWithText(
                applicationContext,
                et_text.text.toString(),
                et_text.currentTextColor,
                0,
                et_text.typeface
            )
            val d: Drawable = BitmapDrawable(
                resources, b2
            )

            if (str != "") {
                Share.FONT_FLAG = true
                Share.FONT_TEXT = et_text.text.toString()
                finish()
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
            } else {
                Toast.makeText(applicationContext, getString(R.string.text_null), Toast.LENGTH_LONG)
                    .show()
            }

            val sticker = DrawableStickerPixel(d)

            val face = Typeface.createFromAsset(assets, Share.FONT_EFFECT.toString() + ".ttf")

            if (Share.COLOR == 0) {
                Share.COLOR = resources.getColor(R.color.colorPrimary)
            }

            Share.TEXT_DRAWABLE1 = sticker
        }

    }

    fun createBitmapFromLayoutWithText(
        context: Context,
        s: String,
        color: Int,
        i: Int,
        face: Typeface?
    ): Bitmap {
        val mInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = mInflater.inflate(R.layout.row_bitmap, null)
        val tv = view.findViewById<View>(R.id.tv_custom_text1) as TextView
        var j = 0
        while (j < s.length) {
            if (s.length >= 40) {
                if (j <= s.length - 40) {
                    if (j == 0) {
                        val m = s.substring(0, 40)
                        tv.text = m
                    } else {
                        tv.append("\n")
                        val l = s.substring(j, j + 40)
                        tv.append(l)
                    }
                } else {
                    tv.append("\n")
                    val l = s.substring(j, s.length)
                    tv.append(l)
                }
            } else {
                tv.text = s
            }
            j += 40
        }
        tv.setTextColor(color)
        tv.typeface = face
        view.layoutParams = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        view.draw(c)
        return bitmap
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }

}