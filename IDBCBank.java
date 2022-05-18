package com.company;

import java.sql.*;
import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class IDBCBank {

    public boolean isUserIdAvailable(String userId){
        int counter = 0;
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            Statement ps = con.createStatement();
            ResultSet r = ps.executeQuery("select * from user");
            while (r.next()){
                if(r.getString(1).equals(userId)){
                    return true;
                }
                else {
                    counter++;
                }
            }
            if(counter>0){
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public boolean login(String userName, String password){
        int counter = 0;
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("select * from user where user_id = ?");
            ps.setString(1,userName);
            ResultSet r = ps.executeQuery();
            while (r.next()){
                if(r.getString(11).equals(password)){
                    System.out.println("Welcome "+r.getString(3)+" "+r.getString(4));
                    counter=0;
                    return true;
                }
                else {
                    counter++;
                }
            }
            if(counter>0){
                return false;
            }
        }
        catch (SQLException e){
            System.out.println("There is some issue with your login. Kindly try later");
            e.printStackTrace();
        }
        return false;
    }

    public boolean Register(String userId, String pass,String first_name, String last_name, Long dob, String address, String phoneNo, String email, String financial_stat, double income){

        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("insert into user(user_id,fname,lname,dob,address,phoneno,email,fin_stat,income,app_password) values(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1,userId);
            ps.setString(2,first_name);
            ps.setString(3,last_name);
            ps.setDate(4,new Date(dob));
            ps.setString(5,address);
            ps.setString(6,phoneNo);
            ps.setString(7,email);
            ps.setString(8,financial_stat);
            ps.setDouble(9,income);
            ps.setString(10,pass);
            ps.executeUpdate();
            return true;

        }
        catch (SQLException e){
            System.out.println("There is some issue with your sign-up activity. Kindly try later");
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneNoValid(String phone)
    {
        Pattern patternObj = Pattern.compile("^((\\+91)?)[789]\\d{9}$");
        Matcher matcherObj = patternObj.matcher(phone);
        return matcherObj.find();
    }
    //email id validator
    public boolean emailValidation(String email){
        Pattern patternObj = Pattern.compile("^\\w+@+[a-z]+\\.[a-z]+");
        Matcher matcherObj = patternObj.matcher(email);
        return matcherObj.find();
    }
    //age validator
    public boolean isAgeValid(String year){
        Integer birthYear = parseInt(year);
        int currentYear = Year.now().getValue();
        if(currentYear-birthYear>15){
            return true;
        }
        else {
            return false;
        }
    }

    public long accountNumberGenerator(){
        long accountNumber = (long)(Math.random()*(9999999999999999L-4000000000000000L+1)+4000000000000000L);
        return accountNumber;
    }

    public  String userIdGenerator(){
        long num = (long)(Math.random()*(999999L-200000L+1)+200000L);
        return "ID" + num;
    }

    public boolean isAccNoValid(long accNo){
        int counter = 0;
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            Statement ps = con.createStatement();
            ResultSet r = ps.executeQuery("select * from user");
            while (r.next()){
                if(r.getLong(2)==accNo){
                    return true;
                }
                else {
                    counter++;
                }
            }
            if(counter>0){
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean doUserHaveAnAccount(String userID){
        int counter = 0;
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            Statement ps = con.createStatement();
            ResultSet r = ps.executeQuery("select * from user");
            while (r.next()){
                if(r.getString(1).equals(userID) && r.getLong(2)==0){
                    return true;
                }
                else {
                    counter++;
                }
            }
            if(counter>0){
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void viewAccBalance(long accNo){
        int counter = 0;
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            Statement ps = con.createStatement();
            ResultSet r = ps.executeQuery("select * from acc_details");
            while (r.next()){
                if(r.getLong(1)==accNo){
                    System.out.println("Your account balance is Rs."+r.getInt(3));
                    counter++;
                }
            }
            if(counter==0){
                System.out.println("There is no account information for the given account number");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewtransactions(long accNo){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("select * from transactions where acc_no =?");
            ps.setLong(1,accNo);
            ResultSet r = ps.executeQuery();
            System.out.format("%10s %20s %8s %40s %15s %15s","Transaction id","Account No","Type","Description","Amount", "Date");
            System.out.println();
            while (r.next()){
                System.out.format("%10s %20s %8s %40s %15s %15s",r.getString(2),r.getLong(3),r.getString(4),r.getString(5),r.getDouble(6), r.getDate(7));
                System.out.println();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deposit(long accNo, int amount){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("update acc_details set acc_bal = acc_bal+? where acc_no = ?");
            ps.setInt(1,amount);
            ps.setLong(2,accNo);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void withDraw(long accNo, int amount){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("update acc_details set acc_bal = acc_bal-? where acc_no = ?");
            ps.setInt(1,amount);
            ps.setLong(2,accNo);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void openAccount(long accNo, String ifsc, String acc_typ){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("insert into acc_details(acc_no, acc_typ, ifsc) values(?,?,?)");
            ps.setLong(1,accNo);
            ps.setString(2,acc_typ);
            ps.setString(3,ifsc);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateAccNo(String userId, long accNo){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("update user set acc_no = ? where user_id = ?");
            ps.setLong(1,accNo);
            ps.setString(2,userId);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void interestCalculator(long accNo){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("select sum(t.amount)*a.intrest from transactions t join acc_details d on d.acc_no = t.acc_no  join acc_types a on a.acc_typ = d.acc_typ  where t.acc_no = ? and t.trans_type = 'credit'");
            ps.setLong(1,accNo);
            ResultSet r = ps.executeQuery();
            while (r.next()){
                System.out.println("The intrest you have gained till now is "+ r.getInt(1));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
