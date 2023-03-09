
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class FullSolution {

    public static void main(String[] args) {
        VaxProgram vx = new VaxProgram();
        Scanner scan = new Scanner(System.in);
        String stuId = "";

        System.out.println("Interactive Mode?:[y/n],(Enter [n] to immediately execute test cases for submission)");
        String choice = scan.next().toUpperCase();
        if (choice.equals("Y")) {
            char mchoice = 'F';
            EntryScreen ec = new EntryScreen();
            System.out.println("Enter number of testcase[0..5] to prime data, or [F/f] for free entry, or [X/x] to  exit ");
            String menu = scan.next().toUpperCase();
            mchoice = menu.charAt(0);
            try {
                int tcase = Integer.parseInt(menu);
                vx.loadData(tcase);
            } catch (NumberFormatException nfe) {
                System.out.println("Initiating free entry");
            }

            while (mchoice != 'X') {
                try {
                    try {
                        Scanner sc = new Scanner(new File("id.txt"));
                        stuId = sc.nextLine();
                        sc.close();
                    } catch (IOException ioe) {
                    }
                    if (stuId.length() == 0)
                        System.out.println("ID not yet set.");
                    else
                        System.out.println("ID number " + stuId);
                    System.out.println("[P]erson entry\n[A]pproval info\n[V]accine batch\n[S]how data\npro[C]ess vaccinations\n[R]eport\nEnter [I]D\ne[X]it");
                    menu = scan.next().toUpperCase();
                    mchoice = menu.charAt(0);
                    switch (mchoice) {
                        case 'P': {
                            Person p = ec.getPerson(scan);
                            if (p != null) {
                                vx.getPlist().add(p);
                                System.out.println("Successfully added person to dataset");
                            } else
                                System.out.println("Something went wrong- Person not added");
                            break;
                        }
                        case 'A': {
                            ApprovedPerson ap = ec.approvePerson(scan, vx);
                            if (ap != null) {
                                vx.getAPlist().add(ap);
                                System.out.println("Successfully approved person");
                            } else
                                System.out.println("Something went wrong- Person not approved");
                            break;
                        }
                        case 'V': {
                            VaccineBatch vb = ec.getVaccineBatch(scan);
                            if (vb != null) {
                                vx.getVBlist().add(vb);
                                System.out.println("Successfully added vaccine batch to dataset");
                            } else
                                System.out.println("Something went wrong- vaccine batch not added");

                            break;
                        }
                        case 'S': {
                            vx.showData(System.out);
                            break;
                        }
                        case 'C': {
                            vx.applyVaxes();//vx.setFVlist(
                            if (vx.countVaxes() > 0)
                                vx.applyRemaining();
                            break;
                        }

                        case 'R': {
                            vx.publishData();
                            break;
                        }
                        case 'I': {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter your student id number:");
                            String stuid = sc.next();
                            sc.close();
                            try {
                                PrintStream p = new PrintStream(new FileOutputStream("id.txt"));
                                p.println(stuid);
                                p.close();
                            } catch (IOException ioe) {
                            }
                            break;
                        }


                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong on your last instruction - Please try again.");
                }
            }
        }

        if (choice.equals("N")) {
            System.out.println("Test functionality currently not implemented. Your first task is to  invoke the tests.");
            //VaxProgram vx1 =new VaxProgram();
            //TestCase.runCases(vx1);
        }
    }

}
