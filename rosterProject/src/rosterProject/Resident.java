package rosterProject;

/**
 * Resident student class Represents a Resident student
 * 
 * @author Robert Reid, Anthony Romanushko
 */
public class Resident extends Student {
    /**
     * maximum financial aid for resident students
     */
    public static final double maxFinAid = 10000;
    /**
     * credit hour rate for resident student
     */
    private static double rate = 404;
    /**
     * tuition for resident student
     */
    private static double tuition = 12536;
    /**
     * financial aid amount
     */
    private double finAid = 0;

    /**
     * Constructor for a resident student
     * 
     * @param name       student's name
     * @param major      major of student
     * @param NUMCREDITS number of credit hours student is taking
     */
    public Resident(String name, Major major, int NUMCREDITS) {
        super(name, major, NUMCREDITS);
    }

    /**
     * Function to calculate tuition due for a resident student
     */
    @Override
    public void tuitionDue() {

        double tuit = 0;
        if (this.checkFullTime()) {
            tuit = Student.fullUniFee + tuition;
            if (this.getNumCredits() > Student.baseCredits) {
                tuit += (rate * (this.getNumCredits() - Student.baseCredits));
            }
            tuit -= this.getFinAid();
        } else {
            tuit = Student.partUniFee + (this.getNumCredits() * rate);
        }
        super.setTuitionDue(tuit - this.getFullPayment());
    }

    /**
     * Helper method for setting the financial aid amount a resident can recieve
     * 
     * @param AID - double amount of aid to apply to tuition must be under 10,000
     *            for residents
     */
    public void setFinAid(double AID) {
        this.finAid = AID;
    }

    /**
     * helper method for getting financial aid amount
     * 
     * @return financial aid amount
     */
    public double getFinAid() {
        return this.finAid;
    }

    /**
     * String representation of a resident student
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return super.toString() + ":resident";
    }
}
