/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.proactifihm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.Animal;
import modele.Employe;
import modele.Intervention;
import modele.Livraison;

/**
 *
 * @author Victor Lezaud
 */
public class Serialisation {

    public static JsonElement prenomNom(Employe employe) {
        JsonObject json = new JsonObject();
        
        json.addProperty("prenom", employe.getPrenom());
        json.addProperty("nom", employe.getNom());
        
        return json;
    }

    public static JsonElement interventions(List<Intervention> interventions) {
        JsonArray jsonListe = new JsonArray();
        
        for(Intervention i : interventions)
        {
            JsonObject item = intervention(i);   
            jsonListe.add(item);
        }
        return jsonListe;
    }
    
    public static JsonObject intervention(Intervention i) {

        JsonObject json = new JsonObject();
        json.addProperty("civClient", i.getClient().getCivilite());
        json.addProperty("prenomClient", i.getClient().getPrenom());
        json.addProperty("nomClient", i.getClient().getNom());
        json.addProperty("telClient", i.getClient().getNumero());
        json.addProperty("rueClient", i.getClient().getRue());
        json.addProperty("villeClient", i.getClient().getVille());
        json.addProperty("mailClient", i.getClient().getMail());
        json.addProperty("description", i.getDescription());
        json.addProperty("estFinie", i.isEstFinie());
        json.addProperty("aucunSoucis", i.isAucunSoucis());
        json.addProperty("civEmploye", i.getEmploye().getCivilite());
        json.addProperty("nomEmploye", i.getEmploye().getNom());
        json.addProperty("lon", i.getClient().getLon());
        json.addProperty("lat", i.getClient().getLat());

        if(i instanceof Animal)
        {
            Animal a = (Animal) i;
            json.addProperty("type", "Animal");
            json.addProperty("animal", a.getAnimal());
        } else if(i instanceof Livraison)
        {
            Livraison li = (Livraison) i;
            json.addProperty("type", "Livraison");
            json.addProperty("objet", li.getObjet());
            json.addProperty("entreprise", li.getEntreprise());
        } else
        {
            json.addProperty("type", "Incident");
        }

        return json;
    }

    public static void printJson(PrintWriter writer, JsonObject json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writer.println(gson.toJson(json));
        writer.close();
    }
    
}
