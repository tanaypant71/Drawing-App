package com.example.kidsapp

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())

        //setting the linear layout
        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.paint_colors)

        mImageButtonCurrentPaint = linearLayoutPaintColors[2] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_selected)
        )

        val brush: ImageButton = findViewById(R.id.brush)
        brush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

    }

    //this method will take care of the size of the brush
    //here we are adding a dialog
    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.brush_size)
        brushDialog.setTitle("Brush size: ")

        //here we are setting the brush size
        val smallBtn:ImageButton = brushDialog.findViewById(R.id.small_brush)
        smallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }

        val mediumBtn:ImageButton = brushDialog.findViewById(R.id.medium_brush)
        mediumBtn.setOnClickListener {
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        val largeBtn:ImageButton = brushDialog.findViewById(R.id.large_brush)
        largeBtn.setOnClickListener {
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

    fun paintClicked(view: View){
            if (view !== mImageButtonCurrentPaint){
                val imageButton = view as ImageButton
                val colorTag = imageButton.tag.toString()
                drawingView?.setColor(colorTag)

                //in this we will be selecting the desired color
                imageButton.setImageDrawable(
                    ContextCompat.getDrawable(this,R.drawable.pallet_selected)
                )

                //here we making the old color unpressed
                mImageButtonCurrentPaint?.setImageDrawable(
                    ContextCompat.getDrawable(this,R.drawable.pallet_normal)
                )

                //should hold the current color
                mImageButtonCurrentPaint = view
            }
         }

}