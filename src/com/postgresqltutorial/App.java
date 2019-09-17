package com.postgresqltutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author Arnold
 */
public class App {
    
    private final String url = "jdbc:postgresql://localhost/pizzashop";
    private final String user = "postgres";
    private final String password = "post";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    public void menu(){
        Scanner in = new Scanner(System.in);
        Scanner param = new Scanner(System.in);
        int menuItem;
        boolean quit = false;
        
        System.out.println("Select an option: ");
        System.out.println("1: Get total revenue");
        System.out.println("2: List all managers");
        System.out.println("3: Find low wage workers");
        System.out.println("4: List totals for each purchase");
        System.out.println("5: Change an item's price on the menu");
        System.out.println("6: Insert a new employee");
        System.out.println("7: Fire an employee");
        System.out.println("8: Find a customer from what item the purchase");
            do {
                  System.out.print("Choose menu item: ");
                  System.out.println("Enter 0 to quit Quit");
                  menuItem = in.nextInt();
                  switch (menuItem) {
                  case 1:
                        System.out.println("Get total revenue");
                        this.totalRevenue();
                        break;

                  case 2:
                        System.out.println("List managers");
                        this.findManager();
                        break;

                  case 3:
                        System.out.println("Find low wage");
                        this.findMinimumWage();
                        break;

                  case 4:
                        System.out.println("Purchase totals");
                        this.getPurchaseTotal();
                        break;

                  case 5:
                        System.out.println("Change item price");
                        System.out.println("Enter item ID: ");
                        int id = param.nextInt();
                        System.out.println("Enter item price: ");
                        int price = param.nextInt();
                        this.changeItemPrice(price, id);
                        break;
                  case 6:
                        System.out.println("Insert new employee");
                        System.out.println("Enter Employee ID: ");
                        int eid = param.nextInt();
                        param.nextLine();
                        System.out.println("Enter first name: ");
                        String fname = param.nextLine();
                        System.out.println("Enter last name: ");
                        String lname = param.nextLine();
                        System.out.println("Enter salary: ");
                        int sal = param.nextInt();
                        param.nextLine();
                        System.out.println("Enter position: ");
                        String pos = param.nextLine();
                        this.insertEmployee(eid, fname, lname, sal, pos);
                        break;
                  case 7:
                        System.out.println("Fire employee");
                        System.out.println("Enter Employee ID: ");
                        int feid = param.nextInt();
                        this.fireEmployee(feid);
                        break;
                  case 8:
                        System.out.println("Customers from item ID");
                        System.out.println("Enter Item ID: ");
                        int xid = param.nextInt();
                        this.findCustomersFromItem(xid);
                        break;

                  case 0:
                        quit = true;
                        break;

                  default:
                        System.out.println("Invalid choice.");
                  }

            } while (!quit);
    }

    
    public void totalRevenue() {
        String SQL = "SELECT sum(totalPrice) AS totalRevenue FROM " +
                "(SELECT quantity*price as totalPrice FROM menu NATURAL JOIN purchases) AS totals";
 
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            //pstmt.setInt(1, item_id);
            ResultSet rs = pstmt.executeQuery();
            //displayActor(rs);
        while (rs.next()) {
            System.out.println(rs.getString("totalRevenue"));
 
        }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void findManager() {
        String SQL = "SELECT * from employees where position = 'Manager'";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            //pstmt.setInt(1, item_id);
            ResultSet rs = pstmt.executeQuery();
            //displayActor(rs);
        while (rs.next()) {
            System.out.println(rs.getString("empID") + "\t" +
                    rs.getString("firstname") + "\t" +
                    rs.getString("lastname") + "\t" +
                    rs.getString("salary") + "\t" +
                    rs.getString("position"));
        }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void findMinimumWage() {
        String SQL = "SELECT * from employees WHERE salary < 15";
 
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            //pstmt.setInt(1, item_id);
            ResultSet rs = pstmt.executeQuery();
            //displayActor(rs);
        while (rs.next()) {
            System.out.println(rs.getString("empID") + "\t" +
                    rs.getString("firstname") + "\t" +
                    rs.getString("lastname") + "\t" +
                    rs.getString("salary") + "\t" +
                    rs.getString("position"));
        }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void getPurchaseTotal() {
        String SQL = "(SELECT pid, cid, empid, quantity, itemid, quantity*price as total FROM menu NATURAL JOIN purchases)";
 
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            //pstmt.setInt(1, item_id);
            ResultSet rs = pstmt.executeQuery();
            //displayActor(rs);
        while (rs.next()) {
            System.out.println(rs.getString("pid") + "\t" +
                    rs.getString("cid") + "\t" +
                    rs.getString("empid") + "\t" +
                    rs.getString("quantity") + "\t" +
                    rs.getString("itemid") + "\t" +
                    rs.getString("total"));
        }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public int changeItemPrice(int newPrice, int item_id) {
        String SQL = "UPDATE menu " +
                "SET price = ? " +
                "WHERE itemid = ?";
 
        int affectedrows = 0;
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, newPrice);
            pstmt.setInt(2, item_id);
            affectedrows = pstmt.executeUpdate();
            //displayActor(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }
    
    public void insertEmployee(int emp_id, String f_name, String l_name, int newSal, String newPos){
        String SQL = "INSERT INTO employees(empid, firstname, lastname, salary, position) "+
                "values (?,?,?,?,?)";
       
        
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {
 
            pstmt.setInt(1, emp_id);
            pstmt.setString(2, f_name);
            pstmt.setString(3, l_name);
            pstmt.setInt(4, newSal);
            pstmt.setString(5, newPos);
 
            int affectedRows = pstmt.executeUpdate();
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void fireEmployee(int emp_id){
        String SQL = "DELETE FROM employees "
                + "WHERE empid = ?";
       
        
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {
 
            pstmt.setInt(1, emp_id);
 
            int affectedRows = pstmt.executeUpdate();
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void findCustomersFromItem(int item_id) {
        String SQL = "SELECT firstname, lastname FROM " +
                "customers NATURAL JOIN purchases "
                + "WHERE itemid = ?";
 
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, item_id);
            ResultSet rs = pstmt.executeQuery();
            //displayActor(rs);
        while (rs.next()) {
            System.out.println(rs.getString("firstname") + "\t"
                    + rs.getString("lastname"));
        }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App app = new App();
        app.menu();
        //app.connect();
        //app.totalRevenue();
        //app.findManager();
        //app.findMinimumWage();
        //app.getPurchaseTotal();
        //app.changeItemPrice(3,1);
        //app.insertEmployee(006, "Gwen", "Stacy", 10, "Server");
        //app.fireEmployee(006);
        //app.findCustomersFromItem(1);
    }
    
}
