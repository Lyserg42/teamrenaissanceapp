package fr.teamrenaissance.julien.teamrenaissance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.teamrenaissance.julien.teamrenaissance.utils.TournamentItem;

public class Emprunter extends Fragment {

    public static final String TAG = "EmprunterActivity";

    private int tournamentId;
    private String quantity;
    private String cardName;
    private String cardNames;
    private EditText quantityView;
    private AutoCompleteTextView cardView;
    private EditText textArea;
    private Button addButton;
    private Button valideButton;
    View focusView = null;
    boolean cancel = false;

    public static Fragment newInstance(){
        Emprunter fragment = new Emprunter();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emprunter,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Tournois-drip down
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<TournamentItem> adapter = new ArrayAdapter<>(view.getContext(),
                R.layout.support_simple_spinner_dropdown_item, new TournamentItem().emprunter_tournamentItems());
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

        //quantity
        quantityView = (EditText) view.findViewById(R.id.qty_number);

        //carte(avec autoComplete)
        cardView = (AutoCompleteTextView) view.findViewById(R.id.cardName);
        ArrayAdapter<CharSequence> adapterName = ArrayAdapter.createFromResource(view.getContext(),
                R.array.cards, android.R.layout.simple_list_item_1);
        cardView.setAdapter(adapterName);

        //la liste des cartes
        textArea = (EditText) view.findViewById(R.id.textArea);

        //ajouter carte
        addButton = (Button) view.findViewById(R.id.add);
        //valider demande
        valideButton = (Button) view.findViewById(R.id.valide);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel = false;

                //quantity et carte ne peuvent pas etre vide
                quantity = quantityView.getText().toString();
                cardName = cardView.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    quantityView.setError(getString(R.string.error_empty_quantity));
                    focusView = quantityView;
                    cancel = true;
                }
                if (TextUtils.isEmpty(cardName)) {
                    cardView.setError(getString(R.string.error_empty_card));
                    focusView = cardView;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    cardNames = textArea.getText().toString();
                    textArea.setText((!"".equals(cardNames) ? cardNames + "\n" : "") + quantity + " " + cardName);
                    quantityView.setText("");
                    cardView.setText("");
                }
            }
        });

        valideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel = false;

                if(TextUtils.isEmpty(textArea.getText())){
                    textArea.setError(getString(R.string.error_empty_textarea));
                    focusView = textArea;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    valideTask();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //TODO status code: 401
    private void valideTask(){

            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "https://teamrenaissance.fr/loan";

            JSONObject dataJSON = new JSONObject();
            JSONObject card = new JSONObject();
            JSONArray cards = new JSONArray();
            try {
                dataJSON.put("tId", tournamentId);

                cardNames = textArea.getText().toString();
                String[] cartes= cardNames.split("\n|\r");
                for(String carte: cartes){
                    //numeros d'extrait
                    String regEx="[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(carte);

                    //remplacer les numeros par des espaces, et supprimer les espaces de debut et de fin
                    carte = carte.replaceAll("([1-9]+[0-9]*|0)(\\.[\\d]+)?", "").trim();

                    card.put("qty",  Integer.valueOf(m.replaceAll("").trim()));
                    card.put("cName", carte);
                    cards.put(card);
                }

                dataJSON.put("cards", cards);
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