package com.adityasonel.delivergo.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adityasonel.delivergo.R;
import com.adityasonel.delivergo.ui.CircleImageView;
import com.adityasonel.delivergo.ui.UserLockBottomSheetBehavior;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ItemLocationActivity extends AppCompatActivity implements OnMapReadyCallback{

    private CoordinatorLayout mainLayout;
    private View llBottomSheet;
    private CircleImageView itemImage;
    private TextView itemDescriptionText, itemAddressText;
    private UserLockBottomSheetBehavior bottomSheetBehavior;

    private GoogleMap mGoogleMap;

    private String address, description, imageUrl;
    private double lat, lng;

    int bottomsheetPeakableHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_location);

        if (getIntent() != null) {
            lat = Double.valueOf(getIntent().getStringExtra("lat"));
            lng = Double.valueOf(getIntent().getStringExtra("lng"));
            address = getIntent().getStringExtra("address");
            description = getIntent().getStringExtra("description");
            imageUrl = getIntent().getStringExtra("imageUrl");
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        bottomsheetPeakableHeight = getResources().getDisplayMetrics().heightPixels / 4;

        mainLayout = findViewById(R.id.parent_main_layout);
        llBottomSheet = findViewById(R.id.bottomsheet_parent_layout);

        itemImage = llBottomSheet.findViewById(R.id.item_image);
        itemDescriptionText = llBottomSheet.findViewById(R.id.item_description);
        itemAddressText = llBottomSheet.findViewById(R.id.item_location);

        Glide.with(this).load(imageUrl).into(itemImage);
        itemDescriptionText.setText(description);
        itemAddressText.setText(address);

        mainLayout.setPadding(0, getStatusBarHeight(), 0, 0);
        bottomSheetBehavior = (UserLockBottomSheetBehavior) UserLockBottomSheetBehavior.from(llBottomSheet);

        bottomSheetBehavior.setPeekHeight(bottomsheetPeakableHeight);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lng),18));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(description + address));
    }

    int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void titleText(View view) {
        onBackPressed();
    }

    public void bottomsheetParentLayout(View view) {
        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lat, lng),18));
        }
    }
}