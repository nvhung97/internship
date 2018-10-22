package com.example.cpu11398_local.etalk.presentation.view.chat.media;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView   mapView;
    LatLng    latLng;
    Bitmap    avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getIntent().getExtras() != null) {
            latLng = new LatLng(
                    getIntent().getExtras().getDouble("lat"),
                    getIntent().getExtras().getDouble("lng")
            );
            avatar = getIntent().getExtras().getParcelable("avatar");
        }
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
        ImageView locationButton = ((View)mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        locationButton.setImageResource(R.drawable.ic_my_location);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        int buttonSize = getResources().getDimensionPixelSize(R.dimen.map_activity_button_size);
        rlp.setMargins(
                0,
                0,
                rlp.rightMargin,
                rlp.rightMargin * 2 + buttonSize
        );
        if (latLng == null) {
            findViewById(R.id.direction).setVisibility(View.GONE);
            new Handler().postDelayed(
                    () -> locationButton.performClick(),
                    1000
            );
            Button sendLocation = findViewById(R.id.send_location);
            FrameLayout.LayoutParams flp= (FrameLayout.LayoutParams)sendLocation.getLayoutParams();
            flp.setMargins(
                    0,
                    0,
                    rlp.rightMargin,
                    rlp.rightMargin
            );
            sendLocation.setOnClickListener(v -> {
                LatLng location = googleMap.getCameraPosition().target;
                Intent intent = new Intent();
                intent.putExtra("lat", location.latitude);
                intent.putExtra("lng", location.longitude);
                setResult(RESULT_OK, intent);
                finish();
            });
        } else {
            findViewById(R.id.marker).setVisibility(View.GONE);
            findViewById(R.id.send_location).setVisibility(View.GONE);
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(
                                    avatar == null
                                            ? BitmapDescriptorFactory.defaultMarker()
                                            : BitmapDescriptorFactory.fromBitmap(createCustomMarker())
                            )
            );
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            Button direction = findViewById(R.id.direction);
            FrameLayout.LayoutParams flp= (FrameLayout.LayoutParams)direction.getLayoutParams();
            flp.setMargins(
                    0,
                    0,
                    rlp.rightMargin,
                    rlp.rightMargin
            );
            direction.setOnClickListener(v -> {
                Toast.makeText(this, "This feature is not ready yet", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private Bitmap createCustomMarker(){
        View marker = getLayoutInflater().inflate(R.layout.avatar_marker, null);
        AvatarImageView avatarImageView = marker.findViewById(R.id.avatar);
        avatarImageView.setImageBitmap(getBitmapClippedCircle(avatar));
        marker.setDrawingCacheEnabled(true);
        marker.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());
        marker.buildDrawingCache(true);
        return marker.getDrawingCache();
    }

    public static Bitmap getBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float)(width / 2),
                (float)(height / 2),
                (float) Math.min((width / 2), (height / 2)),
                Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }
}
