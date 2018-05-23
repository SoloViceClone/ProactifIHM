/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.proactifihm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modele.Animal;
import modele.Client;
import modele.Incident;
import modele.Intervention;
import modele.Livraison;
import service.Service;

/**
 *
 * @author caoxu
 */
public class ActionClient {
    public static void connexionClient(PrintWriter out, String mail, String pw, HttpSession session, Service s) {
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        Client c;
        try {
            
            c = s.connexionClient(mail, pw);
            session.setAttribute("loggedIn", true);
            j.addProperty("connexion",true);
            j.addProperty("id",c.getId());
            j.addProperty("nom",c.getNom());
            j.addProperty("prenom",c.getPrenom());
            j.addProperty("mail",c.getMail()); 
            j.addProperty("rue",c.getRue());
            j.addProperty("ville",c.getVille());
            j.addProperty("pays",c.getPays());
            j.addProperty("numero",c.getNumero());
            j.addProperty("lat",c.getLat());
            j.addProperty("lon",c.getLon());
            
            session.setAttribute("loggedIn", true);
            session.setAttribute("client",c);
        } catch (Exception ex) {
            j.addProperty("connexion",false);
            session.setAttribute("loggedIn", false);
        }
        
        JsonObject container = new JsonObject();
        container.add("user",j);
        out.println(gson.toJson(container));
    }
    
    public static void inscriptionClient(HttpServletRequest request, PrintWriter out, Service s) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        
        String mail = request.getParameter("email");
        String civilite = request.getParameter("civilite");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String dateNaissance = request.getParameter("dateNaissance");
        System.out.println(dateNaissance);
        int jour = Integer.parseInt(dateNaissance.substring(8));
        System.out.println(jour);
        int annee = Integer.parseInt(dateNaissance.substring(0,4));
        System.out.println(annee);
        int mois = Integer.valueOf(dateNaissance.substring(5,7));
        System.out.println(mois);
        String rue = request.getParameter("rue");
        String ville = request.getParameter("ville");
        String codePostal = request.getParameter("codePostal");
        String pays = request.getParameter("pays");
        String telephone = request.getParameter("telephone");
        String mdp = request.getParameter("mdp");
        String confmdp = request.getParameter("confmdp");
        if (mdp.equals(confmdp)) {
            Client c = new Client(civilite, nom, prenom, mdp, jour, mois, annee, rue, ville, pays, telephone, mail);
            try {
                s.inscriptionClient(c);
                j.addProperty("valideInscription",true);
            } catch (Exception ex) {
                j.addProperty("valideInscription",false);
            }
        } else {
            j.addProperty("valideInscription",false);
        }
        out.write(gson.toJson(j));
        
    }
    
    public static void getUser(HttpSession session, PrintWriter out, Service s) {
        Client c;
        c = (Client) session.getAttribute("client");
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        
        if (c != null) {
            j.addProperty("loggedIn",true);
            j.addProperty("nom", c.getNom());
            j.addProperty("prenom", c.getPrenom());
            JsonArray ja = new JsonArray();
            List<Intervention> list = s.getHistoriqueClient(c);
            for (Intervention i : list) {
                System.out.println(i);
                JsonObject ji = new JsonObject();
                ji.addProperty("description",i.getDescription());
                ji.addProperty("employeNom",i.getEmploye().getNom());
                ji.addProperty("employePrenom",i.getEmploye().getPrenom());
                ji.addProperty("finie",i.isEstFinie());
                if (i.isEstFinie()) {
                    ji.addProperty("dateFin",i.getDateFin().toString());
                }

                ji.addProperty("horodate",i.getHorodate().toString());
                if (i instanceof Incident) {
                    ji.addProperty("type","Incident");
                } else if (i instanceof Livraison) {
                    ji.addProperty("type","Livraison");
                } else if (i instanceof Animal) {
                    ji.addProperty("type","Animal");
                }
                ja.add(ji);
            }
            j.add("history",ja);
        } else {
            j.addProperty("loggedIn",false);
        }
        out.write(gson.toJson(j));
        
    }
    
    public static void creerInterIncident(HttpSession session, PrintWriter out, Service s, String desc) {
        Intervention i = new Incident(desc);
        Client c = (Client) session.getAttribute("client");
        System.out.println(c.getNom() + " " + c.getPrenom());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        try {
            s.ajouterUneIntervention(i, c);
            j.addProperty("incidentCree",true);
        } catch (Exception ex) {
            j.addProperty("incidentCree",false);
        }
        out.write(gson.toJson(j));
    }
    
    public static void creerInterAnimal(HttpSession session, PrintWriter out, Service s, String desc, String animal) {
        Intervention i = new Animal(desc,animal);
        
        Client c = (Client) session.getAttribute("client");
        System.out.println(c.getNom() + " " + c.getPrenom());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        try {
            s.ajouterUneIntervention(i, c);
            j.addProperty("incidentCree",true);
        } catch (Exception ex) {
            j.addProperty("incidentCree",false);
        }
        out.write(gson.toJson(j));
    }
    
    public static void creerInterLivraison(HttpSession session, PrintWriter out, Service s,
            String desc, String objet, String entreprise) {
        Intervention i = new Livraison(desc,objet,entreprise);
        
        Client c = (Client) session.getAttribute("client");
        System.out.println(c.getNom() + " " + c.getPrenom());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        try {
            s.ajouterUneIntervention(i, c);
            j.addProperty("livraisonCree",true);
        } catch (Exception ex) {
            j.addProperty("livraisonCree",false);
        }
        out.write(gson.toJson(j));
    }
    
    public static void deconnexion(HttpSession session,PrintWriter out) {
        session.removeAttribute("client");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        out.println(gson.toJson(j));
    }
}
