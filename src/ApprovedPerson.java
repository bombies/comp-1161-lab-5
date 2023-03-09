
import java.util.ArrayList;

public class ApprovedPerson extends BasePerson {
    ArrayList<String> comorbidities;

    public ApprovedPerson(int age, String name, boolean publish, int id) {
        super(name, age, publish);
        super.setId(id);
        comorbidities = new ArrayList<String>();

    }

    public void addComorbidity(String comorbid) {
        comorbidities.add(comorbid);
    }

    public ArrayList<String> getComorbids() {
        return comorbidities;
    }


    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return name;
    }


    public static String getAPHeader() {
        String returnval = "ID\tName\t\tComorbidities";
        returnval += "\n---------------------------------";
        return returnval;

    }


}
