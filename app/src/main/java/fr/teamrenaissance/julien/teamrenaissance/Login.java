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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import fr.teamrenaissance.julien.teamrenaissance.utils.MyApplication;

public class Login extends AppCompatActivity {

    MyApplication myApplication = (MyApplication) getApplication();
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

        loginView = (EditText) findViewById(R.id.login);
        passwordView = (EditText) findViewById(R.id.password);
        loginTest = (TextView) findViewById(R.id.loginTest);
        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectTask(loginView.getText().toString(),passwordView.getText().toString());
            }
        });
        //loginTest.setText("Rien");

        /*
         *Lorsqu'une section de texte est hyperliee, ou que nous devons cliquer sur un hyperlien
         *pour passer a une autre avtivite
         */
        singUpView = (TextView) findViewById(R.id.signUpLink);
        singUpView.setText(getClickableSpan());
        singUpView.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void connectTask(final String login, final String password){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.teamrenaissance.fr/user";
        /*JSONObject dataJSON = new JSONObject();
        try {
            dataJSON.put("typeRequest","connexion");
            dataJSON.put("login",login);
            dataJSON.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dataJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "response: " + response.toString());
                        loginTest.setText("Connexion reussie");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.i(TAG, "error with: " + error.getMessage());
                        if (error.networkResponse != null)
                            Log.i(TAG, "status code: " + error.networkResponse.statusCode);
                        loginTest.setText("Echec connexion");
                    }
                }
        );*/
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.i(TAG, "response: " + response);
                        //TODO le serveur doit rendre quelque chose
                        if("".equals(response)){
                            loginTest.setText("Connexion reussie");
                            //TODO: mettre des infos d'utilisateur dans le variable global
                            //myApplication.setUserID("");...
                            //TODO: redirect to ...
                            Intent intent = new Intent(getApplicationContext(), Accueil.class);
                            startActivity(intent);
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
                header.put("Accept","application/json");
                header.put("Content-Type","application/x-www-form-urlencoded");
                return header;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("typeRequest","connexion");
                params.put("login",login);
                params.put("password",password);
                return params;
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
}
