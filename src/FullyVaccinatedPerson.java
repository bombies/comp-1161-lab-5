
public class FullyVaccinatedPerson extends BasePerson implements Comparable<FullyVaccinatedPerson> {
    private String vaxname;

    public FullyVaccinatedPerson(int age, String name, boolean publish, int id, String vaxname) {
        super(name, age, publish);
        super.setId(id);
        this.vaxname = vaxname;
    }

    public String getVaxName() {
        return vaxname;
    }

    public String getName() {
        final var nameSplit = name.split(" ");
        return String.format("%s, %s", nameSplit[1], nameSplit[0]);
    }

    public String publish() {
        return getPublish() ? String.format("<p>%s took the %s vaccine!!!</p>", getName(), vaxname) : "";
    }

    public static String getFVHeader() {
        String returnval = "ID\tName\t\tVaccine";
        returnval += "\n---------------------------------";
        return returnval;
    }


    @Override
    public String toString() {
        return String.format("%d%s\t%s\t\t%s", getId(), getPublish() ? "*" : "", getName(), getVaxName());
    }

    @Override
    public int compareTo(FullyVaccinatedPerson o) {
        return this.getName().compareTo(o.getName());
    }
}
