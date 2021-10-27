package tuition;
/**
 * NonResident student class Represents a NonResident Student
 * 
 * @author Robert Reid, Anthony Romanushko
 */
public class NonResident extends Student {

    /**
     * credit hour rate for NonResident student
     */
    public static final double rate = 966;
    /**
     * tuition for NonResident student
     */
    public static final double tuition = 29737;
    
    /**
     * Constructor for NonResident Student
     * 
     * @param NAME       name of student
     * @param MAJOR      major of student
     * @param NUMCREDITS credit hours student is taking
     */
    public NonResident(String NAME, Major MAJOR, int NUMCREDITS) {
        super(NAME, MAJOR, NUMCREDITS);
    }

    /**
     * calculates tuition due for a NonResident student
     */
    @Override
    public void tuitionDue() {
        double tuition;
        if (this.checkFullTime()) {
            tuition = Student.fullUniFee + NonResident.tuition;
            if (this.getNumCredits() > Student.baseCredits) {
                tuition += (NonResident.rate * (this.getNumCredits() - Student.baseCredits));
            }
        } else {
            tuition = Student.partUniFee + (this.getNumCredits() * NonResident.rate);
        }
        super.setTuitionDue(tuition - this.getFullPayment());
    }

    /**
     * string representation of NonResident Student
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return super.toString() + ":non-resident";
    }
}
