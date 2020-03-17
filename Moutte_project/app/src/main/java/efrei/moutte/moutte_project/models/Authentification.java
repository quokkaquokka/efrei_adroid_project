package efrei.moutte.moutte_project.models;

import java.util.List;

public class Authentification {
    /**
     * La classe Authentification est un singleton, elle ne peut etre authentifier qu'une seule fois.
     * */

    private static Authentification single_authentification = null;
    private String name = null;
    private List<String> villes = null;
    private List<Annonce> annonces = null;
    private String id = null;
    private String email = null;
    private String password = null;


    private Authentification(String email, String password, String id, List<String> villes, List<Annonce> annonces)
    {
        this.email = email;
        this.password = password;
        this.id = id;
        this.annonces = annonces;
        this.villes = villes;
    }

    public static Authentification getInstance()
    {
        return single_authentification;
    }

    public static Authentification instanciate(String email, String password, String id, List<String> villes, List<Annonce> annonces) {
        single_authentification = new Authentification(email, password, id, villes, annonces);
        return single_authentification;
    }

    public String getId() {
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public List<String> getVilles() {
        return villes;
    }

    public List<Annonce> getAnnonces() {
        return annonces;
    }
}
