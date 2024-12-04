package it2d;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class APPLICATIONS {

    Scanner sc = new Scanner(System.in);

    public void record() {
        String response;

        do {
            System.out.println("|--------------------|     APPLICATIONS      |--------------------|");
            System.out.println("|--------------------|Choose an action:      |--------------------|");
            System.out.println("|--------------------|A. ADD APPLICATION     |--------------------|");
            System.out.println("|--------------------|B. VIEW APPLICATION    |--------------------|");
            System.out.println("|--------------------|C. UPDATE APPLICATION  |--------------------|");
            System.out.println("|--------------------|D. DELETE APPLICATION  |--------------------|");
            System.out.println("|--------------------|E. EXIT                |--------------------|");

            boolean validInput = false;
            String action;
            do {
                System.out.print("|-------------------|Enter action letter (A, B, C, D, E): ");
                action = sc.next().toUpperCase();

                if (action.equals("A") || action.equals("B") || action.equals("C") || action.equals("D") || action.equals("E")) {
                    validInput = true;
                } else {
                    System.out.println("|--------------------|Invalid input! Please enter A, B, C, D, or E.");
                }
            } while (!validInput);

            if (action.equals("A")) {
                addApplications();
            } else if (action.equals("B")) {
                viewApplications();
            } else if (action.equals("C")) {
                updateApplications();
            } else if (action.equals("D")) {
                deleteApplications();
            } else if (action.equals("E")) {
                System.out.println("|--------------------|Exiting...");
                return;
            }

            System.out.print("|--------------------|Do you want to continue? (yes or no): ");
            response = sc.next();
        while (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
                System.out.print("|--------------------| Invalid input! Please enter 'yes' or 'no': ");
                response = sc.next();
            }

        } while (response.equalsIgnoreCase("yes"));

        System.out.println("|--------------------| Thank you, see you!");
    }

    public void addApplications() {
        config conf = new config();
        String ApplicantID, JobID;

        Applicant ap = new Applicant();
        ap.viewApplicant();
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

        Job_Listing a = new Job_Listing();
        a.viewJob();
        while (true) {
            System.out.print("|--------------------|Enter Job ID: ");
            JobID = sc.next();

            String checkJobQuery = "SELECT 1 FROM JobListings WHERE JobID = ?";
            if (conf.recordExists(checkJobQuery, JobID)) {
                break;
            } else {
                System.out.println("Job ID does not exist. Please try again.");
            }
        }

        LocalDate applicationDeadline = null;
        boolean validInput = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        do {
            System.out.print("|-------------------| JOB Application Deadline(yyyy-MM-dd): ");
            String ad = sc.next();

            try {
                applicationDeadline = LocalDate.parse(ad, formatter);

                if (!applicationDeadline.isBefore(LocalDate.now())) {
                    validInput = true;
                } else {
                    System.out.println("|-------------------| Invalid input! Please enter a present or future date.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("|-------------------| Invalid format! Please enter the date in yyyy-MM-dd format.");
            }
        } while (!validInput);

        String status = "";
        validInput = false;

        do {
            System.out.println("Enter Status (Approved-Pending-Declined):");
            status = sc.next();

            if (status.equals("Approved") || status.equals("Pending") || status.equals("Declined")) {
                validInput = true;
            } else {
                System.out.println("Wrong Status! Please enter 'Approved', 'Pending', or 'Declined' with the correct spelling and capitalization.");
            }
        } while (!validInput);

        String qry = "INSERT INTO Applications (ApplicantID, JobID, ApplicationDate, Status) VALUES (?, ?, ?, ?)";
        conf.addRecord(qry, ApplicantID, JobID, java.sql.Date.valueOf(applicationDeadline), status);
    }

    public void viewApplications() {
        config conf = new config();
        String choice;

        do {
            System.out.println("Choose an option:");
            System.out.println("A. View all applications");
            System.out.println("B. View applications applying for one Job ID");
            System.out.println("C. View applications with ApplicationDate");
            System.out.println("D. View applicant report by ApplicantID");
            System.out.println("E. View all approved-pending-declined applications");
            System.out.println("F. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.next().toUpperCase(); // Read the choice as a String

            // Handle the user's choice
            if (choice.equals("A")) {
                String query = "SELECT Applications.ApplicationID, Applicants.Name, JobListings.JobTitle, "
                        + "Applications.Status "
                        + "FROM Applications "
                        + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                        + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID";

                String[] headers = {"ApplicationID", "Name", "JobTitle", "Status"};
                String[] columns = {"ApplicationID", "Name", "JobTitle", "Status"};

                conf.viewRecords(query, headers, columns);

            } else if (choice.equals("B")) {
                String query0 = "SELECT DISTINCT a.JobID, j.JobTitle, j.Department FROM Applications a JOIN JobListings j ON a.JobID = j.JobID";
                String[] headers0 = {"JobID", "JobTitle", "Department"};
                String[] columns0 = {"JobID", "JobTitle", "Department"};
                conf.viewRecords(query0, headers0, columns0);

                int jobId;
                boolean isValidJobId = false;

                while (!isValidJobId) {
                    System.out.print("Enter JobID to view applicants: ");
                    jobId = sc.nextInt();

                    String validationQuery = "SELECT 1 FROM Applications WHERE JobID = ?";
                    isValidJobId = conf.checkExistsWithParam(validationQuery, jobId);

                    if (!isValidJobId) {
                        System.out.println("Invalid JobID. Please enter a valid JobID from the list above.");
                    } else {
                        String query1 = "SELECT JobListings.JobID, Applicants.ApplicantID, Applicants.Name, Applicants.Resume, Applications.Status FROM JobListings INNER JOIN Applications ON JobListings.JobID = Applications.JobID INNER JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID WHERE JobListings.JobID = ?";
                        String[] headers1 = {"JobID", "ApplicantID", "Name", "Resume", "Status"};
                        String[] columns1 = {"JobID", "ApplicantID", "Name", "Resume", "Status"};
                        conf.viewRecordsWithParam(query1, headers1, columns1, jobId);
                    }
                }
            } else if (choice.equals("C")) {
                String query2 = "SELECT Applications.ApplicationDate, JobListings.JobTitle, Applicants.Name "
                        + "FROM Applications "
                        + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID "
                        + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                        + "WHERE Applications.ApplicationDate < CURRENT_DATE";

                String[] headers2 = {"ApplicationDate", "JobTitle", "Name"};
                String[] columns2 = {"ApplicationDate", "JobTitle", "Name"};

                conf.viewRecords(query2, headers2, columns2);

            } else if (choice.equals("D")) {
                String query5 = "SELECT Applicants.ApplicantID, Applicants.Name, JobListings.JobTitle, JobListings.Department "
                        + "FROM Applicants "
                        + "LEFT JOIN Applications ON Applicants.ApplicantID = Applications.ApplicantID "
                        + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID";

                String[] headers5 = {"ApplicantID", "Name", "JobTitle", "Department"};
                String[] columns5 = {"ApplicantID", "Name", "JobTitle", "Department"};

                conf.viewRecords(query5, headers5, columns5);

                System.out.print("Enter ApplicantID to view their report: ");
                int applicantId = sc.nextInt();

                String query4 = "SELECT Applicants.ApplicantID, Applicants.Name, Applicants.Email, Applicants.PhoneNumber, Applications.JobID, Applications.ApplicationDate, Applications.Status FROM Applicants LEFT JOIN Applications ON Applicants.ApplicantID = Applications.ApplicantID WHERE Applicants.ApplicantID = ?";
                String[] headers4 = {"ApplicantID", "Name", "Email", "PhoneNumber", "JobID", "ApplicationDate", "Status"};
                String[] columns4 = {"ApplicantID", "Name", "Email", "PhoneNumber", "JobID", "ApplicationDate", "Status"};
                conf.viewRecordsWithParam(query4, headers4, columns4, applicantId);
            } else if (choice.equals("E")) {
                System.out.println("|--------------------Approved--------------------|");
                String query3 = "SELECT Applications.ApplicationID, Applications.ApplicantID, Applications.ApplicationDate, "
                        + "Applications.Status, Applicants.PhoneNumber, JobListings.JobTitle, Applicants.Name "
                        + "FROM Applications "
                        + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID "
                        + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                        + "WHERE Applications.Status = 'Approved'";
                String[] headers3 = {"ApplicationID", "Name", "ApplicationDate", "Status", "PhoneNumber", "JobTitle", "ApplicantID"};
                String[] columns3 = {"ApplicationID", "Name", "ApplicationDate", "Status", "PhoneNumber", "JobTitle", "ApplicantID"};

                conf.viewRecords(query3, headers3, columns3);

                System.out.println("|--------------------Pending--------------------|");
                String query6 = "SELECT Applications.ApplicationID, Applications.ApplicantID, Applications.ApplicationDate, "
                        + "Applications.Status, Applicants.PhoneNumber, JobListings.JobTitle, Applicants.Name "
                        + "FROM Applications "
                        + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID "
                        + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                        + "WHERE Applications.Status = 'Pending'";

                String[] headers6 = {"ApplicationID", "Name", "ApplicationDate", "Status", "PhoneNumber", "JobTitle", "ApplicantID"};
                String[] columns6 = {"ApplicationID", "Name", "ApplicationDate", "Status", "PhoneNumber", "JobTitle", "ApplicantID"};

                conf.viewRecords(query6, headers6, columns6);

                System.out.println("|--------------------Declined--------------------|");
                String query7 = "SELECT Applications.ApplicationID, Applications.ApplicantID, Applications.ApplicationDate, "
                        + "Applications.Status, Applicants.PhoneNumber, JobListings.JobTitle, Applicants.Name "
                        + "FROM Applications "
                        + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID "
                        + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                        + "WHERE Applications.Status = 'Declined'";

                String[] headers7 = {"ApplicationID", "Name", "ApplicationDate", "Status", "PhoneNumber", "JobTitle", "ApplicantID"};
                String[] columns7 = {"ApplicationID", "Name", "ApplicationDate", "Status", "PhoneNumber", "JobTitle", "ApplicantID"};

                conf.viewRecords(query7, headers7, columns7);
            } else if (choice.equals("F")) {
                System.out.println("Exiting program. Goodbye!");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("F")); // Loop until the user chooses to exit
    }

    public void updateApplications() {
        config conf = new config();
        String applicationID;

        String query = "SELECT Applications.ApplicationID, Applicants.Name, JobListings.JobTitle, "
                + "Applications.Status "
                + "FROM Applications "
                + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID";

        String[] headers = {"ApplicationID", "Name", "JobTitle", "Status"};
        String[] columns = {"ApplicationID", "Name", "JobTitle", "Status"};

        conf.viewRecords(query, headers, columns);

        while (true) {
            System.out.print("|--------------------|Enter Application ID: ");
            applicationID = sc.next();

            String checkApplicationQuery = "SELECT 1 FROM Applications WHERE ApplicationID = ?";
            if (conf.recordExists(checkApplicationQuery, applicationID)) {
                break;
            } else {
                System.out.println("Application ID does not exist. Please try again.");
            }
        }

        String query5 = "SELECT * FROM Applicants";
        String[] headers5 = {"ApplicantID", "Name", "Email", "PhoneNumber", "Resume"};
        String[] columns5 = {"ApplicantID", "Name", "Email", "PhoneNumber", "Resume"};

        conf.viewRecords(query5, headers5, columns5);
        System.out.print("|--------------------|EDIT ApplicantID: ");
        String newApplicantID = sc.next();

        String checkApplicantQuery = "SELECT 1 FROM Applicants WHERE ApplicantID = ?";
        while (!conf.recordExists(checkApplicantQuery, newApplicantID)) {
            System.out.println("ApplicantID did not exist. Please try again.");
            System.out.print("|--------------------|EDIT ApplicantID: ");
            newApplicantID = sc.next();
        }

        String query8 = "SELECT * FROM JobListings";
        String[] headers8 = {"JobID", "JobTitle", "Department", "Location", "ApplicationDeadline"};
        String[] columns8 = {"JobID", "JobTitle", "Department", "Location", "ApplicationDeadline"};
        conf.viewRecords(query8, headers8, columns8);

        System.out.print("|--------------------|EDIT JobID: ");
        String newJobID = sc.next();

        String checkJobQuery = "SELECT 1 FROM JobListings WHERE JobID = ?";
        while (!conf.recordExists(checkJobQuery, newJobID)) {
            System.out.println("JobID did not exist. Please try again.");
            System.out.print("|--------------------|EDIT JobID: ");
            newJobID = sc.next();  // Ask again until a valid JobID is entered
        }

        LocalDate newApplicationDate = null;
        boolean validInput = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        do {
            System.out.print("|-------------------| JOB Application Deadline (yyyy-MM-dd): ");
            String ad = sc.next();

            try {
                newApplicationDate = LocalDate.parse(ad, formatter);

                // Check if the date is today or in the future
                if (!newApplicationDate.isBefore(LocalDate.now())) {
                    validInput = true;
                } else {
                    System.out.println("|-------------------| Invalid input! The date cannot be in the past.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("|-------------------| Invalid format! Please enter the date in yyyy-MM-dd format.");
            }
        } while (!validInput);

        String newStatus = "";
        validInput = false;

        do {
            System.out.println("Enter Status (Approved-Pending-Declined):");
            newStatus = sc.next();

            if (newStatus.equals("Approved") || newStatus.equals("Pending") || newStatus.equals("Declined")) {
                validInput = true;
            } else {
                System.out.println("Wrong Status! Please enter 'Approved', 'Pending', or 'Declined' with the correct spelling and capitalization.");
            }
        } while (!validInput);

        String qry = "UPDATE Applications SET ApplicantID = ?, JobID = ?, ApplicationDate = ?, Status = ? WHERE ApplicationID = ?";
        conf.updateRecord(qry, newApplicantID, newJobID, newApplicationDate, newStatus, applicationID);
        String query1 = "SELECT Applications.ApplicationID, Applicants.Name, JobListings.JobTitle, "
                + "Applications.Status "
                + "FROM Applications "
                + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID";

        String[] headers2 = {"ApplicationID", "Name", "JobTitle", "Status"};
        String[] columns3 = {"ApplicationID", "Name", "JobTitle", "Status"};

        conf.viewRecords(query1, headers2, columns3);
    }

    public void deleteApplications() {
        config conf = new config();
        String applicationID;
        String query = "SELECT Applications.ApplicationID, Applicants.Name, JobListings.JobTitle, "
                + "Applications.Status "
                + "FROM Applications "
                + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID";

        String[] headers = {"ApplicationID", "Name", "JobTitle", "Status"};
        String[] columns = {"ApplicationID", "Name", "JobTitle", "Status"};

        conf.viewRecords(query, headers, columns);

        while (true) {
            System.out.print("|--------------------|Enter Application ID: ");
            applicationID = sc.next();

            String checkApplicationQuery = "SELECT 1 FROM Applications WHERE ApplicationID = ?";
            if (conf.recordExists(checkApplicationQuery, applicationID)) {
                break;
            } else {
                System.out.println("Application ID does not exist. Please try again.");
            }
        }

       String query1 = "SELECT Applications.ApplicationID, Applicants.Name, JobListings.JobTitle, "
                + "Applications.Status "
                + "FROM Applications "
                + "LEFT JOIN Applicants ON Applications.ApplicantID = Applicants.ApplicantID "
                + "LEFT JOIN JobListings ON Applications.JobID = JobListings.JobID";

        String[] headers1 = {"ApplicationID", "Name", "JobTitle", "Status"};
        String[] columns1= {"ApplicationID", "Name", "JobTitle", "Status"};

        conf.viewRecords(query1, headers1, columns1);
    }
}
