package org.biz.employees.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the department database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Department.findAll", query = "select d from Department d")
})
@XmlRootElement(name="rows")
public class Department implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int departmentId;

	//@Lob
	private String address;

	private int budget;

	//@Lob
	private String name;

	//bi-directional one-to-many association to Employee
	//@OneToMany(mappedBy="department")
	//private List<Employee> employees;

	public Department() {
	}

	public int getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBudget() {
		return this.budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
/*
	public List<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
*/	
	public Department copy() {
		Department dep = new Department();
		dep.setDepartmentId(getDepartmentId());
		dep.setName(getName());
		dep.setAddress(getAddress());
		dep.setBudget(getBudget());
		return dep;
	}

	/*
	@Override public boolean equals(Object o) {
	    if (!(o instanceof Department))
	      return false;
	    return department.getSsoId().equals((User)o.getSsoId());
	}
	*/
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Department)) {
            return false;
        }
        Department other = (Department) object;
        if ((this.departmentId == 0 && other.departmentId != 0) || (this.departmentId != 0 && !(this.departmentId == other.departmentId))) {
            return false;
        }
        return true;
    }


}