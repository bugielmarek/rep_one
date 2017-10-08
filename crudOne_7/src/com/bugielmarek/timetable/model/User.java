package com.bugielmarek.timetable.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 6, max = 6)
	@Pattern(regexp = "^\\w{6}$")
	@Column(nullable = false)
	private String username;

	@Pattern(regexp = "^\\S+$")
	@Size(min = 6, max = 15)
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String authority;

	@Column(nullable = false)
	private boolean enabled;
	
	@Transient
	@Pattern(regexp = "^\\S+$")
	@Size(min = 6, max = 15)
	private String formPassword;

	public User() {
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", authority=" + authority + ", enabled="
				+ enabled + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFormPassword() {
		return formPassword;
	}

	public void setFormPassword(String formPassword) {
		this.formPassword = formPassword;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		if (enabled != other.enabled)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	private User(UserBuilder userBuilder){
		this.id = userBuilder.id;
		this.username = userBuilder.username;
		this.authority = userBuilder.authority;
		this.enabled = userBuilder.enabled;
		this.password = userBuilder.password;
		this.formPassword = userBuilder.formPassword;
	}
	
	public static class UserBuilder{
		private Long id;
		private String username;
		private String authority;
		private boolean enabled;
		private String password;
		private String formPassword;
		
		public UserBuilder id(Long id){
			this.id = id;
			return this;
		}
		
		public UserBuilder username(String username){
			this.username = username;
			return this;
		}
		
		public UserBuilder authority(String authority){
			this.authority = authority;
			return this;
		}
		
		public UserBuilder enabled(boolean enabled){
			this.enabled = enabled;
			return this;
		}
		
		public UserBuilder password(String password){
			this.password = password;
			return this;
		}
		
		public UserBuilder formPassword(String formPassword){
			this.formPassword = formPassword;
			return this;
		}
		
		public User build(){
			return new User(this);
		}
	}
	
}
