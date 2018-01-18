package fr.teamrenaissance.julien.teamrenaissance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import fr.teamrenaissance.julien.teamrenaissance.utils.CircleImageView;
import fr.teamrenaissance.julien.teamrenaissance.utils.GetLatLongByURL;
import fr.teamrenaissance.julien.teamrenaissance.utils.MyApplication;

public class Profil extends Fragment implements OnMapReadyCallback {

    private CircleImageView avatar;
    private TextView pseudo;
    private TextView name;
    private TextView phone;
    private TextView dci;
    private TextView home;
    private TextView email;
    private ImageView facebook;
    private ImageView twitter;
    private ImageView edit;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private String apiKey_geocoder = "AIzaSyBQd1pLp088PuXWMFpITv-p7rYWvr4XUXU";
    private String[] address;
    private String city;
    private String zipcode;
    private double lat;
    private double lng;
    private String formattedAddress;

    public static Fragment newInstance(){
        Profil fragment = new Profil();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profil,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MyApplication myApplication = (MyApplication) getActivity().getApplication();

        avatar = (CircleImageView) view.findViewById(R.id.avatar);
        pseudo = (TextView) view.findViewById(R.id.pseudo);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        dci = (TextView) view.findViewById(R.id.dci);
        home = (TextView) view.findViewById(R.id.home);
        email = (TextView) view.findViewById(R.id.email);
        facebook = (ImageView) view.findViewById(R.id.fb);
        twitter = (ImageView) view.findViewById(R.id.tw);
        edit = (ImageView) view.findViewById(R.id.edit);


        /*TODO
        int id = getResources().getIdentifier("lyserg", "drawable", getContext().getPackageName());
        int id = getResources().getIdentifier(myApplication.getAvatar(), "drawable", getContext().getPackageName());
        avatar.setImageResource(id);*/
        avatar.setImageResource(R.drawable.lyserg);

        /* TODO
        pseudo.setText(myApplication.getUsername());
        name.setText(myApplication.getName());
        phone.setText(myApplication.getPhoneNumber());
        dci.setText(myApplication.getDciNumber());
        home.setText(myApplication.getAddress()+ ", "+ myApplication.getZipCode()+ " "+ myApplication.getCity());
        email.setText(myApplication.getEmail());*/
        pseudo.setText("Lyserg");
        name.setText("Julien Henry");
        phone.setText("06 71 77 86 63");
        dci.setText("3213675748");
        home.setText("39 Avenue du Président Wilson, 94230 Cachan");
        email.setText("julien.henry@gmx.com");

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TODO
                intent(myApplication.getFacebook()); */
                intent("https://www.facebook.com/julien.henry404");

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TODO
                intent(myApplication.getTwitter());*/
                intent("https://twitter.com/lyserg42?lang=fr");
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfilEdit.class);
                startActivity(intent);
            }
        });
        /* TODO
        address = myApplication.getAddress().split(" ");
        city = myApplication.getCity();
        zipcode = myApplication.getZipCode();*/

        //google maps
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Sets the map type to be "hybrid"
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // sets the built-in zoom controls
        final UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // sets my location
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);
        }else {
            mMap.setMyLocationEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
        }

        /* TODO
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        for(String s : address){
            url += s + "+";
        }
        url += zipcode + "+"+ zipcode+ "&key="+ apiKey_geocoder;*/
        //String url= "https://maps.googleapis.com/maps/api/geocode/json?address=102+北京市朝阳区望京南湖东园+100102+北京&key=AIzaSyBQd1pLp088PuXWMFpITv-p7rYWvr4XUXU";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=39+Avenue+du+Président+Wilson+94230+Cachan&key=AIzaSyBQd1pLp088PuXWMFpITv-p7rYWvr4XUXU";

        try {
            //TODO optimise
            String[] response= new GetLatLongByURL().execute(url).get();

            JSONObject jsonObject = new JSONObject(response[0]);
            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            formattedAddress = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getString("formatted_address");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Add a marker and move the camera
        LatLng latLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(formattedAddress));
        // Move the camera instantly to location with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        // Zoom in, animating the camera. ZoomTO moves the camera viewpoint to a particular zoom level(5=country,10=city, 15=street..).
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
    }

    private void intent(String url){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}

