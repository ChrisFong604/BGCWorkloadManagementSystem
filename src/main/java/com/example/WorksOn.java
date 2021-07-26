import java.util.ArrayList;

public class worksOn {

    /*
        Equivalent to the SQL composite type that will be implemented.
        Start of each week the employee is working is saved in WorkingWeeks[].
        The work capacity allocated to each week for the employee will be stored 
        in WorkingCapacities[]. The indices will match
    */
    public class WeeklyCapacity {
        private ArrayList<String> WorkingWeeks[];
        private ArrayList<Float> WorkingCapacities[];

        public void appendToWorkingWeeks(String date) {
            this.WorkingWeeks.add(date)
        }

        public void appendToWorkingCapacities(Float WorkCapacity) {
            this.WorkingCapacities.add(WorkCapacity)
        }
    }

    private String projectID;
    private String employeeID;
    private WeeklyCapacity AssignedWork;

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID
    }

    public String getProjectID() {
        return this.projectID
    }

    public String getEmployeeID() {
        return this.employeeID;
    }


}