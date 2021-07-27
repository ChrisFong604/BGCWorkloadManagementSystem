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
            this.WorkingWeeks.add(date);
        }

        public void appendToWorkingCapacities(Float WorkCapacity) {
            this.WorkingCapacities.add(WorkCapacity);
        }

         public void setWorkingWeeks(ArrayList<String> WorkingWeeks) {
            this.WorkingWeeks = WorkingWeeks;
        }

        public void setWorkingCapacities(ArrayList<Float> WorkCapacities) {
            this.WorkingCapacities = WorkCapacity;
        }
    }

    //Main 'worksOn' Bean class
    private String projectID;
    private String employeeID;
    private WeeklyCapacity AssignedWork;

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID
    }

    public void setAssignedWork(ArrayList<String> WorkingWeeks, ArrayList<Float> WorkCapacities) {
        this.AssignedWork.
    }

    public String getProjectID() {
        return this.projectID
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public ArrayList<String> getAssignedWork() {
        return this.AssignedWork;
    }
}