package com.dpoint.dpointsuser.view.module.membership

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Menu
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.preferences.AppPreferences
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_membership_card.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddMembershipCardActivity : BaseActivity() {

    var imgStr: String = ""
    var ext: String = ""
    override val layout: Int = R.layout.activity_add_membership_card
    private val viewModel by lazy { getVM<MemberShipCardViewModel>(this) }
    private var shop_id: Int = 0
    private var actionType = "add"
    private var menuModel: Menu? = null

    override fun init() {
        backBtn.setOnClickListener {
            onBackPressed()
        }
        checkPermission_WRITE_EXTERNAL_STORAGE(this)
        var membership_title: String = et_membership_title.text.toString()
        btnImage.setOnClickListener {
            selectImage(this)
        }

        nextBT.setOnClickListener {
            if (validate()) {
                viewModel.addMemberShipCardState(
                    UserPreferences.instance.getTokken(this)!!,
                    UserPreferences.instance.getUser(this)!!.id,
                    et_membership_title.text.toString(),
                    imgStr,
                    ext
                )

                showProgress(this)

            }
        }
        addObserver()
    }

    private fun selectImage(context: Context) {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
               requestCameraPermission()
            } else if (options[item] == "Choose from Gallery") {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 102)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }
    private val CAMERA_PERMISSIONS_REQUEST = 2

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                // Permission Denied
                Toast.makeText(
                    this,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSIONS_REQUEST
            )
        } else {

            openCamera()
        }
    }
    private val REQUEST_CODE = 10
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.dpoint.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_CODE)
                    }
                }
            }
        }
    }
    private fun addObserver() {

        viewModel.addMemberShipCardState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATAMEMBERSHIP", state.data?.message.toString())
                    onSuccess(state.data!!.message)
                    hideProgress()
                    finish()


                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

    }

    lateinit var progress: ProgressDialog
    override fun showProgress(context: Context) {

        progress =
            ProgressDialog.show(context, "Please wait...", "Processing Data...", false, false)

    }

    override fun hideProgress() {
        Log.e("initialized", "" + ::progress.isInitialized)
        when {
            (::progress.isInitialized) -> {
                progress.dismiss()
            }
        }
    }

    private fun validate(): Boolean {
        if (et_membership_title.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please give membership title.", Toast.LENGTH_SHORT).show()
            return false
        } else if (imgStr.isNullOrEmpty()) {
            if (actionType.equals("edit", true))
                return true
            Toast.makeText(this, "Please select image.", Toast.LENGTH_SHORT).show()
            return false
        } else
            return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 102 && resultCode == Activity.RESULT_OK && data != null) {
            var bitmap =
                MediaStore.Images.Media.getBitmap(
                    applicationContext.contentResolver,
                    data.data
                )
            btnImage.setImageBitmap(bitmap)
            val extension: String?
            val contentResolver = applicationContext.contentResolver
            val mimeTypeMap = MimeTypeMap.getSingleton()
            extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(data.data))
            val filePath = getRealPathFromURI(data.data, contentResolver)
            Log.e("PATH", filePath)
            //      bitmap = SiliCompressor.with(applicationContext).getCompressBitmap(filePath, true);
            val bytes = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
            imgStr = AppPreferences.instance.getStringImage(bitmap)
            ext = extension
            Log.e("EXTEN", imgStr)
        }else  if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            var file = File(currentPhotoPath);
            var bitmap =
                MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file))
            btnImage.setImageBitmap(bitmap)
            val bytes = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes)
            imgStr = AppPreferences.instance.getStringImage(bitmap)
            ext = ".jpg"
            Log.e("EXTEN", imgStr)
        }
    }


    fun getRealPathFromURI(uri: Uri?, contentResolver: ContentResolver): String {
        var result: String? = null
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = contentResolver.query(uri, proj, null, null, null)!!
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                var index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Url Not Found"
        }
        return result
    }

}
