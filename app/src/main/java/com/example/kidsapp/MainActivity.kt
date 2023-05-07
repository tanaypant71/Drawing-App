package com.example.kidsapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null

    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == RESULT_OK && result.data != null){
                val imageBackGround: ImageView = findViewById(R.id.im_bg)

                imageBackGround.setImageURI(result.data?.data)
            }
        }

    @SuppressLint("SuspiciousIndentation")
    val requestPermission: ActivityResultLauncher<Array<String>> =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    val permissionName = it.key
                    val isGranted = it.value
                    if (isGranted) {
                        //check the permission name and perform the specific operation
                        Toast.makeText(
                            this@MainActivity,
                            "Permission granted for storage files",
                            Toast.LENGTH_SHORT
                        ).show()

                        val pickIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            openGalleryLauncher.launch(pickIntent)
                    }
                    else{
                        if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                            Toast.makeText(
                                this@MainActivity,
                                "Permission not granted for storage files",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

    @SuppressLint("MissingInflatedId")
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

        val myGallery: ImageButton = findViewById(R.id.gallery)
        myGallery.setOnClickListener {
            requestStoragePermission()
        }

        val undoIt: ImageButton = findViewById(R.id.undo)
        undoIt.setOnClickListener {
            drawingView?.onClickUndo()
        }

        val redoIt: ImageButton = findViewById(R.id.redoIt)
        redoIt.setOnClickListener {
            drawingView?.onClickRedo()
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


    private fun requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            //call the rationale dialog to tell the user why they need to allow permission request
            showRationaleDialog("Kids Drawing App","Kids Drawing App " +
                    "needs to Access Your External Storage")
        }else{
            requestPermission.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            //TODO - writing external storage permission
            ))
        }
    }


    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}