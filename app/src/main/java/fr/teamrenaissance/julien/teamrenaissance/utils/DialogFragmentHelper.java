package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.teamrenaissance.julien.teamrenaissance.R;
import fr.teamrenaissance.julien.teamrenaissance.beans.Card;
import fr.teamrenaissance.julien.teamrenaissance.beans.Dialog;

public class DialogFragmentHelper extends DialogFragment{
    public static final String TAG = "Dialog";

    public static DialogFragmentHelper newInstance(Dialog dialogContent){
        DialogFragmentHelper f = new DialogFragmentHelper();
        Bundle args = new Bundle();
        args.putSerializable("content", (Serializable) dialogContent);
        f.setArguments(args);//passer la valeur par setArguments
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //recuperer le parametre
        final Dialog dialog = (Dialog) getArguments().getSerializable("content");

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);//pour ne pouvoir pas cliquer en dehors de la dialogue
        final View view = inflater.inflate(R.layout.dialog, container, false);

        LinearLayout linearLayout =view.findViewById(R.id.dialogContent);

        TextView title = new TextView(getContext());
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 130);
        title.setLayoutParams(tp);
        title.setText(dialog.getTitle());
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        title.setBackgroundResource(R.color.colorAccent);
        linearLayout.addView(title);

        final List<Card> cards = dialog.getCards();

        // variable i: on besoin de donner l'id au nouveux views, pour apres on peut recuperer des valeurs de ces nouveaux views
        int i =1;
        for(Card card: cards){
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            linearLayout.addView(relativeLayout);
            EditText cardQty = new EditText(getContext());
            RelativeLayout.LayoutParams cp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            cp.leftMargin= 50;
            cp.topMargin= 15;
            cardQty.setLayoutParams(cp);
            cardQty.setId(Integer.valueOf(i));

            cardQty.setText(String.valueOf(card.getQty()));
            cardQty.setInputType(InputType.TYPE_CLASS_NUMBER);
            cardQty.setBackgroundResource(R.drawable.edittext_selector);
            relativeLayout.addView(cardQty);

            TextView cardName = new TextView(getContext());
            RelativeLayout.LayoutParams cnp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            cnp.topMargin= 50;
            cnp.addRule(RelativeLayout.RIGHT_OF, cardQty.getId());
            cardName.setLayoutParams(cnp);
            cardName.setText(card.getcName());
            cardName.setGravity(Gravity.CENTER_HORIZONTAL);
            relativeLayout.addView(cardName);
            i++;
        }

        Button btn_cancel = (Button) view.findViewById(R.id.cancel) ;
        Button btn_confirm = (Button) view.findViewById(R.id.confirm);
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();//fermer Dialog
            }
        } );
        btn_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               //reecrire la liste de card d'objet Dialog
                List<Card> card_newQty = new ArrayList<Card>();

                for(int j= 0; j< cards.size(); j++){
                    //faire attention ici c'est view(la meme avec la ligne 56) mais pas v(argument de la methode onClick)
                    System.out.println(".......apres modification=="+((EditText)view.findViewById(Integer.valueOf(j+1))).getText() +"&"+cards.get(j).getcName());
                    Card card = new Card();
                    card.setcId(cards.get(j).getcId());
                    card.setcName(cards.get(j).getcName());
                    card.setQty(Integer.valueOf( ((EditText)view.findViewById(Integer.valueOf(j+1))).getText().toString()) );
                    card_newQty.add(card);
                }
                dialog.setCards(card_newQty);

                //TODO status code: 401
                //envoyer une requete <<put>> au serveur
                modifyTask(dialog);

                //TODO apres fermer dialog, verifier est-ce que les chiffres sur la page <<MES PRETES>> est synchronize ou pas
                //fermer dialog
                dismiss();
            }
        } );

        return view;
    }

    private void modifyTask(Dialog dialog){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://teamrenaissance.fr/loan?request=modifier";

        JSONObject dataJSON = new JSONObject();
        JSONArray cards = new JSONArray();
        try {
            dataJSON.put("tId", dialog.gettId());
            if(!"demande".equals(dialog.getType())){
                dataJSON.put("uId", dialog.getuId());
            }
            dataJSON.put("type", dialog.getType());

            for(Card card1: dialog.getCards()){
                JSONObject card = new JSONObject();

                card.put("cId", card1.getcId());
                card.put("qty", card1.getQty());
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
