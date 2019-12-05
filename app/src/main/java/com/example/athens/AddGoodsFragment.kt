package com.example.athens


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.athens.main.StationActivity
import kotlinx.android.synthetic.main.fragment_add_goods.*
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.content.Intent
import com.bumptech.glide.Glide
import android.content.ContentResolver
import android.content.Context
import android.icu.text.IDNA
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.example.athens.api.*
import kotlinx.android.synthetic.main.fragment_add_goods.goods_image
import okhttp3.MediaType


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddGoodsFragment : Fragment() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
    private var uri : Uri? = null
    private var path: String? = null
    private val infoFragment = InfoFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_goods, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val manager = (activity as StationActivity).supportFragmentManager

        //開啟相機權限
        cameraPermission()

        //取相簿圖片
        goods_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 2)
        }

        //確認上船貨物
        addgoods_confirm.setOnClickListener {
            addGoods(path!!)
        }

        btn_back.setOnClickListener {
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.framelayout_station, infoFragment).commit()
        }

    }

    //取得照片uri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2){
            if (null != data){
                uri = data.data
                Glide.with(goods_image.context).load(uri).into(goods_image)  //顯示圖片
                getAbsolutePath(this.context!!, uri)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    (activity as StationActivity).finish()
                }
                return
            }
        }
    }

    //轉uri為真實路徑
    fun getAbsolutePath(context: Context, uri: Uri?): String?{
        if (uri == null) return ""
        else {
            val resolver = context.contentResolver
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = resolver.query(uri, projection, null, null, null)
            cursor!!.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            println("===========path=$index")
            path = cursor.getString(index)
            cursor.close()
            println("===========path=$path")
        }
        return path
    }

    fun addGoods(file_path: String){
        if (path == null) return

        API.apiInterface.addGoods(AddGoodsRequest(ed_goods_name.text.toString(), ed_goods_describe.text.toString(),
            ed_goods_fee.text.toString().toInt(), ed_goods_destination.text.toString(),
            ed_goods_location.text.toString(), ed_goods_weight.text.toString().toInt())).enqueue(object: Callback<AddGoodsResponse>{
            override fun onFailure(call: Call<AddGoodsResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<AddGoodsResponse>, response: Response<AddGoodsResponse>) {
                if (response.code() == 200){
                    val responsebody = response.body()
                    val good_id = responsebody!!.data.id
                    println("===========good_id=$good_id")

                    uploadImage(file_path, good_id)
                }
            }
        })
    }

    //上傳圖片
    fun uploadImage(file_path: String, good_id: Int){
        if (path == null) return

        val file = File(file_path)
        val fileReqBody: RequestBody =  file.asRequestBody("image/*".toMediaType())
        val part_file = MultipartBody.Part.createFormData("photo", file.name, fileReqBody)
        val part_goodId = MultipartBody.Part.createFormData("good_id", good_id.toString())

        API.apiInterface.uploadImage(part_file, part_goodId).enqueue(object: Callback<UploadImageResponse>{
            override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<UploadImageResponse>, response: Response<UploadImageResponse>) {
                if (response.code() == 200){
                    Toast.makeText(context, "新增成功", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    //相機權限
    fun cameraPermission(){
        if (ContextCompat.checkSelfPermission(this.context!!,  Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {  //判斷是否未取得權限(!=)
            if (ActivityCompat.shouldShowRequestPermissionRationale((activity as StationActivity), Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this.context!!)
                    .setMessage("我需要相機才能送貨，給我權限吧？")
                    .setPositiveButton("OK") { _, _ ->
                        //跳出視窗，嘗試向使用者取得權限
                        ActivityCompat.requestPermissions((activity as StationActivity),
                            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    .setNegativeButton("No") { _, _ -> (activity as StationActivity).finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions((activity as StationActivity),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }
    }

}



