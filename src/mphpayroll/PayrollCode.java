package mphpayroll;

import java.io.*;
import java.util.*;

public class PayrollCode {

    public static void main(String[] args) {
        String employeesFile = "employees.txt";
        String attendanceFile = "attendance.txt";

        // Store Employee Hourly Rates
        Map<Integer, Double> hourlyRates = new HashMap<>();
        
     // Store Total Net Salary per Employee
        Map<Integer, Double> totalSalary = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(employeesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int empId = Integer.parseInt(parts[0]);
                double hourlyRate = Double.parseDouble(parts[1]);
                hourlyRates.put(empId, hourlyRate);
            }
        } catch (IOException e) {
            System.out.println("Error reading employees file.");
            e.printStackTrace();
        }

        // Process Attendance & Calculate Payroll with Deductions
        System.out.println("Employee # | Hours Worked | Gross Salary | Deductions | Net Salary");
        try (BufferedReader br = new BufferedReader(new FileReader(attendanceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int empId = Integer.parseInt(parts[0]);
                String loginTime = parts[1];
                String logoutTime = parts[2];

                double hoursWorked = calculateHoursWorked(loginTime, logoutTime);
                double grossSalary = hoursWorked * hourlyRates.getOrDefault(empId, 0.0);

                // Calculate Deductions
                double deductions = calculateDeductions(grossSalary);
                double netSalary = grossSalary - deductions;
                
             // Accumulate total salary per employee
                totalSalary.put(empId, totalSalary.getOrDefault(empId, 0.0) + netSalary);

                System.out.printf("%d | %.2f hours | PHP %.2f | PHP %.2f | PHP %.2f%n", 
                                  empId, hoursWorked, grossSalary, deductions, netSalary);
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
            e.printStackTrace();
            
            System.out.println("\nTotal Salary Per Employee:");
            for (Map.Entry<Integer, Double> entry : totalSalary.entrySet()) {
                System.out.printf("Employee %d | Total Net Salary: PHP %.2f%n", entry.getKey(), entry.getValue());
            }
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

    // **Correctly Defined Deduction Calculation Method**
    public static double calculateDeductions(double grossSalary) {
        double tax = grossSalary * 0.10;       // 10% Tax
        double sss = 500.0;                    // Fixed SSS deduction
        double philhealth = grossSalary * 0.02; // 2% PhilHealth
        double pagibig = 100.0;                 // Fixed PAG-IBIG contribution

        return tax + sss + philhealth + pagibig;
    }
}
