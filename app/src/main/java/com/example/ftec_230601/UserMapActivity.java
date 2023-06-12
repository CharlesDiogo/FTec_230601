package com.example.ftec_230601;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ftec_230601.databinding.ActivityUserMapBinding;

public class UserMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private double latitude, longitude;
    private String nome, sobrenome;
    Bitmap foto;
    private GoogleMap mMap;
    private ActivityUserMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        nome = intent.getStringExtra("nome");
        sobrenome= intent.getStringExtra("sobrenome");
        byte[] baPhoto = intent.getByteArrayExtra("foto");
        foto = BitmapFactory.decodeByteArray(baPhoto, 0, baPhoto.length);

        binding = ActivityUserMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in localPessoa and move the camera
        LatLng localPessoa = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(localPessoa)
                .title(nome + " " + sobrenome)
                .icon(BitmapDescriptorFactory.fromBitmap(foto))
        );
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(localPessoa));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localPessoa, 7));
    }
}