package rosterProject;

/**
 * Class which consists of name and major of a student
 * 
 * @author Anthony Romanushko, Robert Reid
 *
 */
public class Profile {
    /**
     * Name of student
     */
    private String name;
    /**
     * 5 majors and 2-character each: CS, IT, BA, EE, ME
     */
    private Major major;

    /**
     * Constructor for profile
     * 
     * @param NAME  name of student
     * @param MAJOR major of student
     */
    public Profile(String NAME, Major MAJOR) {
        name = NAME;
        major = MAJOR;
    }

    /**
     * Getter function for profile major
     * 
     * @return major of profile
     */
    public Major getMajor() {
        return major;
    }

    /**
     * Getter function for profile name
     * 
     * @return name of profile
     */
    public String getName() {
        return name;
    }
}
