package fr.teamrenaissance.julien.teamrenaissance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import fr.teamrenaissance.julien.teamrenaissance.utils.MyApplication;

public class ProfilEdit extends AppCompatActivity {

    public static final String TAG = "ProfilEditActivity";

    private TextView pseudoView;
    private EditText lastNameView;
    private EditText firstNameView;
    private EditText dciView;
    private EditText phoneView;
    private EditText addressView;
    private EditText zipcodeView;
    private EditText cityView;
    private EditText emailView;
    private EditText facebookView;
    private EditText twitterView;
    private EditText avatarView;
    private EditText passwordView;
    private EditText confirmView;
    private Button valideButton;

    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiledit);

        final MyApplication myApplication = (MyApplication) getApplication();

        pseudoView = (TextView) findViewById(R.id.pseudo);
        //TODO null or isEmpty???
        if(null != myApplication.getuName() || !TextUtils.isEmpty(myApplication.getuName())){
            pseudoView.setText(myApplication.getuName());
        }

        lastNameView = (EditText) findViewById(R.id.lastName);
        if(null != myApplication.getLastName()){
            lastNameView.setText(myApplication.getLastName());
        }

        firstNameView = (EditText) findViewById(R.id.firstName);
        if(null != myApplication.getFirstName()){
            firstNameView.setText(myApplication.getFirstName());
        }

        dciView = (EditText) findViewById(R.id.dci);
        if(null != myApplication.getDCI()){
            dciView.setText(myApplication.getDCI());
        }

        phoneView = (EditText) findViewById(R.id.phone);
        if(null != myApplication.getPhone()){
            phoneView.setText(myApplication.getPhone());
        }

        addressView = (EditText) findViewById(R.id.address);
        if(null != myApplication.getAddress()){
            avatarView.setText(myApplication.getAddress());
        }

        zipcodeView = (EditText) findViewById(R.id.zipcode);
        if(null != myApplication.getZipCode()){
            zipcodeView.setText(myApplication.getZipCode());
        }

        cityView = (EditText) findViewById(R.id.city);
        if(null != myApplication.getCity()){
            cityView.setText(myApplication.getCity());
        }

        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        if(null != myApplication.getEmail()){
            emailView.setText(myApplication.getEmail());
        }

        facebookView = (EditText) findViewById(R.id.fb);
        if(null != myApplication.getFacebook()){
            facebookView.setText(myApplication.getFacebook());
        }

        twitterView = (EditText) findViewById(R.id.tw);
        if(null != myApplication.getTwitter()){
            twitterView.setText(myApplication.getTwitter());
        }

        avatarView = (EditText) findViewById(R.id.avatar);
        if(null != myApplication.getAvatar()){
            avatarView.setText(myApplication.getAvatar());
        }

        passwordView = (EditText) findViewById(R.id.password);
        confirmView = (EditText) findViewById(R.id.confirm);
        /*TODO
        if(null != myApplication.getPassword()){
            passwordView.setText(myApplication.getPassword());
            confirmView.setText(myApplication.getPassword());
        }*/


        valideButton = (Button) findViewById(R.id.valideButton);
        valideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valideProfilTask();
            }
        });
    }

    //TODO statu code 400
    private void valideProfilTask(){
        emailView.setError(null);
        passwordView.setError(null);
        confirmView.setError(null);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String pwConfirm = confirmView.getText().toString();

        boolean cancel = false;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }
        if( TextUtils.isEmpty(password)){
            passwordView.setError(getString(R.string.error_empty_password));
            focusView = passwordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(pwConfirm) || (!TextUtils.isEmpty(pwConfirm) && !isPwConfirmValid(password, pwConfirm))) {
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

            JSONObject dataJSON = new JSONObject();
            try {
                dataJSON.put("typeRequest","setUserProfil");
                dataJSON.put("name", lastNameView.getText());
                dataJSON.put("firstName", firstNameView.getText());
                //dataJSON.put("email", (!TextUtils.isEmpty(emailView.getText()))? emailView.getText().toString(): "");
                dataJSON.put("email", emailView.getText());
                dataJSON.put("address", addressView.getText());
                dataJSON.put("zipCode", zipcodeView.getText());
                dataJSON.put("city", cityView.getText());
                dataJSON.put("avatar", avatarView.getText());
                dataJSON.put("phoneNumber", phoneView.getText());
                dataJSON.put("dciNumber", dciView.getText());
                dataJSON.put("facebook", facebookView.getText());
                dataJSON.put("twitter", twitterView.getText());
                dataJSON.put("password", passwordView.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("--------------------dataJSON="+ dataJSON.toString());
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    dataJSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "response: " + response.toString());
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
            );

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