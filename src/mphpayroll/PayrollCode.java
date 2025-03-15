package mphpayroll;

import java.io.*;
import java.util.*;

public class PayrollCode {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	System.out.print("Enter the full path for employees.txt: ");
    	String employeesFile = scanner.nextLine();
    	System.out.print("Enter the full path for attendance.txt: ");
    	String attendanceFile = scanner.nextLine();

        // Store Employee Hourly Rates
        Map<Integer, Double> hourlyRates = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(employeesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int empId = Integer.parseInt(parts[0].trim());
                double hourlyRate = Double.parseDouble(parts[1].trim());
                hourlyRates.put(empId, hourlyRate);
            }
        } catch (IOException e) {
            System.out.println("Error reading employees file.");
            e.printStackTrace();
        }

        // Process Attendance & Calculate Payroll
        System.out.println("Employee # | Hours Worked | Salary");
        try (BufferedReader br = new BufferedReader(new FileReader(attendanceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int empId = Integer.parseInt(parts[0].trim());
                String loginTime = parts[1].trim();
                String logoutTime = parts[2].trim();

                double hoursWorked = calculateHoursWorked(loginTime, logoutTime);
                double salary = hoursWorked * hourlyRates.getOrDefault(empId, 0.0);

                System.out.printf("%d | %.2f hours | PHP %.2f%n", empId, hoursWorked, salary);
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
            e.printStackTrace();
        }
    }

    // Convert time strings to total hours worked
    public static double calculateHoursWorked(String login, String logout) {
        String[] inParts = login.split(":");
        String[] outParts = logout.split(":");

        int inHour = Integer.parseInt(inParts[0]);
        int inMin = Integer.parseInt(inParts[1]);
        int outHour = Integer.parseInt(outParts[0]);
        int outMin = Integer.parseInt(outParts[1]);

        double inTime = inHour + (inMin / 60.0);
        double outTime = outHour + (outMin / 60.0);

        return outTime - inTime;
    }
}

