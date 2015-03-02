/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecos.ecosdeve6.view;

import com.ecos.ecosdeve6.model.CalcularSimpsonRule;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MainView
 * 
 * modela la capa de presentacion e interaciones con los usuarios
 * 
 * @author Dev
 * @version 1.0
 * @since 1.0
 */
public class MainView {

    /**
     * showHome
     * 
     * devuelve la respuesta a la peticion http con la vista inicial de la aplicacion
     * 
     * @param req
     * @param resp
     * @param tabla
     * @throws ServletException
     * @throws IOException
     */
    public static void showHome(HttpServletRequest req, HttpServletResponse resp, List<CalcularSimpsonRule> tabla)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.getWriter().println("<style type=\"text/css\">");
        resp.getWriter().println(".myTable { background-color:#eee;border-collapse:collapse; }");
        resp.getWriter().println(".myTable th { background-color:#000;color:white;width:50%; }");
        resp.getWriter().println(".myTable td, .myTable th { padding:5px;border:1px solid #000; }");
        resp.getWriter().println("</style>");

        resp.getWriter().println("<style type=\"text/css\">");
        resp.getWriter().println(".myOtherTable { background-color:#FFFFE0;border-collapse:collapse;color:#000;font-size:18px; }");
        resp.getWriter().println(".myOtherTable th { background-color:#BDB76B;color:white;width:50%; }");
        resp.getWriter().println(".myOtherTable td, .myOtherTable th { padding:5px;border:0; }");
        resp.getWriter().println(".myOtherTable td { border-bottom:1px dotted #BDB76B; }");
        resp.getWriter().println("</style>");
        try {

            resp.getWriter().println("Tabla 1");
            resp.getWriter().println("<table class=\"myTable\">");
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<td colspan=\"2\">");
            resp.getWriter().println("Test");
            resp.getWriter().println("</td>");
            resp.getWriter().println("<td>");
            resp.getWriter().println("Expected Value");
            resp.getWriter().println("</td>");
            resp.getWriter().println("<td>");
            resp.getWriter().println("Actual Value");
            resp.getWriter().println("</td>");
            resp.getWriter().println("</tr>");
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<td>");
            resp.getWriter().println("p");
            resp.getWriter().println("</td>");
            resp.getWriter().println("<td>");
            resp.getWriter().println("dof");
            resp.getWriter().println("</td>");
            resp.getWriter().println("<td>");
            resp.getWriter().println("x");
            resp.getWriter().println("</td>");
            resp.getWriter().println("<td>");
            resp.getWriter().println("");
            resp.getWriter().println("</td>");
            resp.getWriter().println("</tr>");
            for (CalcularSimpsonRule fila : tabla) {
                resp.getWriter().println("<tr>");
                resp.getWriter().println("<td>");
                resp.getWriter().println(fila.getResultado());
                resp.getWriter().println("</td>");
                resp.getWriter().println("<td>");
                resp.getWriter().println(fila.getDof());
                resp.getWriter().println("</td>");
                resp.getWriter().println("<td>");
                resp.getWriter().println("0 Hasta x=" + fila.getX().setScale(5, RoundingMode.HALF_UP));
                resp.getWriter().println("</td>");
                resp.getWriter().println("<td>");
                resp.getWriter().println("");
                resp.getWriter().println("</td>");
                resp.getWriter().println("</tr>");
            }
            resp.getWriter().println("</table>");

        } catch (Exception e) {
            resp.getWriter().println("Ocurrio un Error : " + e.getMessage());
        }
    }

    /**
     * error
     * 
     * administra los posibles mensajes de excepcion controlados en la aplicacion
     * 
     * @param req
     * @param resp
     * @param ex
     * @throws ServletException
     * @throws IOException
     */
    public static void error(HttpServletRequest req, HttpServletResponse resp, Exception ex)
            throws ServletException, IOException {
        resp.getWriter().println("Error!!! :" + ex.getMessage());
    }
}