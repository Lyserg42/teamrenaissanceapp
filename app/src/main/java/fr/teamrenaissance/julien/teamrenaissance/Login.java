package fr.teamrenaissance.julien.teamrenaissance;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText loginView;
    private EditText passwordView;
    private TextView loginTest;
    private Button signInButton;

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

        loginTest.setText("Rien");

    }

    private void connectTask(final String login, String password){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.teamrenaissance.fr/user";
        JSONObject dataJSON = new JSONObject();
        try {
            dataJSON.put("typeRequest","connexion");
            dataJSON.put("login",login);
            dataJSON.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dataJSON,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        loginTest.setText("Connexion reussie");
                    }

                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"Error: "+error.getMessage());
                        loginTest.setText("Echec connexion");
                    }
                }
        );
        queue.add(request);

        /* new AsyncTask<String, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(String... params) {

                Boolean retour = false;

                JSONObject requestData = new JSONObject();
                try {
                    requestData.accumulate("typeRequest","connexion");
                    requestData.accumulate("login",params[0]);
                    requestData.accumulate("password",params[1]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String response = null;
                try {
                    URL url = new URL("https://www.teamrenaissance.fr/user");
                    HttpURLConnection conn = (HttpURLCon    nection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type","application/json");
                    conn.setDoOutput(true);

                    OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                    writer.write(requestData.toString());

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        retour = true;
                    }

                    System.out.println("hello");

                } catch (Exception e) {
                    Log.e(TAG, "Exception: ", e);
                }

                return retour;
            }

            @Override
            protected void onPostExecute(Boolean response){
                if(response){
                    loginTest.setText("Connexion reussie");
                }
                else{
                    loginTest.setText("Erreur connexion");
                }

            }
        }.execute(login, password); */
    }

    private static String convertStreamToString(InputStream is) {
        Log.i(TAG, "convertStreamToString");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "ERROR", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "ERROR", e);
            }
        }
        return sb.toString();
    }
}
