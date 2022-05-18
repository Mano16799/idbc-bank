package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App {
    public static void main(String[] args) throws ParseException {
        App app =new App();
        Scanner sc = new Scanner(System.in);
        IDBCBank bank = new IDBCBank();
        String userName = "";
        String userIdToCreateAccount = "";
        System.out.println("--------Welcome to IDBC Bank portal---------");
        System.out.println("1. Login\n2. Signup");
        int login_opt = sc.nextInt();
        sc.nextLine();
        int flag = 0, flag1=0, flag2=0;
        if(login_opt==1){
            System.out.println("Enter the user id");
            userName = sc.nextLine();
            if(bank.isUserIdAvailable(userName)) {
                do {
                    System.out.println("Enter the pass word");
                    String pass = sc.nextLine();
                    if (bank.login(userName, pass)) {
                        System.out.println("Login successful");
                        flag++;
                    } else {
                        System.out.println("Password mis-match. Try again");
                    }
                }while(flag==0);
            }
            else {
                System.out.println("User Id not available. Please register");
                login_opt=2;
            }
        }
        if(login_opt==2){
            System.out.println("Enter your first name");
            String firstName = sc.nextLine();
            System.out.println("Enter your last name");
            String lastName = sc.nextLine();
            System.out.println("Enter the date of birth - format(YYYY/MM/DD)");
            String date = sc.nextLine();
            String[] dateArr = date.split("/");
            if(bank.isAgeValid(dateArr[1])){
                do {
                    System.out.println("Enter your phone number");
                    String phoneNo = sc.nextLine();
                    System.out.println("Enter your email");
                    String email = sc.nextLine();
                    if (bank.emailValidation(email) && bank.isPhoneNoValid(phoneNo)) {
                        flag1++;
                        System.out.println("Enter your address");
                        String address = sc.nextLine();
                        String status = "";
                        do {
                            System.out.println("Are you salaried or an Entrepreneur\n1. Salaried\n2. Entrepreneur");
                            int fin_stat = sc.nextInt();
                            if (fin_stat == 1) {
                                status = "Salaried";
                                flag2++;
                            } else if (fin_stat == 2) {
                                status = "Entrepreneur";
                                flag2++;
                            } else {
                                System.out.println("Choose correct option");
                            }
                        }while (flag2==0);
                        System.out.println("Enter your income per month");
                        double income = sc.nextDouble();
                        String userId = bank.userIdGenerator();
                        userIdToCreateAccount = userId;
                        System.out.println("Your user id is"+userId);
                        String pass = setupPassword();
                        bank.Register(userId,pass,firstName,lastName,new SimpleDateFormat("yyyy/MM/dd").parse(date).getTime(),address,phoneNo,email,status,income);
                    } else {
                        System.out.println("Email or phone no not valid. Please re-enter");
                    }
                }while (flag1==0);
            }
            else {
                System.out.println("Age not valid");
            }
            System.out.println("Signup- successful");
        }
        int flag3=0;
        do {
            System.out.println("select 1 option\n1 View your bank accounts\n2. Open new account (applicable only for users without a bank account in IDBC bank)");
            int choice = sc.nextInt();
            if (choice == 1) {
                app.viewBankAccounts();
                flag3++;
            } else if (choice == 2) {
                if(!bank.doUserHaveAnAccount(userIdToCreateAccount)){
                    System.out.println("Sorry, the facility of opening multiple bank accounts is not available now.\nStay tuned for any future updates");
                    flag3++;
                }
                else{
                    app.createAccount(userIdToCreateAccount);
                    flag3++;
                }
            }
            else{
                flag3=0;
            }
        }while(flag3==0);

    }

    public static String setupPassword(){
        Scanner sc = new Scanner(System.in);
        int flag=0;
        String pass="";
        do {
            System.out.println("Set-up your password\nEnter a password\n1. Password should be minimum 8 characters\n2. must contain one lower-case letter\n3. must contain one upper-case letter\n4. must have one special character[ @#$%^&-+=() ]");
            pass = sc.nextLine();
            Pattern patternObj = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$");
            Matcher matcherObj = patternObj.matcher(pass);
            if (matcherObj.find()) {
                return pass;
            } else {
                System.out.println("Re-enter password");
            }
        }while(flag==0);
        return pass;
    }

    public void viewBankAccounts(){
        IDBCBank bank = new IDBCBank();
        Transactions t = new Transactions();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your account number");
        long accNo = sc.nextLong();
        int flag=0;
        if (bank.isAccNoValid(accNo)){
            do {
                System.out.println("Choose 1 option\n1. View Account balance\n2. Deposit\n3. View previous transactions\n4. Fund transfer\n5. Withdraw\n6. Show gained intrest\n7. Exit");
                int opt = sc.nextInt();
                switch (opt) {
                    case 1: {
                        bank.viewAccBalance(accNo);
                        flag++;
                        break;
                    }
                    case 2:{
                        System.out.println("Enter the amount to deposit");
                        int amount = sc.nextInt();
                        bank.deposit(accNo,amount);
                        t.credit(accNo,amount);
                        System.out.println("Amount deposited");
                        flag++;
                        break;
                    }
                    case 3: {
                        bank.viewtransactions(accNo);
                        flag++;
                        break;
                    }
                    case 4:{
                        System.out.println("Enter the receiver account number ");
                        long toAccNo = sc.nextLong();
                        System.out.println("Enter the amount to transfer");
                        int amount = sc.nextInt();
                        bank.deposit(toAccNo,amount);
                        bank.withDraw(accNo,amount);
                        t.debit(accNo,amount,toAccNo);
                        t.credit(toAccNo,amount,accNo);
                        System.out.println("amount transferred");
                        flag++;
                        break;
                    }
                    case 5:{
                        System.out.println("Enter the amount to withdraw");
                        int amount = sc.nextInt();
                        bank.withDraw(accNo,amount);
                        t.debit(accNo,amount);
                        flag++;
                        break;
                    }
                    case 6: {
                        bank.interestCalculator(accNo);
                        flag++;
                        break;
                    }
                    case 7: flag= 0;
                }
            }while (flag!=0);
        }
        else {
            System.out.println("No account info available");
        }
    }

    public void createAccount(String userId){
        Transactions t = new Transactions();
        IDBCBank bank = new IDBCBank();
        String ifsc = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("Opening bank accounts became as simple as a click!");
        System.out.println("Choose 1 option\n1. open savings account\n2. open pay account");
        int option = sc.nextInt();
        int flag=0;
        do {
            System.out.println("Choose your nearest bank to continue\n1. Coimbatore\n2. Chennai\n3. Madurai\n4. Hyderabad\n5. Cochin\n6. Bangalore\n7. Palakkad");
            int bankChoice = sc.nextInt();
            switch (bankChoice) {
                case 1: ifsc = "IDB1000982";flag++;break;
                case 2: ifsc = "IDB1000989";flag++;break;
                case 3: ifsc = "IDB1000978";flag++;break;
                case 4: ifsc = "IDB1000907";flag++;break;
                case 5: ifsc = "IDB1000956";flag++;break;
                case 6: ifsc = "IDB1000924";flag++;break;
                case 7: ifsc = "IDB1000963";flag++;break;
                default:
                    flag=0;
                    System.out.println("choose correct option");
            }
        }while(flag==0);
        Long accNo = bank.accountNumberGenerator();
        System.out.println("Congratulations! Your bank account has been created against your user ID\nYour account number is "+accNo);
        if(option==1){
            bank.openAccount(accNo,ifsc,"savings");
            bank.updateAccNo(userId,accNo);
            int flag1=0;
            do {
                System.out.println("The minimum balance for a savings account is Rs 5,000\nKindly deposit to proceed");
                int amount = sc.nextInt();
                if (amount >= 5000) {
                    bank.deposit(accNo, amount);
                    t.credit(accNo,amount);
                    flag1++;
                } else {
                    System.out.println("Kindly enter an amount greater than 5000");
                    flag1=0;
                }
            }while(flag1==0);
        }
        else if(option==2){
            bank.openAccount(accNo,ifsc,"pay");
            bank.updateAccNo(userId,accNo);
            int flag1=1;
            do {
                System.out.println("The minimum balance for a pay account is Rs 10,000\nKindly deposit to proceed");
                int amount = sc.nextInt();
                if (amount >= 10000) {
                    bank.deposit(accNo, amount);
                    t.credit(accNo,amount);
                    flag1++;
                }
                else {
                    System.out.println("Kindly enter an amount greater than 10000");
                    flag1=0;
                }
            }while(flag1==0);
        }

    }

}
