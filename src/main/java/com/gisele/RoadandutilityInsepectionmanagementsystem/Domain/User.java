package com.gisele.RoadandutilityInsepectionmanagementsystem.Domain;



import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Validation.FieldMatch;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@FieldMatch.List({
		@FieldMatch(first = "password", second = "matchingPassword", message = "The password"
				+"fields must match")
})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "first_name")
//	@NotNull(message="is Required")
	@Size(min=1, message="is required")
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
	private String firstName;
//	@NotNull(message="is Required")
	@Size(min=1, message="is required")
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid name")
	@Column(name = "last_name")
	private String lastName;
//	@NotNull(message="is Required")
	@Size(min=1, message="is required")
	@Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Invalid username")
	@Column(name = "username")
	private String userName;

	@Column(name = "email")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email address")
//	@NotNull(message="is Required")
	private String email;
//	@NotNull(message="is Required")
	@Size(min=10, message="At least 10 digits required")
	@Pattern(regexp = "^07[2|3|8|9]\\d{7}$", message = "start with 07, [2|3|8|9], followed by 7 digits")
	private String telephone;
//	@NotNull(message="is Required")
	@Transient
	private String formRole;
//	@NotNull(message="Password is Required")
	@Size(min=5, message="At least 5 characters required")
	@Column(name = "password")
	private String password;

	@Transient
	@NotBlank(message = "Confirm Password is required")
	@Size(min = 5, message = "Confirm Password must be at least 5 characters")
	private String matchingPassword;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
	joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="user",
			cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
					CascadeType.REFRESH})
	private List<Project> projects;


	public User() {
	}

	// add convenience methods for bi-directional relationship

	public void add(Project tempProject)
	{
		if(projects == null)
		{
			projects = new ArrayList<>();
		}

		projects.add(tempProject);
		tempProject.setUser(this);
	}

	public User(String firstName, String lastName, String userName, String email, String telephone, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.telephone = telephone;
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFormRole() {
		return formRole;
	}

	public void setFormRole(String formRole) {
		this.formRole = formRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}


	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", userName='" + userName + '\'' +
				", email='" + email + '\'' +
				", telephone='" + telephone + '\'' +
				", formRole='" + formRole + '\'' +
				", password='" + password + '\'' +
				//", roles=" + roles +
				'}';
	}
}
