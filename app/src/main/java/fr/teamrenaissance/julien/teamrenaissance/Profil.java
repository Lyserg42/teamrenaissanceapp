package fr.teamrenaissance.julien.teamrenaissance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fr.teamrenaissance.julien.teamrenaissance.utils.CircleImageView;
import fr.teamrenaissance.julien.teamrenaissance.utils.GetLatLongByURL;
import fr.teamrenaissance.julien.teamrenaissance.utils.MyApplication;
import fr.teamrenaissance.julien.teamrenaissance.utils.PersistentCookieStore;

public class Profil extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "Profil";

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
    private ImageView logout;

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

        Log.i(TAG,"Cookies : "+((CookieManager) CookieHandler.getDefault()).getCookieStore().getCookies());

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
        logout = (ImageView) view.findViewById(R.id.logout);

        getProfilTask();

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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deconnectTask();
                Intent intent = new Intent();
                intent.setClass(getContext(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1000, null);
    }

    private void getProfilTask(){

       /* DefaultHttpClient httpclient = new DefaultHttpClient();

        CookieStore cookieStore = ((CookieManager)CookieHandler.getDefault()).getCookieStore();
        httpclient.setCookieStore( cookieStore );

        HttpStack httpStack = new HttpClientStack( httpclient );*/
        RequestQueue queue = Volley.newRequestQueue(getContext()/*, httpStack*/  );

        String url = "https://teamrenaissance.fr/user";

        try {
            JSONObject dataJson = new JSONObject("{typeRequest:\"getUser\", uName:\"\"}");

            Log.i(TAG, "Cookies :  " + ((CookieManager)CookieHandler.getDefault()).getCookieStore().getCookies().toString() );

            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    dataJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "response : " + response.toString());

                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.i(TAG, "error with: " + error.getMessage());
                            if (error.networkResponse != null)
                                Log.i(TAG, "status code : " + error.networkResponse.statusCode);
                        }
                    }
            ) /*{
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Cookie",((CookieManager)CookieHandler.getDefault()).getCookieStore().getCookies().toString());
                    return headers;
                }
            }*/;

            request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setTag("POST");

            /*try {
                Log.i(TAG,"HEADERS : "+request.getHeaders().toString());
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }*/
            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void intent(String url){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void deconnectTask(){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://www.teamrenaissance.fr/user";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.i(TAG, "response: " + response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i(TAG, "error with: " + error.getMessage());
                        if (error.networkResponse != null)
                            Log.i(TAG, "status code: " + error.networkResponse.statusCode);
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("Content-Type","application/json");
                return header;
            }

            @Override
            public byte[] getBody()
            {
                JSONObject body = new JSONObject();
                try {
                    body.put("typeRequest","deconnexion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return body.toString().getBytes();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("POST");
        queue.add(request);
    }

}

