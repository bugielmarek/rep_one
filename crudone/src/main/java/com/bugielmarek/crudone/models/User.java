package com.bugielmarek.crudone.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Object <code>User</code> represents the user of the web application. Apart
 * from field 'formPassword' everything else is self-explanatory. 'formPassword'
 * allows to set the @Size constraint to user's password. Since 'password' field
 * is being encrypted before saving it's impossible to limit length of user's
 * 'password' to some small number by putting @Size annotation above 'password'
 * field. After encrypting is done 'password' is going to be exactly 80
 * characters long (using StandardPasswordEncoder). This is where 'formPassword'
 * enters the field. We bind it with 'password' by checking whether both fields match
 * when the form is submitted - if not, the form is returned to admin with
 * proper message. This binding allows us to limit the length of 'password' by
 * annotating 'formPassword' with @Size. Since we don't want 'formPassword' to
 * be saved into DB it is annotated with @Transient
 * @author Bugiel Marek
 *
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(exclude = { "password", "formPassword" })
@ToString(exclude = { "password", "formPassword" })
@Builder
@AllArgsConstructor
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = "\\w{6}\\b")
	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String authority;

	@Column(nullable = false)
	private boolean enabled;

	@Transient
	@Pattern(regexp = "\\S+\\b")
	@Size(min = 6, max = 15)
	private String formPassword;

	public User() {
	}
}
