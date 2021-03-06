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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    private EditText pseudoView;
    private EditText lastNameView;
    private EditText firstNameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmView;
    private Button signUpButton;

    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pseudoView = (EditText) findViewById(R.id.pseudo);
        lastNameView = (EditText) findViewById(R.id.lastName);
        firstNameView = (EditText) findViewById(R.id.firstName);
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.newPassword);
        confirmView = (EditText) findViewById(R.id.confirm);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpTask();
            }
        });
    }
    private void signUpTask(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.teamrenaissance.fr/user";

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

        if (!TextUtils.isEmpty(pwConfirm) && !isPwConfirmValid(password, pwConfirm)) {
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
            sendSignUp();
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if(response.equals("success")){
//                                Toast.makeText(SignUp.this, "New User Created.", Toast.LENGTH_SHORT).show();
//                                //TODO: mettre des infos de l'utilisateur dans le variable global
//                                //TODO: redirect to
//                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
//                                startActivity(intent);
//
//                            }else if("".equals(response)){
//                                Log.i(TAG, "response: " + response);
//                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
//                                startActivity(intent);
//                            }else{
//                                passwordView.setError("Something went wrong. Please try again!"+ response);
//                                passwordView.requestFocus();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("typeRequest", "inscription");
//                    params.put("name", lastNameView.getText().toString());
//                    params.put("firstname", firstNameView.getText().toString());
//                    params.put("login", pseudoView.getText().toString());
//                    params.put("email", emailView.getText().toString());
//                    params.put("password", passwordView.getText().toString());
//                    return params;
//                }
//            };
//            queue.add(stringRequest);
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

    private void sendSignUp(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.teamrenaissance.fr/user";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,"Inscription reussie");
                        Toast.makeText(SignUp.this, "Inscription reussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i(TAG, "error with Inscription: " + error.getMessage());
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
                    body.put("typeRequest", "inscription");
                    body.put("name", lastNameView.getText().toString());
                    body.put("firstname", firstNameView.getText().toString());
                    body.put("login", pseudoView.getText().toString());
                    body.put("email", emailView.getText().toString());
                    body.put("password", passwordView.getText().toString());

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