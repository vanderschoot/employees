package org.biz.employeesJH.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Date;


/**
 * The persistent class for the employee database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "employee.list", query = "select e from Employee e")
})
@XmlRootElement(name="rows")
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int employeeId;

	//@Temporal(TemporalType.DATE)
	private Date birthDate;

	@Lob
	private String firstName;

	@Lob
	private String lastName;

	//bi-directional many-to-one association to Department
	@ManyToOne
	@JoinColumn(name="DepartmentId")
	private Department department;

	public Employee() {
	}
	
	public Employee copy() {
		Employee emp = new Employee();
		emp.setEmployeeId(getEmployeeId());
		emp.setFirstName(getFirstName());
		emp.setLastName(getLastName());
		emp.setBirthDate(getBirthDate());
		emp.setDepartment(getDepartment());
		return emp;
	}
	@XmlElement
	public int getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	@XmlElement
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@XmlElement
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !Employee.class.isAssignableFrom(o.getClass())) {
            return false;
        }

        Employee emp = (Employee) o;

        return employeeId == emp.employeeId;
    }

    @Override
    public int hashCode() {
        return (int) (employeeId ^ (employeeId >>> 32));
    }
}