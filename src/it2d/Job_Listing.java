package it2d;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Job_Listing {

    Scanner sc = new Scanner(System.in);

    public void Job_Listings() {
        String response;

        do {
            System.out.println("|--------------------|     JOB LIST     |----------------------|");
            System.out.println("|--------------------|Choose an action: |----------------------|");
            System.out.println("|--------------------|A. ADD JOB        |----------------------|");
            System.out.println("|--------------------|B. VIEW JOB       |----------------------|");
            System.out.println("|--------------------|C. UPDATE JOB     |----------------------|");
            System.out.println("|--------------------|D. DELETE JOB     |----------------------|");
            System.out.println("|--------------------|E. EXIT           |----------------------|");

            boolean validInput = false;
            String action;

            do {
                System.out.print("|-------------------|Enter action letter (A, B, C, D, or E): ");
                action = sc.next().toUpperCase();

                if (action.equals("A") || action.equals("B") || action.equals("C") || action.equals("D") || action.equals("E")) {
                    validInput = true;
                } else {
                    System.out.println("|--------------------|Invalid input! Please enter A, B, C, D, or E.");
                }
            } while (!validInput);
            if (action.equals("A")) {
                AddJob();
            } else if (action.equals("B")) {
                viewJob();
            } else if (action.equals("C")) {
                viewJob();
                updateJob();
                viewJob();
            } else if (action.equals("D")) {
                viewJob();
                deleteJob();
                viewJob();
            } else if (action.equals("E")) {
                System.out.println("|--------------------|Exiting...");
                return;
            }

            System.out.print("|--------------------| Do you want to continue? (yes or no): ");
            response = sc.next();

            while (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
                System.out.print("|--------------------| Invalid input! Please enter 'yes' or 'no': ");
                response = sc.next();
            }

        } while (response.equalsIgnoreCase("yes"));

        System.out.println("|--------------------| Thank you, see you!");
    }

    public void AddJob() {
    System.out.print("|-------------------|JOB Title: ");
    String title = sc.next(); 

    System.out.print("|-------------------|JOB Department: ");
    String dep = sc.next(); 

    System.out.print("|-------------------|JOB Location: ");
    String loc = sc.next(); 

    LocalDate applicationDeadline = null;
    boolean validInput = false;

    // Define date format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    do {
        System.out.print("|-------------------| JOB Application Deadline (yyyy-MM-dd): ");
        String ad = sc.next(); // Use nextLine() for consistent input handling

        try {
            // Parse input date
            applicationDeadline = LocalDate.parse(ad, formatter);

            // Check if the date is present or future
            if (!applicationDeadline.isBefore(LocalDate.now())) {
                validInput = true; // Valid date
            } else {
                System.out.println("|-------------------| Invalid input! Please enter a present or future date.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("|-------------------| Invalid format! Please enter the date in yyyy-MM-dd format.");
        }
    } while (!validInput);

    String sql = "INSERT INTO JobListings (JobTitle, Department, Location, ApplicationDeadline) VALUES (?, ?, ?, ?)";
    config conf = new config();
    conf.addRecord(sql, title, dep, loc, applicationDeadline);
}


    public void viewJob() {
        config conf = new config();
        String query = "SELECT * FROM JobListings";
        String[] headers = {"JobID", "JobTitle", "Department", "Location", "ApplicationDeadline"};
        String[] columns = {"JobID", "JobTitle", "Department", "Location", "ApplicationDeadline"};
        conf.viewRecords(query, headers, columns);
    }

    public void updateJob() {
        config conf = new config();
        String JobID;
        while (true) {
            System.out.print("|--------------------|Enter JobID: ");
            JobID = sc.next();

            String checkEmployeeQuery = "SELECT 1 FROM JobListings WHERE JobID = ?";
            if (conf.recordExists(checkEmployeeQuery, JobID)) {
                break;
            } else {
                System.out.println("JobID does not exist. Please try again.");
            }
        }

        System.out.print("|-------------------|EDIT JOB Title: ");
        String ntitle = sc.next();
        System.out.print("|-------------------|EDIT JOB Department: ");
        String ndep = sc.next();
        System.out.print("|-------------------|EDIT JOB Location: ");
        String nloc = sc.next();
        LocalDate applicationDeadline = null; // To store the valid deadline date
        boolean validInput = false;

        // Define date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        do {
            System.out.print("|-------------------| JOB Application Deadline(yyyy-MM-dd): ");
            String nad = sc.next();

            try {
                // Parse input date
                applicationDeadline = LocalDate.parse(nad, formatter);

                // Check if the date is present or future
                if (!applicationDeadline.isBefore(LocalDate.now())) {
                    validInput = true; // Valid date
                } else {
                    System.out.println("|-------------------| Invalid input! Please enter a present or future date.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("|-------------------| Invalid format! Please enter the date in yyyy-MM-dd format.");
            }
        } while (!validInput);

        String qry = "UPDATE JobListings SET JobTitle = ?, Department = ?, Location = ?, ApplicationDeadline = ? WHERE JobID = ?";
        conf.updateRecord(qry, ntitle, ndep, nloc, applicationDeadline, JobID);
    }

    public void deleteJob() {
        config conf = new config();
        String JobID;
        while (true) {
            System.out.print("|--------------------|Enter JobID: ");
            JobID = sc.next();

            String checkEmployeeQuery = "SELECT 1 FROM JobListings WHERE JobID = ?";
            if (conf.recordExists(checkEmployeeQuery, JobID)) {
                break;
            } else {
                System.out.println("JobID does not exist. Please try again.");
            }
        }

        String qry = "DELETE FROM JobListings WHERE JobID = ?";
        conf.deleteRecord(qry, JobID);
    }
}
