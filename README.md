# 코틀린으로 카메라 사용해보기

## 권한 설정

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-feature
        android:name="android.hardware.camera2"
        android:required="true" />
```

```kotlin
var permission_list = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)
```

```kotlin
fun checkPermission(){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
        return
    }
    for(permission: String in permission_list){
        var chk = checkCallingOrSelfPermission(permission)
        if(chk == PackageManager.PERMISSION_DENIED){
            requestPermissions(permission_list, 0);
            break
        }
    }
}
```



## 카메라로 찍은 이미지 가져오기

```kotlin
val REQUEST_IMAGE_CAPTURE = 1
```

```kotlin
private fun dispatchTakePictureIntent() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        takePictureIntent.resolveActivity(packageManager)?.also {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
}
```

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        val imageBitmap = data?.extras?.get("data") as Bitmap
        imageView.setImageBitmap(imageBitmap)
    } 
    ...
}
```



## 갤러리에서 이미지 가져오기

```kotlin
val OPEN_GALLERY = 2
```

```kotlin
private fun openGallery() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.setType("image/*")
    startActivityForResult(intent, OPEN_GALLERY)
}
```

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    ...
    else if (requestCode == OPEN_GALLERY && resultCode == Activity.RESULT_OK) {
        val currentImageUrl : Uri? = data?.data
        try{
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
            imageView.setImageBitmap(bitmap)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
```

