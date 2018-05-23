/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.proactifihm;

import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Service sv = new Service();
        String action = request.getParameter("action");
        
        if (null != action) switch (action) {
            case "connecter":
                String mail = request.getParameter("login");
                String password = request.getParameter("password");
                ActionClient.connexionClient(out,mail,password,session,sv);
                break;
            case "inscription":
                ActionClient.inscriptionClient(request,out,sv);
                break;
            case "getUser":
                ActionClient.getUser(session,out,sv);
                break;
            case "creerIncident":{
                String desc = request.getParameter("description");
                ActionClient.creerInterIncident(session,out,sv,desc);
                    break;
                }
            case "creerLivraison":{
                String desc = request.getParameter("description");
                String objet = request.getParameter("objet");
                String entreprise = request.getParameter("entreprise");
                ActionClient.creerInterLivraison(session,out,sv,desc,objet,entreprise);
                    break;
                }
            case "creerAnimal":{
                String desc = request.getParameter("description");
                String animal = request.getParameter("animal");
                ActionClient.creerInterAnimal(session,out,sv,desc,animal);
                    break;
                }
            case "deconnexion":
                ActionClient.deconnexion(session,out);
                break;
            default:
                break;
        }
        out.close();
        JpaUtil.destroy();
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
