package de.hauke_stieler.geonotes.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hauke_stieler.geonotes.R;
import de.hauke_stieler.geonotes.notes.Note;
import de.hauke_stieler.geonotes.notes.NoteStore;

/**
 * Class to handles displaying and interacting with the Map seen on the main page
 */
public class Map {
    private static final int PICK_IMAGE = 1;
    private final MapView map;
    private final IMapController mapController;
    private MarkerWindow markerInfoWindow;
    private Marker.OnMarkerClickListener markerClickListener;

    private Marker markerToMove;

    private MyLocationNewOverlay locationOverlay;
    private CompassOverlay compassOverlay;
    private ScaleBarOverlay scaleBarOverlay;

    private final PowerManager.WakeLock wakeLock;
    private final Drawable normalIcon;
    private final Drawable selectedIcon;

    private final NoteStore noteStore;

    /**
     * Constructor to create a new instance of Map
     *
     * @param context      - application context
     * @param map          - view of map
     * @param wakeLock     - wake lock
     * @param locationIcon - icon for location
     * @param normalIcon   - default icon
     * @param selectedIcon - icon for selected icon
     */
    public Map(Context context, MapView map, PowerManager.WakeLock wakeLock, Drawable locationIcon, Drawable normalIcon, Drawable selectedIcon) {
        this.wakeLock = wakeLock;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
        this.map = map;

        Configuration.getInstance().setUserAgentValue(context.getPackageName());

        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);

        // Initial location and zoom
        mapController = map.getController();
        mapController.setZoom(17.0);
        GeoPoint startPoint = new GeoPoint(53.563, 9.9866);
        mapController.setCenter(startPoint);

        createOverlays(context, map, (BitmapDrawable) locationIcon);
        createMarkerWindow(map);

        noteStore = new NoteStore(context);
        for (Note n : noteStore.getAllNotes(false)) {
            Marker marker = createMarker(n.description, new GeoPoint(n.lat, n.lon), markerClickListener);
            marker.setId("" + n.id);
        }
    }

    /**
     * Will create an overlay on the map
     *
     * @param context      - application context
     * @param map          - view of map
     * @param locationIcon - icon for location
     */
    private void createOverlays(Context context, MapView map, BitmapDrawable locationIcon) {
        // Add location icon
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
        locationOverlay.enableMyLocation();
        locationOverlay.setPersonIcon(locationIcon.getBitmap());
        locationOverlay.setPersonHotspot(32, 32);
        map.getOverlays().add(this.locationOverlay);

        // Add compass
        compassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), map);
        compassOverlay.enableCompass();
        map.getOverlays().add(this.compassOverlay);

        // Add scale bar
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        scaleBarOverlay = new ScaleBarOverlay(map);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 20);
        map.getOverlays().add(this.scaleBarOverlay);

        // Add marker click listener. Will be called when the user clicks/taps on a marker.
        markerClickListener = (marker, mapView) -> {
            // When we are in the state of moving an existing marker, we do not want to interact with other markers -> simply return
            if (markerToMove != null) {
                return true;
            }


            // If a marker is currently selected -> deselect it
            if (markerInfoWindow.getSelectedMarker() != null) {
                setNormalIcon(markerInfoWindow.getSelectedMarker());
                // We don't need to deselect the marker or close the window as we will directly assign a new marker below
            }
            centerLocationWithOffset(marker.getPosition());
            selectMarker(marker);

            return true;
        };
        // React to touches on the map
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                // When we have a marker to move, set its new position, store that and disable move-state
                if (markerToMove != null) {
                    markerToMove.setPosition(p);
                    selectMarker(markerToMove);

                    // If the ID is set, the marker exists in the DB, therefore we store that new location
                    String id = markerToMove.getId();
                    if (id != null) {
                        noteStore.updateLocation(Long.parseLong(id), p);
                    }

                    markerToMove = null;
                } else {
                    // Marker move state is not active -> normally select or create marker
                    if (markerInfoWindow.getSelectedMarker() != null) {
                        // Deselect selected marker:
                        setNormalIcon(markerInfoWindow.getSelectedMarker());
                        markerInfoWindow.close();
                    } else {
                        // No marker currently selected -> create new marker at this location
                        Marker marker = createMarker("", p, markerClickListener);
                        selectMarker(marker);
                    }
                }

                centerLocationWithOffset(p);

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        map.getOverlays().add(new MapEventsOverlay(mapEventsReceiver));
    }

    /**
     * Will add listener to the map
     *
     * @param listener          - listener to be added onto map
     * @param touchDownListener - listener to be added onto map on touch
     */
    @SuppressLint("ClickableViewAccessibility")
    public void addMapListener(MapListener listener, TouchDownListener touchDownListener) {
        map.addMapListener(listener);
        map.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                touchDownListener.onTouchDown();
            }
            return false;
        });
    }

    /**
     * Will create new marker window
     *
     * @param map - map
     */
    private void createMarkerWindow(MapView map) {
        // General marker info window
        markerInfoWindow = new MarkerWindow(R.layout.maker_window, map, new MarkerWindow.MarkerEventHandler() {
            @Override
            public void onDelete(Marker marker) {
                // Task came from database and should therefore be removed.
                if (marker.getId() != null) {
                    noteStore.removeNote(Long.parseLong(marker.getId()));
                }
                map.getOverlays().remove(marker);
            }

            @Override
            public void onSave(Marker marker) {
                // Check whether marker already exists in the database (this is the case when the
                // marker has an ID attached) and update the DB entry. Otherwise, we'll create a new DB entry.
                if (marker.getId() != null) {
                    noteStore.updateDescription(Long.parseLong(marker.getId()), marker.getSnippet());
                } else {
                    // MediaType and URI will be null until image/audio notes are fully implemented
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String date_str = formatter.format(date);
                    Note newNote = new Note(0, marker.getSnippet(), marker.getPosition().getLatitude(), marker.getPosition().getLongitude(), Note.MediaType.NULL, Uri.parse(""), date_str);
                    long id = noteStore.addNote(newNote);
                    marker.setId("" + id);
                }

                setNormalIcon(marker);
            }

            @Override
            public void onShare(Marker marker) {
                // Create message to share
                String message = "From Geonotes:\n";
                message += marker.getSnippet();
                message += "\nhttps://google.ca/maps/place/" + marker.getPosition().getLatitude() + "," + marker.getPosition().getLongitude();

                // Create intent to send message
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                // Open sharing dialogue
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                map.getContext().startActivity(shareIntent);
                setNormalIcon(marker);
            }

            @Override
            public void onUploadImage(Marker marker) {
                // Create intent to let the user capture an image
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Activity mapContextActivity = (Activity) map.getContext();
                mapContextActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                setNormalIcon(marker);
            }

            @Override
            public void onMove(Marker marker) {
                markerToMove = marker;
            }
        });
    }

    /**
     * Select a marker on the map
     *
     * @param marker - marker to be set as selected marker
     */
    private void selectMarker(Marker marker) {
        Marker selectedMarker = markerInfoWindow.getSelectedMarker();
        if (selectedMarker != null) {
            // This icon will not be the selected marker after "showInfoWindow", therefore we set the normal icon here.
            setNormalIcon(selectedMarker);
        }

        setSelectedIcon(marker);
        marker.showInfoWindow();
        markerInfoWindow.focusEditField();
    }

    /**
     * Set a selected icon
     *
     * @param marker - marker for selected icon
     */
    private void setSelectedIcon(Marker marker) {
        marker.setIcon(selectedIcon);
    }

    /**
     * Set a selected icon
     *
     * @param marker - marker for normal icon
     */
    private void setNormalIcon(Marker marker) {
        marker.setIcon(normalIcon);
    }

    /**
     * Will enable or disable zoom button visibility
     *
     * @param visible - if the marker should be visible or invisible
     */
    public void setZoomButtonVisibility(boolean visible) {
        map.getZoomController().setVisibility(visible ? CustomZoomButtonsController.Visibility.ALWAYS : CustomZoomButtonsController.Visibility.NEVER);
    }

    /**
     * Set map scale factor
     *
     * @param factor - scale factor
     */
    public void setMapScaleFactor(float factor) {
        map.setTilesScaleFactor(factor);
    }

    /**
     * Will center location
     *
     * @param p - location of note
     */
    private void centerLocationWithOffset(GeoPoint p) {
        centerLocationWithOffset(p, map.getZoomLevelDouble());
    }

    /**
     * Will centre location with specified zoom
     *
     * @param p    - location of note
     * @param zoom - level of zoom
     */
    private void centerLocationWithOffset(GeoPoint p, double zoom) {
        Point locationInPixels = new Point();
        map.getProjection().toPixels(p, locationInPixels);
        IGeoPoint newPoint = map.getProjection().fromPixels(locationInPixels.x, locationInPixels.y);

        mapController.setCenter(newPoint);
        mapController.setZoom(zoom);
    }

    /**
     * Will create a new instance of a Marker
     *
     * @param description         - description of marker
     * @param p                   - location of marker
     * @param markerClickListener - listener for marker
     */
    private Marker createMarker(String description, GeoPoint p, Marker.OnMarkerClickListener markerClickListener) {
        Marker marker = new Marker(map);
        marker.setPosition(p);
        marker.setSnippet(description);
        marker.setInfoWindow(markerInfoWindow);
        marker.setOnMarkerClickListener(markerClickListener);

        setNormalIcon(marker);

        map.getOverlays().add(marker);

        return marker;
    }

    /**
     * Will resume map
     */
    public void onResume() {
        map.onResume();
    }

    /**
     * Will pause map
     */
    public void onPause() {
        map.onPause();
    }

    /**
     * Will release wakelock
     */
    public void onDestroy() {
        wakeLock.release();
    }

    /**
     * Will centre location with given latitude
     *
     * @param lat - latitude to centre location on
     */
    public void setLatitude(float lat) {
        double lon = map.getMapCenter().getLongitude();
        centerLocationWithOffset(new GeoPoint(lat, lon));
    }

    /**
     * Will centre location with given longitude
     *
     * @param lon - longitude to centre location on
     */
    public void setLongitude(float lon) {
        double lat = map.getMapCenter().getLatitude();
        centerLocationWithOffset(new GeoPoint(lat, lon));
    }

    /**
     * Will return location of central point on map
     *
     * @return - geo point of center of map
     */
    public IGeoPoint getLocation() {
        return map.getMapCenter();
    }

    /**
     * Will centre location
     *
     * @param lat  - latitude to centre location on
     * @param lon  - longitude to centre location on
     * @param zoom - level of zoom
     */
    public void setLocation(float lat, float lon, float zoom) {
        centerLocationWithOffset(new GeoPoint(lat, lon), zoom);
    }

    /**
     * Will get the current zoom level
     *
     * @return - zoom level
     */
    public float getZoom() {
        return (float) map.getZoomLevelDouble();
    }

    /**
     * Turns the follow mode on or off. If it's turned on, the map will follow the current location.
     */
    public void setLocationFollowMode(boolean followingLocationEnabled) {
        if (followingLocationEnabled) {
            this.locationOverlay.enableFollowLocation();
        } else {
            this.locationOverlay.disableFollowLocation();
        }
    }

    /**
     * Checks if follow location is enabled
     *
     * @return - true or false
     */
    public boolean isFollowLocationEnabled() {
        return this.locationOverlay.isFollowLocationEnabled();
    }
}
