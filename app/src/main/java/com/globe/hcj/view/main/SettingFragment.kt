package com.globe.hcj.view.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.globe.hcj.R
import com.globe.hcj.data.firestore.AlbumPhoto
import com.globe.hcj.data.firestore.User
import com.globe.hcj.preference.TraySharedPreference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by baeminsu on 10/10/2018.
 */

class SettingFragment() : Fragment() {

    private var photoUri: Uri? = null
    private var currentPhotoPath: String? = null//실제 사진 파일 경로
    lateinit var mImageCaptureName: String//이미지 이름
    private val GALLERY_CODE = 1112
    private val CAMERA_CODE = 1111

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false);
        val db = FirebaseFirestore.getInstance();



        with(view) {

            //프로필 사진
            Glide.with(context)
                    .load(FirebaseAuth.getInstance().currentUser!!.photoUrl)
                    .into(view.settingProfileIv)
            //그외정보
            db.collection("user").document(FirebaseAuth.getInstance().currentUser!!.email!!)
                    .get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = it.result.toObject(User::class.java)
                            view.settingNameTv.text = user!!.name
                            view.settingEmailTv.text = user.email
                            user.pairRefName!!.get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val pair = it.result.toObject(User::class.java)
                                            view.settingPairNameTv.text = pair!!.name
                                            view.settingPairEmailTv.text = pair.email
                                        }
                                    }
                        }
                    }

            view.settingProfileIv.setOnClickListener {
                selectGallery()
            }

        }

        return view
    }


    private fun selectPhoto() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(context!!.getPackageManager()) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {

                }

                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(context!!, context!!.packageName, photoFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(intent, CAMERA_CODE)
                }
            }

        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val dir = File(Environment.getExternalStorageDirectory().toString() + "/path/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        mImageCaptureName = "$timeStamp.png"

        val storageDir = File(Environment.getExternalStorageDirectory().absoluteFile.toString() + "/path/"

                + mImageCaptureName)
        currentPhotoPath = storageDir.absolutePath

        return storageDir

    }


    private fun getPictureForPhoto() {
        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(currentPhotoPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val exifOrientation: Int
        val exifDegree: Int

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            exifDegree = exifOrientationToDegrees(exifOrientation)
        } else {
            exifDegree = 0
        }

    }

    private fun selectGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            when (requestCode) {
                GALLERY_CODE -> sendPicture(data!!.data) //갤러리에서 가져오기
                CAMERA_CODE -> getPictureForPhoto() //카메라에서 가져오기

                else -> {
                }
            }

        }
    }


    private fun sendPicture(imgUri: Uri?) {

        val imagePath = getRealPathFromURI(imgUri) // path 경로
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(imagePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //        assert exif != null;
        //        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        //        int exifDegree = exifOrientationToDegrees(exifOrientation);

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference("profile_url/" + FirebaseAuth.getInstance().currentUser!!.email)

        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환


        val uploadTask = storageRef.putFile(Uri.fromFile(File(imagePath)))

        uploadTask.addOnFailureListener {
        }.addOnSuccessListener {
            storageRef.downloadUrl.addOnCompleteListener { task ->
                val uri = task.result


                val db = FirebaseFirestore.getInstance()
                db.collection("user").document(FirebaseAuth.getInstance().currentUser!!.email!!)
                        .update("profileURL", uri.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                var user = FirebaseAuth.getInstance().currentUser
                                var profileUpdate = UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build()
                                user!!.updateProfile(profileUpdate).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(context, "프로필 이미지 변경 완료", Toast.LENGTH_SHORT).show()
                                        //프로필 사진
                                        Glide.with(context!!)
                                                .load(FirebaseAuth.getInstance().currentUser!!.photoUrl)
                                                .into(this.view!!.settingProfileIv)
                                    }
                                }
                            }
                        }
            }
        }


    }


    private fun exifOrientationToDegrees(exifOrientation: Int): Int {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }

    private fun rotate(src: Bitmap, degree: Float): Bitmap {

        // Matrix 객체 생성
        val matrix = Matrix()
        // 회전 각도 셋팅
        matrix.postRotate(degree)
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.width,
                src.height, matrix, true)
    }

    private fun getRealPathFromURI(contentUri: Uri?): String {
        var column_index = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context!!.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(column_index)
    }

}