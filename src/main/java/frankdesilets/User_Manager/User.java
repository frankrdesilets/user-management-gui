package frankdesilets.User_Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

/**
 * An object of this class represents a user account in a system.
 */
public class User {

	private String emailID; // unique email identifier
	private String firstName; // first name
	private String lastName; // last name
	private Role role; // a user's position in an organization
	private Status status; // a user's account activity
	/*
	 * The ComboBox allowing for the editing or deletion of this user, displayed in
	 * the Dashboard TableView.
	 */
	private UserEditComboBox editComboBox;

	/**
	 * The only constructor for this class accepts user properties as parameters and
	 * appropriately sets member variables.
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param role
	 * @param status
	 */
	public User(String email, String firstName, String lastName, Role role, Status status) {
		this.emailID = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.status = status;
		this.editComboBox = new UserEditComboBox(this);
	}

	/*
	 * The following methods are getters and setters.
	 */

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UserEditComboBox getEditComboBox() {
		return editComboBox;
	}

	public void setEditComboBox(UserEditComboBox actionComboBox) {
		this.editComboBox = actionComboBox;
	}

}
