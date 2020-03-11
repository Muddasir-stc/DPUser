package com.dpoint.dpointsuser.view.module.membership

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Menu
import com.dpoints.dpointsmerchant.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.shop.Menu
import com.dpoints.dpointsmerchant.datasource.remote.shop.MenuModel
import com.dpoints.dpointsmerchant.preferences.AppPreferences
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.android.synthetic.main.activity_add_membership_card.*
import kotlinx.android.synthetic.main.activity_add_menu.*
import kotlinx.android.synthetic.main.fragment_first_form_page.*
import kotlinx.android.synthetic.main.fragment_first_form_page.btnImage
import kotlinx.android.synthetic.main.fragment_first_form_page.nextBT
import java.io.ByteArrayOutputStream

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
        if (intent.getStringExtra("actionType") != null) {
            actionType = intent.getStringExtra("actionType")
            menuModel = intent.getSerializableExtra("menu") as Menu
            setData(menuModel!!)
        } else shop_id = Integer.valueOf(intent.getStringExtra("data"))
        btnImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 102)
        }
        nextBT.setOnClickListener {
            if (validate()) {
                showProgress(this)
                if (actionType.equals("add", true))
                    viewModel.addMenu(
                        UserPreferences.instance.getTokken(this)!!,
                        Menu("", "", 0, imgStr, shop_id, etMenuTitle.text.toString(), ext, "")
                    ) else if (actionType.equals("edit", true))
                    viewModel.editMenu(
                        UserPreferences.instance.getTokken(this)!!,
                        Menu(
                            "",
                            "",
                            menuModel!!.id,
                            imgStr,
                            menuModel!!.shop_id,
                            etMenuTitle.text.toString(),
                            ext,
                            ""
                        )
                    )
            }
        }
        addObserver()
    }

    private fun setData(model: Menu) {
        shop_id = model.shop_id
        Glide.with(this).load(model!!.image).into(btnImage)
        ext = model.image!!.substring(model!!.image!!.length - 4, model!!.image!!.length)
        imgStr = ""
        etMenuTitle!!.setText(model.title)
    }

    private fun addObserver() {
        viewModel.addMenuState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer //showProgress(this)
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    hideProgress()
                    finish()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
        viewModel.editMenuState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer //showProgress(this)
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    hideProgress()
                    finish()
                }
                is NetworkState.Error -> {
                    onError(state.message)
                    hideProgress()
                }

                is NetworkState.Failure -> {
                    onFailure(getString(R.string.request_error))
                    hideProgress()
                }
                else -> {
                    onFailure(getString(R.string.connection_error))
                    hideProgress()
                }
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
        if (etMenuTitle.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please give menu title.", Toast.LENGTH_SHORT).show()
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
                MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), data.data)
            btnImage.setImageBitmap(bitmap)
            val extension: String?
            val contentResolver = applicationContext.contentResolver
            val mimeTypeMap = MimeTypeMap.getSingleton()
            extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(data.data))
            val filePath = getRealPathFromURI(bitmap, contentResolver)
            Log.e("PATH", filePath)
            bitmap = SiliCompressor.with(applicationContext).getCompressBitmap(filePath, true);
            imgStr = AppPreferences.instance.getStringImage(bitmap)

            ext = extension
            Log.e("EXTEN", imgStr)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_GET_CONTENT
                    intent.type = "image/*"
                    startActivityForResult(intent, 102)
                } else {
                    showPermissionDialog("External storage permission is necessary to access photos for upload", applicationContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        }
    }

    fun getRealPathFromURI(inImage: Bitmap, contentResolver: ContentResolver):String {
        val bytes = ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "shop_image", null);
        val cursor: Cursor = contentResolver.query(Uri.parse(path), null, null, null, null)!!
        cursor.moveToFirst();
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}
