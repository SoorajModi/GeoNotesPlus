package de.hauke_stieler.geonotes;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatSupportable;

public class BaseActivity extends AppCompatActivity  implements SkinCompatSupportable {

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    @Override
    public void applySkin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(SkinCompatResources.getInstance().getColor(R.color.primary));
        }
    }
}
