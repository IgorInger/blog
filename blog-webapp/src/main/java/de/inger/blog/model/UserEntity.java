package de.inger.blog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * User.
 * 
 * @author Igor Inger
 * 
 */
@Entity(name = "User")
@Table(name = "b_user")
public class UserEntity implements Serializable, CreationHistory {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 8013195140968006591L;

	/**
	 * Id.
	 */
	@Id
	@GeneratedValue
	private long id;

	/**
	 * Login.
	 */
	private String name;

	/**
	 * Password.
	 */
	private String password;

	/**
	 * EMail.
	 */
	@Column(unique = true)
	private String email;

	/**
	 * Public email.
	 */
	private String publicEmail;

	/**
	 * Openid.
	 */
	@Column(unique = true)
	private String openid;

	/**
	 * Active.
	 */
	private boolean active = false;

	/**
	 * Creation date.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();

	/**
	 * Roles.
	 */
	@ManyToMany(targetEntity = RoleEntity.class)
	private List<RoleEntity> roles = new ArrayList<RoleEntity>();

	/**
	 * Returns the id value.
	 * 
	 * @return the id value
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id value.
	 * 
	 * @param id
	 *            the id value to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the login value.
	 * 
	 * @return the login value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the login value.
	 * 
	 * @param login
	 *            the login value to set
	 */
	public void setName(String login) {
		this.name = login;
	}

	/**
	 * Returns the password value.
	 * 
	 * @return the password value
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password value.
	 * 
	 * @param password
	 *            the password value to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets email.
	 * 
	 * @return email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 * 
	 * @param email
	 *            email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets roles.
	 * 
	 * @return roles.
	 */
	public List<RoleEntity> getRoles() {
		return roles;
	}

	/**
	 * Sets roles.
	 * 
	 * @param roles
	 *            roles.
	 */
	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	/**
	 * Adds role.
	 * 
	 * @param role
	 *            Role.
	 */
	public void addRole(RoleEntity role) {
		roles.add(role);
	}

	/**
	 * Removes role.
	 * 
	 * @param role
	 *            role.
	 */
	public void removeRole(RoleEntity role) {
		roles.add(role);
	}

	/**
	 * Is active.
	 * 
	 * @return actice status.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets active status.
	 * 
	 * @param active
	 *            active status.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(Date date) {
		this.created = date;
	}

	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}

	/**
	 * @param openid
	 *            the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * @return the publicEmail
	 */
	public String getPublicEmail() {
		return publicEmail;
	}

	/**
	 * @param publicEmail
	 *            the publicEmail to set
	 */
	public void setPublicEmail(String publicEmail) {
		this.publicEmail = publicEmail;
	}

}
