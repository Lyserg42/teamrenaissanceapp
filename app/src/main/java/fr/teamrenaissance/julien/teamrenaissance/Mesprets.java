package fr.teamrenaissance.julien.teamrenaissance;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.DialogFragment;

import fr.teamrenaissance.julien.teamrenaissance.beans.Card;
import fr.teamrenaissance.julien.teamrenaissance.beans.Dialog;
import fr.teamrenaissance.julien.teamrenaissance.beans.LoanBorrow;
import fr.teamrenaissance.julien.teamrenaissance.beans.Tournament;
import fr.teamrenaissance.julien.teamrenaissance.utils.DialogFragmentHelper;
import fr.teamrenaissance.julien.teamrenaissance.utils.ImageAdapter;
import fr.teamrenaissance.julien.teamrenaissance.utils.NoScrollGridView;
import fr.teamrenaissance.julien.teamrenaissance.utils.TournamentItem;


public class Mesprets extends Fragment {
    public static final String TAG = "Mesprets";

    private int tournamentId = -100;
    private String option = "jePrete";
    List<Tournament> tournamentList = new ArrayList<>();
    int type;

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


        type =1;
        final TextView textView = view.findViewById(R.id.type);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO optimiser
                if(type == 2){
                    textView.setText("Texte");
                    type = 1;
                    addNewViews();
                }else {
                    type = 2;
                    textView.setText("Images");
                    addNewViews();
                }
            }
        });

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
                        mesPretsTask(); //lentCards
                        break;
                    case R.id.onMePrete:
                        option = "onMePrete";
                        mesPretsTask();//borrowedCards
                        break;
                    case R.id.ilMeManque:
                        option = "ilMeManque";
                        mesPretsTask();//demandes
                        break;
                    default:
                        Log.d(TAG,"How to monitor????");
                        break;
                }

            }
        });

        mesPretsTask();
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

        LinearLayout dynamique_form = getView().findViewById(R.id.dynamique_form);
        dynamique_form.removeAllViews();

        for(final Tournament tournament: tournaments){
            TextView tournamentName = new TextView(getContext());
            LinearLayout.LayoutParams tnp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tnp.leftMargin = 50;
            tnp.topMargin = 30;
            tournamentName.setLayoutParams(tnp);

            tournamentName.setText(tournament.gettName() + " du " + tournament.getDate());
            tournamentName.setBackgroundColor(Color.LTGRAY);

            dynamique_form.addView(tournamentName);

            if("jePrete".equals(option)) {
                for (final LoanBorrow lb : tournament.getLentCards()) {
                    TextView userName = new TextView(getContext());
                    LinearLayout.LayoutParams unp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    unp.leftMargin = 50;
                    userName.setLayoutParams(unp);

                    userName.setText(lb.getuName());
                    userName.setTextColor(Color.parseColor("#000000"));
                    userName.setTextSize(17);
                    userName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    userName.setCompoundDrawables(null, null, drawable, null);//set drawableRight
                    userName.setCompoundDrawablePadding(15);
                    userName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialogContent = new Dialog();
                            dialogContent.settId(tournament.gettId());
                            dialogContent.setuId(lb.getuId());
                            dialogContent.setType("pret");
                            dialogContent.setTitle("Modifier");
                            dialogContent.setCards(lb.getCards());

                            DialogFragment dialog = DialogFragmentHelper.newInstance(dialogContent);
                            dialog.show(getFragmentManager(),"dialog");
                            getFragmentManager().executePendingTransactions();
                            dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    mesPretsTask();
                                }
                            });
                        }
                    });
                    dynamique_form.addView(userName);

                    if(type == 2) {//text
                        for (Card c : lb.getCards()) {
                            TextView card = new TextView(getContext());
                            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            cp.leftMargin = 50;
                            card.setLayoutParams(cp);

                            card.setText(c.getQty() + " " + c.getcName());
                            dynamique_form.addView(card);
                        }
                    }else if(type == 1) {//image
                        NoScrollGridView gridView = new NoScrollGridView(getContext());
                        gridView.setNumColumns(4);
                        GridView.LayoutParams glp = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
                        gridView.setLayoutParams(glp);
                        ImageAdapter imageAdapter = new ImageAdapter(getContext(), lb.getCards());
                        gridView.setAdapter(imageAdapter);
                        //show large image
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                LayoutInflater inflater = LayoutInflater.from(getContext());
                                View imgEntryView = inflater.inflate(R.layout.maxpicture, null);
                                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                ImageView img = imgEntryView.findViewById(R.id.large_image);
                                Glide.with(getContext()).load(lb.getCards().get(position).getImg()).asBitmap().into(img);
                                dialog.setView(imgEntryView);
                                dialog.show();
                                imgEntryView.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View paramView) {
                                        dialog.cancel();
                                    }
                                });
                            }
                        });
                        dynamique_form.addView(gridView);
                    }

                }
            }else if("onMePrete".equals(option)){
                for (final LoanBorrow lb : tournament.getBorrowedCards()) {
                    TextView userName = new TextView(getContext());
                    LinearLayout.LayoutParams unp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    unp.leftMargin = 50;
                    userName.setLayoutParams(unp);

                    userName.setText(lb.getuName());
                    userName.setTextColor(Color.parseColor("#000000"));
                    userName.setTextSize(17);
                    userName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    userName.setCompoundDrawables(null, null, drawable, null);//set drawableRight
                    userName.setCompoundDrawablePadding(15);
                    userName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialogContent = new Dialog();
                            dialogContent.settId(tournament.gettId());
                            dialogContent.setuId(lb.getuId());
                            dialogContent.setType("emprunt");
                            dialogContent.setTitle("Modifier");
                            dialogContent.setCards(lb.getCards());

                            DialogFragment dialog = DialogFragmentHelper.newInstance(dialogContent);
                            dialog.show(getFragmentManager(),"dialog");
                            getFragmentManager().executePendingTransactions();
                            dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    mesPretsTask();
                                }
                            });
                        }
                    });
                    dynamique_form.addView(userName);

                    if(type == 2) {//text
                        for (Card c : lb.getCards()) {
                            TextView card = new TextView(getContext());
                            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            cp.leftMargin = 50;
                            card.setLayoutParams(cp);

                            card.setText(c.getQty() + " " + c.getcName());
                            dynamique_form.addView(card);
                        }
                    }else if(type == 1) {//image
                        GridView gridView = new GridView(getContext());
                        gridView.setNumColumns(4);
                        GridView.LayoutParams glp = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
                        gridView.setLayoutParams(glp);
                        ImageAdapter imageAdapter = new ImageAdapter(getContext(), lb.getCards());
                        gridView.setAdapter(imageAdapter);
                        //show large image
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                LayoutInflater inflater = LayoutInflater.from(getContext());
                                View imgEntryView = inflater.inflate(R.layout.maxpicture, null);
                                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                ImageView img = imgEntryView.findViewById(R.id.large_image);
                                Glide.with(getContext()).load(lb.getCards().get(position).getImg()).asBitmap().into(img);
                                dialog.setView(imgEntryView); // 自定义dialog
                                dialog.show();
                                imgEntryView.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View paramView) {
                                        dialog.cancel();
                                    }
                                });
                            }
                        });
                        dynamique_form.addView(gridView);
                    }
                }
            }else if("ilMeManque".equals(option)){
                if(type == 2) {//text
                    for (int i= 0; i< tournament.getDemands().size(); i++) {
                        TextView card = new TextView(getContext());
                        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        cp.leftMargin = 50;
                        card.setLayoutParams(cp);

                        card.setText(tournament.getDemands().get(i).getQty() + " " + tournament.getDemands().get(i).getcName());
                        if(i== 0){
                            card.setCompoundDrawables(null, null, drawable, null);//set drawableRight
                            card.setCompoundDrawablePadding(15);
                            card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Dialog dialogContent = new Dialog();
                                    dialogContent.settId(tournament.gettId());
                                    dialogContent.setType("demande");
                                    dialogContent.setTitle("Modifier");
                                    dialogContent.setCards(tournament.getDemands());

                                    DialogFragment dialog = DialogFragmentHelper.newInstance(dialogContent);
                                    dialog.show(getFragmentManager(),"dialog");

                                    getFragmentManager().executePendingTransactions();
                                    dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            mesPretsTask();
                                        }
                                    });
                                }
                            });
                        }
                        dynamique_form.addView(card);
                    }
                }else if(type == 1) {//image
                    GridView gridView = new GridView(getContext());
                    gridView.setNumColumns(4);
                    GridView.LayoutParams glp = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
                    gridView.setLayoutParams(glp);
                    ImageAdapter imageAdapter = new ImageAdapter(getContext(), tournament.getDemands());
                    gridView.setAdapter(imageAdapter);
                    //show large image
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            View imgEntryView = inflater.inflate(R.layout.maxpicture, null);
                            final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                            ImageView img = imgEntryView.findViewById(R.id.large_image);
                            Glide.with(getContext()).load(tournament.getDemands().get(position).getImg()).asBitmap().into(img);
                            dialog.setView(imgEntryView);
                            dialog.show();
                            imgEntryView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View paramView) {
                                    dialog.cancel();
                                }
                            });
                        }
                    });
                    dynamique_form.addView(gridView);

                    if(tournament.getDemands().size() > 0) {
                        Button button = new Button(getContext());
                        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        bp.leftMargin = 50;
                        button.setLayoutParams(bp);
                        button.setText("Modifier");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog dialogContent = new Dialog();
                                dialogContent.settId(tournament.gettId());
                                dialogContent.setType("demande");
                                dialogContent.setTitle("Modifier");
                                dialogContent.setCards(tournament.getDemands());

                                DialogFragment dialog = DialogFragmentHelper.newInstance(dialogContent);
                                dialog.show(getFragmentManager(), "dialog");

                                getFragmentManager().executePendingTransactions();
                                dialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        mesPretsTask();
                                    }
                                });
                            }
                        });

                        dynamique_form.addView(button);
                    }
                }
            }

        }

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
                        c1.setImg((array.getJSONObject(i).getJSONArray("borrowedCards").getJSONObject(b).getJSONArray("cards")).getJSONObject(bc).getString("img"));
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
                        c2.setImg((array.getJSONObject(i).getJSONArray("lentCards").getJSONObject(l).getJSONArray("cards")).getJSONObject(lc).getString("img"));
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
                    c3.setImg((array.getJSONObject(i).getJSONArray("demands")).getJSONObject(d).getString("img"));
                    demandsCard.add(c3);
                }
                tournament.setDemands(demandsCard);
                tournamentList.add(tournament);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }


    public void mesPretsTask(){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://www.teamrenaissance.fr/loan";
        JSONObject dataJSON = new JSONObject();
        try {
            dataJSON.put("typeRequest","mesprets");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dataJSON,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"Response getMesPrest mesprets: "+response);
                        parserResult(response);
                        addNewViews();
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
