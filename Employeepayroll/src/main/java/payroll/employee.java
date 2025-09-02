package payroll;

public class employee {

	private int empId;
	private String name;
	private String department;
	private double salary;
	
	//constructor class for new employee entry
	public employee(String name, String department, double salary) {
		super();
		this.name = name;
		this.department = department;
		this.salary = salary;
	}

	
	//constructor with id for view existing id
	public employee(int empId, String name, String departement, double salary) {
		this.empId = empId;
		this.name = name;
		this.department = departement;
		this.salary = salary;
	}

	
		
		public int getEmpId() {
		return empId;
	}


	public void setEmpId(int empId) {
		this.empId = empId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public double getSalary() {
		return salary;
	}


	public void setSalary(double salary) {
		this.salary = salary;
	}


		public String toString() {
			return "ID:  "+ empId + " | " + "Name: " + name +" | " +" Department: "+ department + " | " +"Salary: "+ salary; 
		}
}
