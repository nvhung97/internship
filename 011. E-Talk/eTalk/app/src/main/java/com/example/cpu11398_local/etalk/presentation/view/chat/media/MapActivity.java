package com.example.cpu11398_local.etalk.presentation.view.chat.media;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.cpu11398_local.etalk.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView   mapView;
    LatLng    latLng;

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
            new Handler().postDelayed(
                    () -> locationButton.performClick(),
                    1000
            );
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
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
    }
}
