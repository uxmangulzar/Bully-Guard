package com.example.codesteembullyguard.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.codesteembullyguard.activities.NotificationsChildActivity;
import com.example.codesteembullyguard.adapters.SingleChildAdapter;
import com.example.codesteembullyguard.helpers.HelperKeys;
import com.example.codesteembullyguard.helpers.SessionManager;
import com.example.codesteembullyguard.helpers.Util;
import com.example.codesteembullyguard.models.AllLocation;
import com.example.codesteembullyguard.models.ChildrenModel;
import com.example.codesteembullyguard.models.AppsModel;
import com.example.codesteembullyguard.adapters.AppsAdapter;
import com.example.codesteembullyguard.models.MainResponseModel;
import com.example.codesteembullyguard.models.NotificationsModel;
import com.example.codesteembullyguard.R;
import com.example.codesteembullyguard.network.ApiClient;
import com.example.codesteembullyguard.network.ApiInterface;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageAlertFragment extends Fragment implements OnMapReadyCallback {

    View view;
    private RecyclerView recyclerViewNotifications;
    private ChildrenModel childrenModel;
    private RecyclerView recyclerViewApps;
    private GoogleMap mMap;
    // Sample latitude and longitude coordinates
    // Sample latitude, longitude, and server image URLs for each coordinate

    private List<MarkerOptions> markerOptionsList = new ArrayList<>();
    private List<AllLocation> allLocationsList=new ArrayList<>();

    String blocked="yes";
    private Button block_all;

    public MessageAlertFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_message_alert, container, false);
        Bundle args = getArguments();
        if (args != null) {
            childrenModel = (ChildrenModel) args.getSerializable("model");
            if (childrenModel != null) {
                // You now have access to the ChildrenModel object
                // Use it as needed
            }
        }
        TextView see_all = view.findViewById(R.id.see_all);
        block_all = view.findViewById(R.id.block_all);


        see_all.setOnClickListener(view -> {
            Intent int1=new Intent(getActivity(), NotificationsChildActivity.class);
            int1.putExtra("model",childrenModel);
            startActivity(int1);
        });
        block_all.setOnClickListener(view -> {
            if (blocked.equals("yes")){
                updateBlocked("no");
            }else {
                updateBlocked("yes");
            }

        });
        recyclerViewNotifications = view.findViewById(R.id.recyclerView2);
        getNotifications("message");


        recyclerViewApps = view.findViewById(R.id.recyclerView);
        getAppStats();
        getLocations();
        return view;

    }

    private void updateBlocked(String blocked) {
        AlertDialog dialog = Util.progressDialog(getActivity());
        dialog.show();
        ApiClient apiClient = new ApiClient();
        ApiInterface apiInterface = apiClient.getClient().create(ApiInterface.class);

        Call<MainResponseModel> responseModelCall = apiInterface.block_all(
                childrenModel.getId().toString(),blocked
        );

        responseModelCall.enqueue(new Callback<MainResponseModel>() {
            @Override
            public void onResponse(Call<MainResponseModel> call, Response<MainResponseModel> response) {
                dialog.dismiss();
                MainResponseModel data11 = response.body();
                if (data11 != null && data11.getStatus()==200) {
                    Toast.makeText(getActivity(), "Status Changed Successfully", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                } else {
                    Toast.makeText(getActivity(), "Not able to set", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Not able to set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNotifications(String type) {
        AlertDialog dialog = Util.progressDialog(getActivity());
        dialog.show();
        ApiClient apiClient = new ApiClient();
        ApiInterface apiInterface = apiClient.getClient().create(ApiInterface.class);

        Call<MainResponseModel> responseModelCall = apiInterface.get_notificationsForChild(1,3,
                childrenModel.getId().toString(),type
        );

        responseModelCall.enqueue(new Callback<MainResponseModel>() {
            @Override
            public void onResponse(Call<MainResponseModel> call, Response<MainResponseModel> response) {
                dialog.dismiss();
                MainResponseModel data11 = response.body();
                if (data11 != null && data11.getStatus()==200) {
                    List<NotificationsModel> listOne=data11.getAllNotifications();

                    List<NotificationsModel> items = new ArrayList<>(listOne);
                    Context ApplicationContext = requireContext().getApplicationContext();
                    recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerViewNotifications.setAdapter(new SingleChildAdapter(ApplicationContext, items));
                } else {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAppStats() {
        AlertDialog dialog = Util.progressDialog(getActivity());
        dialog.show();
        ApiClient apiClient = new ApiClient();
        ApiInterface apiInterface = apiClient.getClient().create(ApiInterface.class);

        Call<MainResponseModel> responseModelCall = apiInterface.get_app_stats(
                childrenModel.getId().toString(),Util.getCurrentDate()
        );

        responseModelCall.enqueue(new Callback<MainResponseModel>() {
            @Override
            public void onResponse(Call<MainResponseModel> call, Response<MainResponseModel> response) {
                dialog.dismiss();
                MainResponseModel data11 = response.body();
                if (data11 != null && data11.getStatus()==200) {
                    List<AppsModel> listOne=data11.getAllappstats();
                    if (listOne!=null&&listOne.size()!=0){
                        blocked=listOne.get(0).getBlocked();
                        if (blocked.equals("yes")){
                            block_all.setText("Unblock All");
                        }
                    }
                    List<AppsModel> items = new ArrayList<>(listOne);
                    LinearLayoutManager HorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewApps.setLayoutManager(HorizontalLayout);
                    recyclerViewApps.setAdapter(new AppsAdapter(getActivity(), items));
                } else {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (AllLocation coordinate : allLocationsList) {
            // Load the server-side image with Glide
            Glide.with(this)
                    .asBitmap()
                    .load(ApiClient.BASE_URL+"/files/"+childrenModel.getUserImage())
                    .placeholder(R.drawable.c3) // Optional placeholder image
                    .error(R.drawable.c3) // Optional error image
                    .apply(RequestOptions.circleCropTransform())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            // Create a custom marker view with the loaded image
                            View customMarkerView = getMarkerView(resource);

                            // Create a custom marker with the custom view
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(coordinate.getUserLat()),Double.parseDouble(coordinate.getUserLon())))
                                    .icon(BitmapDescriptorFactory.fromBitmap(createBitmapFromView(customMarkerView)));

                            markerOptionsList.add(markerOptions);

                            // Add markers when all images have been loaded
                            if (markerOptionsList.size() == allLocationsList.size()) {
                                addMarkersToMap();

                                // Adjust the camera position to fit all markers within the visible area
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (AllLocation coordinate : allLocationsList) {
                                    builder.include(new LatLng(Double.parseDouble(coordinate.getUserLat()),Double.parseDouble(coordinate.getUserLon())));
                                }
                                LatLngBounds bounds = builder.build();
                                int padding = 100; // Padding in pixels
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.animateCamera(cameraUpdate);
                            }
                        }
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            // Handle image loading error here
                            // You can use the errorDrawable parameter to get the error placeholder if needed
                            Log.e("Glide", "Image loading failed");
                            // Create a custom marker view with the loaded image
                            View customMarkerView = getMarkerView(drawableToBitmap(errorDrawable));

                            // Create a custom marker with the custom view
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(coordinate.getUserLat()),Double.parseDouble(coordinate.getUserLon())))
                                    .icon(BitmapDescriptorFactory.fromBitmap(createBitmapFromView(customMarkerView)));

                            markerOptionsList.add(markerOptions);

                            // Add markers when all images have been loaded
                            if (markerOptionsList.size() == allLocationsList.size()) {
                                addMarkersToMap();

                                // Adjust the camera position to fit all markers within the visible area
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (AllLocation coordinate : allLocationsList) {
                                    builder.include(new LatLng(Double.parseDouble(coordinate.getUserLat()),Double.parseDouble(coordinate.getUserLon())));
                                }
                                LatLngBounds bounds = builder.build();
                                int padding = 100; // Padding in pixels
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.animateCamera(cameraUpdate);
                            }
                        }
                    });
        }
    }
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    private View getMarkerView(Bitmap image) {
        // Create and inflate your custom marker layout
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(requireContext().LAYOUT_INFLATER_SERVICE);
        View customMarkerView = inflater.inflate(R.layout.custom_marker_layout, null);

        // Find the ImageView in your custom marker layout
        ImageView profileImage = customMarkerView.findViewById(R.id.profileImage);

        // Set the loaded image to the ImageView
        profileImage.setImageBitmap(image);

        // Add your logic to customize the marker view here

        return customMarkerView;
    }

    private Bitmap createBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    private void addMarkersToMap() {
        for (MarkerOptions markerOptions : markerOptionsList) {
            mMap.addMarker(markerOptions);
        }

        // Move the camera to the first coordinate
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(allLocationsList.get(0).getUserLat()),Double.parseDouble(allLocationsList.get(0).getUserLon())), 10));
    }





    private void getLocations() {
        AlertDialog dialog = Util.progressDialog(getActivity());
        dialog.show();
        ApiClient apiClient = new ApiClient();
        ApiInterface apiInterface = apiClient.getClient().create(ApiInterface.class);

        Call<MainResponseModel> responseModelCall = apiInterface.get_locations_on_child(1,100,
               childrenModel.getId().toString(),Util.getCurrentDate()
        );

        responseModelCall.enqueue(new Callback<MainResponseModel>() {
            @Override
            public void onResponse(Call<MainResponseModel> call, Response<MainResponseModel> response) {
                dialog.dismiss();
                MainResponseModel data11 = response.body();
                if (data11 != null && data11.getStatus()==200) {
                    allLocationsList.addAll(data11.getAllLocations());
                    if (allLocationsList.size()!=0){

                        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MessageAlertFragment.this);
                    }

                } else {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}