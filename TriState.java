package tuition;
/**
 * Tristate student class Represents a Tristate student
 * 
 * @author Robert Reid, Anthony Romanushko
 */
public class TriState extends NonResident {
    /**
     * State of TriState student
     */
    private String state;
    /**
     * fin aid for NY students
     */
    private double nyFinAid = 4000;
    /**
     * fin aid for CT students
     */
    private double ctFinAid = 5000;
    /**
     * Constructor for TriState Student
     * 
     * @param NAME       name of student
     * @param MAJOR      major of student
     * @param NUMCREDITS credit hours student is taking
     * @param STATE      state of TriState student
     */
    public TriState(String NAME, Major MAJOR, int NUMCREDITS, String STATE) {
        super(NAME, MAJOR, NUMCREDITS);
        state = STATE;
    }

    /**
     * Calculate tuition due for TriState Student
     */
    @Override
    public void tuitionDue() {
        double tuition;
        double finAid = 0;
        if (this.checkFullTime()) {
            tuition = Student.fullUniFee + NonResident.tuition;
            if (this.getNumCredits() > Student.baseCredits) {
                tuition += (NonResident.rate * (this.getNumCredits() - Student.baseCredits));
            }
            if (this.state.equals("NY")) {
                finAid = nyFinAid;
            } else if (this.state.equals("CT")) {
                finAid = ctFinAid;
            }
        } else {
            tuition = Student.partUniFee + (this.getNumCredits() * NonResident.rate);
        }
        tuition -= finAid;
        this.setTuitionDue(tuition - this.getFullPayment());
    }

    /**
     * String representation of NonResident Student
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        String output = super.toString() + "(tri-state):" + this.state;
        return output;
    }
}
