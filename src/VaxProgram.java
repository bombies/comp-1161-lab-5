
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class VaxProgram {
    private ArrayList<Person> plist = new ArrayList<>();
    private ArrayList<ApprovedPerson> aplist = new ArrayList<>();
    private ArrayList<VaccineBatch> vblist = new ArrayList<VaccineBatch>();
    private ArrayList<FullyVaccinatedPerson> fvlist = new ArrayList<>();
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
        Scanner pscan;
        ArrayList<Person> plist = new ArrayList<Person>();
        try {
            pscan = new Scanner(new File(pfile));
            while (pscan.hasNext()) {
                String[] nextLine = pscan.nextLine().split(" ");
                String name = nextLine[0] + " " + nextLine[1];
                int age = Integer.parseInt(nextLine[2]);
                boolean publish;
                publish = !nextLine[3].equals("0");
                Person p = new Person(name, age, publish);
                plist.add(p);
            }
            pscan.close();
        } catch (IOException e) {
        }

        return plist;
    }

    public void loadApproved(String afile) {
        try {
            Scanner apscan = new Scanner(new File(afile));
            while (apscan.hasNext()) {
                String apLine = apscan.nextLine();
                String[] nextLine = apLine.split(" ");

                int id = Integer.parseInt(nextLine[0]);
                int foundpos = findPerson(plist, id);//.indexOf(id);//***
                if (foundpos >= 0) {
                    Person person = plist.get(foundpos);
                    ApprovedPerson ap = new ApprovedPerson(
                            person.getAge(),
                            person.getName(),
                            person.getPublish(),
                            person.getId()
                    );

                    for (int i = 1; i < nextLine.length; i++)
                        ap.addComorbidity(nextLine[i]);
                    aplist.add(ap);
                    plist.remove(foundpos);
                }
            }

            this.initApproved = aplist.size();
        } catch (FileNotFoundException e) {
            System.out.println("There was no file at the path: " + afile);
        }
    }

    public ArrayList<VaccineBatch> loadVCBatches(String vcfile) throws IOException, ArrayIndexOutOfBoundsException {
        Scanner vscan = new Scanner(new File(vcfile));
        ArrayList<VaccineBatch> vlist = new ArrayList<VaccineBatch>();

        while (vscan.hasNext()) {
            final var fileLine = vscan.nextLine().split(" ");
            vlist.add(new VaccineBatch(
                    fileLine[0],
                    Integer.parseInt(fileLine[1]),
                    Integer.parseInt(fileLine[2]),
                    fileLine.length >= 4 ? fileLine[3] : ""
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
        } catch (IOException | ArrayIndexOutOfBoundsException ioe) {
        }
    }

    public String getPersonInFile(int caseNo) {
        return "src/cases/TestCase" + caseNo + ".persons.txt";

    }

    public String getApprovalInFile(int caseNo) {
        return "src/cases/TestCase" + caseNo + ".approved.txt";

    }

    public String getVBatchInFile(int caseNo) {
        return "src/cases/TestCase" + caseNo + ".batches.txt";

    }


    public int findPerson(ArrayList<? extends BasePerson> p, int id) {
        return IntStream.range(0, p.size())
                .filter(i -> p.get(i).getId() == id)
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
        Collections.sort(aplist);
        if (header)
            outStream.println(ApprovedPerson.getAPHeader());
        for (ApprovedPerson ap : aplist)
            outStream.println(ap);
    }

    public void printAllVaxed(PrintStream outStream, boolean header) {
        Collections.sort(fvlist);
        if (header)
            outStream.println(FullyVaccinatedPerson.getFVHeader());
        for (FullyVaccinatedPerson fv : fvlist)
            outStream.println(fv);
    }

    public void reportAllVaxed(PrintStream outStream) {
        String pub;
        Collections.sort(fvlist);
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
        return vblist.stream()
                .map(VaccineBatch::getBalance)
                .reduce(0, Integer::sum);
    }

    public void applyVaxes() {
        if (aplist.size() > 0) {
            Collections.sort(aplist);
            Collections.sort(vblist);
            for (VaccineBatch vb : vblist) {
                int apos = aplist.size() - 1;
                while ((apos >= 0) && (vb.getBalance() > 0)) {
                    ApprovedPerson ap = aplist.get(apos);
//                    Person p = (Person) plist.get(findPerson(plist, ap.getId()));
                    if (!(vb.contraImpact(ap.getComorbids()))) {
                        vb.reduceBalance();

                        FullyVaccinatedPerson fv = new FullyVaccinatedPerson(
                                ap.getAge(),
                                ap.getSimpleName(),
                                ap.getPublish(),
                                ap.getId(),
                                vb.getName()
                        );

                        fvlist.add(fv);
                        aplist.remove(apos);

                    }
                    apos--;
                }

            }
        }
    }

    public void applyRemaining() {
        Collections.sort(vblist);
        Collections.sort(plist);
        for (VaccineBatch vb : vblist) {
            int pos = plist.size() - 1;
            while ((pos >= 0) && (vb.getBalance() > 0)) {
                Person p = plist.get(pos);
                if (findPerson(fvlist, p.getId()) == -1) {

                    FullyVaccinatedPerson fv = new FullyVaccinatedPerson(
                            p.getAge(), p.getName(), p.getPublish(), p.getId(), vb.getName()
                    );
                    fvlist.add(fv);
                    vb.reduceBalance();
                    plist.remove(pos);

                    pos--;
                }
            }
        }
    }

    public void publishData() {
        int numPersons = plist.size();
        //int initApproved 
        int approvedRemaining = aplist.size();
//        ArrayList<VaccineBatch> vblist, ArrayList<FullyVaccinatedPerson> fvlist
        String header = "<html>";
        header += "<head><meta http-equiv='refresh' content='30'></head>";
        header += "<hr>";
        header += "<p><font face =Arial size=2>Total Applicants:" + numPersons + "</font></p>";
        header += "<p><font face =Arial size=2>Initially Approved:" + initApproved + "</font></p>";
        header += "<p><font face =Arial size=2>Total Administered:" + fvlist.size() + "</font></p>";
        header += "<p><font face =Arial size=2>Postponed:" + approvedRemaining + "</font></p><hr>";

        StringBuilder vcdata = new StringBuilder("<table border = 0><tr><td>Name</td><td>Size</td><td>Balance</td></tr>");
        Collections.sort(vblist);
        for (VaccineBatch vb : vblist)
            vcdata.append("<tr><td>").append(vb.getName()).append("</td><td>").append(vb.getSize()).append("</td><td>").append(vb.getBalance()).append("</td></tr>");
        vcdata.append("</table>");

        StringBuilder personalPublish = new StringBuilder();
        String pub;
        Collections.sort(fvlist);
        for (FullyVaccinatedPerson fv : fvlist) {
            pub = fv.publish();
            if (pub.length() > 0)
                personalPublish.append(fv.publish());
        }
        String footer = "<hr></html>";

        String pubFile = header + vcdata + personalPublish + footer;
        PrintWriter outwriter;
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

