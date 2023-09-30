import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

class Employees{
    private String name;
    private String position;

    public Employees(String name, String position){
        this.name=name;
        this.position=position;
    }
    public String toString(){//overriding the toString() method  
     return name + " " + position + "   ";  
    }  
    
}

public class Assignment {
    public static void main(String[] args) {
        String csvFile = "C:\\Users\\lenovo\\Downloads\\Assignment_Timecard - Sheet1.csv"; // Replace with the actual path to your CSV file
        String line;
        String csvSplitBy = ",";

        List<Employees> list1 = new ArrayList<>();
        List<Employees> list2 = new ArrayList<>();
        List<Employees> list3 = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip the header row
            String prevName = "";
            Date prevOutDateTime = null;
            int consecutiveDays = 0;

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);

                String name = data[7];
                String position = data[1];
                String inTime = data[2];
                String outTime = data[3];
                
                // Handle empty or invalid date values
                Date inDateTime, outDateTime;
                try {
                    inDateTime = dateFormat.parse(inTime);
                    outDateTime = dateFormat.parse(outTime);
                } catch (ParseException e) {
                    continue; // Skip rows with invalid date values
                }

                // Calculate the duration of the shift in hours
                long durationMillis = outDateTime.getTime() - inDateTime.getTime();
                double durationHours = durationMillis / (60.0 * 60.0 * 1000);

                // Check if the employee worked for 7 consecutive days
                if (name.equals(prevName)) {
                    long timeBetweenShifts = inDateTime.getTime() - prevOutDateTime.getTime();
                    double hoursBetweenShifts = timeBetweenShifts / (60.0 * 60.0 * 1000);

                    if (hoursBetweenShifts >= 1 && hoursBetweenShifts <= 10) {
                        list1.add(new Employees(name,position));
                        // System.out.println(name + " (" + position + ") : Less than 10 hours between shifts & greater than 1 hour");
                    } else if (hoursBetweenShifts > 10) {
                        // consecutiveDays = 0;
                    }

                    consecutiveDays++;
                    if (consecutiveDays == 7) {
                        // System.out.println(name + " (" + position + ") : Worked for 7 consecutive days");
                        list2.add(new Employees(name,position));
                    }
                } else {
                    consecutiveDays = 1;
                }

                // Check if the employee worked for more than 14 hours in a single shift
                if (durationHours > 14) {
                    // System.out.println(name + " (" + position + ") : Worked for more than 14 hours in a single shift");
                    list3.add(new Employees(name,position));
                }

                prevName = name;
                prevOutDateTime = outDateTime;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Employees who has worked for 7  consecutive days are : ");
        System.out.println(list2);
        System.out.println("Employees who have less than 10 hours of time between shifts but greater than 1 hour are : ");
        System.out.println(list1);
        System.out.println("Employees who has worked for more than 14 hours in a single shift are : ");
        System.out.println(list3);
    }
}

