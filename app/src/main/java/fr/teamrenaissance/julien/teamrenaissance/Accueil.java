package fr.teamrenaissance.julien.teamrenaissance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Intent intent = new Intent(this, Login.class);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.home:
                //TODO
                Intent intent = new Intent(this, Profil.class);
                this.startActivity(intent);
                break;
            case R.id.borrow:
                //TODO
                break;
            case R.id.myloans:
                //TODO
                break;
            case R.id.profil:
                //TODO
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
