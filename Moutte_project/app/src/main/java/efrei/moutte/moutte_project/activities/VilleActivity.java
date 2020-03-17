package efrei.moutte.moutte_project.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import efrei.moutte.moutte_project.R;
import efrei.moutte.moutte_project.models.Annonce;
import efrei.moutte.moutte_project.models.Authentification;
import efrei.moutte.moutte_project.models.Constante;

public class VilleActivity extends AppCompatActivity {
    private ListView mListView;
    private List<String> villes;

    private JsonObjectRequest request ;
    private RequestQueue requestQueue ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villes);

        // vue dynamique, celon si l'utilisateur est connecte la vue sera differente.
        if(Authentification.getInstance() == null) {
            // affiche un warning disant a l'utilisateur de se connecter
             LinearLayout linear_layout = findViewById(R.id.content_connected);
             linear_layout.setVisibility(View.INVISIBLE);
            TextView tv_warning = findViewById(R.id.ville_warning);
            tv_warning.setText("Si vous souhaitez, entrer des informations supplémentaires ou les visualisez. Veuillez-vous connecter!");
        }else
        {
            // affiche la liste des villes de l'utilisateur
            villes = new ArrayList<String>();
            villes = Authentification.getInstance().getVilles();

            mListView = (ListView) findViewById(R.id.lv_villes);

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(VilleActivity.this,
                    android.R.layout.simple_list_item_1, villes);
            mListView.setAdapter(adapter);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /**
     * onOptionsItemSelected : Les actions du menu
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_villes) {
            Intent villesIntent = new Intent(this, VilleActivity.class);
            startActivity(villesIntent);
            return true;
        }
        if (id == R.id.action_login) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            return true;
        }
        if (id == R.id.action_annonces) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * submit : la fonction est appele lorsqu'on clique sur le boutton 'enregistrer'
     * La fonction enregistre la nouvelle ville de l'utilisateur
     * Change de vue, lorsque la réponse arrive
     * */

    public void submit(View view) throws JSONException {
        EditText editText_ville = (EditText) findViewById(R.id.editText_add_ville);
        String ville = editText_ville.getText().toString();

        jsonrequest(ville);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    /**
     * jsonrequest : la fonction est afait la requete a l'API avec PUT pour ajouter la donnee
     * La fonction enregistre la nouvelle ville de l'utilisateur
     * Change de vue, lorsque la réponse arrive
     * Ce n'est pas une modification partiel de l'utilisateur, il faut donc envoyer toute les informations afin de ne pas ecraser les autres donnees
     * */
    private void jsonrequest(String ville) throws JSONException {
        // recuperation des valeurs du singleton
        String user_id = Authentification.getInstance().getId();
        String email = Authentification.getInstance().getEmail();
        String password = Authentification.getInstance().getPassword();
        List<String> villes = Authentification.getInstance().getVilles();
        List<Annonce> annonces = Authentification.getInstance().getAnnonces();
        // l'adresse de l'API
        String JSON_URL = "https://9kmebpt3vb-dsn.algolia.net/1/indexes/user/" + user_id+ "?x-algolia-application-id=9KMEBPT3VB&x-algolia-api-key=" + Constante.getAlgolia_key_admin;

        // ajout la ville a la liste des villes
        villes.add(ville);
        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);
        params.put("annonces", annonces);
        params.put("villes", villes);

        JSONArray jvilles = new JSONArray();
        for(int i = 0; i < villes.size(); i++){
            jvilles.put(villes.get(i));
        }
        params.put("villes", jvilles);

        JSONArray jannonces = new JSONArray();
        for(int i = 0; i < annonces.size(); i++) {
            JSONObject jannonce_tmp = new JSONObject();
            jannonce_tmp.put("objectID", annonces.get(i).getObjectID());
            jannonce_tmp.put("montant", annonces.get(i).getMontant());
            jannonce_tmp.put("travaux", annonces.get(i).getTravaux());

            JSONArray jlocation_types_tmp = new JSONArray();
            for(int j = 0; j < annonces.get(i).getLocations().size(); j++){
                jlocation_types_tmp.put(annonces.get(i).getLocations().get(j));
            }

            jannonce_tmp.put("locations", jlocation_types_tmp);
            jannonces.put(jannonce_tmp);
        }
        params.put("annonces", jannonces);

        // fait la requete avec PUT sur l'API
        request = new JsonObjectRequest(Request.Method.PUT, JSON_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // toast disant a l'utilisateur que la donnee a bien ete enregistrer
                Context context = getApplicationContext();
                CharSequence text = "Vous avez enregistré les données!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // si la donnee n'a pas pu etre enregistre
                Context context = getApplicationContext();
                CharSequence text = "Une erreur est survenue, nous n'avons pas pu enregistré les informations!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        })
        {
            // met un referer pour savoir d'ou vient la requete
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Referer", "https://www.rendementlocatif.com/investissement/annonces");
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(VilleActivity.this);
        requestQueue.add(request) ;


    }

}
