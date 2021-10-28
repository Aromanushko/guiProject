package tuition;

/**
 * International student class Represents an International student
 * 
 * @author Robert Reid, Anthony Romanushko
 */
public class International extends NonResident {
    /**
     * studyAbroad status of international student
     */
    private boolean studyAbroad;
    /**
     * additional fee paid by international student
     */
    public static double additionFee = 2650;
    /**
     * minimum credits an international student may enroll in
     */
    public static final int minCredits = 12;

    /**
     * Constructors for an international student
     * 
     * @param NAME        name of student
     * @param MAJOR       major of student
     * @param NUMCREDITS  number of credit hours student is taking
     * @param STUDYABROAD study abroad status of International Student
     */
    public International(String NAME, Major MAJOR, int NUMCREDITS, Boolean STUDYABROAD) {
        super(NAME, MAJOR, NUMCREDITS);
        studyAbroad = STUDYABROAD;
    }

    /**
     * Setter method for studyAbroad status
     * 
     * @param status study abroad status of international student
     */
    public void setStudyAbroad(boolean status) {
        studyAbroad = status;
        this.setFullPayment(-this.getFullPayment());
    }

    /**
     * Getter method for studyAbroad status
     * 
     * @return studyAbroad status
     */
    public boolean getStudyAbroad() {
        return this.studyAbroad;
    }

    /**
     * Calculate tuition due for international student
     */
    @Override
    public void tuitionDue() {
        double tuition = 0;
        if (this.getStudyAbroad()) {
            if (this.checkFullTime()) {
                tuition += Student.fullUniFee + additionFee;
            } else {
                tuition += Student.partUniFee + additionFee;
            }
        } else {

            if (this.checkFullTime()) {
                tuition += NonResident.tuition;
                tuition += Student.fullUniFee;
                tuition += additionFee;
                if (this.getNumCredits() > 16) {
                    tuition += (this.getNumCredits() - 16) * NonResident.rate;
                }
            } else {
                tuition = Student.partUniFee + (this.getNumCredits() * NonResident.rate);
            }
        }
        this.setTuitionDue(tuition - this.getFullPayment());
    }

    /**
     * string representation of international student
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        String output = super.toString() + ":international";
        if (this.getStudyAbroad()) {
            output += ":study abroad";
        }
        return output;
    }
}
