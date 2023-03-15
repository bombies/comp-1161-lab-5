import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author PGaynor
 */
public class EntryScreen {
    /**
     * This program provides entry screens to accept persons, approvals and batches.
     *
     * @param args
     */
    public EntryScreen() {
        /**
         * This is the default constructor, which is the only one needed.
         */
    }

    public Person getPerson(Scanner scan) {
        /**
         * This method accepts information on a person
         */
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
            if (willpub == 0)
                pub = false;
            else
                pub = true;
            p = new Person(name, age, pub);
            ;
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input");
        }
        return p;

    }

    /**
     * This method accepts information on a batch of vaccines
     */
    public VaccineBatch getVaccineBatch(Scanner scan) {
        String name;
        int size, preference;
        String contraIndicators;

        //Complete code to accect vacci nebatch from screen
        scan.nextLine();
        System.out.print("Enter the vaccine batch name: ");
        name = scan.nextLine();
        System.out.print("Enter the vaccine batch size:  ");
        size = Integer.parseInt(scan.nextLine());
        System.out.print("Enter the vaccine batch preference:  ");
        preference = Integer.parseInt(scan.nextLine());
        System.out.print("Enter the vaccine batch contra indicators: ");
        contraIndicators = scan.nextLine();

        return new VaccineBatch(name, size, preference, contraIndicators);

    }


    public ApprovedPerson approvePerson(Scanner scan, VaxProgram vx) {
        /**
         * This method accepts approves a person
         */
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
