package payroll;

import java.util.Scanner;
import java.util.List;

public class PayrollApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		EmployeeDAO dao = new EmployeeDAO();

		while (true) {
			System.out.println("\n== Employee payroll System ==");
			System.out.println("1. Add Employee");
			System.out.println("2.View Employees");
			System.out.println("3.Update salary");
			System.out.println("4. Delete Employee");
			System.out.println("5. Search Employee");
			System.out.println("6. Generate Payslip");
			System.out.println("7. Export Payslip to File");
			System.out.println("8. Export payslip to PDF");
			System.out.println("9. Exit");
			System.out.println("chosse option: ");
			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				System.out.println("Enter Name: ");
				String name = sc.next();
				System.out.println("Enter Department: ");
				String department = sc.next();
				System.out.println("Enter Salary: ");
				double salary = sc.nextDouble();

				employee emp = new employee(name, department, salary);
				dao.addemployee(emp);
				break;
			case 2:
				List<employee> employees = dao.getAllemployees();
				if (employees.isEmpty()) {
					System.out.println("No Employee found. ");
				} else {
					System.out.println("+------+----------------+----------------+----------+ ");
					System.out.printf("| %-4s | %-14s | %-14s | %-8s |%n", "ID", "Name", "Department", "Salary");
					System.out.println("+------+----------------+----------------+----------+ ");
					for (employee e : employees) {
						System.out.printf("| %-4s | %-14s | %-14s | %-8.2f |%n", e.getEmpId(), e.getName(),
								e.getDepartment(), e.getSalary());
					}
					System.out.println("+------+----------------+----------------+----------+ ");
				}
				break;

			case 3:
				System.out.print("ENTER EMPLOYEE ID to update Salary: ");
				int empIdUp = sc.nextInt();
				System.out.print("ENTER NEW SALARY: ");
				double newsalary = sc.nextDouble();
				dao.updateSalary(empIdUp, newsalary);
				break;

			case 4:
				System.out.print("ENTER EMPLOYEE ID TO DELETE: ");
				int empIdDel = sc.nextInt();
				dao.deleteEmployee(empIdDel);
				break;


			case 5:
				System.out.println("Search By:");
				System.out.println("1. ID "); 
				System.out.println("2. Name ");
				System.out.println("Chose option: ");
				int searchChoice = sc.nextInt();
				sc.nextLine();

				if (searchChoice == 1) {
					System.out.print("Enter Employee ID: ");
					int searchId = sc.nextInt();
					employee e = dao.getEmployeesById(searchId);
					if (e != null) {
						System.out.println("Employee Found: ");
						System.out.printf("ID: %d, Name: %s, Dept: %s, Salary: %.2f%n", e.getEmpId(), e.getName(),
								e.getDepartment(), e.getSalary());
					} else {
						System.out.println("No employee found with ID " + searchId);
					}
				} else if (searchChoice == 2) {
					System.out.print("Enter Name: ");
					String searchName = sc.nextLine();
					List<employee> found = dao.getEmployeesByName(searchName);
					if (found.isEmpty()) {
						System.out.println("No employees found with name " + searchName);
					} else {
						System.out.println("Employees Found:");
						for (employee e : found) {
							System.out.printf("ID: %d, Name: %s, Dept: %s, Salary: %.2f%n", 
									e.getEmpId(), e.getName(),e.getDepartment(), e.getSalary());
										}
					}
				} else {
					System.out.println("Invalid search option.");
				}
				break;
			
			case 6:
				System.out.print("Enter Employee ID for payslip: ");
				int empId = sc.nextInt();
				dao.generatePayslip(empId);
				break;

			case 7:
				System.out.print("Enter Employee ID to export payslip: ");
				int empIdExport = sc.nextInt();
				dao.exportPayslipToFile(empIdExport);
				break;
			
			case 8:
				System.out.println("Enter Employee ID to Export PDF Payslip");
				int empIdPDF = sc.nextInt();
				dao.exportPayslipToPDF(empIdPDF);
				break;

			case 9:
				sc.close();
				System.out.println("Existing.....GoodBye! ");
				return;	
				
			default:
				System.out.println("Invalid choice!");

			}

		}

	}

}
