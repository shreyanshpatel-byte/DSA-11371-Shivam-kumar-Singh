import java.util.*;

class Donor {
    int id, age;
    String name, bloodGroup, phone;

    Donor(int id, String name, int age,
          String bloodGroup, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
    }

    void display() {
        System.out.println(id + " | " + name + " | " +
                age + " | " + bloodGroup + " | " + phone);
    }
}

class BloodRequest {
    String patientName, bloodGroup;
    int units;

    BloodRequest(String name, String group, int units) {
        this.patientName = name;
        this.bloodGroup = group;
        this.units = units;
    }
}

public class BloodBankManager {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Donor> donors = new ArrayList<>();
    static Queue<BloodRequest> requests = new LinkedList<>();
    static HashMap<String, Integer> stock = new HashMap<>();

    static String[] groups = {
        "A+", "A-", "B+", "B-",
        "AB+", "AB-", "O+", "O-"
    };

    static void initializeStock() {
        for (String g : groups) {
            stock.put(g, 0);
        }
    }

    static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    static boolean validGroup(String group) {
        return stock.containsKey(group.toUpperCase());
    }

    static void addDonor() {
        int id = readInt("Enter Donor ID: ");

        for (Donor d : donors) {
            if (d.id == id) {
                System.out.println("Donor ID already exists!");
                return;
            }
        }

        System.out.print("Enter Donor Name: ");
        String name = sc.nextLine();

        int age = readInt("Enter Age: ");

        if (age < 18 || age > 65) {
            System.out.println("Invalid age! Donor age must be 18-65.");
            return;
        }

        System.out.print("Enter Blood Group: ");
        String group = sc.nextLine().trim().toUpperCase();

        if (!validGroup(group)) {
            System.out.println("Invalid Blood Group!");
            return;
        }

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        donors.add(new Donor(id, name, age, group, phone));
        System.out.println("Donor Added Successfully!");
    }

    static void displayDonors() {
        System.out.println("\n--- DONOR LIST ---");

        if (donors.isEmpty()) {
            System.out.println("No Donors Available!");
            return;
        }

        for (Donor d : donors) {
            d.display();
        }
    }

    static void searchDonor() {
        System.out.print("Enter Blood Group: ");
        String group = sc.nextLine().trim().toUpperCase();

        if (!validGroup(group)) {
            System.out.println("Invalid Blood Group!");
            return;
        }

        boolean found = false;

        for (Donor d : donors) {
            if (d.bloodGroup.equals(group)) {
                d.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No Donor Found!");
        }
    }

    static void sortDonors() {
        donors.sort(Comparator.comparing(
            d -> d.name.toLowerCase()
        ));

        System.out.println("Donors Sorted By Name!");
        displayDonors();
    }

    static void addBloodStock() {
        System.out.print("Enter Blood Group: ");
        String group = sc.nextLine().trim().toUpperCase();

        if (!validGroup(group)) {
            System.out.println("Invalid Blood Group!");
            return;
        }

        int units = readInt("Enter Units: ");

        if (units <= 0) {
            System.out.println("Invalid Units!");
            return;
        }

        stock.put(group, stock.get(group) + units);
        System.out.println("Blood Stock Updated!");
    }

    static void requestBlood() {
        System.out.print("Enter Patient Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Blood Group: ");
        String group = sc.nextLine().trim().toUpperCase();

        if (!validGroup(group)) {
            System.out.println("Invalid Blood Group!");
            return;
        }

        int units = readInt("Enter Required Units: ");

        if (units <= 0) {
            System.out.println("Invalid Units!");
            return;
        }

        requests.add(new BloodRequest(name, group, units));
        System.out.println("Blood Request Added To Queue!");
    }

    static void processRequest() {
        if (requests.isEmpty()) {
            System.out.println("No Pending Requests!");
            return;
        }

        BloodRequest r = requests.peek();

        if (stock.get(r.bloodGroup) >= r.units) {
            stock.put(r.bloodGroup,
                stock.get(r.bloodGroup) - r.units);

            requests.poll();

            System.out.println("Blood Issued Successfully!");
            System.out.println("Patient: " + r.patientName);
        } else {
            System.out.println("Insufficient Stock!");
            System.out.println("Request Remains In Queue.");
        }
    }

    static void displayStock() {
        System.out.println("\n--- AVAILABLE BLOOD STOCK ---");

        for (String group : groups) {
            System.out.println(group + " : " +
                    stock.get(group) + " Units");
        }
    }

    public static void main(String[] args) {
        initializeStock();

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("       BLOOD BANK MANAGER");
            System.out.println("================================");
            System.out.println("1. Add New Donor");
            System.out.println("2. Display All Donors");
            System.out.println("3. Search Donor");
            System.out.println("4. Sort Donors");
            System.out.println("5. Add Blood Stock");
            System.out.println("6. Request Blood");
            System.out.println("7. Process Blood Request");
            System.out.println("8. Display Blood Stock");
            System.out.println("9. Exit");

            choice = readInt("Enter Your Choice: ");

            switch (choice) {
                case 1: addDonor(); break;
                case 2: displayDonors(); break;
                case 3: searchDonor(); break;
                case 4: sortDonors(); break;
                case 5: addBloodStock(); break;
                case 6: requestBlood(); break;
                case 7: processRequest(); break;
                case 8: displayStock(); break;
                case 9: System.out.println("Thank You!"); break;
                default: System.out.println("Invalid Choice!");
            }

        } while (choice != 9);

        sc.close();
    }
}