package fr.teamrenaissance.julien.teamrenaissance;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import fr.teamrenaissance.julien.teamrenaissance.utils.MyApplication;
import fr.teamrenaissance.julien.teamrenaissance.utils.TournamentItem;


public class Mesprets extends Fragment {
    public static final String TAG = "Mesprets";

    private int tournamentId;
    JSONObject result;

    public static Fragment newInstance(){
        Mesprets fragment = new Mesprets();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mesprets,null);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO status code 401, comment traiter "session" ??
        mesPretsTask();
        /*-----------TODO tester, delete-----------------------*/
        try {
            String data = "{\n" +
                    "\t\"tournaments\": [{\n" +
                    "\t\t\"date\": \"2018-02-02\",\n" +
                    "\t\t\"borrowedCards\": [],\n" +
                    "\t\t\"lentCards\": [],\n" +
                    "\t\t\"tName\": \"PT Bilbao\",\n" +
                    "\t\t\"tId\": 13,\n" +
                    "\t\t\"demands\": [{\n" +
                    "\t\t\t\"cName\": \"Black Lotus\",\n" +
                    "\t\t\t\"qty\": 5,\n" +
                    "\t\t\t\"cId\": 9299\n" +
                    "\t\t}]\n" +
                    "\t}, {\n" +
                    "\t\t\"date\": \"2018-01-27\",\n" +
                    "\t\t\"borrowedCards\": [],\n" +
                    "\t\t\"lentCards\": [],\n" +
                    "\t\t\"tName\": \"GP London\",\n" +
                    "\t\t\"tId\": 12,\n" +
                    "\t\t\"demands\": [{\n" +
                    "\t\t\t\"cName\": \"Porphyry Nodes\",\n" +
                    "\t\t\t\"qty\": 1,\n" +
                    "\t\t\t\"cId\": 21355\n" +
                    "\t\t}]\n" +
                    "\t}, {\n" +
                    "\t\t\"date\": \"2017-12-09\",\n" +
                    "\t\t\"borrowedCards\": [],\n" +
                    "\t\t\"lentCards\": [{\n" +
                    "\t\t\t\"uId\": 1,\n" +
                    "\t\t\t\"cards\": [{\n" +
                    "\t\t\t\t\"cName\": \"Mox Opal\",\n" +
                    "\t\t\t\t\"qty\": 2,\n" +
                    "\t\t\t\t\"cId\": 3792\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t\"cName\": \"Glimmervoid\",\n" +
                    "\t\t\t\t\"qty\": 3,\n" +
                    "\t\t\t\t\"cId\": 11664\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t\"cName\": \"Tezzeret, Agent of Bolas\",\n" +
                    "\t\t\t\t\"qty\": 2,\n" +
                    "\t\t\t\t\"cId\": 15132\n" +
                    "\t\t\t}],\n" +
                    "\t\t\t\"uName\": \"Lyserg\"\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"uId\": 3,\n" +
                    "\t\t\t\"cards\": [{\n" +
                    "\t\t\t\t\"cName\": \"Snapcaster Mage\",\n" +
                    "\t\t\t\t\"qty\": 2,\n" +
                    "\t\t\t\t\"cId\": 2343\n" +
                    "\t\t\t}],\n" +
                    "\t\t\t\"uName\": \"Balkany\"\n" +
                    "\t\t}],\n" +
                    "\t\t\"tName\": \"GP Madrid\",\n" +
                    "\t\t\"tId\": 11,\n" +
                    "\t\t\"demands\": [{\n" +
                    "\t\t\t\"cName\": \"Grafdigger's Cage\",\n" +
                    "\t\t\t\"qty\": 1,\n" +
                    "\t\t\t\"cId\": 2514\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"cName\": \"Mox Opal\",\n" +
                    "\t\t\t\"qty\": 2,\n" +
                    "\t\t\t\"cId\": 3792\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"cName\": \"Tezzeret, Agent of Bolas\",\n" +
                    "\t\t\t\"qty\": 1,\n" +
                    "\t\t\t\"cId\": 15132\n" +
                    "\t\t}]\n" +
                    "\t}]\n" +
                    "}";
            result = new JSONObject(data);
        }catch (JSONException e){
            e.printStackTrace();
        }
         /*----------------------------------*/

        //Tournois-drip down
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<TournamentItem> adapter = new ArrayAdapter<>(view.getContext(),
                R.layout.support_simple_spinner_dropdown_item, new TournamentItem().mesPrete_tournamentItems());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tournamentId = ((TournamentItem) parent.getSelectedItem()).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //tabs:je prete | on me prete | il me manque
        RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //TODO idee: comment afficher different view en fonction de switch??
                switch (checkedId){
                    case R.id.jePrete:
                        break;
                    case R.id.onMePrete:
                        //test
                        addonMePrete();
                        break;
                    case R.id.ilMeManque:
                        break;
                    default:
                        Log.d(TAG,"How to monitor????");
                        break;
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void mesPretsTask(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://teamrenaissance.fr/loan?request=mesprets";

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "response: " + response.toString());
                        result = response;
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

        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("GET");
        queue.add(request);
    }

    private void addonMePrete(){
        RelativeLayout relative = getView().findViewById(R.id.mesprets_form);

        TextView user = new TextView(getContext());
        user.setId(Integer.valueOf(1));
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, getView().findViewById(R.id.radiogroup).getId());
        lp.leftMargin= 50;
        user.setLayoutParams(lp);
        user.setText("Test");
        user.setTextSize(15);
        user.setTextColor(Color.BLUE);
        relative.addView(user);
    }
}
