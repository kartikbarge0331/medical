/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
public class view extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        String medid=request.getParameter("n1");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet view</title>");            
            out.println("</head>");
            out.println("<body>");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","medical","medical");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from med_info where MID='"+medid+"'");
           rs.next();
                out.println(
             "<table border=1 align=center cellpadding=5 cellspacing=0 bgcolor=cyan width=500>"+
            "<tr>"
            + "<th>Sr.No</th>"+
            "<th>Med_ID</th>"+                
            "<th>Medicine_Name</th>"+
            "<th>Price</th>"+                
            "<th>Quantity</th>"+          
            "<th>Discription</th>"+
            "<th>Company_Name</th>"+
            "<th>Mfg_Date</th>"+
            "<th>Exp_Date</th>"+
            "<th>Dealer_Name</th>"+
            "<th>Dealer_Contact</th>"+
            "<th>Supply_Date</th>"+                             
                    "</tr>" +
                    
               
                    
             "<tr>"
             + "<td>"+rs.getInt(1)+"</td>"+
            "<td>"+rs.getString(2)+"</td>"+                
            "<td>"+rs.getString(3)+"</td>"+
            "<td>"+rs.getInt(4)+"</td>"+                
            "<td>"+rs.getInt(5)+"</td>"+          
            "<td>"+rs.getString(6)+"</td>"+
            "<td>"+rs.getString(7)+"</td>"+
            "<td>"+rs.getString(8)+"</td>"+
            "<td>"+rs.getString(9)+"</td>"+         
            "<td>"+rs.getString(10)+"</td>"+
            "<td>"+rs.getLong(11)+"</td>"+
            "<td>"+rs.getString(12)+"</td>"+         
                    "</tr>"+       
                    
            "</table>"     
               ); 
           
           
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
        }
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