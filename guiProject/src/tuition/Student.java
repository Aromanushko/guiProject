package tuition;
/**
 * Student class Represents a Student
 * 
 * @author Robert Reid, Anthony Romanushko
 */
public class Student {
    /**
     * profile of name and major of student
     */
    private Profile profile;
    /**
     * credit hours student is taking
     */
    private int numCredits;
    /**
     * last date a payment was made
     */
    private Date lastPaymentDate;
    /**
     * amount of last payment that was made
     */
    private double lastPayment;
    /**
     * amount of tuition due
     */
    private double tuitionDue = 0;
    /**
     * one time fee for full time students
     */
    public static final double fullUniFee = 3268;
    /**
     * one time fee for part time students
     */
    public static double partUniFee = (3268 * .8);
    /**
     * default empty payment date
     */
    public static final String defaultPaymentDate = "--/--/--";
    /**
     * Full time credits
     */
    public static final int fullTimeCredits = 12;
    /**
     * credits covered by base tuition
     */
    public static final int baseCredits = 16;
    /**
     * total paid so far
     */
    private double fullPayment = 0;
    /**
     * maximum credits a student can enroll in
     */
    public static final int maxCredits = 24;
    /**
     * minimum credits a student can enroll in
     */
    public static final int minCredits = 3;

    /**
     * Constructor for student when student is added to roster
     * 
     * @param name       name of student
     * @param major      major of student
     * @param NUMCREDITS credit hours student is taking
     */
    public Student(String name, Major major, int NUMCREDITS) {
        profile = new Profile(name, major);
        numCredits = NUMCREDITS;
    }

    /**
     * Constructor for student when student is needed to check for other student
     * 
     * @param name  name of student
     * @param major major of student
     */
    public Student(String name, Major major) {
        profile = new Profile(name, major);
    }

    /**
     * get credits of student
     * 
     * @return numCredits
     */
    public int getNumCredits() {
        return numCredits;
    }

    /**
     * helper method to set the credits of the student
     * 
     * @param CREDITS - number of credits to be set
     */
    public void setNumCredits(int CREDITS) {
        this.numCredits = CREDITS;
    }

    /**
     * get tuitionDue of student
     * 
     * @return tuitionDue
     */
    public double getTuitionDue() {
        return this.tuitionDue;
    }

    /**
     * set tuitionDue of student
     * 
     * @param TUITION for student
     */
    public void setTuitionDue(double TUITION) {
        this.tuitionDue = TUITION;
    }

    /**
     * Function to determine if student is full or part time based off of credits
     * 
     * @return true if student is full time, false otherwise
     */
    public boolean checkFullTime() {
        return (numCredits >= fullTimeCredits);
    }

    /**
     * blank declaration of tuition due, to be overridden in child classes
     */
    public void tuitionDue() {
        System.out.println("Uh oh");
    }

    /**
     * getter method for student name
     * 
     * @return student name
     */
    public String getName() {
        return profile.getName();
    }

    /**
     * getter method for last payment date
     * 
     * @return date of last payment
     */
    public Date getLastPaymentDate() {
        return this.lastPaymentDate;
    }

    /**
     * setter method for last payment date
     * 
     * @param DATE Date of last payment
     */
    public void setLastPaymentDate(Date DATE) {
        this.lastPaymentDate = DATE;
    }

    /**
     * setter method for last payment
     * 
     * @param LAST_PAYMENT amount of last payment
     */
    public void setLastPayment(double LAST_PAYMENT) {
        this.lastPayment = LAST_PAYMENT;
    }

    /**
     * getter method for last payment
     * 
     * @return amount of last payment
     */
    public double getLastPayment() {
        return this.lastPayment;
    }

    /**
     * getter method for Full payment
     * 
     * @return amount of Full payment
     */
    public double getFullPayment() {
        return this.fullPayment;
    }

    /**
     * setter method for Full payment
     * 
     * @param payment amount of full payment
     */
    public void setFullPayment(double payment) {
        this.fullPayment += payment;
    }

    /**
     * getter method for student major
     * 
     * @return student major
     */
    public Major getMajor() {
        return profile.getMajor();
    }

    /**
     * Function to check if student is equal to another object
     * 
     * @param other other object to compare student to
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Student) {
            return ((((Student) other).getName().equals(this.getName()))
                    && ((Student) other).getMajor().equals(this.getMajor()));
        } else {
            return false;
        }
    }

    /**
     * string representation of student
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        String lastPDate = defaultPaymentDate;
        if (this.getLastPaymentDate() != null) {
            lastPDate = this.getLastPaymentDate().toString();
        }
        String output = this.getName() + ":" + this.getMajor() + ":" + this.getNumCredits()
                + " credit hours:tuition due:" + String.format("%,.2f", this.getTuitionDue()) + ":total payment:"
                + String.format("%,.2f", this.getFullPayment()) + ":last payment date: " + lastPDate;
        return output;
    }
}
