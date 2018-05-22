/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.proactifihm;

import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modele.Employe;
import service.Service;

/**
 *
 * @author caoxu
 */
@WebServlet(name = "ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {
    
    private boolean first = true;

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
        if(first)
        {
            initialisation();
            first=false;
        }
        response.setContentType("application/json");
        String action = request.getParameter("action");
        
        if("connecterEmploye".equals(action))
        {
            Action.connexionEmploye(request, response);
        } 
        else if(request.getSession().getAttribute("employe")!= null)
        {
            if("historiqueIntervention".equals(action))
            {
                Action.historique(request, response);
            }
            else if("deconnexion".equals(action))
            {
                Action.deconnexion(request);
            }
            else if("choixIntervention".equals(action))
            {
                Action.choixIntervention(request, response);
            }
            else if("InterventionCourante".equals(action))
            {
                Action.interventionCourante(request, response);
            }
            else if("ValiderIntervention".equals(action))
            {
                Action.validerIntervention(request, response);
            }
            else if("TableauDeBord".equals(action))
            {
                Action.tableauDeBord(request, response);
            }
            else if("choixInterventionTDB".equals(action))
            {
                Action.choixInterventionTDB(request, response);
            }
            else if("FocusTableauDeBord".equals(action))
            {
                Action.focusTableauDeBord(request, response);
            }
        } 
        else
        {
            Action.nonConnecte(response);
        }
    }
    
    protected void connecter(String login, String password) {
        Service s = new Service();
        try {
            s.connexionClient(login, password);
        } catch (Exception ex) {
            System.out.print("Error");
        }
    }
    
    private void initialisation() {
        JpaUtil.init();
        Service s = new Service();
        try {
            s.peuplerLaBD();
        } catch (Exception ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
