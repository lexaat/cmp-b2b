package platform

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//object NotificationPermissionRequester {
//
//    private const val REQUEST_CODE = 1001
//
//    fun requestPermissionIfNeeded(activity: Activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val granted = ContextCompat.checkSelfPermission(
//                activity,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//
//            if (!granted) {
//                ActivityCompat.requestPermissions(
//                    activity,
//                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                    REQUEST_CODE
//                )
//            }
//        }
//    }
//}