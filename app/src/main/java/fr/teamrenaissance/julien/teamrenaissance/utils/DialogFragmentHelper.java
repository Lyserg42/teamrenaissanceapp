package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.teamrenaissance.julien.teamrenaissance.Login;
import fr.teamrenaissance.julien.teamrenaissance.R;
import fr.teamrenaissance.julien.teamrenaissance.beans.Card;
import fr.teamrenaissance.julien.teamrenaissance.beans.Dialog;

public class DialogFragmentHelper extends DialogFragment{
    public static final String TAG = "Dialog";
    private DialogInterface.OnDismissListener onDismissListener;

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
            //le chiffre doit etre entre 0 et nombre demande
            cardQty.setFilters(new InputFilter[]{ new InputFilterMinMax(0, card.getQty())});
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
                View focusView = null;
                boolean cancel = false;
                //pour assurer que l'input d'editText n'est pas null
                for(int j= 0; j< cards.size(); j++) {
                    if (TextUtils.isEmpty(((EditText)view.findViewById(Integer.valueOf(j+1))).getText())) {
                        EditText editTextView =(EditText)view.findViewById(Integer.valueOf(j+1));
                        editTextView.setError(getString(R.string.error_field_required));
                        focusView = editTextView;
                        cancel = true;
                    }
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    //reecrire la liste de card d'objet Dialog
                    List<Card> card_newQty = new ArrayList<Card>();

                    for (int j = 0; j < cards.size(); j++) {
                        //faire attention ici c'est view(la meme avec la ligne 57) mais pas v(argument de la methode onClick)
                        System.out.println(".......apres modification==" + ((EditText) view.findViewById(Integer.valueOf(j + 1))).getText() + "&" + cards.get(j).getcName());
                        Card card = new Card();
                        card.setcId(cards.get(j).getcId());
                        card.setcName(cards.get(j).getcName());
                        card.setQty(Integer.valueOf(((EditText) view.findViewById(Integer.valueOf(j + 1))).getText().toString()));
                        card_newQty.add(card);
                    }
                    dialog.setCards(card_newQty);

                    String param="modifier";
                    if(dialog.getType().equals("nouveauPret")){
                        param = "preter";
                    }
                    sendRequest(param,getDialogJSON(dialog));


                }
            }
        } );

        return view;
    }


    // add a listener and override the onDismiss
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }

    }

    private JSONObject getDialogJSON(Dialog dialog){

        JSONObject dataJSON = new JSONObject();
        JSONArray cards = new JSONArray();
        try {
            dataJSON.put("tId", dialog.gettId());
            if(!"demande".equals(dialog.getType())){
                dataJSON.put("uId", dialog.getuId());
            }
            if(!"nouveauPret".equals(dialog.getType())) {
                dataJSON.put("type", dialog.getType());
            }

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

        return dataJSON;

    }

    public void sendRequest(final String typeRequest, final JSONObject dataJSON){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://www.teamrenaissance.fr/loan?request="+typeRequest;

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,typeRequest+" reussi");
                        Toast.makeText(getActivity(), "Modification enregistrée.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i(TAG, "error with "+typeRequest+": " + error.getMessage());
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
                return dataJSON.toString().getBytes();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("PUT");
        queue.add(request);
    }

}
