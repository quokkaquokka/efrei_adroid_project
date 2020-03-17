package efrei.moutte.moutte_project.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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

public class AnnonceActivity extends AppCompatActivity {
    private List<String> location_types = new ArrayList<String>();
    String objectID = null;

    private JsonObjectRequest request ;
    private RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce);

        // Recoit les donnees tranferrees par le RecyclerViewAdapter
        String ville  = getIntent().getExtras().getString("annonce_ville");
        String loyet = getIntent().getExtras().getString("annonce_loyet");
        String description = getIntent().getExtras().getString("annonce_description");
        String price = getIntent().getExtras().getString("annonce_price");
        String type = getIntent().getExtras().getString("annonce_type");
        String rendement = getIntent().getExtras().getString("annonce_rendement");
        String link = getIntent().getExtras().getString("annonce_link");
        String image_url = getIntent().getExtras().getString("annonce_img") ;
        objectID = getIntent().getExtras().getString("annonce_objectId");

        // initialise les données en recuperant les elements dans le fichier activity_annonce.xml grâce aux ids
        TextView tv_ville = findViewById(R.id.annonce_ville);
        TextView tv_loyet = findViewById(R.id.annonce_loyet);
        TextView tv_description = findViewById(R.id.annonce_description);
        TextView tv_price= findViewById(R.id.annonce_price);
        TextView tv_type  = findViewById(R.id.annonce_type) ;
        TextView tv_rendement  = findViewById(R.id.annonce_redement);
        TextView tv_link = findViewById(R.id.annonce_link);
        ImageView img = findViewById(R.id.annonce_img);

        // met une valeur dans les différents element graphique
        tv_ville.setText(ville);
        tv_loyet.setText(loyet);
        tv_description.setText(description);
        tv_price.setText(price);
        tv_type.setText(type);
        tv_rendement.setText(rendement);
        tv_link.setText(link);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        // utilise Glide pour mettre l'image
        Glide.with(this).load(image_url).apply(requestOptions).into(img);

        if(Authentification.getInstance() == null) {
            LinearLayout linear_layout = findViewById(R.id.content_connected);
            linear_layout.setVisibility(View.INVISIBLE);

            TextView tv_warning = findViewById(R.id.annonce_warning);
            tv_warning.setText("Si vous souhaitez, entrer des informations supplémentaires dans l'annonce du bien. Veuillez-vous connecter!");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
     * checkbox : quand on clique sur un item, l'item est rajouté à la liste.
     * */
    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkbox_location_nu:
                if (checked){
                    location_types.add("Location nu");
                }
                else
                    break;
            case R.id.checkbox_location_meuble:
                if (checked) {
                    location_types.add("Location meublée");
                }
                else
                    break;
            case R.id.checkbox_location_meuble_lcd:
                if (checked) {
                    location_types.add("Location meublée et courte durée");
                }
                else
                    break;
            case R.id.checkbox_location_cd:
                if (checked) {
                    location_types.add("Location courte durée");
                }
                else
                    break;

        }
    }
    /**
     * onSubmit : la fonction est appele lorsqu'on clique sur le boutton 'enregistrer'
     * La fonction sauvegarde les donnees en base que l'utilisateur a entree.
     * */
    public void onSubmit(View view) throws JSONException {

        EditText et_travaux = findViewById(R.id.editText_travaux);
        EditText et_montant = findViewById(R.id.editText_Montant);

        String travaux = et_travaux.getText().toString();
        String montant = et_montant.getText().toString();

        jsonrequest(travaux, montant,location_types);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    /**
     * jsonrequest : la fonction est appele dans onSubmit
     * La fonction fait la requete d'update de l'utilisateur sur l'API, en entrant les nouvelles données.
     * Ce n'est pas une modification partiel de l'utilisateur, il faut donc envoyer toute les informations afin de ne pas ecraser les autres donnees
     * */
    private void jsonrequest(String travaux, String montant, List<String> location_types) throws JSONException {
        String user_id = Authentification.getInstance().getId();
        String email = Authentification.getInstance().getEmail();
        String password = Authentification.getInstance().getPassword();
        List<String> villes = Authentification.getInstance().getVilles();
        List<Annonce> annonces = Authentification.getInstance().getAnnonces();
        // URL de l'api
        String JSON_URL = "https://9kmebpt3vb-dsn.algolia.net/1/indexes/user/" + user_id+ "?x-algolia-application-id=9KMEBPT3VB&x-algolia-api-key=" + Constante.getAlgolia_key_admin;

        // creation d'une nouvelle annonce pour la sauvegarde dans la liste des annonces de l'utilisateur
        // construction de l'object json a envoyer a l'API
        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);
        JSONArray jvilles = new JSONArray();
        for(int i = 0; i < villes.size(); i++){
            jvilles.put(villes.get(i));
        }
        params.put("villes", jvilles);

        JSONObject jannonce = new JSONObject();
        jannonce.put("objectID", objectID);
        jannonce.put("montant", montant);
        jannonce.put("travaux", travaux);
        JSONArray jlocation_types = new JSONArray();
        for(int i = 0; i < location_types.size(); i++){
            jlocation_types.put(location_types.get(i));
        }
        jannonce.put("location_types", jlocation_types);


        JSONArray jannonces = new JSONArray();
        jannonces.put(jannonce);
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



        request = new JsonObjectRequest(Request.Method.PUT, JSON_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // envoie un toast si la donnee a reussi a etre inserer en base de donnee
                Context context = getApplicationContext();
                CharSequence text = "Vous avez enregistré les données!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getApplicationContext();
                // Une erreur a l'utilisateur si la donnee n'est pas sauvegarde en base
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

        requestQueue = Volley.newRequestQueue(AnnonceActivity.this);
        requestQueue.add(request) ;


    }
}
