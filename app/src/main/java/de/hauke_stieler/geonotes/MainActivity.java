package de.hauke_stieler.geonotes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

import de.hauke_stieler.geonotes.map.Map;
import de.hauke_stieler.geonotes.map.TouchDownListener;

/**
 * Activity class that main page of the app
 */
public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private Map map;
    private SharedPreferences preferences;
    public static final int PICK_IMAGE = 1;


    /**
     * Create Main Page
     *
     * @param savedInstanceState - instance of the app
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createHamburgerMenu(toolbar);

        // Set HTML text of copyright label
        ((TextView) findViewById(R.id.copyright)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.copyright)).setText(Html.fromHtml("© <a href=\"https://openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));

        final Context context = getApplicationContext();

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        createMap(context);

        preferences = getSharedPreferences(getString(R.string.pref_file), MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(this::preferenceChanged);

        loadPreferences();
    }

    /**
     * Create Map for main page
     *
     * @param context - application context
     */
    private void createMap(Context context) {
        // Keep device on
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "geonotes:wakelock");
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);

        Drawable locationIcon = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_location, null);
        Drawable selectedIcon = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_note_selected, null);
        Drawable normalIcon = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_note, null);

        MapView mapView = findViewById(R.id.map);
        map = new Map(context, mapView, wakeLock, locationIcon, normalIcon, selectedIcon);

        addMapListener();
    }

    /**
     * Load user preferences
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        for (String key : sharedPreferences.getAll().keySet()) {
            preferenceChanged(sharedPreferences, key);
        }

        float lat = preferences.getFloat(getString(R.string.pref_last_location_lat), 0f);
        float lon = preferences.getFloat(getString(R.string.pref_last_location_lon), 0f);
        float zoom = preferences.getFloat(getString(R.string.pref_last_location_zoom), 2);
        map.setLocation(lat, lon, zoom);
    }


    /**
     * Update user preference upon change
     *
     * @param pref - user preferences to be updated
     * @param key  - user preferences key
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void preferenceChanged(SharedPreferences pref, String key) {
        if ("zoom".equals(key)) {
            boolean showZoomButtons = pref.getBoolean(key, true);
            map.setZoomButtonVisibility(showZoomButtons);
        } else if ("scaling".equals(key)) {
            float mapScale = Float.parseFloat(pref.getString(key, "1.0f"));
            map.setMapScaleFactor(mapScale);
        } else if ("dark".equals(key)) {
            boolean is_dark_mode = pref.getBoolean(key, false);
            Toolbar toolbar = findViewById(R.id.toolbar);
            LinearLayout ll = findViewById(R.id.main_page);
            if (is_dark_mode) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.light_grey));
                getWindow().setStatusBarColor(getResources().getColor(R.color.black));
                ll.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
                getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
                ll.setBackgroundColor(Color.WHITE);
            }
        }
    }

    private void createHamburgerMenu(Toolbar toolbar) {
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle t = new ActionBarDrawerToggle(this, dl, toolbar, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        NavigationView nv = (NavigationView) findViewById(R.id.navigationView);
        nv.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.list_all_notes:
                    intent = new Intent(MainActivity.this, ListNotesActivity.class);
                    startActivity(intent);
                    break;
                case R.id.settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * Create Options Menu Page
     *
     * @param menu - options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Handles changes to menu item
     *
     * @param item - menu item that was selected
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_btn_gps_follow) {
            boolean followingLocationEnabled = !map.isFollowLocationEnabled();
            this.map.setLocationFollowMode(followingLocationEnabled);

            if (followingLocationEnabled) {
                item.setIcon(R.drawable.ic_my_location);
            } else {
                item.setIcon(R.drawable.ic_location_searching);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Resume main activity
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        loadPreferences();
        super.onResume();
        map.onResume();
    }

    /**
     * Pause main activity
     */
    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }

    /**
     * Destroy main activity
     */
    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    /**
     * Request permissions result, wrapper for requestPermissionsIfNecessary
     *
     * @param requestCode  - request code
     * @param permissions  - list of permissions
     * @param grantResults - list of grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestPermissionsIfNecessary(permissions);
    }

    /**
     * Request permissions result
     *
     * @param permissions - list of permissions
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Adds listener to Map
     */
    private void addMapListener() {
        DelayedMapListener delayedMapListener = new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                storeLocation();
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                storeLocation();
                return true;
            }
        }, 500);

        @SuppressLint("RestrictedApi")
        TouchDownListener touchDownListener = () -> {
            ActionMenuItemView menuItem = findViewById(R.id.toolbar_btn_gps_follow);
            if (menuItem != null) {
                menuItem.setIcon(getResources().getDrawable(R.drawable.ic_location_searching));
            }
        };

        map.addMapListener(delayedMapListener, touchDownListener);
    }

    /**
     * Stores the current map location and zoom in the shared preferences.
     */
    private void storeLocation() {
        IGeoPoint location = map.getLocation();
        float zoom = map.getZoom();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(getString(R.string.pref_last_location_lat), (float) location.getLatitude());
        editor.putFloat(getString(R.string.pref_last_location_lon), (float) location.getLongitude());
        editor.putFloat(getString(R.string.pref_last_location_zoom), zoom);
        editor.apply();
    }

    // activity to accept the result of capturing the image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: store the image in the DB
            Toast.makeText(this, "Image has been Captured Succefully!", Toast.LENGTH_SHORT).show();
            
        }
    }
}
