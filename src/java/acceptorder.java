/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class acceptorder extends HttpServlet {

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
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        String orderid=request.getParameter("a1");
        int id=Integer.parseInt(orderid);
        
        String mednm=request.getParameter("a2");
        
        String medqty=request.getParameter("a3");
        int mqty=Integer.parseInt(medqty);
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet acceptorder</title>");            
            out.println("</head>");
            out.println("<body>");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","medical","medical");
            Statement stmt=con.createStatement();
            
            PreparedStatement ps=con.prepareStatement("insert into received_order values(recv_id.nextval,?,?,?,?,sysdate)");
            ps.setInt(1,id);
            ps.setString(2,mednm);
            ps.setInt(3,mqty);
            ps.setString(4,"Received");           
            int res=ps.executeUpdate();
            
            if(res>0){
                out.println("<h3>Order Accepted Status Received</h3>");  
                PreparedStatement ps2=con.prepareStatement("update required_order set status=? where order_id=?");
            ps2.setString(1,"Received");
            ps2.setInt(2,id);
            int r=ps2.executeUpdate();
                
            //here we fetch existing quantity from med_info table
            ResultSet rs=stmt.executeQuery("select quantity from med_info where upper(med_name)=upper('"+mednm+"')");
            rs.next();   
            int q=rs.getInt(1);
            int fqty=q+mqty;   
            //here we update quantity in med_info table
            PreparedStatement ps4=con.prepareStatement("update med_info set Quantity=? where upper(med_name)=upper(?)");
            ps4.setInt(1,fqty);
            ps4.setString(2,mednm);
            int record=ps4.executeUpdate();
            
            if(record>0){
                out.println("<h3>Final</h3>");  
            }
            return;
            }
                       
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
            Logger.getLogger(acceptorder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(acceptorder.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(acceptorder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(acceptorder.class.getName()).log(Level.SEVERE, null, ex);
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