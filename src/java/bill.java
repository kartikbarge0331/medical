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
public class bill extends HttpServlet {

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
        String custid=request.getParameter("m1");
        int cid=Integer.parseInt(custid);
        String mnm=request.getParameter("n1");
        String quantity=request.getParameter("n3");
        int qty=Integer.parseInt(quantity);
                
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet bill</title>");            
            out.println("</head>");
            out.println("<body>");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","medical","medical");
            Statement stmt=con.createStatement();
            con.setAutoCommit(false);
            try{
            
            int fqty=0;
            
            ResultSet rs=stmt.executeQuery("select price,quantity from med_info where upper(med_name)=upper('"+mnm+"')");
            if(!rs.next()){
                out.println("<h3>Medicine Not Found</h3>");
                return;
            }           
             int q=rs.getInt(2);
             if(qty<=q){       //q is Original quantity from Database
                    fqty=q-qty;
              }else{
                    out.println("<h3>Invalid Quantity Your Quantity: "+q+" Please Refill Yoour Stock </h3>");
                    return;  //return for stop executing existing code
                 }  
             
            //here we fetch original price from med_info and calculate bill
            int med_price=rs.getInt(1);
            double totalbill=med_price*qty;
            
           //here fetch custid and custname from customer table           
           ResultSet rsid=stmt.executeQuery("select custid,cname from customer where custid="+cid+" ");
           rsid.next();
           int id=rsid.getInt(1);
           String cnm=rsid.getString(2);
           int res=0; 
           
           if(cid==id){
           res=stmt.executeUpdate("insert into salereport values("+id+",billno.nextval,'"+mnm+"',"+med_price+","+qty+","+totalbill+",sysdate)");
           }else{
               out.println("<h3>Invalid Custid</h3>");
               return;
           }
            //update Quantity in Med_info table
           int r=stmt.executeUpdate("update med_info set Quantity="+fqty+" where upper(med_name)=upper('"+mnm+"') ");
            if(res>0 && r>0 ){               
                out.println("<h3> Bill Genrated Successfully.</h3>");
                out.println("<h3> Cutomer Name : "+cnm+"</h3>");
                 }
              }catch(Exception e){
                con.rollback(); // rollback entry everything
                out.println("<h3>Error in Your billing</h3>");
                out.println(e);
               }finally{
                con.setAutoCommit(true);
                con.close();
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
            Logger.getLogger(bill.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(bill.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(bill.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(bill.class.getName()).log(Level.SEVERE, null, ex);
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