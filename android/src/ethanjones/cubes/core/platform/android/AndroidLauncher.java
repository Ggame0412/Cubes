package ethanjones.cubes.core.platform.android;

import ethanjones.cubes.core.platform.Compatibility;
import ethanjones.cubes.core.platform.Launcher;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class AndroidLauncher extends AndroidApplication implements Launcher {

  private static final int PERMISSION_REQUEST_CODE = 101;
  private boolean cubesInitialized = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (hasStoragePermission()) {
      initCubes();
    } else {
      requestPermissions(
          new String[]{
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.READ_EXTERNAL_STORAGE
          },
          PERMISSION_REQUEST_CODE
      );
    }
  }

  private boolean hasStoragePermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return true; // На Android 5.1 и ниже права выдаются при установке
    }
    return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        initCubes();
      } else {
        finish();
      }
    }
  }

  private void initCubes() {
    if (cubesInitialized) return;
    cubesInitialized = true;
    new AndroidCompatibility(this).startCubes();
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
  }

  @Override
  public void onBackPressed() {
    ((AndroidCompatibility) Compatibility.get()).back = true;
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    int action = event.getAction();
    int keyCode = event.getKeyCode();
    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
      if (action == KeyEvent.ACTION_DOWN) {
        ((AndroidCompatibility) Compatibility.get()).modifier = true;
      } else if (action == KeyEvent.ACTION_UP) {
        ((AndroidCompatibility) Compatibility.get()).modifier = false;
      }
    }
    return super.dispatchKeyEvent(event);
  }
}