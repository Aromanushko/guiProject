package rosterProject;

/**
 * Roster class Contains logic related to interacting with roster and students
 * within a roster
 * 
 * @author Robert Reid, Anthony Romanushko
 */
public class Roster {
    /**
     * roster containing students
     */
    private Student[] roster = new Student[4];
    /**
     * keep track of the number of students in the roster
     */
    private int size = 0;

    /**
     * Function to find the index of a student in roster
     * 
     * @param student to search for
     * @return index of student if found, -1 if student was not found
     */
    private int find(Student student) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (roster[i] != null) {
                if (student.equals(roster[i])) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * Function to get student from roster, returns null if student isn't found
     * 
     * @param student to look for
     * @return reference to student
     */
    public Student getStudent(Student student) {
        if (find(student) != -1) {
            return roster[find(student)];
        } else {
            return null;
        }
    }

    /**
     * Function to grow roster if limit is reached
     * 
     */
    private void grow() {
        Student[] t = new Student[size + 4];
        for (int i = 0; i < size; i++) {
            t[i] = roster[i];
        }
        roster = t;
    }

    /**
     * Function to add a student from the roster if the student is not already
     * present
     * 
     * @param student to add to array
     * @return if the student was added successfully
     */
    public boolean add(Student student) {
        if (find(student) != -1 && size != 0) {
            return false;
        }
        if (size == roster.length)
            grow();
        roster[size] = student;
        size++;
        return true;
    }

    /**
     * Function to calculate tuitions for students present in roster
     * 
     * @return status of command execution
     */
    public String calcTuition() {
        String retMes = "";
        boolean empty = true;
        for (int x = 0; x < size; x++) {
            if (roster[x] != null) {
                empty = false;
            }
        }
        if (empty) {
            retMes = "Student roster is empty!";
            return retMes;
        } else {
            for (int i = 0; i < size; i++) {
                roster[i].tuitionDue();
            }
        }
        retMes = "Calculation completed.";
        return retMes;
    }

    /**
     * Function to set a students study abroad status
     * 
     * @param student to set study abroad status for
     * @return return message regarding status of operations
     */
    public String setSAS(Student student) {
        double defaultPayment = 0;
        String retMes = "";
        int index = find(student);
        if (index == -1) {
            retMes = "Couldn't find the international student.";
        } else {
            String[] sArray = roster[index].toString().split(":");
            String status = sArray[sArray.length - 1].trim();
            if (status.equals("study aboard") || status.equals("international")) {
                if (roster[index].getNumCredits() > International.minCredits) {
                    roster[index].setNumCredits(International.minCredits);
                }
                ((International) roster[index]).setStudyAbroad(true);
                roster[index].setLastPaymentDate(null);
                roster[index].setLastPayment(defaultPayment);
                roster[index].tuitionDue();
                retMes = "Tuition updated.";
            }
        }
        return retMes;
    }

    /**
     * Function to return roster as array
     * 
     * @return roster array
     */
    public Student[] getRoster() {
        return roster;
    }

    /**
     * Function to return roster as array sorted by student name
     * 
     * @return roster array sorted by name
     */
    public Student[] getRosterBN() {
        // Going to use selection sort
        Student[] troster = roster;
        // for(int i = 0; i < roster.length; i++) {troster[i] = roster[i];}
        int n = troster.length;
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (troster[j] != null) {
                    if (troster[j].getName().compareTo(troster[min_idx].getName()) < 0)
                        min_idx = j;
                }
            }
            Student temp = troster[min_idx];
            troster[min_idx] = troster[i];
            troster[i] = temp;
        }
        Student[] fTroster = new Student[n];
        for (int i = 0; i < n; i++) {
            if (troster[i] != null) {
                fTroster[i] = roster[find(troster[i])];
            }
        }
        return fTroster;
    }

    /**
     * Function to return roster as array sorted by last payment date
     * 
     * @return roster array sorted by last payment date
     */
    public Student[] getRosterBPD() {
        Student[] troster = roster;
        // Going to use selection sort
        for (int i = 0; i < troster.length - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < troster.length; j++) {
                if (troster[j] != null) {
                    // do nothing
                    if (troster[j].getLastPaymentDate() == null && troster[min_idx].getLastPaymentDate() == null) {
                    }
                    // do nothing
                    else if (troster[j].getLastPaymentDate() != null && troster[min_idx].getLastPaymentDate() == null) {
                    }
                    // move dateless student down
                    else if (troster[j].getLastPaymentDate() == null && troster[min_idx].getLastPaymentDate() != null) {
                        min_idx = j;
                    }
                    // actually compare dates
                    else if (troster[j].getLastPaymentDate().compareTo(troster[min_idx].getLastPaymentDate()) < 0) {
                        min_idx = j;
                    }
                }
            }
            Student temp = troster[min_idx];
            troster[min_idx] = troster[i];
            troster[i] = temp;
        }
        Student[] fTroster = new Student[troster.length];
        for (int i = 0; i < troster.length; i++) {
            if (troster[i] != null) {
                fTroster[i] = roster[find(troster[i])];
            }
        }
        return fTroster;
    }

    /**
     * Function to remove a student from the roster if the student is found
     * 
     * @param student student to remove from array
     * @return if the student was removed successfully
     */
    public boolean remove(Student student) {
        int index = find(student);
        if (index != -1) {
            // Student exists in roster so we move the indexes down
            for (int r = index; r < (size - 1); r++) {
                roster[r] = roster[r + 1];
            }
            size--;
            return true;
        } else {
            return false;
        }
    }

}
