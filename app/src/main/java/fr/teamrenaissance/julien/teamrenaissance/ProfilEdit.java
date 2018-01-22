package fr.teamrenaissance.julien.teamrenaissance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.teamrenaissance.julien.teamrenaissance.beans.User;

public class ProfilEdit extends AppCompatActivity {

    public static final String TAG = "ProfilEditActivity";

    private User user = new User();

    private TextView pseudoView;
    private EditText lastNameView;
    private EditText firstNameView;
    private EditText phoneView;
    private EditText dciView;
    private EditText addressView;
    private EditText zipcodeView;
    private EditText cityView;
    private EditText emailView;
    private EditText facebookView;
    private EditText twitterView;
    private EditText avatarView;
    private EditText newPasswordView;
    private EditText confirmView;
    private EditText currentPasswordView;
    private Button valideButton;

    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiledit);


        pseudoView = (TextView) findViewById(R.id.pseudo);
        lastNameView = (EditText) findViewById(R.id.lastName);
        firstNameView = (EditText) findViewById(R.id.firstName);
        dciView = (EditText) findViewById(R.id.dci);
        phoneView = (EditText) findViewById(R.id.phone);
        addressView = (EditText) findViewById(R.id.address);
        zipcodeView = (EditText) findViewById(R.id.zipcode);
        cityView = (EditText) findViewById(R.id.city);
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        facebookView = (EditText) findViewById(R.id.fb);
        twitterView = (EditText) findViewById(R.id.tw);
        avatarView = (EditText) findViewById(R.id.avatar);
        newPasswordView = (EditText) findViewById(R.id.newPassword);
        confirmView = (EditText) findViewById(R.id.confirm);
        currentPasswordView = (EditText) findViewById(R.id.currentPassword);

        valideButton = (Button) findViewById(R.id.valideButton);
        valideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valideProfilTask();
            }
        });

        getUserProfil();

    }

    private void getUserProfil(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.teamrenaissance.fr/user";
        JSONObject dataJSON = new JSONObject();
        try {
            dataJSON.put("typeRequest","getUser");
            dataJSON.put("uName","");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dataJSON,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"Response getUser ProfilEdit: "+response);
                        parserResponse(response);
                        syncProfil();
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
                });

        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("POST");
        queue.add(request);
    }

    public void parserResponse(JSONObject result){
        try {
            user.setuName(result.getString("uName"));
            user.setuId(result.getString("uId"));
            user.setFirstName(result.getString("firstName"));
            user.setLastName(result.getString("lastName"));
            user.setPhone(result.getString("phone"));
            user.setDCI(result.getString("DCI"));
            user.setAddress(result.getString("address"));
            user.setZipCode(result.getString("zipCode"));
            user.setCity(result.getString("city"));
            user.setFacebook(result.getString("facebook"));
            user.setTwitter(result.getString("twitter"));
            user.setEmail(result.getString("email"));
            user.setAvatar(result.getString("avatar"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void syncProfil(){

        pseudoView.setText(user.getuName());
        lastNameView.setText(user.getLastName());
        firstNameView.setText(user.getFirstName());
        phoneView.setText(user.getPhone());
        dciView.setText(user.getDCI());
        addressView.setText(user.getAddress());
        zipcodeView.setText(user.getZipCode());
        cityView.setText(user.getCity());
        emailView.setText(user.getEmail());
        facebookView.setText(user.getFacebook());
        twitterView.setText(user.getTwitter());
        avatarView.setText(user.getAvatar());
    }

    //TODO statu code 400
    private void valideProfilTask(){
        emailView.setError(null);
        newPasswordView.setError(null);
        confirmView.setError(null);
        String email = emailView.getText().toString();
        String newPassword = newPasswordView.getText().toString();
        String pwConfirm = confirmView.getText().toString();
        String currentPassword = currentPasswordView.getText().toString();

        boolean cancel = false;

        if (!TextUtils.isEmpty(newPassword) && !isPasswordValid(newPassword)) {
            newPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = newPasswordView;
            cancel = true;
        }
        if( TextUtils.isEmpty(currentPassword)){
            currentPasswordView.setError(getString(R.string.error_empty_password));
            focusView = currentPasswordView;
            cancel = true;
        }
        if (!newPassword.equals(pwConfirm)) {
            confirmView.setError(getString(R.string.error_invalid_confirmPassword));
            focusView = confirmView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "https://www.teamrenaissance.fr/user";

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TAG, "response: " + response);
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            intent.putExtra("userloginflag", 4);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.i(TAG, "error with: " + error.getMessage());
                            if (error.networkResponse != null)
                                Log.i(TAG, "status code: " + error.networkResponse.statusCode);
                        }
                    }
            )
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
                        body.put("typeRequest","setUserProfil");
                        body.put("name", lastNameView.getText());
                        body.put("firstName", firstNameView.getText());
                        body.put("email", emailView.getText());
                        body.put("address", addressView.getText());
                        body.put("zipCode", zipcodeView.getText());
                        body.put("city", cityView.getText());
                        body.put("avatar", avatarView.getText());
                        body.put("phoneNumber", phoneView.getText());
                        body.put("dciNumber", dciView.getText());
                        body.put("facebook", facebookView.getText());
                        body.put("twitter", twitterView.getText());
                        body.put("discord", "discord");
                        if(!TextUtils.isEmpty(newPasswordView.getText().toString())){
                            body.put("newPassword", newPasswordView.getText());
                        }
                        body.put("password", currentPasswordView.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return body.toString().getBytes();
                }
            };

            queue.add(request);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isPwConfirmValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

}