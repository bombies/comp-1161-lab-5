
import java.util.ArrayList;

public class ApprovedPerson extends BasePerson implements Comparable<ApprovedPerson> {
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
        final var nameSplit = name.split(" ");
        return String.format("%s, %s", nameSplit[1], nameSplit[0]);
    }

    public String getSimpleName() {
        return name;
    }


    public static String getAPHeader() {
        String returnval = "ID\tName\t\tComorbidities";
        returnval += "\n---------------------------------";
        return returnval;
    }

    @Override
    public String toString() {
        return String.format("%d\t%s\t\t%s", getId(), getName(), getComorbids());
    }

    @Override
    public int compareTo(ApprovedPerson o) {
        return o.getAge() - this.getAge();
    }
}
