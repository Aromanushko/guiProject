package application;

import tuition.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class TuitionGuiController {
	private Roster roster = new Roster();
	@FXML private Button btnAddStudent;
	@FXML private Button btnRemoveStudent;
	@FXML private TextField txtStudentName;
	@FXML private TextField txtCredits;
	@FXML private Label txtOutputArea;
	
	@FXML private ToggleGroup Majors;
	@FXML private RadioButton rdbCS;
	@FXML private RadioButton rdbEE;
	@FXML private RadioButton rdbME;
	@FXML private RadioButton rdbIT;
	@FXML private RadioButton rdbBA;
	
	@FXML private ToggleGroup ResidentStatus;
	@FXML private RadioButton rdbRes;
	@FXML private RadioButton rdbNonRes;
	
	@FXML private ToggleGroup NonResidentStatus;
	@FXML private RadioButton rdbNY;
	@FXML private RadioButton rdbCT;
	@FXML private RadioButton rdbInt;
	@FXML private ToggleButton tgSAS;
	//Were going to check for invalid characters in the student name
	private Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
	private Pattern d = Pattern.compile("[^0-9]");
	
	@FXML
	void AddStudent(ActionEvent Event) {
		String errorMessage = validateStudentInfoEntered();
		if(!(errorMessage.equals(""))) {
			txtOutputArea.setText(errorMessage);
		}else {
			String[] student = createStudent();
			txtOutputArea.setText(execCommand(student));
		}
	}
	
	@FXML
	void RemoveStudent(ActionEvent Event) {
		String errorMessage = validateStudentRemovedInfoEntered();
		if(!(errorMessage.equals(""))) {
			txtOutputArea.setText(errorMessage);
		}else {
			String[] student = deleteStudent();
			txtOutputArea.setText(execCommand(student));
		}
	}
	
	@FXML
	void tuitionDue(ActionEvent Event)
	{
	    String errorMessage = validateStudentRemovedInfoEntered();
	    if(!(errorMessage.equals(""))) {
            txtOutputArea.setText(errorMessage);
        }else {
            txtOutputArea.setText(roster.calcSingle(new Student(txtStudentName.getText(),parseMajor(getMajor()))));
        }
	}
	/**
     * method that resolves user commands and then executes them if valid, returns
     * operation status of command afterwards
     * 
     * @param rawInput from the parseCommand method
     * @return returned message to the user
     */
     public String execCommand(String[] arr) {
        String retMes = "";
        String command = arr[0];
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
    
	public String validateStudentInfoEntered() {
		Matcher m = p.matcher(txtStudentName.getText());
		Matcher n = d.matcher(txtCredits.getText());
		if(m.find()) {
			return "Student names cannot contain special characters.";
		}else if(txtStudentName.getText().trim().length() == 0){
			return "Please enter a student name.";
		}
		try {
			if(Majors.getSelectedToggle().equals(null)) {
				return "All students must delcare a major.";
			}
		}catch(NullPointerException e) {
			return "All students must delcare a major.";
		}
		if(ResidentStatus.getSelectedToggle() == null) {
			return "Please declare residency status of student.";
		}
		if(n.find() || txtCredits.getText().trim().length() == 0) {
			return "Credits entered incorrectly.";
		}
		return "";
	}
	
	public String validateStudentRemovedInfoEntered() {
		Matcher m = p.matcher(txtStudentName.getText());
		Matcher n = d.matcher(txtCredits.getText());
		if(m.find()) {
			return "Student names cannot contain special characters.";
		}else if(txtStudentName.getText().trim().length() == 0){
			return "Please enter a student name.";
		}
		try {
			if(Majors.getSelectedToggle().equals(null)) {
				return "Enter students major.";
			}
		}catch(NullPointerException e) {
			return "Enter students major.";
		}
		return "";
	}
	
	private String[] createStudent() {
		String major = getMajor();
		String name = txtStudentName.getText();
		String status = "";
		String credits = txtCredits.getText();
		String command = "";
		if(rdbRes.isSelected()) {
			command = "AR";
		}else {//They are non Resident
			command = "AT";
			if(rdbNY.isSelected()) {
				status = "NY";
			}else if(rdbCT.isSelected()) {
				status = "CT";
			}else if(rdbInt.isSelected()){
				command = "AI";
				if(tgSAS.isSelected()) {
					status = "true";
				}else {
					status = "false";
				}
			}else {
				command = "AN";
				status = "";
			}
		}
		return new String[]{command, name, major, credits, status};
	}
	
	private String[] deleteStudent() {
		String command = "R";
		String name = txtStudentName.getText();
		String major = getMajor();
		return new String[] {command, name, major};
	}
	 
    /**
     * function to validate input
     * 
     * @param arr arguments presented
     * @return retMes status of operations
     */
    private String valInput(String[] arr) 
    {
        String retMes = "";
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
    public Major parseMajor(String m) {
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
	
	public String getMajor() {
		if(rdbCS.isSelected()) {
			return "CS";
		}else if(rdbIT.isSelected()) {
			return "IT";
		}else if(rdbEE.isSelected()) {
			return "EE";
		}else if(rdbME.isSelected()) {
			return "ME";
		}else{
			return "BA";
		}
	}
	
	 /**
     * function to remove Student from roster
     * 
     * @param arr arguments to execute command upon
     * @return status of operations
     */
    private String removeStudent(String[] arr) {
        String retMes = "";
            if (roster.remove(new Student(arr[1], parseMajor(arr[2])))) {
                retMes = "Student removed from the roster.";
            } else {
                retMes = "Student is not in the roster.";
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
	
	@FXML
	void AddNonResOptions(ActionEvent Event) {
		rdbNY.setDisable(false);
		rdbCT.setDisable(false);
		rdbInt.setDisable(false);
		tgSAS.setDisable(false);
	}
	
	
	@FXML
	void RemoveNonResOptions(ActionEvent Event) {
		rdbNY.setDisable(true);
		rdbCT.setDisable(true);
		rdbInt.setDisable(true);
		tgSAS.setDisable(true);
		tgSAS.setSelected(false);
		rdbInt.setSelected(true);
		rdbInt.setSelected(false);
	}
	
	
}
