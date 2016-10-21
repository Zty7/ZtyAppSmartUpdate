package demo.zty.smartupdate;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zty.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 777;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setText("CurVersion: " + AppUtil.get(MainActivity.this).getVersion());
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行增量升级
                AppUtil.get(MainActivity.this).doBspatchThread.start();
                mBtn.setClickable(false);
                mBtn.setText("restart to try again");
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> dangerousPermissions = new ArrayList<String>();
        // 可以整个权限组，也可以单个权限，建议每个都单独申请
        dangerousPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        dangerousPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermissions(dangerousPermissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PERMISSION_REQUEST_CODE == requestCode) {
            setOnRequestPermissionsResultListener(new onRequestPermissionsResultListener() {
                @Override
                public void onSuccessful(String permission) {
                    mBtn.setClickable(true);
                }

                @Override
                public void onFailure(String permission) {
                    mBtn.setClickable(false);
                }
            }, permissions, grantResults);
        }
    }
}
