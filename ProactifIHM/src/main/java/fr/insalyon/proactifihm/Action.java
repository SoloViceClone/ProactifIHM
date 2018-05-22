/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.proactifihm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.insalyon.proactifihm.Serialisation;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modele.*;
import service.Service;
/**
 *
 * @author victor
 */
public class Action {

    public static void connexionEmploye(HttpServletRequest request, HttpServletResponse response)
    {
        JsonObject json = new JsonObject();
        
        Employe e;
        Service s = new Service();
        try{
            e = s.connexionEmploye(request.getParameter("login"), request.getParameter("password"));
            json.addProperty("connexion", Boolean.TRUE);
            request.getSession().setAttribute("employe", e);
        } catch (Exception ex) {
            //Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
            json.addProperty("connexion", Boolean.FALSE);
            
        }
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void historique(HttpServletRequest request, HttpServletResponse response) {
        JsonObject json = new JsonObject();
        json.addProperty("connexion", true);
        
        Employe e = (Employe) request.getSession().getAttribute("employe");
        json.add("employe", Serialisation.prenomNom(e));
        
        
        Service s = new Service();
        List<Intervention> interventions = s.getHistoriqueEmploye(e);
        
        json.add("interventions", Serialisation.interventions(interventions));
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deconnexion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static void nonConnecte(HttpServletResponse response) {
        JsonObject json = new JsonObject();
        
        json.addProperty("connexion", false);
        
       try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void choixIntervention(HttpServletRequest request, HttpServletResponse response) {
        JsonObject json = new JsonObject();
        json.addProperty("connexion", true);
        
        HttpSession session = request.getSession();
        Service s = new Service();
        
        Employe e = (Employe) session.getAttribute("employe");
        int i = Integer.parseInt(request.getParameter("indice"));
        
        List<Intervention> interventions = s.getHistoriqueEmploye(e);
        session.setAttribute("intervention", interventions.get(i));
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void interventionCourante(HttpServletRequest request, HttpServletResponse response) {
        JsonObject json = new JsonObject();
        json.addProperty("connexion", true);
        
        Employe e = (Employe) request.getSession().getAttribute("employe");
        json.add("employe", Serialisation.prenomNom(e));
        
        Intervention i = (Intervention) request.getSession().getAttribute("intervention");
        json.add("intervention", Serialisation.intervention(i));
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void validerIntervention(HttpServletRequest request, HttpServletResponse response) {
        JsonObject json = new JsonObject();
        json.addProperty("connexion", true);
        
        Intervention intervention = (Intervention) request.getSession().getAttribute("intervention");
        boolean aucunSoucis = Boolean.parseBoolean(request.getParameter("aucunSoucis"));
        String commentaire = request.getParameter("commentaire");
        
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date dateFin = new Date();
        try {
            Date heureFin = format.parse(request.getParameter("dateFin"));
            dateFin.setHours(heureFin.getHours());
            dateFin.setMinutes(heureFin.getMinutes());
            dateFin.setSeconds(0);
        } catch (ParseException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Service s = new Service();
        try {
            s.terminerIntervention(intervention, aucunSoucis, dateFin, commentaire);
        } catch (Exception ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void tableauDeBord(HttpServletRequest request, HttpServletResponse response) {
        JsonObject json = new JsonObject();
        json.addProperty("connexion", true);
        
        Employe e = (Employe) request.getSession().getAttribute("employe");
        json.add("employe", Serialisation.prenomNom(e));
        
        Service s = new Service();
        List<Intervention> interventions = s.getHistoriqueAllInterventionDuJour();
        json.add("interventions", Serialisation.interventions(interventions));
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void choixInterventionTDB(HttpServletRequest request, HttpServletResponse response) {
        JsonObject json = new JsonObject();
        json.addProperty("connexion", true);
        
        HttpSession session = request.getSession();
        Service s = new Service();
        
        int i = Integer.parseInt(request.getParameter("indice"));
        
        List<Intervention> interventions = s.getHistoriqueAllInterventionDuJour();
        session.setAttribute("intervention", interventions.get(i));
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void focusTableauDeBord(HttpServletRequest request, HttpServletResponse response) {
        JsonObject json = new JsonObject();
        json.addProperty("connexion", true);
        
        Employe e = (Employe) request.getSession().getAttribute("employe");
        json.add("employe", Serialisation.prenomNom(e));
        
        Intervention i = (Intervention) request.getSession().getAttribute("intervention");
        json.add("intervention", Serialisation.intervention(i));
        
        try {
            Serialisation.printJson(response.getWriter(), json);
        } catch (IOException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
}
