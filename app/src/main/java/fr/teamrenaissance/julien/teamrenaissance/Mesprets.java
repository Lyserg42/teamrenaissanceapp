package fr.teamrenaissance.julien.teamrenaissance;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.teamrenaissance.julien.teamrenaissance.beans.Card;
import fr.teamrenaissance.julien.teamrenaissance.beans.LoanBorrow;
import fr.teamrenaissance.julien.teamrenaissance.beans.Tournament;
import fr.teamrenaissance.julien.teamrenaissance.utils.MyApplication;
import fr.teamrenaissance.julien.teamrenaissance.utils.TournamentItem;


public class Mesprets extends Fragment {
    public static final String TAG = "Mesprets";

    private int tournamentId = -100;
    private String option = "jePrete";
    JSONObject result;
    List<Tournament> tournamentList = new ArrayList<>();

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

        //par defaut, view Tournois-drip down
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<TournamentItem> adapter = new ArrayAdapter<>(view.getContext(),
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

        //par defaut, view tabs:je prete | on me prete | il me manque
        RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.jePrete:
                        option = "jePrete";
                        addNewViews();//lentCards
                        break;
                    case R.id.onMePrete:
                        option = "onMePrete";
                        addNewViews();//borrowedCards
                        break;
                    case R.id.ilMeManque:
                        option = "ilMeManque";
                        addNewViews();//demandes
                        break;
                    default:
                        Log.d(TAG,"How to monitor????");
                        break;
                }

            }
        });

        //TODO status code 401, comment traiter "session" ??
        // quand on charge cette page, d'abord recuperer les donnees depuis le serveur pour que apres l'on ajouter des views correspondantes en dynamique
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

            /*//TODO
            data = "{\"tournaments\":[]}";*/

            result = new JSONObject(data);
        }catch (JSONException e){
            e.printStackTrace();
        }
         /*---------------delete-------------------*/

        //parser le donnees et donner jsonArray au variable globale tournamentList
        parserResult(result);
        //afficher les <<je prete>> de tous les tournois(id = -100) par defaut
        addNewViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        newViews(new_tournamentList, option);
    }

    private void newViews(List<Tournament> tournaments, String option){
        int img[] = {R.drawable.edit};
        Drawable drawable= ResourcesCompat.getDrawable(getResources(), img[0], null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        // int i =1;
        LinearLayout mesprets_form = getView().findViewById(R.id.mesprets_form);
        if(getView().findViewById(Integer.valueOf(1)) != null){
            mesprets_form.removeView(getView().findViewById(Integer.valueOf(1)));
        }
        LinearLayout new_form = new LinearLayout(getContext());
        new_form.setOrientation(LinearLayout.VERTICAL);
        new_form.setId(Integer.valueOf(1));
        //i++;

        for(Tournament tournament: tournaments){
            TextView tournamentName = new TextView(getContext());
            LinearLayout.LayoutParams tnp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tnp.leftMargin = 50;
            tnp.topMargin = 30;
            tournamentName.setLayoutParams(tnp);

            //tournamentName.setId(Integer.valueOf(i));
            //i++;
            tournamentName.setText(tournament.gettName() + " du " + tournament.getDate());
            tournamentName.setBackgroundColor(Color.LTGRAY);

            new_form.addView(tournamentName);

            if("jePrete".equals(option)) {
                for (LoanBorrow lb : tournament.getLentCards()) {
                    TextView userName = new TextView(getContext());
                    LinearLayout.LayoutParams unp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    unp.leftMargin = 50;
                    userName.setLayoutParams(unp);
                    //userName.setId(Integer.valueOf(i));
                    //i++;
                    userName.setText(lb.getuName());
                    userName.setTextColor(Color.parseColor("#c8e8ff"));
                    userName.setCompoundDrawables(null, null, drawable, null);//set drawableRight
                    userName.setCompoundDrawablePadding(15);
                    new_form.addView(userName);

                    for (Card c : lb.getCards()) {
                        TextView card = new TextView(getContext());
                        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        cp.leftMargin = 50;
                        card.setLayoutParams(cp);

                        card.setText(c.getQty() + " " + c.getcName());
                        new_form.addView(card);
                    }
                }
            }else if("onMePrete".equals(option)){
                for (LoanBorrow lb : tournament.getBorrowedCards()) {
                    TextView userName = new TextView(getContext());
                    LinearLayout.LayoutParams unp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    unp.leftMargin = 50;
                    userName.setLayoutParams(unp);
                    //userName.setId(Integer.valueOf(i));
                    //i++;
                    userName.setText(lb.getuName());
                    userName.setTextColor(Color.parseColor("#c8e8ff"));
                    userName.setCompoundDrawables(null, null, drawable, null);//set drawableRight
                    userName.setCompoundDrawablePadding(15);
                    new_form.addView(userName);

                    for (Card c : lb.getCards()) {
                        TextView card = new TextView(getContext());
                        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        cp.leftMargin = 50;
                        card.setLayoutParams(cp);

                        card.setText(c.getQty() + " " + c.getcName());
                        new_form.addView(card);
                    }
                }
            }else if("ilMeManque".equals(option)){
                for (Card c : tournament.getDemands()) {
                    TextView card = new TextView(getContext());
                    LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    cp.leftMargin = 50;
                    card.setLayoutParams(cp);

                    card.setText(c.getQty() + " " + c.getcName());
                    new_form.addView(card);
                }
            }

        }

        mesprets_form.addView(new_form);

    }



    private void parserResult(JSONObject result){
        try {
            JSONArray array = result.getJSONArray("tournaments");

            for(int i=0; i< array.length(); i++){
                Tournament tournament = new Tournament();
                tournament.setDate(array.getJSONObject(i).getString("date"));
                tournament.settId(array.getJSONObject(i).getInt("tId"));
                tournament.settName(array.getJSONObject(i).getString("tName"));

                List<LoanBorrow> borrowsList = new ArrayList<>();
                for(int b= 0; b< array.getJSONObject(i).getJSONArray("borrowedCards").length(); b++){
                    LoanBorrow borrowed = new LoanBorrow();
                    borrowed.setuId(array.getJSONObject(i).getJSONArray("borrowedCards").getJSONObject(b).getInt("uId"));
                    borrowed.setuName(array.getJSONObject(i).getJSONArray("borrowedCards").getJSONObject(b).getString("uName"));
                    List<Card> borrowedCards = new ArrayList<>();
                    for(int bc= 0; bc< array.getJSONObject(i).getJSONArray("borrowedCards").getJSONObject(b).getJSONArray("cards").length(); bc++){
                        Card c1 = new Card();
                        c1.setcId((array.getJSONObject(i).getJSONArray("borrowedCards").getJSONObject(b).getJSONArray("cards")).getJSONObject(bc).getInt("cId"));
                        c1.setcName((array.getJSONObject(i).getJSONArray("borrowedCards").getJSONObject(b).getJSONArray("cards")).getJSONObject(bc).getString("cName"));
                        c1.setQty((array.getJSONObject(i).getJSONArray("borrowedCards").getJSONObject(b).getJSONArray("cards")).getJSONObject(bc).getInt("qty"));
                        borrowedCards.add(c1);
                    }
                    borrowed.setCards(borrowedCards);
                    borrowsList.add(borrowed);
                }
                tournament.setBorrowedCards(borrowsList);

                List<LoanBorrow> lentsList = new ArrayList<>();
                for(int l= 0; l< array.getJSONObject(i).getJSONArray("lentCards").length(); l++){
                    LoanBorrow lent = new LoanBorrow();
                    lent.setuId(array.getJSONObject(i).getJSONArray("lentCards").getJSONObject(l).getInt("uId"));
                    lent.setuName(array.getJSONObject(i).getJSONArray("lentCards").getJSONObject(l).getString("uName"));
                    List<Card> lentCards = new ArrayList<>();
                    for(int lc= 0; lc< array.getJSONObject(i).getJSONArray("lentCards").getJSONObject(l).getJSONArray("cards").length(); lc++){
                        Card c2 = new Card();
                        c2.setcId((array.getJSONObject(i).getJSONArray("lentCards").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getInt("cId"));
                        c2.setcName((array.getJSONObject(i).getJSONArray("lentCards").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getString("cName"));
                        c2.setQty((array.getJSONObject(i).getJSONArray("lentCards").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getInt("qty"));
                        lentCards.add(c2);
                    }
                    lent.setCards(lentCards);
                    lentsList.add(lent);
                }
                tournament.setLentCards(lentsList);

                List<Card> demandsCard = new ArrayList<>();
                for(int d= 0; d<array.getJSONObject(i).getJSONArray("demands").length(); d++){
                    Card c3 = new Card();
                    c3.setcId((array.getJSONObject(i).getJSONArray("demands")).getJSONObject(d).getInt("cId"));
                    c3.setcName((array.getJSONObject(i).getJSONArray("demands")).getJSONObject(d).getString("cName"));
                    c3.setQty((array.getJSONObject(i).getJSONArray("demands")).getJSONObject(d).getInt("qty"));
                    demandsCard.add(c3);
                }
                tournament.setDemands(demandsCard);
                tournamentList.add(tournament);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

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

}
