package it2d;

import java.util.Scanner;

public class Applicant {
    

   public void Applicants() { 
    String response;
    Scanner sc = new Scanner(System.in);

    do {
        String action;
        
        // Prompt the user for action input in a do-while loop
        do {
            System.out.println("|--------------------|     APPLICANT     |----------------------|");
            System.out.println("|--------------------|Choose an action:  |----------------------|");
            System.out.println("|--------------------|A. ADD APPLICANT   |----------------------|");
            System.out.println("|--------------------|B. VIEW APPLICANT  |----------------------|");
            System.out.println("|--------------------|C. UPDATE APPLICANT|----------------------|");
            System.out.println("|--------------------|D. DELETE APPLICANT|----------------------|");
            System.out.println("|--------------------|E. EXIT            |----------------------|");
            boolean validInput = false;
            
            do {
            System.out.print("|-------------------| Enter action letter (A, B, C, D, or E): ");
            action = sc.next().toUpperCase();

            if (action.equals("A") || action.equals("B") || action.equals("C") || action.equals("D") || action.equals("E")) {
                validInput = true;
            } else {
                System.out.println("|--------------------| Invalid input! Please enter A, B, C, D, or E.");
            }
        } while (!validInput);
 

            // Check for valid input (A-E)
            if (action.equals("A")) {
                addApplicant();
                break;
            } else if (action.equals("B")) {
                viewApplicant();
                break;
            } else if (action.equals("C")) {
                viewApplicant();
                updateApplicant();
                viewApplicant();
                break;
            } else if (action.equals("D")) {
                viewApplicant();
                deleteApplicant();
                viewApplicant();
                break;
            } else if (action.equals("E")) {
                System.out.println("|--------------------|Exiting...");
                return;
            } 

        } while (true); 
        System.out.print("|--------------------| Do you want to continue? (yes or no): ");
            response = sc.next();

            // Validate the response
            while (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
                System.out.print("|--------------------| Invalid input! Please enter 'yes' or 'no': ");
                response = sc.next();
            }

        } while (response.equalsIgnoreCase("yes")); // Repeat if the user wants to continue

        System.out.println("|--------------------| Thank you, see you!");
}


    public void addApplicant() {
        Scanner sc = new Scanner(System.in);
       
        System.out.print("|-------------------|Applicant Full Name : ");
        String fname = sc.next();
        System.out.print("|-------------------| Applicant Email: ");
        String email = sc.next();

        // Validate the email format
        while (!email.endsWith("@gmail.com")) {
            System.out.print("|-------------------| Invalid Gmail! Please enter a valid Gmail address (e.g., example@gmail.com): ");
            email = sc.next();
        }
        System.out.print("|-------------------| Applicant Phone Number: ");
        String pn = sc.next();

        // Validate phone number
        while (pn.length() != 11 || !pn.matches("\\d+")) {
            System.out.print("|-------------------| Invalid number! A valid phone number must have exactly 11 digits. Please try again: ");
            pn = sc.next();
        }
        System.out.print("|-------------------|Applicant Resume : ");
        String res = sc.next();
        String sql = "INSERT INTO Applicants (Name,Email,PhoneNumber,Resume) VALUES (?,?,?,?)";
        config conf = new config();
        conf.addRecord(sql, fname, email, pn, res);
    }

    public void viewApplicant() {
        config conf = new config();
        String query = "SELECT * FROM Applicants";    
        String[] headers = {"ApplicantID", "Name", "Email", "PhoneNumber", "Resume"};
        String[] columns = {"ApplicantID", "Name", "Email", "PhoneNumber", "Resume"};

        conf.viewRecords(query, headers, columns);
    }

    public void updateApplicant() {
    config conf = new config();
    Scanner sc = new Scanner(System.in);
    
    String ApplicantID;
    while (true) {
        System.out.print("|--------------------|Enter Applicant ID: ");
        ApplicantID = sc.next();
        String checkApplicantQuery = "SELECT 1 FROM Applicants WHERE ApplicantID = ?";
        if (conf.recordExists(checkApplicantQuery, ApplicantID)) {
            break;
        } else {
            System.out.println("Applicant ID does not exist. Please try again.");
        }
    }
    
    System.out.print("|-------------------|EDIT Name: ");
    String nname = sc.next();
    System.out.print("|-------------------|EDIT Email: ");
    String nemail = sc.next();
    while (!nemail.endsWith("@gmail.com")) {
            System.out.print("|-------------------| Invalid to edit Gmail! Please enter a valid Gmail address (e.g., example@gmail.com): ");
            nemail = sc.next();
        }
    System.out.print("|-------------------|EDIT PhoneNumber: ");
    String npn = sc.next();
    while (npn.length() != 11 || !npn.matches("\\d+")) {
            System.out.print("|-------------------| Invalid number! A valid phone number must have exactly 11 digits. Please try again: ");
            npn = sc.next();
        }
    System.out.print("|-------------------|EDIT Resume: ");
    String nres = sc.next();

    String qry = "UPDATE Applicants SET Name = ?, Email = ?, PhoneNumber = ?, Resume = ? WHERE ApplicantID = ?";
    conf.updateRecord(qry, nname, nemail, npn, nres, ApplicantID); 
}


    public void deleteApplicant() {
        config conf = new config();
        Scanner sc = new Scanner(System.in);
        String ApplicantID;
    while (true) {
        System.out.print("|--------------------|Enter Employee ID: ");
        ApplicantID = sc.next();
        
        String checkEmployeeQuery = "SELECT 1 FROM Applicants WHERE ApplicantID = ?";
        if (conf.recordExists(checkEmployeeQuery, ApplicantID)) {
            break;
        } else {
            System.out.println("ApplicantID does not exist. Please try again.");
        }
    }
        
        String qry = "DELETE FROM Applicants WHERE ApplicantID = ?";
        
        conf.deleteRecord(qry, ApplicantID);
    }
}
