package com.otc.backend.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//@Document(indexName ="user1")
@Entity
@Table(name = "users")
public class Users implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@Column(unique = true)
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String telephone;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	/* Security related */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role_junction", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> authorities;

	// mention
	@OneToOne(fetch = FetchType.EAGER)
	@JoinTable(name = "user_address_junction", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "address_id") })

	private Address address;

	private Boolean enabled;

	@Column(nullable = true)
	// hide user verification code in the json response
	@JsonIgnore
	private Long verification;

	/*
	 * // new
	 * public void assignAuthoritesToUser(Users user) {
	 * // user.getAuthorities().add(null);//this may causse errror - we can remove
	 * user
	 * // but not authorities
	 * this.getUsers().add(user);
	 * }
	 * 
	 * public void removeAuthoritesToUser(Users user) {
	 * // user.getAuthorities().remove(user);// this may causse errror
	 * this.getUsers().remove(user);
	 * }
	 * 
	 * public void removeAllUsersFromRole() {
	 * if (this.getUsers() != null) {
	 * List<Users> rolesUsers = this.getUsers().stream().toList();
	 * rolesUsers.forEach(this::removeAuthoritesToUser);
	 * }
	 * }
	 * 
	 */

	public Users() {
		// super();
		this.authorities = new HashSet<>();
		// when we first create account user should not be able to use it
		// till user is verified
		this.enabled = true;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Long getVerification() {
		return verification;
	}

	public void setVerification(Long verification) {
		this.verification = verification;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Users(String password, String firstName, String lastName, String emailAddress, String telephone,
		 Set<Role> authorities ) {
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.telephone = telephone;
		this.authorities = authorities;
	}

	public Users(String firstName, String lastName, String emailAddress, String password, String telephone
				  ) {
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.telephone = telephone;

	}

	public Users(String username, String password, String emailAddress, String telephone, Set<Role> authorities) {
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
		this.telephone = telephone;
		this.authorities = authorities;
	}

	public Users(String username, String password, String firstName, String lastName, String emailAddress,
				 String telephone) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.telephone = telephone;
	}

	public Users(Long userId, String username, String password, String firstName, String lastName, String emailAddress,
			String telephone, Set<Role> authorities, Address address, Boolean enabled, Long verification) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.telephone = telephone;
		this.authorities = authorities;
		this.address = address;
		this.enabled = enabled;
		this.verification = verification;
	}

	public Users(String username) {
		this.username = username;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setId(Long userId) {
		this.userId = userId;
	}

	public void setAuthorities(Set<Role> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.authorities;
	}

	@Override
	public String getPassword() {

		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * If you want account locking capabilities create variables and ways to set
	 * them for the methods below
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", username=" + username + ", password=" + password + ", firstName="
				+ firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress + ", telephone=" + telephone
				+ ", authorities=" + authorities + ", address=" + address + ", enabled=" + enabled + ", verification="
				+ verification + "]";
	}

}
