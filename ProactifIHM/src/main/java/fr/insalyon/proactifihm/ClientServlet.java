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
import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modele.Client;
import service.Service;

/**
 *
 * @author caoxu
 */
@WebServlet(name = "ClientServlet", urlPatterns = {"/ClientServlet"})
public class ClientServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JpaUtil.init();
        HttpSession session = request.getSession(true);
        session.setAttribute("x","test");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Service sv = new Service();
        String action = request.getParameter("action");
        
        if ("connecter".equals(action)) {
            String mail = request.getParameter("login");
            String password = request.getParameter("password");
            connexionClient(out,mail,password,session,sv);
            System.out.println(session.getAttribute("x"));
        } else if ("inscription".equals(action)) {
            
            inscriptionClient(request,out,sv);
            System.out.println(session.getAttribute("x"));
        }
        out.close();
        JpaUtil.destroy();
    }

    
    protected void connexionClient(PrintWriter out, String mail, String pw, HttpSession session, Service s) {
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject j = new JsonObject();
        Client c;
        try {
            
            c = s.connexionClient(mail, pw);
            session.setAttribute("loggedIn", true);
            j.addProperty("connexion",true);
            j.addProperty("id",c.getId());
            session.setAttribute("idUser", c.getId());
            j.addProperty("nom",c.getNom());
            j.addProperty("prenom",c.getPrenom());
            j.addProperty("mail",c.getMail());
            session.setAttribute("mail",c.getMail());
            j.addProperty("rue",c.getRue());
            j.addProperty("ville",c.getVille());
            j.addProperty("pays",c.getPays());
            j.addProperty("numero",c.getNumero());
            j.addProperty("lat",c.getLat());
            j.addProperty("lon",c.getLon());
            session.setAttribute("password",pw);
        } catch (Exception ex) {
            j.addProperty("connexion",false);
            session.setAttribute("loggedIn", "false");
        }
        
        JsonObject container = new JsonObject();
        container.add("user",j);
        out.println(gson.toJson(container));
    }
    
    protected void inscriptionClient(HttpServletRequest request, PrintWriter out, Service s) {
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
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
