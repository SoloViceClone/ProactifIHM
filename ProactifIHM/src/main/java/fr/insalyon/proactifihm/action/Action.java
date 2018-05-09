/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.proactifihm.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Client;
import service.Service;
/**
 *
 * @author victor
 */
public class Action {
    
    public static void connnexion(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject json = new JsonObject();
        
        Client c;
        Service s = new Service();
        try{
            c = s.connexionClient(request.getParameter("mail"), request.getParameter("mdp"));
            json.addProperty("connexion", Boolean.TRUE);
        } catch (Exception ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
            json.addProperty("connexion", Boolean.FALSE);
        }
        try {
            response.getWriter().println(gson.toJson(json));
            response.getWriter().close();
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void inscription(String civilite, String nom, String prenom,String mdp, int jour, int mois,int annee, String rue, 
            String ville, String pays, String numero, String mail)
    {
        Client c = new Client(civilite, nom, prenom, mdp, jour, mois, annee, rue, ville, pays, numero, mail);
        Service s = new Service();
        
        try {
            s.inscriptionClient(c);
        } catch (Exception ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void inscription1(HttpServletRequest request, HttpServletResponse response) {
        
    }
    
}
