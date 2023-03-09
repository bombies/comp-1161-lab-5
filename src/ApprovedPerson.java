
import java.util.ArrayList;

public class ApprovedPerson extends BasePerson {
    ArrayList<String> comorbidities;

    public ApprovedPerson(int age, String name, boolean publish, int id) {
        super(name, age, publish);
        super.setId(id);
        comorbidities = new ArrayList<>();

    }

    public void addComorbidity(String comorbid) {
        comorbidities.add(comorbid);
    }

    public ArrayList<String> getComorbids() {
        return comorbidities;
    }


    @Override
    public String getName() {
        final var nameSplit = this.name.split(" ");
        return String.format("%s,%s", nameSplit[1], nameSplit[2]);
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
        return String.format("%s\t%s\t\t%s", getId(), getName(), getComorbids().toString());
    }
}
