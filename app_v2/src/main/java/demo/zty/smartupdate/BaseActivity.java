package demo.zty.smartupdate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zty on 2016/10/21.
 */

public class BaseActivity extends Activity {
    private final String TAG = this.getClass().getSimpleName().replace("Activity", "Aty");

    /**
     * 单个权限检查
     *
     * @param permission
     * @return
     */
    public boolean isPermissionNeed2Apply(String permission) {
        return ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED;
    }

    /**
     * 排查未授权的权限
     *
     * @param permissions
     * @return
     */
    public List<String> findDeniedPermissions(String... permissions) {
        List<String> deniedPermissions = new ArrayList<String>();
        for (String permission : permissions) {
            if (isPermissionNeed2Apply(permission)) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 申请敏感权限
     *
     * @param permissions
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public void requestPermissions(List<String> permissions, int requestCode) {
        if (permissions.size() > 0) {
            List<String> deniedPermissions = findDeniedPermissions(permissions.toArray(new String[permissions.size()]));
            if (deniedPermissions.size() > 0) {
                requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                Log.i(TAG, "no permission is denied");
            }
        } else {
            Log.i(TAG, "permissions.size() =< 0");
        }
    }

    /**
     * 申请权限结果处理
     *
     * @param listener
     * @param permissions
     * @param grantResults
     */
    public void setOnRequestPermissionsResultListener(onRequestPermissionsResultListener listener,
                                                      String[] permissions, int[] grantResults) {
        if (listener != null) {
            if (permissions.length > 0 && grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        listener.onSuccessful(permissions[i]);
                    } else {
                        listener.onFailure(permissions[i]);
                    }
                }
            } else {
                Log.i(TAG, "no request result");
            }
        } else {
            throw new NullPointerException("onRequestPermissionsResultListener is null");
        }
    }

    public interface onRequestPermissionsResultListener {
        void onSuccessful(String permission);

        void onFailure(String permission);
    }
}
