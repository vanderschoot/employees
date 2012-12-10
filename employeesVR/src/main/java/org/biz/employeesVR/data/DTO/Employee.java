package org.biz.employeesVR.data.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee implements Serializable {
	@XmlTransient
	private static final long serialVersionUID = 1L;

    @XmlElement(required = true)
	private int employeeId;

    @XmlElement(required = true)
	private Date birthDate;

    @XmlElement(required = true)
	private String firstName;

    @XmlElement(required = true)
	private String lastName;

    @XmlElement(required = true)
	private Department department;

	public Employee() {
	}
	
	public int getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

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