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
public class report extends HttpServlet {

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
        String type = request.getParameter("type");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet report</title>"); 
            out.println("<style>");
        out.println("body{font-family:Arial;background:#f4f6f9;}");
        out.println("table{border-collapse:collapse;width:80%;margin:auto;}");
        out.println("th,td{border:1px solid #000;padding:8px;text-align:center;}");
        out.println("th{background:#eaeaea;}");
        out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","medical","medical");
            
            PreparedStatement ps = null;
            if("daily".equals(type)) {
    String date = request.getParameter("date");
    ps = con.prepareStatement(
        "SELECT * FROM salereport WHERE TRUNC(bill_date) = TO_DATE(?, 'YYYY-MM-DD')");
    ps.setString(1, date);
}

else if("monthly".equals(type)) {
    String month = request.getParameter("month"); // yyyy-mm
    ps = con.prepareStatement(
        "SELECT * FROM salereport WHERE TO_CHAR(bill_date,'YYYY-MM') = ?");
    ps.setString(1, month);
}

else if("between".equals(type)) {
    ps = con.prepareStatement(
        "SELECT * FROM salereport WHERE TRUNC(bill_date) BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD')");
    ps.setString(1, request.getParameter("from"));
    ps.setString(2, request.getParameter("to"));
}

else if("item".equals(type)) {
    ps = con.prepareStatement(
        "SELECT * FROM salereport WHERE medname = ?");
    ps.setString(1, request.getParameter("med"));
}
     
      ResultSet rs = ps.executeQuery();

            double grandTotal = 0;

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Cust Id</th>");
            out.println("<th>Bill No</th>");
            out.println("<th>Medicine Name</th>");
            out.println("<th>Price</th>");
            out.println("<th>Quantity</th>");
            out.println("<th>Total Bill</th>");
            out.println("<th>Bill Date</th>");
            out.println("</tr>");

            boolean found = false;

            while(rs.next()) {
                found=true;
    double total = rs.getDouble(6);
    grandTotal += total;

    out.println("<tr>");
    out.println("<td>" + rs.getInt(1) + "</td>");
    out.println("<td>" + rs.getInt(2) + "</td>");
    out.println("<td>" + rs.getString(3) + "</td>");
    out.println("<td>" + rs.getInt(4) + "</td>");
    out.println("<td>" + rs.getInt(5) + "</td>");
    out.println("<td>" + total + "</td>");
    out.println("<td>" + rs.getDate(7) + "</td>");
    out.println("</tr>");
}


            if(found) {
                out.println("<tr>");
                out.println("<th colspan='6'>Grand Total</th>");
                out.println("<th>" + grandTotal + "</th>");
                out.println("</tr>");
            } else {
                out.println("<tr><td colspan='7'>No Records Found</td></tr>");
            }

            out.println("</table>");

            con.close();

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
            Logger.getLogger(report.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(report.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(report.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(report.class.getName()).log(Level.SEVERE, null, ex);
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