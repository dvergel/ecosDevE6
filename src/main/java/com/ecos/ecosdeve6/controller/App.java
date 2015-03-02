package com.ecos.ecosdeve6.controller;

import com.ecos.ecosdeve6.model.CalcularSimpsonRule;
import com.ecos.ecosdeve6.view.MainView;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * App
 * 
 * modela el controlador de peticiones de la aplicacion web
 * 
 * @author Dev
 * @version 1.0
 * @since 1.0
 */
public class App extends HttpServlet {

    /**
     * doGet
     * 
     * controla las peticiones realizadas por el metodo get
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<CalcularSimpsonRule> ejercicios =new ArrayList<CalcularSimpsonRule>();
            CalcularSimpsonRule ejercicio=new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1).setScale(0, RoundingMode.HALF_UP), new BigDecimal(10).setScale(0, RoundingMode.HALF_UP), new BigDecimal(6).setScale(0, RoundingMode.HALF_UP),new BigDecimal(0.20).setScale(2, RoundingMode.HALF_UP));
            ejercicio.proyectarX();
            ejercicios.add(ejercicio);
            ejercicio=new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1).setScale(0, RoundingMode.HALF_UP), new BigDecimal(10).setScale(0, RoundingMode.HALF_UP), new BigDecimal(15).setScale(0, RoundingMode.HALF_UP),new BigDecimal(0.45).setScale(2, RoundingMode.HALF_UP));
            ejercicio.proyectarX();
            ejercicios.add(ejercicio);
            ejercicio=new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1).setScale(0, RoundingMode.HALF_UP), new BigDecimal(10).setScale(0, RoundingMode.HALF_UP), new BigDecimal(4).setScale(0, RoundingMode.HALF_UP),new BigDecimal(0.495).setScale(3, RoundingMode.HALF_UP));
            ejercicio.proyectarX();
            ejercicios.add(ejercicio);
            MainView.showHome(req, resp,ejercicios);
        } catch (Exception ex) {
            MainView.error(req, resp, ex);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * doPost
     * 
     * controla las peticiones por el metodo Post
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            MainView.showHome(req, resp,new ArrayList<CalcularSimpsonRule>());
        } catch (Exception ex) {
            MainView.error(req, resp, ex);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * main
     * 
     * configura el controlador embebido del servidor de aplicacion jetty
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        Server server = new Server(80);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new App()), "/*");
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception();
        }
    }
}

