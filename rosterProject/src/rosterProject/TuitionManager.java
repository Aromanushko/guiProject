package rosterProject;

import java.util.Scanner;

/**
 * Tuition Manager class user interface Used to resolve user input into commands
 * that are then executed
 * 
 * @author Robert Reid, Anthony Romanushko
 */
public class TuitionManager {
    /**
     * roster object for tuitionManager to use in function
     */
    private Roster roster = new Roster();
 
    /**
     * function to validate input
     * 
     * @param arr arguments presented
     * @return retMes status of operations
     */
    private String valInput(String[] arr) 
    {
        String retMes = "";
        int valParam = 4;
        int mCPos = 3;
        if(arr[0].equals("AT") || arr[0].equals("AI")) {valParam++;}
        if (arr.length == valParam) {
            if (validateMajor(arr[2])) {
                try {
                    if (arr[0].equals("AT") && !validateState(arr[4])) {
                        retMes = "Not part of the tri-state area.";
                    }
                    else if (Integer.parseInt(arr[3]) < 0) {
                        retMes = "Credit hours cannot be negative.";
                    } else if (Integer.parseInt(arr[3]) < Student.minCredits) {
                        retMes = "Minimum credit hours is 3.";
                    } else if ( arr[0].equals("AI") && (Integer.parseInt(arr[3]) < International.minCredits)) {
                        retMes = "International students must enroll at least 12 credits.";
                    } else if(Integer.parseInt(arr[3]) > Student.maxCredits) {
                        retMes = "Credit hours exceed the maximum 24.";
                    } 
                } catch (NumberFormatException e) {
                    return "Invalid credit hours.";
                }
            } else {
                retMes = "'" + arr[2] + "' is not a valid major.";
            }
        } else if (arr.length == mCPos) {
            retMes = "Credit hours missing.";
        } else {
            retMes = "Missing data in command line.";
        }
        return retMes;
    }
    
    
    
    /**
     * function to add Resident student to roster
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String addResident(String[] arr) {
        String retMes = "";
        if(valInput(arr).length() > 0) {retMes = valInput(arr);}
        else if (roster.add(new Resident(arr[1], parseMajor(arr[2]), Integer.parseInt(arr[3])))) {
            retMes = "Student added.";
        } else {
            retMes = "Student is already in the roster.";
        }
        return retMes;
    }

    /**
     * function to add Non Resident student to roster
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String addNonResident(String[] arr) {
        String retMes = "";
        if(valInput(arr).length() > 0) {retMes = valInput(arr);}
        else if (roster.add(new NonResident(arr[1], parseMajor(arr[2]), Integer.parseInt(arr[3])))) {
            retMes = "Student added.";
        } else {
            retMes = "Student is already in the roster.";
        }
        return retMes;
    }

    /**
     * function to add Tristate student to roster
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String addTristate(String[] arr) {
        String retMes = "";
        if(valInput(arr).length() > 0) {retMes = valInput(arr);}
        else if (roster.add(new TriState(arr[1], parseMajor(arr[2]), Integer.parseInt(arr[3]),
                arr[4].toUpperCase()))) {
                retMes = "Student added.";
        } else {
            retMes = "Student is already in the roster.";
        }
        return retMes;
    }

    /**
     * function to add International Student to roster
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String addInternational(String[] arr) {
        String retMes = "";
        if(valInput(arr).length() > 0) {retMes = valInput(arr);}
        else if (roster.add(new International(arr[1], parseMajor(arr[2]), Integer.parseInt(arr[3]),
                Boolean.parseBoolean(arr[4])))) {
                retMes = "Student added.";
        } else {
            retMes = "Student is already in the roster.";
        }
        return retMes;
    }

    /**
     * function to remove Student from roster
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String removeStudent(String[] arr) {
        String retMes = "";
        int valParam = 3;
        if (arr.length == valParam) {
            if (roster.remove(new Student(arr[1], parseMajor(arr[2])))) {
                retMes = "Student removed from the roster.";
            } else {
                retMes = "Student is not in the roster.";
            }
        } else {
            retMes = "Missing data in command line.";
        }
        return retMes;
    }

    /**
     * function to calculate tuition of all students in roster
     * 
     * @return status of operations
     */
    private String calculateTuition() {
        return roster.calcTuition();
    }

    /**
     * function to submit a tuition payment for a specified student
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String payT(String[] arr) {
        String retMes = "";
        int valParam = 5;
        if (arr.length == valParam) {
            Date date = new Date(arr[4]);
            Double payment = Double.parseDouble(arr[3]);
            if (new Date(arr[4]).isValid()) {
                if (roster.getStudent(new Student(arr[1], parseMajor(arr[2]))) == null) {
                    retMes = "Student is not in the roster.";
                } else {
                    if (payment <= 0) {
                        retMes = "Invalid amount.";
                    } else if (payment > roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).getTuitionDue()) {
                        retMes = "Amount is greater than amount due.";
                    } else {
                        retMes = "Payment applied.";
                        roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).setTuitionDue(
                                roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).getTuitionDue() - payment);
                        roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).setLastPaymentDate(date);
                        roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).setFullPayment(payment);
                    }
                }
            } else {
                retMes = "Payment date invalid.";
            }
        } else if (arr.length == (valParam - 1) && Double.parseDouble(arr[3]) <= 0) {
            retMes = "Invalid amount.";
        } else if (arr.length == (valParam - 2)) {
            retMes = "Payment amount missing.";
        } else {
            retMes = "Missing data in command line.";
        }
        return retMes;
    }

    /**
     * function to set study abroad status of student to true
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String setSASTrue(String[] arr) {
        String retMes = "";
        int valParam = 4;
        if (arr.length == valParam) {
            retMes = roster.setSAS(new Student(arr[1], parseMajor(arr[2])));
        } else {
            retMes = "Missing data in command line.";
        }
        return retMes;
    }

    /**
     * function to add set the financial aid amount for a resident student
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String setFinAid(String[] arr) {
        String retMes = "";
        int valParam = 4;
        if (arr.length == valParam) {
            Double payment = Double.parseDouble(arr[3]);
            if ((roster.getStudent(new Student(arr[1], parseMajor(arr[2]))) == null)) {
                retMes = "Student not in the roster.";
            } else {
                String[] sArray = ((roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).toString()).split(":"));
                String status = sArray[sArray.length - 1].trim();
                if (status.equals("resident")) {
                    if (!(roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).checkFullTime())) {
                        retMes = "Parttime student doesn't qualify for the award.";
                    } else if (payment < 0
                            || payment > (roster.getStudent(new Student(arr[1], parseMajor(arr[2])))).getTuitionDue()
                            || (payment > Resident.maxFinAid)) {
                        retMes = "Invalid amount.";
                    } else {
                        if (((Resident) roster.getStudent(new Student(arr[1], parseMajor(arr[2])))).getFinAid() > 0) {
                            retMes = "Awarded once already.";
                        } else {
                            ((Resident) roster.getStudent(new Student(arr[1], parseMajor(arr[2])))).setFinAid(payment);
                            roster.getStudent(new Student(arr[1], parseMajor(arr[2]))).tuitionDue();
                            retMes = "Tuition updated.";
                        }
                    }

                } else {
                    retMes = "Not a resident student.";
                }
            }
        } else if (arr.length == valParam - 1) {
            retMes = "Missing the amount.";
        } else {
            retMes = "Missing data in command line.";
        }
        return retMes;
    }

    /**
     * function to print roster of students
     * 
     * @return status of operations
     */
    private String printRoster() {
        String retMes;
        boolean empty = true;
        Student[] rosterB = roster.getRoster();
        for (int y = 0; y < rosterB.length; y++) {
            if (rosterB[y] != null)
                empty = false;
        }
        if (empty) {
            retMes = "Student roster is empty!";
        } else {
            System.out.println("* list of students in the roster **");
            for (int i = 0; i < rosterB.length; i++) {
                if (rosterB[i] != null) {
                    System.out.println(rosterB[i].toString());
                }
            }
            retMes = "** end of roster **";
        }
        return retMes;
    }

    /**
     * function to print roster of students sorted by student name
     * 
     * @return status of operations
     */
    private String printRosterBN() {
        String retMes;
        boolean empty = true;
        Student[] rosterBN = roster.getRosterBN();
        for (int y = 0; y < rosterBN.length; y++) {
            if (rosterBN[y] != null)
                empty = false;
        }
        if (empty) {
            retMes = "Student roster is empty!";
        } else {
            System.out.println("* list of students ordered by name **");
            for (int x = 0; x < rosterBN.length; x++) {
                if (rosterBN[x] != null)
                    System.out.println(rosterBN[x].toString());
            }
            retMes = "** end of roster **";
        }
        return retMes;
    }

    /**
     * function to print roster of students who have made payments, ordered by the
     * payment date
     * 
     * @return status of operations
     */
    private String printRosterBPD() {
        String retMes;
        boolean empty = true;
        Student[] rosterBPD = roster.getRosterBPD();
        for (int y = 0; y < rosterBPD.length; y++) {
            if (rosterBPD[y] != null) {
                empty = false;
            }
        }
        if (empty) {
            retMes = "Student roster is empty!";
        } else {
            System.out.println("* list of students made payments ordered by payment date **");
            for (int x = 0; x < rosterBPD.length; x++) {
                if (rosterBPD[x] != null && rosterBPD[x].getLastPaymentDate() != null)
                    System.out.println(rosterBPD[x].toString());
            }
            retMes = "** end of roster **";
        }
        return retMes;
    }

    /**
     * method that resolves user commands and then executes them if valid, returns
     * operation status of command afterwards
     * 
     * @param rawInput from the parseCommand method
     * @return returned message to the user
     */
    private String execCommand(String rawInput) {
        String command = parseCommand(rawInput);
        String retMes = "";
        String[] arr = rawInput.split(",");
        if (command.length() == 0) {
            return retMes;
        }
        switch (command) {
        case "AR":
            retMes = addResident(arr);
            break;
        case "AN":
            retMes = addNonResident(arr);
            break;
        case "AT":
            retMes = addTristate(arr);
            break;
        case "AI":
            retMes = addInternational(arr);
            break;
        case "R":
            retMes = removeStudent(arr);
            break;
        case "C":
            retMes = calculateTuition();
            break;
        case "T":
            retMes = payT(arr);
            break;
        case "S":
            retMes = setSASTrue(arr);
            break;
        case "F":
            retMes = setFinAid(arr);
            break;
        case "P":
            retMes = printRoster();
            break;
        case "PN":
            retMes = printRosterBN();
            break;
        case "PT":
            retMes = printRosterBPD();
            break;
        case "Q":
            retMes = "Tuition Manager Terminated";
            break;
        default:
            retMes = "Command '" + command + "' not supported!";
            break;
        }
        return retMes;
    }

    /**
     * Function to check if provided major is a valid major
     * 
     * @param m; the major as string
     * @return if m is a valid major
     */
    private boolean validateMajor(String m) {
        String[] majorStrings = { "CS", "IT", "BA", "EE", "ME" };
        for (String x : majorStrings) {
            if ((m.toUpperCase()).equals(x)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Function to parse major from string version of major
     * 
     * @param m; the major as a string
     * @return enum version of major
     */
    private Major parseMajor(String m) {
        return Major.valueOf(m.toUpperCase());
    }

    /**
     * Function to check if provided state is in the tristate area
     * 
     * @param s; the state as string
     * @return if s is a valid state
     */
    private boolean validateState(String s) {
        String[] stateStrings = { "NY", "CT", };
        for (String x : stateStrings) {
            if ((s.toUpperCase()).equals(x)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param rawInput, the command line input
     * @return string command
     */
    private String parseCommand(String rawInput) {
        if (rawInput.contains(",")) {
            return rawInput.split(",")[0];
        } else {
            return rawInput;
        }
    }

    /**
     * function with loop to read in user commands to execute, terminates program
     * when user issues Q command
     */
    public void run() {
        String command = "";
        String retMes = "";
        // define scanner
        Scanner in = new Scanner(System.in);
        // start notification
        System.out.println("Tuition Manager starts running.");
        // run loop until quit command (Q) is issued by user
        while (!(command.equals("Q"))) {
            command = in.nextLine();
            retMes = execCommand(command);
            if (retMes.length() > 0)
                System.out.println(retMes);
        }
        in.close();
    }
}