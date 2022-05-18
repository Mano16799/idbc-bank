package com.company;
import java.sql.*;
import java.text.ParseException;


public class Transactions {

    public void credit(long accNo, int amount){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("insert into transactions(transac_id,acc_no,trans_type,description,amount,transac_date) values(?,?,?,?,?,?)");
            ps.setString(1,generateTransacId());
            ps.setLong(2,accNo);
            ps.setString(3,"credit");
            ps.setString(4,"amount credited");
            ps.setInt(5,amount);
            ps.setDate(6,new Date(getDate()));
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void credit(long accNo, int amount,long fromAcc){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("insert into transactions(transac_id,acc_no,trans_type,description,amount,transac_date) values(?,?,?,?,?,?)");
            ps.setString(1,generateTransacId());
            ps.setLong(2,accNo);
            ps.setString(3,"credit");
            ps.setString(4,"amount credited by accNo "+fromAcc);
            ps.setInt(5,amount);
            ps.setDate(6,new Date(getDate()));
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void debit(long accNo, int amount){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("insert into transactions(transac_id,acc_no,trans_type,description,amount,transac_date) values(?,?,?,?,?,?)");
            ps.setString(1,generateTransacId());
            ps.setLong(2,accNo);
            ps.setString(3,"debit");
            ps.setString(4,"amount debited");
            ps.setInt(5,amount);
            ps.setDate(6,new Date(getDate()));
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void debit(long accNo, int amount, long toAcc){
        try(Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/idbc_bank", "root", "Password@123")){
            PreparedStatement ps = con.prepareStatement("insert into transactions(transac_id,acc_no,trans_type,description,amount,transac_date) values(?,?,?,?,?,?)");
            ps.setString(1,generateTransacId());
            ps.setLong(2,accNo);
            ps.setString(3,"debit");
            ps.setString(4,"amount credited to "+toAcc);
            ps.setInt(5,amount);
            ps.setDate(6,new Date(getDate()));
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static String generateTransacId(){
        long num = (long)(Math.random()*(999999L-200000L+1)+200000L);
        return "ID" + num;
    }

    public static Long getDate() throws ParseException {
        return System.currentTimeMillis();
    }

}
