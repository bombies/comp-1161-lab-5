
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class VaxProgram {
    private ArrayList<Person> plist = new ArrayList<Person>();
    private ArrayList<ApprovedPerson> aplist = new ArrayList<ApprovedPerson>();
    private ArrayList<VaccineBatch> vblist = new ArrayList<VaccineBatch>();
    private ArrayList<FullyVaccinatedPerson> fvlist = new ArrayList<FullyVaccinatedPerson>();
    private int initApproved;
    /*
    vx.loadPersons("persons.txt"); 
    ArrayList<ApprovedPerson> aplist = vx.loadApproved(plist, "approved.txt");
    int initApproved = aplist.size();
    ArrayList<VaccineBatch> vblist = vx.loadVcBatches("vcbatches.txt");
    //3
    //3. savdvclist
    ArrayList<FullyVaccinatedPerson> fvlist = vx.applyVaxes(aplist, vblist);
     */

    public void clearData() {
        plist.clear();
        aplist.clear();
        vblist.clear();
        fvlist.clear();
        Person.resetId();
    }

    public ArrayList<Person> loadPersons(String pfile) {
        Scanner pscan = null;
        ArrayList<Person> plist = new ArrayList<Person>();
        try {
            pscan = new Scanner(new File(pfile));
            while (pscan.hasNext()) {
                String[] nextLine = pscan.nextLine().split(" ");
                String name = nextLine[0] + " " + nextLine[1];
                int age = Integer.parseInt(nextLine[2]);
                boolean publish = false;
                if (nextLine[3].equals("0"))
                    publish = false;
                else
                    publish = true;
                Person p = new Person(name, age, publish);
                plist.add(p);
            }

            pscan.close();
        } catch (IOException e) {
        }

        return plist;

    }

    public void loadApproved(String afile) {
        final var fileName = "src/cases/" + afile.split("cases/")[1];
        try (final var apscan = new Scanner(new File(fileName))) {
            while (apscan.hasNext()) {
                String apLine = apscan.nextLine();
                String[] nextLine = apLine.split(" ");

                int id = Integer.parseInt(nextLine[0]);
                int foundpos = findPerson(plist, id);//.indexOf(id);//***
                if (foundpos >= 0) {
                    ApprovedPerson ap = new ApprovedPerson(plist.get(foundpos).getAge(),
                            plist.get(foundpos).getName(), plist.get(foundpos).getPublish(),
                            plist.get(foundpos).getId());
                    for (int i = 1; i < nextLine.length; i++)
                        ap.addComorbidity(nextLine[i]);
                    aplist.add(ap);
                    plist.remove(foundpos);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("There was no file with the name: " + fileName);
        }
    }

    public ArrayList<VaccineBatch> loadVCBatches(String vcfile) throws IOException, ArrayIndexOutOfBoundsException {
        final var fileName = "src/cases/" + vcfile.split("cases/")[1];
        Scanner vscan = new Scanner(new File(fileName));
        ArrayList<VaccineBatch> vlist = new ArrayList<>();

        while (vscan.hasNext()) {
            final var fileLine = vscan.nextLine().split("\\t");
            vlist.add(new VaccineBatch(
                    fileLine[0],
                    Integer.parseInt(fileLine[1]),
                    Integer.parseInt(fileLine[3]),
                    fileLine[4]
            ));
        }

        return vlist;

    }

    public ArrayList<Person> getPlist() {
        return plist;
    }

    public ArrayList<ApprovedPerson> getAPlist() {
        return aplist;
    }

    public ArrayList<VaccineBatch> getVBlist() {
        return vblist;
    }

    public ArrayList<FullyVaccinatedPerson> getFVlist() {
        return fvlist;
    }

    //public void updateApproved(ArrayList<ApprovedPerson> aplist)
    public void updateApproved() {

        this.aplist = aplist;
    }

    public int getInitApproved() {
        return initApproved;
    }

    public void loadData(int caseNo) {
        plist = loadPersons(getPersonInFile(caseNo));
        loadApproved(getApprovalInFile(caseNo));
        try {
            vblist = loadVCBatches(getVBatchInFile(caseNo));
        } catch (IOException ioe) {
        } catch (ArrayIndexOutOfBoundsException ae) {
        }
    }

    public String getPersonInFile(int caseNo) {
        return "./cases/TestCase" + caseNo + ".persons.txt";

    }

    public String getApprovalInFile(int caseNo) {
        return "./cases/TestCase" + caseNo + ".approved.txt";

    }

    public String getVBatchInFile(int caseNo) {
        return "./cases/TestCase" + caseNo + ".batches.txt";

    }


    public int findPerson(ArrayList<? extends BasePerson> p, int id) {
        return IntStream.range(0, p.size())
                .filter(i -> id == p.get(i).getId())
                .findFirst()
                .orElse(-1);
    }

    public int countPersons() {
        return plist.size();
    }

    public int countApproved() {
        return aplist.size();
    }

    public int countVaxed() {
        return fvlist.size();
    }

    public void printAllVBatches(PrintStream outStream, boolean header) {

        //Collections.sort(vblist);
        if (header)
            outStream.println(VaccineBatch.getVBHeader());
        for (VaccineBatch vb : vblist)
            outStream.println(vb);
    }

    public void printAllPersons(PrintStream outStream, boolean header) {
        if (header)
            outStream.println(Person.getPHeader());
        for (Person p : plist)
            outStream.println(p);
    }

    public void printAllApproved(PrintStream outStream, boolean header) {
        //Collections.sort(aplist);
        if (header)
            outStream.println(ApprovedPerson.getAPHeader());
        for (ApprovedPerson ap : aplist)
            outStream.println(ap);
    }

    public void printAllVaxed(PrintStream outStream, boolean header) {
        //Collections.sort(fvlist);
        if (header)
            outStream.println(FullyVaccinatedPerson.getFVHeader());
        for (FullyVaccinatedPerson fv : fvlist)
            outStream.println(fv);
    }

    public void reportAllVaxed(PrintStream outStream) {
        String pub;
        //Collections.sort(fvlist);
        for (FullyVaccinatedPerson fv : fvlist) {
            pub = fv.publish();
            if (pub.length() > 0)
                outStream.println(pub);
        }
    }

    public void printFindPerson(PrintStream outStream) {

        for (BasePerson p : plist)
            outStream.println(findPerson(plist, p.getId()));
        for (BasePerson ap : aplist)
            outStream.println(findPerson(aplist, ap.getId()));

        for (BasePerson fv : fvlist)
            outStream.println(findPerson(fvlist, fv.getId()));

    }

    public int countVaxes() {
        int sum = 0;
        int i = 0;
        //ArrayList<VaccineBatch> vlist
        boolean found = false;
        for (VaccineBatch vb : vblist)
            sum += vb.getBalance();

        return sum;

    }

    public List<FullyVaccinatedPerson> applyVaxes() {
        ArrayList<FullyVaccinatedPerson> fvlist = new ArrayList<>();
        if (aplist.size() > 0) {
            Collections.sort(aplist);
            Collections.sort(vblist);
            for (VaccineBatch vb : vblist) {
                int apos = aplist.size() - 1;
                while ((apos >= 0) && (vb.getBalance() > 0)) {
                    ApprovedPerson ap = aplist.get(apos);
                    Person p = (Person)plist.get(findPerson(plist,ap.getId()));
                    if (!(vb.contraImpact(ap.getComorbids()))) {
                        vb.reduceBalance();

                        FullyVaccinatedPerson fv = new FullyVaccinatedPerson(
                                ap.getAge(), ap.getSimpleName(), ap.getPublish(), ap.getId(), vb.getName());
                        fvlist.add(fv);
                        aplist.remove(apos);

                    }
                    apos--;
                }

            }
        }

        fvlist.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
        return fvlist;
    }

    public void applyRemaining() {
        //Collections.sort(vblist);
        Collections.sort(plist);
        for (VaccineBatch vb : vblist) {
            int pos = plist.size() - 1;
            while ((pos >= 0) && (vb.getBalance() > 0)) {
                Person p = (Person) plist.get(pos);
                //if (findPerson(fvlist, p.getId())>=0 )//pos id not in fully vaccinated
                FullyVaccinatedPerson fv = new FullyVaccinatedPerson(
                        p.getAge(), p.getName(), p.getPublish(), p.getId(), vb.getName());
                fvlist.add(fv);
                vb.reduceBalance();
                plist.remove(pos);

                pos--;
            }

        }

    }

    public void publishData() {
        int numPersons = plist.size();
        //int initApproved 
        int approvedRemaining = aplist.size();
        //ArrayList<VaccineBatch> vblist, ArrayList<FullyVaccinatedPerson> fvlist
        String header = "<html>";
        header += "<head><meta http-equiv='refresh' content='30'></head>";
        header += "<hr>";
        header += "<p><font face =Arial size=2>Total Applicants:" + numPersons + "</font></p>";
        header += "<p><font face =Arial size=2>Initially Approved:" + initApproved + "</font></p>";
        header += "<p><font face =Arial size=2>Total Administered:" + fvlist.size() + "</font></p>";
        header += "<p><font face =Arial size=2>Postponed:" + approvedRemaining + "</font></p><hr>";

        String vcdata = "<table border = 0><tr><td>Name</td><td>Size</td><td>Balance</td></tr>";
        //Collections.sort(vblist);
        for (VaccineBatch vb : vblist)
            vcdata += "<tr><td>" + vb.getName() + "</td><td>" + vb.getSize() + "</td><td>" + vb.getBalance() + "</td></tr>";
        vcdata += "</table>";

        String personalPublish = "";
        String pub;
        //Collections.sort(fvlist);
        for (FullyVaccinatedPerson fv : fvlist) {
            pub = fv.publish();
            if (pub.length() > 0)
                personalPublish += fv.publish();
        }
        String footer = "<hr></html>";

        String pubFile = header + vcdata + personalPublish + footer;
        PrintWriter outwriter = null;
        try {

            outwriter = new PrintWriter(new FileOutputStream("publish.html"));
            outwriter.write(pubFile);
            outwriter.close();
            String userdir = System.getProperty("user.dir");
            System.out.println("Report written to " + userdir + "\\publish.html");
        } catch (IOException ioe) {
        }

    }

    public void showData(PrintStream outStream) {
        boolean header = true;
        outStream.print("=========" + countVaxes());
        outStream.println(" VACCINE BATCH(ES)=======");
        printAllVBatches(outStream, header);
        outStream.print("=========" + countPersons());
        outStream.println(" PERSON(S)=======");
        printAllPersons(outStream, header);
        outStream.print("=========" + countApproved());
        outStream.println(" APPROVED PERSON(S) OUTSTANDING======");
        printAllApproved(outStream, header);
        outStream.print("=========" + countVaxed());
        outStream.println(" FULLY VACCINATED PERSON(S)=======");
        printAllVaxed(outStream, header);
    }

}

