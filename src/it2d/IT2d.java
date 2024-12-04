  package it2d;

import java.util.Scanner;

public class IT2d {
    public static void main(String[] args) {
        String response;
        Scanner sc = new Scanner(System.in); 
        do { 
            System.out.println("|-----------|'Job Application Management System'|--------------|");
            System.out.println("|---------------------|A. APPLICANTS    |----------------------|");
            System.out.println("|---------------------|B. JOB LISTING   |----------------------|");
            System.out.println("|---------------------|C. APPLICATION   |----------------------|");
            System.out.println("|---------------------|D. EXIT          |----------------------|");
           
            String action;
            boolean validInput = false;
            
            do {
                System.out.print("|-------------------|Enter action letter (A, B, C, D): ");
                action = sc.next().toUpperCase();
                
                if (action.equals("A") || action.equals("B") || action.equals("C") || action.equals("D")) {
                    validInput = true;
                } else {
                    System.out.println("|--------------------|Invalid input! Please enter A, B, C, or D.");
                }
            } while (!validInput);
            
            Applicant Action1 = new Applicant();
            Job_Listing Action2 = new Job_Listing();
            APPLICATIONS Action3 = new APPLICATIONS();
            
            switch (action) {
                case "A":
                    Action1.Applicants();
                    break;
                case "B":
                    Action2.Job_Listings();
                    break;
                case "C":
                    Action3.record();
                    break;
                case "D":
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("|--------------------|Invalid input! Please enter A, B, C, or D.");
            }
            
            if (!action.equals("D")) {
                System.out.print("Do you want to continue? (yes or no): ");
                response = sc.next();
                sc.nextLine();
            } else {
                response = "no";  // Ensure the loop exits after selecting "D"
            }
        } while (response.equalsIgnoreCase("yes"));
        
        System.out.println("Thank you, see you!");
        sc.close(); 
    }
}
