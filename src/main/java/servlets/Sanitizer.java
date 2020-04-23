/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.encoder.Encode;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

@WebServlet(name = "Sanitizer", urlPatterns = {"/Sanitizer"})
public class Sanitizer extends HttpServlet {

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

        PolicyFactory policy = new HtmlPolicyBuilder()
                .allowElements("b")
                .allowElements("i")
                .allowElements(
                        (String elementName, List<String> attrs) -> {
                            // Add a class attribute.
                            attrs.add("class");
                            attrs.add("header-" + elementName);
                            // Return elementName to include, null to drop.
                            return "div";
                        })
                .toFactory();
        
        PolicyFactory policy2 = new HtmlPolicyBuilder()
                .allowElements(
                        (String elementName, List<String> attrs) -> {
                            // Add a class attribute.
                            attrs.add("class");
                            attrs.add("header-" + elementName);
                            // Return elementName to include, null to drop.
                            return "div";
                        })
                .toFactory();

        response.setContentType("text/html;charset=UTF-8");
        String plainUserData = request.getParameter("input");
        String encoded = "<b>Change me</b> to show the original string, HTML-Encoded";
        String simpleTags = "<b>Change me</b> to allow simple tags (<b>bold</b> and <i>italic</i>), <p>but</p> no other tags";
        String noTags = "<b>Change me</b> <p>to</p> <h1>allow</h1> NO tags at all";
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Sanitizer</title>");
            out.println("</head>");
            out.println("<body style=\"font-family:sans-serif\">");
            out.println("<h2>Encoder + Sanitizer Demo</h2>");
            out.println("<p>Use your browsers 'inspect' button to se the actual content of the string below</p>");
            out.print(plainUserData);
            out.println("<br/><br/>");
            out.println(Encode.forHtml(encoded));
            out.println("<br/><br/>");
            out.println(policy.sanitize(simpleTags));
            out.println("<br/><br/>");
            out.println(policy2.sanitize(noTags));
            out.print("<div style=\"margin-top:25px;\"><a href=\"" + request.getContextPath() + "/index.html\">Home</a></div>");
            out.println("</body>");
            out.println("</html>");
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
