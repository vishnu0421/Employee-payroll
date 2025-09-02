package payroll;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.io.FileNotFoundException;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class EmployeeDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/payroll_db";
	private static final String USER = "root";
	private static final String PASS = "Vishnu@123";
	private static final String Connection = null;

	private Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL, USER, PASS);

	}

	public void addemployee(employee emp) {
		try (Connection con = getConnection()) {
			String sql = "INSERT INTO employees (name,department,salary) VALUES (?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, emp.getName());
			ps.setString(2, emp.getDepartment());
			ps.setDouble(3, emp.getSalary());
			ps.executeUpdate();
			System.out.println("EMPLOYEE ADDED SUCCESSFULLY");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<employee> getAllemployees() {
		List<employee> list = new ArrayList<>();
		try (Connection con = getConnection()) {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT*FROM employees");
			while (rs.next()) {
				employee emp = new employee(rs.getInt("emp_id"), rs.getString("name"), rs.getString("department"),
						rs.getDouble("salary"));

				list.add(emp);

			}
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public void updateSalary(int id, double newSalary) {
		try (Connection con = getConnection()) {
			String sql = "Update employees SET Salary=? WHERE emp_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDouble(1, newSalary);
			ps.setInt(2, id);
			int rows = ps.executeUpdate();
			if (rows > 0)
				System.out.println("salary Updated");
			else
				System.out.println("Employee not found!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Delete Employee

	public void deleteEmployee(int id) {
		try (Connection con = getConnection()) {
			String sql = "DELETE FROM  employees WHERE emp_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			int row = ps.executeUpdate();
			if (row > 0)
				System.out.println("EMPLOYEE DELETED SUCCESSFULLY");
			else
				System.out.println("Employee NOt Found!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Search employee by ID
	public employee getEmployeesById(int empId) {
		String sql = "SELECT * FROM employees WHERE emp_id=?";
		try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, empId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new employee(rs.getInt("emp_id"), rs.getString("name"), rs.getString("department"),
						rs.getDouble("salary"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // not found
	}

	// Search employees by Name
	public List<employee> getEmployeesByName(String name) {
		List<employee> list = new ArrayList<>();
		String sql = "SELECT * FROM employees WHERE name LIKE ?";
		try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new employee(rs.getInt("emp_id"), rs.getString("name"), rs.getString("department"),
						rs.getDouble("salary")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Generate payslip for an employee
	public void generatePayslip(int empId) {
		employee e = getEmployeesById(empId);
		if (e == null) {
			System.out.println("Employee not found with ID: " + empId);
			return;
		}

		double Gross = e.getSalary();
		double hra = Gross * 0.20; // 20% HRA
		double pf = Gross * 0.12; // 12% PF
		double tax = Gross * 0.10; // 10% Tax
		double netSalary = Gross + hra - (pf + tax);

		System.out.println("\n========== PAYSLIP ==========");
		System.out.printf("Employee ID   : %d%n", e.getEmpId());
		System.out.printf("Name          : %s%n", e.getName());
		System.out.printf("Department    : %s%n", e.getDepartment());
		System.out.println("-----------------------------");
		System.out.printf("Gross Salary  : %.2f%n", Gross);
		System.out.printf("HRA (20%%)     : %.2f%n", hra);
		System.out.printf("PF (12%%)      : %.2f%n", pf);
		System.out.printf("Tax (10%%)     : %.2f%n", tax);
		System.out.println("-----------------------------");
		System.out.printf("Net Salary    : %.2f%n", netSalary);
		System.out.println("=============================\n");
	}

	public void exportPayslipToFile(int empId) {
		employee e = getEmployeesById(empId);
		if (e == null) {
			System.out.println("Employee not found with ID: " + empId);
			return;
		}

		double Gross = e.getSalary();
		double hra = Gross * 0.20;
		double pf = Gross * 0.12;
		double tax = Gross * 0.10;
		double netSalary = Gross + hra - (pf + tax);

		String fileName = "Payslip_Emp" + empId + ".txt";

		try (FileWriter Writer = new FileWriter(fileName)) {
			Writer.write("========== PAYSLIP ==========\n");
			Writer.write("Employee ID   : " + e.getEmpId() + "\n");
			Writer.write("Name          : " + e.getName() + "\n");
			Writer.write("Department    : " + e.getDepartment() + "\n");
			Writer.write("-----------------------------\n");
			Writer.write(String.format("Gross Salary  : %.2f%n", Gross));
			Writer.write(String.format("HRA (20%%)     : %.2f%n", hra));
			Writer.write(String.format("PF (12%%)      : %.2f%n", pf));
			Writer.write(String.format("Tax (10%%)     : %.2f%n", tax));
			Writer.write("-----------------------------\n");
			Writer.write(String.format("Net Salary    : %.2f%n", netSalary));
			Writer.write("=============================\n");

			System.out.println("Payslip exported successfully to " + fileName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	

	public void exportPayslipToPDF(int empId) {
	    employee e = getEmployeesById(empId);
	    if (e == null) {
	        System.out.println("Employee not found with ID: " + empId);
	        return;
	    }

	    try {
	        String fileName = "Payslip_Emp" + empId + ".pdf";
	        PdfWriter writer = new PdfWriter(fileName);
	        PdfDocument pdf = new PdfDocument(writer);
	        Document document = new Document(pdf);

	        // Company Header
	        Paragraph header = new Paragraph("KINGPIN LOGISTICS PVT. LTD.")
	                .setFontSize(18)
	                .setBold()
	                .setTextAlignment(TextAlignment.CENTER);
	        document.add(header);

	        Paragraph payslipTitle = new Paragraph("Employee Payslip")
	                .setFontSize(14)
	                .setBold()
	                .setMarginBottom(20)
	                .setTextAlignment(TextAlignment.CENTER);
	        document.add(payslipTitle);

	        // Employee Details Section
	        Table empTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
	                .useAllAvailableWidth();

	        empTable.addCell(new Cell().add(new Paragraph("Employee ID").setBold()));
	        empTable.addCell(String.valueOf(e.getEmpId()));

	        empTable.addCell(new Cell().add(new Paragraph("Name").setBold()));
	        empTable.addCell(e.getName());

	        empTable.addCell(new Cell().add(new Paragraph("Department").setBold()));
	        empTable.addCell(e.getDepartment());

	        document.add(empTable);

	        document.add(new Paragraph("\nEarnings & Deductions")
	                .setFontSize(13)
	                .setBold()
	                .setUnderline()
	                .setMarginTop(15));

	        // Salary Calculations
	        double Gross = e.getSalary();
	        double hra = Gross * 0.20;
	        double pf = Gross * 0.12;
	        double tax = Gross * 0.10;
	        double netSalary = Gross + hra - (pf + tax);

	        // Table with color headers
	        Color headerColor = new DeviceRgb(200, 230, 255);
	        Table salaryTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
	                .useAllAvailableWidth();

	        salaryTable.addHeaderCell(new Cell().add(new Paragraph("Description").setBackgroundColor(headerColor).setBold()));
	        salaryTable.addHeaderCell(new Cell().add(new Paragraph("Amount").setBackgroundColor(headerColor).setBold()));

	        salaryTable.addCell(new Cell().add(new Paragraph("Gross Salary")));
	        salaryTable.addCell(String.format("%.2f", Gross));

	        salaryTable.addCell(new Cell().add(new Paragraph("HRA (20%)")));
	        salaryTable.addCell(String.format("%.2f", hra));

	        salaryTable.addCell(new Cell().add(new Paragraph("PF (12%)")));
	        salaryTable.addCell(String.format("%.2f", pf));

	        salaryTable.addCell(new Cell().add(new Paragraph("Tax (10%)")));
	        salaryTable.addCell(String.format("%.2f", tax));

	        // Net salary highlighted
	        salaryTable.addCell(new Cell().add(new Paragraph("Net Salary").setBold()));
	        salaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", netSalary)).setBold()));

	        document.add(salaryTable);

	        // Footer Note
	        Paragraph footer = new Paragraph("\nThis is a computer-generated payslip and does not require signature.")
	                .setFontSize(10)
	                .setTextAlignment(TextAlignment.CENTER)
	                .setItalic()
	                .setMarginTop(20);
	        document.add(footer);

	        document.close();
	        System.out.println("Beautiful Payslip with Logo exported: " + fileName);

	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}


}