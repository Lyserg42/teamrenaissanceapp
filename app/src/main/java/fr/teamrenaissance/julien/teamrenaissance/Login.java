package fr.teamrenaissance.julien.teamrenaissance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.teamrenaissance.julien.teamrenaissance.utils.LoginOcclusionProblem.InputManagerHelper;
import fr.teamrenaissance.julien.teamrenaissance.utils.LoginOcclusionProblem.KeyboardListenLayout;
import fr.teamrenaissance.julien.teamrenaissance.utils.MyApplication;
import fr.teamrenaissance.julien.teamrenaissance.utils.PersistentCookieStore;

public class Login extends AppCompatActivity {

    MyApplication app = (MyApplication) getApplication();
    public static final String TAG = "LoginActivity";

    private EditText loginView;
    private EditText passwordView;
    private TextView loginTest;
    private Button signInButton;
    private TextView singUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CookieHandler.setDefault(new CookieManager(new PersistentCookieStore(getApplicationContext()),CookiePolicy.ACCEPT_ALL));

        checkIfConnected();

        loginView = (EditText) findViewById(R.id.login);
        passwordView = (EditText) findViewById(R.id.newPassword);
        loginTest = (TextView) findViewById(R.id.loginTest);
        signInButton = (Button) findViewById(R.id.signInButton);

        /*Resoudre le probleme d'occlusion des editText de "singIn"*/
        KeyboardListenLayout keyboardListenLayout = (KeyboardListenLayout) findViewById(R.id.layout_keyboard);
        InputManagerHelper.attachToActivity(this).bind(keyboardListenLayout, signInButton).offset(16);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectTask(loginView.getText().toString(),passwordView.getText().toString());
            }
        });

        /*
         *Lorsqu'une section de texte est hyperliee, ou que nous devons cliquer sur un hyperlien
         *pour passer a une autre avtivite
         */
        singUpView = (TextView) findViewById(R.id.signUpLink);
        singUpView.setText(getClickableSpan());
        singUpView.setMovementMethod(LinkMovementMethod.getInstance());

    }

    //Restart activity on new Intent
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if ("ExitApp".equals(intent.getAction())) {
            finish();
        }
    }

    private void launchHomePage(){
//        List<HttpCookie> cookies = ((CookieManager)CookieHandler.getDefault()).getCookieStore().getCookies();
//
//        String[] cookieArray = new String[cookies.size()];
//        //copy your List of Strings into the Array
//        int i=0;
//        for(HttpCookie c : cookies ){
//            cookieArray[i]=c.toString();
//            Log.i(TAG,"Envoi cookie : "+c.toString());
//            i++;
//        }
//
//        Log.i(TAG,"Envoi cookie : "+cookieArray[0]);
//        intent.putExtra("cookieArray",cookieArray);
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

    private void connectTask(final String login, final String password){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.teamrenaissance.fr/user";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.i(TAG, "response: " + response);
                        if("".equals(response)){
                            checkIfConnected();
                            launchHomePage();

                        }

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
                        loginTest.setText("Echec connexion");
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
                    body.put("typeRequest","connexion");
                    body.put("login",login);
                    body.put("password",password);

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

    /*
     * Definir le texte du lien
     * */
    private SpannableString getClickableSpan(){
        SpannableString spannableString = new SpannableString("No account yet? Create one");
        spannableString.setSpan(new UnderlineSpan(), 16, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        }, 16, 26, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void checkIfConnected(){
        boolean isConnected = false;
        RequestQueue queue = Volley.newRequestQueue(this);
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
                        Log.i(TAG,"Response getUser: "+response);
                        launchHomePage();
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
}
