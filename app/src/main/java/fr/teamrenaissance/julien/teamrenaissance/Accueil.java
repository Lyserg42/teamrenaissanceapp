package fr.teamrenaissance.julien.teamrenaissance;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.teamrenaissance.julien.teamrenaissance.beans.Card;
import fr.teamrenaissance.julien.teamrenaissance.beans.Dialog;
import fr.teamrenaissance.julien.teamrenaissance.beans.LoanBorrow;
import fr.teamrenaissance.julien.teamrenaissance.beans.Tournament;
import fr.teamrenaissance.julien.teamrenaissance.utils.DialogFragmentHelper;
import fr.teamrenaissance.julien.teamrenaissance.utils.ImageAdapter;
import fr.teamrenaissance.julien.teamrenaissance.utils.NoScrollGridView;
import fr.teamrenaissance.julien.teamrenaissance.utils.TournamentItem;

public class Accueil extends Fragment {

    public static final String TAG = "Accueil";

    private int tournamentId = -100;
    List<Tournament> tournamentList = new ArrayList<>();
    View globalView;
    //TODO mettre en variable global pour l'application, les autres pages doient changer aussi
    int type;

    public static Fragment newInstance(){
        Accueil fragment = new Accueil();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accueil,null);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalView = view;

        type =1;
        final TextView textView = view.findViewById(R.id.type);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO optimiser
                if(type == 2){
                    textView.setText("en texte");
                    type = 1;
                    accueilTask();
                }else {
                    type = 2;
                    textView.setText("en image");
                    accueilTask();
                }
            }
        });

        accueilTask();

    }

    private void updateView(){

        //afficher les <<je prete>> de tous les tournois(id = -100) par defaut
        addNewViews();

        Spinner spinner = globalView.findViewById(R.id.spinner);
        ArrayAdapter<TournamentItem> adapter = new ArrayAdapter<>(globalView.getContext(),
                R.layout.support_simple_spinner_dropdown_item, new TournamentItem().mesPrete_tournamentItems());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tournamentId = ((TournamentItem) parent.getSelectedItem()).getId();
                addNewViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void newViews(List<Tournament> tournaments){

        int img[] = {R.drawable.edit};
        Drawable drawable= ResourcesCompat.getDrawable(getResources(), img[0], null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        LinearLayout dynamique_form = getView().findViewById(R.id.dynamique_form);
        dynamique_form.removeAllViews();

        for(final Tournament tournament: tournaments){
            TextView tournamentName = new TextView(getContext());
            Log.i(TAG, tournament.gettName());
            LinearLayout.LayoutParams tnp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            tnp.leftMargin = 50;
            tnp.topMargin = 30;
            tournamentName.setLayoutParams(tnp);

            tournamentName.setText(tournament.gettName() + " du " + tournament.getDate());
            tournamentName.setBackgroundColor(Color.LTGRAY);

            dynamique_form.addView(tournamentName);

            for (final LoanBorrow lb : tournament.getLentCards()) {
                TextView userName = new TextView(getContext());
                LinearLayout.LayoutParams unp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                unp.leftMargin = 50;
                userName.setLayoutParams(unp);
                userName.setText(lb.getuName());
                userName.setTextColor(Color.parseColor("#000000"));
                userName.setTextSize(17);
                userName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                dynamique_form.addView(userName);

                if(type == 2) {//text
                    int id = 11;
                    for (Card c : lb.getCards()) {
                        TextView card = new TextView(getContext());
                        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        cp.leftMargin = 50;
                        card.setLayoutParams(cp);

                        card.setText(c.getQty() + " " + c.getcName());
                        dynamique_form.addView(card);
                    }
                }else if(type == 1) {//image
                    //GridView gridView = new GridView(getContext());
                    NoScrollGridView gridView = new NoScrollGridView(getContext());
                    gridView.setNumColumns(4);
                    GridView.LayoutParams glp = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
                    gridView.setLayoutParams(glp);
                    ImageAdapter imageAdapter = new ImageAdapter(getContext(), lb.getCards());
                    gridView.setAdapter(imageAdapter);
                    dynamique_form.addView(gridView);
                }

                Button button = new Button(getContext());
                LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                bp.leftMargin = 50;
                button.setLayoutParams(bp);
                button.setText("Prêter des cartes");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialogContent = new Dialog();
                        dialogContent.settId(tournament.gettId());
                        dialogContent.setuId(lb.getuId());
                        dialogContent.setType("preter");
                        dialogContent.setTitle("Prêt à "+lb.getuName()+ "pour le "+tournament.gettName());
                        dialogContent.setCards(lb.getCards());

                        DialogFragment dialog = DialogFragmentHelper.newInstance(dialogContent);
                        dialog.show(getFragmentManager(),"dialog");
                    }
                });
                dynamique_form.addView(button);
            }
        }
    }


    private void addNewViews(){
        List<Tournament> new_tournamentList = new ArrayList<>();
        if(-100 == tournamentId){
            new_tournamentList = tournamentList;
        }else {
            for(Tournament tournament: tournamentList){
                if(tournamentId == tournament.gettId()){
                    new_tournamentList.add(tournament);
                }
            }

        }
        newViews(new_tournamentList);
    }


    private void parserResult(JSONObject result){
        tournamentList.clear();



        try {

            JSONArray array = result.getJSONArray("tournaments");

            for(int i=0; i< array.length(); i++){
                Tournament tournament = new Tournament();
                tournament.setDate(array.getJSONObject(i).getString("date"));
                tournament.settId(array.getJSONObject(i).getInt("tId"));
                tournament.settName(array.getJSONObject(i).getString("tName"));

                //demandes
                List<LoanBorrow> lentsList = new ArrayList<>();
                for(int l= 0; l< array.getJSONObject(i).getJSONArray("demandes").length(); l++){
                    LoanBorrow lent = new LoanBorrow();
                    lent.setuId(array.getJSONObject(i).getJSONArray("demandes").getJSONObject(l).getInt("uId"));
                    lent.setuName(array.getJSONObject(i).getJSONArray("demandes").getJSONObject(l).getString("uName"));
                    List<Card> lentCards = new ArrayList<>();
                    for(int lc= 0; lc< array.getJSONObject(i).getJSONArray("demandes").getJSONObject(l).getJSONArray("cards").length(); lc++){
                        Card c2 = new Card();
                        c2.setcId((array.getJSONObject(i).getJSONArray("demandes").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getInt("cId"));
                        c2.setcName((array.getJSONObject(i).getJSONArray("demandes").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getString("cName"));
                        c2.setQty((array.getJSONObject(i).getJSONArray("demandes").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getInt("qty"));
                        c2.setImg((array.getJSONObject(i).getJSONArray("demandes").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getString("img"));
                        lentCards.add(c2);
                    }
                    lent.setCards(lentCards);
                    lentsList.add(lent);
                }
                tournament.setLentCards(lentsList);
                tournamentList.add(tournament);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void accueilTask(){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://www.teamrenaissance.fr/loan";
        JSONObject dataJSON = new JSONObject();
        try {
            dataJSON.put("typeRequest","demandes");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dataJSON,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"Response getDemandes Accueil: "+response);
                        parserResult(response);
                        updateView();
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
