import java.util.ArrayList;
import java.util.Scanner;

/**
 * This program provides entry screens to accept persons, approvals and batches.
 *
 * @author PGaynor
 *
 */
public class EntryScreen {

    /**
     * This is the default constructor, which is the only one needed.
     */
    public EntryScreen() {

    }

    /**
     * This method accepts information on a person
     */
    public Person getPerson(Scanner scan) {

        Person p = null;
        scan.nextLine();//This clears the buffer of any previously waiting input
        try {
            System.out.println("Enter Name:");
            String name = scan.nextLine();
            System.out.println("Enter Age:");
            int age = Integer.parseInt(scan.nextLine());
            System.out.println("Willing to publish?(1=yes,0=no):");
            int willpub = Integer.parseInt(scan.nextLine());
            boolean pub;
            pub = willpub != 0;
            p = new Person(name, age, pub);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input");
        }
        return p;

    }

    /**
     * This method accepts information on a batch of vaccines
     */
    public VaccineBatch getVaccineBatch(Scanner scan) {

        scan.nextLine(); // Clear input buffer

        try {
            String name, contraIndicators;
            int size, preference;

            System.out.print("Enter size: ");
            size = Integer.parseInt(scan.nextLine());
            System.out.print("Enter name: ");
            name = scan.nextLine();
            System.out.print("Enter preference: ");
            preference = Integer.parseInt(scan.nextLine());
            System.out.print("Enter contra indicators (comma separated): ");
            contraIndicators = scan.nextLine();

            return new VaccineBatch(name, size, preference, contraIndicators);
        } catch (NumberFormatException e) {
            System.out.println("There was an error parsing an input to a number!");
            return null;
        }
    }

    /**
     * This method accepts approves a person
     */
    public ApprovedPerson approvePerson(Scanner scan, VaxProgram vx) {

        ApprovedPerson ap = null;
        try {
            scan.nextLine();//This clears the buffer of any previously waiting input
            ArrayList<? extends BasePerson> plist = vx.getPlist();
            System.out.println("Enter ID:");
            int id = Integer.parseInt(scan.nextLine());
            int foundpos = -1;//vx.findPerson(plist, id);//.indexOf(id);//***
            if (foundpos >= 0) {
                Person foundPerson = (Person) plist.get(foundpos);
                ap = new ApprovedPerson(foundPerson.getAge(),
                        foundPerson.getName(), foundPerson.getPublish(), foundPerson.getId());

                String co = "";
                do {

                    if (co.length() > 1)
                        ap.addComorbidity(co);
                    System.out.println("Add a co-morbidity, or press [X] to exit:");
                }
                while (!(co = scan.nextLine()).equalsIgnoreCase("x"));
            }
        } catch (NumberFormatException nfe) {
        }
        return ap;
    }

}
