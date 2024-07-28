package frankdesilets.User_Manager;

import java.awt.event.*;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * The only controller for the application. Fields annotated with @FXML allow
 * elements of the UI to be accessed; annotated methods enable visibility to
 * controls. There are four main views of the UI: the Dashboard, Filter User
 * form, Add User form, and Edit User form. Every UI component is contained in
 * this class.
 * 
 * The method initialize() is called on application launch to initialize the UI.
 */
public class Controller implements Initializable {
	
	/* 
	 * ---
	 * TABLE OF CONTENTS:
	 * ---
	 * 
	 * MEMBER VARIABLES
	 * 
	 * 	Final Variables
	 * 	General High-Level Variables
	 * 
	 * 	Dashboard UI Components
	 * 	Filter User Form UI Components
	 * 	Add User Form UI Components
	 * 	Edit User Form UI Components
	 * 	Bottom Pane UI Components
	 * 
	 * METHODS
	 * 
	 * 	addUser()
	 * 	deleteUser()
	 * 	editUser()
	 * 	cancelEditUser()
	 * 	searchUser()
	 * 	filterUsers()
	 * 	clearSearchAndFilter()
	 * 
	 * 	switchToDashboard()
	 * 	switchToFilterUserForm()
	 * 	switchToAddUserForm()
	 * 	switchToEditUserForm()
	 * 
	 * 	populateAccountList()
	 * 	updateTotalUsers()
	 * 	updateCurrentStatus()
	 * 	getUserIndexFromEmailID()
	 *  toggleFilterForm()
	 * 	resetFilterForm()
	 * 
	 * 	isAddUserFormAllInputFound()
	 * 	isAddUserFormInputUnderCharLimit()
	 * 	isEditUserFormInputUnderCharLimit()
	 * 	isEmailAcceptable()
	 * 	isEmailCorrectFormat()
	 * 	resetAddUserFormErrorStyle()
	 * 	resetEditUserFormErrorStyle()
	 * 
	 * 	setEditBoxOnAction()
	 * 	userEditBoxOnAction()
	 * 
	 * 	initialize()
	 * 	initializeMainAnchorPane()
	 * 	initializeDashboard()
	 * 	initializeFilterUserForm()
	 * 	initializeAddUserForm()
	 * 	initializeEditUserForm()
	 * 
	 * 	quit()
	 * 
	 * CLASSES
	 * 
	 * 	LexicographicFirstNameComparator
	 * 	LexicographicLastNameComparator
	 * 	LexicographicEmailIDComparator
	 * 	NullTableViewSelectionModel
	 */
	

	// FINAL VARIABLES

	/*
	 * The following final variables are editable information in a single point of
	 * maintenance.
	 */

	private final String STORAGE_FILE_NAME = "mainUserList.txt"; // the name of the .txt file used to store users
	/*
	 * The character maximum for any user property. The length of text input when
	 * adding or editing a user cannot exceed this value.
	 */
	private final int INPUT_CHAR_LIMIT = 35;
	/*
	 * The current number of user properties, used to check file formatting in file
	 * IO.
	 */
	private final int USER_PROPERTY_COUNT = 5;

	
	// *----------*

	
	// GENERAL HIGH-LEVEL VARIABLES

	/*
	 * The following variables are high-level objects used by the application.
	 */

	/*
	 * The main list of users, initially populated from the storage .txt file on
	 * application launch.
	 */
	private ObservableList<User> mainUserList;
	private UserFileIO fileIO; // the IO object used to read and write users to the storage .txt file

	@FXML
	private AnchorPane mainAnchorPane; // the Anchor Pane containing the majority of the UI

	
	// *----------*

	
	// DASHBOARD UI COMPONENTS

	/*
	 * The following JavaFX container and controls are associated with the Dashboard
	 * view of the application. The Dashboard displays users and their properties in
	 * a TableView, and provides controls to add, delete, edit, filter, or search
	 * for users.
	 */
	// *----------
	@FXML
	private VBox dashboard; // the container containing the elements for the Dashboard
	@FXML
	private TableView<User> userTableView; // the TableView that displays users
	@FXML
	private TableColumn<User, String> emailColumn; // the TableView column that displays a user's EmailID
	@FXML
	private TableColumn<User, String> firstNameColumn; // the TableView column that displays a user's first name
	@FXML
	private TableColumn<User, String> lastNameColumn; // the TableView column that displays a user's last name
	@FXML
	private TableColumn<User, String> roleColumn; // the TableView column that displays a user's role
	@FXML
	private TableColumn<User, String> statusColumn; // the TableView column that displays a user's status

	/*
	 * The TableView column that displays a user's UserEditComboBox (the ComboBox
	 * used to edit or delete a user)
	 */
	@FXML
	private TableColumn<User, UserEditComboBox> actionColumn;
	@FXML
	private TextField searchTextField; // the TextField used to search for a user
	// ----------*

	
	// FILTER USER FORM UI COMPONENTS

	/*
	 * The following JavaFX container and controls are associated with the Filter
	 * User form. This form provides the ability to filter the user TableView by a
	 * user property.
	 */
	// *----------
	@FXML
	private VBox filterUserForm; // the container containing the elements for the Filter User form

	/*
	 * The ChoiceBox used to select which user property to filter by. The remaining
	 * controls in the Filter User form can be enabled or disabled depending on the
	 * selection, as they may only be applicable to a certain property.
	 */
	@FXML
	private ChoiceBox<String> filterUserFilterBy;

	/*
	 * The ChoiceBox used to select which role to filter by. It is only enabled if
	 * Role is the property being filtered.
	 */
	@FXML
	private ChoiceBox<Role> filterUserRoleSelection;

	/*
	 * The ChoiceBox used to select which status to filter by. It is only enabled if
	 * Status is the property being filtered.
	 */
	@FXML
	private ChoiceBox<Status> filterUserStatusSelection;

	/*
	 * The RadioButton used to toggle filtering the user list in
	 * reverse-lexicographic order. It is only enabled for filtering properties
	 * EmailID, First Name, or Last Name. The counterpart to filterUserDescending.
	 * Only one button can be selected at a time.
	 */
	@FXML
	private RadioButton filterUserAscending;

	/*
	 * The RadioButton used to toggle filtering the user list in lexicographic
	 * order. It is only enabled for filtering properties EmailID, First Name, or
	 * Last Name. The counterpart to filterUserAscending. Only one button can be
	 * selected at a time.
	 */
	@FXML
	private RadioButton filterUserDescending;

	/*
	 * The CheckBox used to toggle displaying only the selected role. It is only
	 * enabled if Role is the property being filtered.
	 */
	@FXML
	private CheckBox filterUserOnlyRole;

	/*
	 * The CheckBox used to toggle displaying only the selected status. It is only
	 * enabled if Status is the property being filtered.
	 */
	@FXML
	private CheckBox filterUserOnlyStatus;
	// ----------*

	
	// ADD USER FORM UI COMPONENTS

	/*
	 * The following JavaFX container and controls are associated with the Add User
	 * form. This form provides the ability to add a new user to the system.
	 */
	// *----------
	@FXML
	private VBox addUserForm; // the container containing the elements for the Add User form
	@FXML
	private TextField addUserFirstNameEntry; // the TextField used for inputting a new user's first name
	@FXML
	private TextField addUserLastNameEntry; // the TextField used for inputting a new user's last name

	/*
	 * The TextField used for inputting a new user's emailID. On attempted user
	 * addition, the potential email is checked for correct email address formatting
	 * and to ensure that it is not already in use with another account.
	 */
	@FXML
	private TextField addUserEmailEntry;
	@FXML
	private ChoiceBox<Role> addUserRoleEntry; // the ChoiceBox used to select a new user's role
	@FXML
	private ChoiceBox<Status> addUserStatusEntry; // the ChoiceBox used to select a new user's status

	/*
	 * The label used to display error information on attempted user addition,
	 * including missing information in the form, the character limit being reached
	 * in an input TextField, or an incorrectly formatted or duplicate email
	 * address.
	 */
	@FXML
	private Label addUserInputError;
	// ----------*

	
	// EDIT USER FORM UI COMPONENTS

	/*
	 * The following JavaFX container and controls are associated with the Edit User
	 * form. This form provides the ability to edit an existing user of the system.
	 */
	// *----------
	@FXML
	private VBox editUserForm; // the container containing the elements for the Edit User form
	@FXML
	private Label editUserFirstNameDisplay; // the label displaying the user's current first name
	@FXML
	private TextField editUserFirstNameEntry; // the TextField used for inputting the user's updated first name
	@FXML
	private Label editUserLastNameDisplay; // the label displaying the user's current last name
	@FXML
	private TextField editUserLastNameEntry; // the TextField used for inputting the user's updated last name
	@FXML
	private Label editUserEmailDisplay; // the label displaying the user's current emailID
	@FXML
	private TextField editUserEmailEntry; // the TextField used for inputting the user's updated emailID
	@FXML
	private Label editUserRoleDisplay; // the label displaying the user's current role
	@FXML
	private ChoiceBox<Role> editUserRoleEntry; // the ChoiceBox used for inputting the user's updated role
	@FXML
	private Label editUserStatusDisplay; // the label displaying the user's current status
	@FXML
	private ChoiceBox<Status> editUserStatusEntry; // the ChoiceBox used for inputting the user's updated status

	/*
	 * The label used to display error information on attempted user update,
	 * including the character limit being reached in an input TextField, or an
	 * incorrectly formatted or duplicate email address.
	 */
	@FXML
	private Label editUserInputError;
	// ----------*

	
	// BOTTOM PANE UI COMPONENTS

	/*
	 * The following JavaFX container and controls are associated with the bottom
	 * pane that displays the total users and current status. They are always
	 * visible.
	 */
	// *----------
	@FXML
	private Label totalUsersLabel;
	@FXML
	private Label currentStatusLabel;
	// ----------*

	
	// METHODS

	/**
	 * Adds a user to the system with form input, called when the "Add User" button
	 * is pressed in the Add User form.
	 */
	@FXML
	private void addUser() {

		resetAddUserFormErrorStyle(); // error styling is reset on each addition attempt

		/*
		 * The form is checked for missing information, TextFields with input exceeding
		 * the character count, or an invalid email (incorrectly formatted or
		 * duplicate). The methods called determine the error and add red styling to the
		 * controls and display a label to highlight the error. The addition is not
		 * completed if an error is found.
		 */
		if (!isAddUserFormAllInputFound() || !isAddUserFormInputUnderCharLimit()
				|| !isEmailAcceptable(addUserEmailEntry, addUserInputError)) {
			return;
		}

		/*
		 * A new user is created with user-inputed text retrieved from TextFields
		 * located in the Add User form screen, addUserVBox.
		 */

		/*
		 * The first name of the user is capitalized.
		 */
		String firstNameTextFieldInput = addUserFirstNameEntry.getText();
		String firstNameCapitalized = firstNameTextFieldInput.substring(0, 1).toUpperCase()
				+ firstNameTextFieldInput.substring(1);

		/*
		 * The last name of the user is capitalized.
		 */
		String lastNameTextFieldInput = addUserLastNameEntry.getText();
		String lastNameCapitalized = lastNameTextFieldInput.substring(0, 1).toUpperCase()
				+ lastNameTextFieldInput.substring(1);

		/*
		 * A new user is created with information retrieved from the input controls,
		 * with the email set to be all lower case.
		 */
		User newAccount = new User(addUserEmailEntry.getText().toLowerCase(), firstNameCapitalized, lastNameCapitalized,
				(Role) addUserRoleEntry.getSelectionModel().getSelectedItem(),
				(Status) addUserStatusEntry.getSelectionModel().getSelectedItem(), false);
		// the new user is added to the list and the storage .txt file is updated
		mainUserList.add(newAccount);
		fileIO.writeFile(STORAGE_FILE_NAME, mainUserList);

		populateAccountList(); // the account list is re-populated from the .txt file
		setEditBoxOnAction(); // the UserEditComboBox of each user is reset their onAction
		updateTotalUsers(); // the total number of users label is updated
		switchToDashboard(); // the view is switched to the dashboard, resetting the form

		/*
		 * The current status label is updated to display that a user was added for 3
		 * seconds, reverting to regular display after the time has elapsed.
		 */
		currentStatusLabel.setStyle("-fx-text-fill: green");
		currentStatusLabel.setText("User Added");
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3), e -> updateCurrentStatus()));
		timeline.setCycleCount(1);
		Platform.runLater(timeline::play);
	}

	/**
	 * Deletes a user from the system. Called when "Delete User" is selected from an
	 * UserEditComboBox associated with a user in the user TableView.
	 * 
	 * @param toDelete
	 */
	private void deleteUser(User toDelete) {

		/*
		 * The new user is deleted from the main user list and the storage .txt file is
		 * updated.
		 */
		mainUserList.remove(toDelete);
		fileIO.writeFile(STORAGE_FILE_NAME, mainUserList);
		populateAccountList(); // the account list is re-populated from the .txt file
		updateTotalUsers(); // the total number of users label is updated
		resetFilterForm(); // the filter user form is reset

		/*
		 * The current status label is updated to display that a user was deleted for 3
		 * seconds, reverting to regular display after the time has elapsed.
		 */
		currentStatusLabel.setStyle("-fx-text-fill: red");
		currentStatusLabel.setText("User Deleted");
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3), e -> updateCurrentStatus()));
		timeline.setCycleCount(1);
		Platform.runLater(timeline::play);
	}

	/**
	 * Edits a current user in the system with form input, called when the "Save
	 * Changes" button is pressed in the Edit User form.
	 */
	@FXML
	private void editUser() {

		resetEditUserFormErrorStyle(); // error styling is reset on each edit attempt

		/*
		 * The form is checked for TextFields with input exceeding the character count,
		 * or an invalid email (incorrectly formatted or duplicate). The methods called
		 * determine the error and add red styling to the controls and display a label
		 * to highlight the error. The edit is not completed if an error is found.
		 */
		if (!isEditUserFormInputUnderCharLimit() || !isEmailAcceptable(editUserEmailEntry, editUserInputError)) {
			return;
		}

		/*
		 * The user to edit is retrieved by matching the email in the current user
		 * emailID label with an account.
		 */

		User toEdit = mainUserList.get(getUserIndexFromEmailID(editUserEmailDisplay.getText()));

		/*
		 * If there is input in the first name TextField, the input is capitalized and
		 * set to be the user's new first name.
		 */
		if (editUserFirstNameEntry.getText() != "") {
			String firstNameTextFieldInput = editUserFirstNameEntry.getText();
			String firstNameCapitalized = firstNameTextFieldInput.substring(0, 1).toUpperCase()
					+ firstNameTextFieldInput.substring(1);
			toEdit.setFirstName(firstNameCapitalized);
		}
		/*
		 * If there is input in the last name TextField, the input is capitalized and
		 * set to be the user's new last name.
		 */
		if (editUserLastNameEntry.getText() != "") {
			String lastNameTextFieldInput = editUserLastNameEntry.getText();
			String lastNameCapitalized = lastNameTextFieldInput.substring(0, 1).toUpperCase()
					+ lastNameTextFieldInput.substring(1);

			toEdit.setLastName(lastNameCapitalized);
		}
		/*
		 * If there is input in the emailID TextField, the input is changed to lowercase
		 * and set to be the user's new emailID.
		 */
		if (editUserEmailEntry.getText() != "") {
			toEdit.setEmailID(editUserEmailEntry.getText().toLowerCase());
		}
		/*
		 * If there is a selection in the role ChoiceBox, the selection is set to be the
		 * user's new role.
		 */
		if (!editUserRoleEntry.getSelectionModel().isEmpty()) {
			toEdit.setRole((Role) editUserRoleEntry.getSelectionModel().getSelectedItem());
		}
		/*
		 * If there is a selection in the status ChoiceBox, the selection is set to be
		 * the user's new status.
		 */
		if (!editUserStatusEntry.getSelectionModel().isEmpty()) {
			toEdit.setStatus((Status) editUserStatusEntry.getSelectionModel().getSelectedItem());
		}

		fileIO.writeFile(STORAGE_FILE_NAME, mainUserList); // the storage .txt file is updated

		populateAccountList(); // the account list is re-populated from the .txt file
		setEditBoxOnAction(); // the UserEditComboBox of each user is reset their onAction
		updateTotalUsers(); // the total number of users label is updated
		// the selection of the edited user's UserEditComboBox is cleared
		toEdit.getEditComboBox().getSelectionModel().clearSelection();
		switchToDashboard(); // the view is switched to the dashboard, resetting the form

	}

	/**
	 * Cancels the editing of a user, clears the form, switches the display to the
	 * dashboard, and resets the edit box of the account being edited. Called when
	 * the "Cancel" button is pressed in the Edit User form.
	 */
	@FXML
	private void cancelEditUser() {

		/*
		 * The user that was being edited is retrieved by matching the email in the
		 * current user emailID label with an account
		 */
		int userIndex = getUserIndexFromEmailID(editUserEmailDisplay.getText());
		User userToResetEditBox = mainUserList.get(userIndex);

		/*
		 * The selection of the user's UserEditComboBox is cleared and the view is
		 * switched to the dashboard, resetting the form.
		 */
		userToResetEditBox.getEditComboBox().getSelectionModel().clearSelection();
		switchToDashboard();
	}

	/**
	 * Populates the Dashboard TableView with a list of users that have any property
	 * containing an occurrence of the text from the search TextField.
	 * 
	 * This method is called when the text in the search TextField is edited.
	 */
	@FXML
	private void searchUsers() {

		// the Filter User form is reset as the current filter is removed on search
		resetFilterForm();

		String toFind = searchTextField.getText(); // the search input text

		// the list of users containing an occurrence of the search text in any property
		ObservableList<User> sortResults = FXCollections.observableArrayList();

		/*
		 * If the search input text is empty the TableView list is reset to show all
		 * users, the placeholder text that displays when there are no users in the
		 * system is reset to display appropriate information, and the method returns.
		 */
		if (toFind.isEmpty()) {
			userTableView.setItems(mainUserList);
			userTableView.setPlaceholder(new Label("Add users to the system via the \"Add User\" tab above."));
			return;
		}

		/*
		 * The search input is checked for occurrences in each user in each property. If
		 * any of a user's properties contains the search input, the user is a match and
		 * is added to the results list.
		 * 
		 * Case sensitivity in matching is disabled by converting the user property
		 * being matched and the search input to lower case.
		 */
		String toFindLowerCase = toFind.toLowerCase();
		for (int i = 0; i < mainUserList.size(); i++) {
			User currentAccount = mainUserList.get(i); // the current user being checked
			if (!sortResults.contains(currentAccount)) {
				/*
				 * If any of the current user's properties contains the search input, the user
				 * is a match and is added to the results list.
				 */
				if ((currentAccount.getEmailID().toLowerCase().contains(toFindLowerCase))
						|| (currentAccount.getFirstName().toLowerCase().contains(toFindLowerCase))
						|| (currentAccount.getLastName().toLowerCase().contains(toFindLowerCase))
						|| (currentAccount.getRole().toString().toLowerCase().contains(toFindLowerCase))
						|| (currentAccount.getStatus().toString().toLowerCase().contains(toFindLowerCase))) {
					sortResults.add(currentAccount);
				}
			}
		}

		/*
		 * The placeholder text in the TableView is set to display
		 * "No accounts matching search criteria.", as the only time this would display
		 * is when there are no users in the results list, implying that there were no
		 * users matching the search input.
		 */
		userTableView.setPlaceholder(new Label("No accounts matching search criteria."));
		userTableView.setItems(sortResults);

	}

	/**
	 * Filters the Dashboard TableView to include users matching the filter criteria
	 * specified in the Filter Users form. Called when the "Apply Filter" button is
	 * pressed in the Edit User form.
	 */
	@FXML
	private void filterUsers() {

		/*
		 * The list of users matching filter criteria to display in the Dashboard
		 * TableView.
		 */

		ObservableList<User> filterResults = FXCollections.observableArrayList();

		/*
		 * Used to determine the user property to filter. Its value is set by the index
		 * of the selection of the filter selection ChoiceBox.
		 * 
		 * 0 = EmailID 1 = First Name 2 = Last Name 3 = Role 4 = Status
		 */
		int filterBySelectionIndex = filterUserFilterBy.getSelectionModel().getSelectedIndex();
		/*
		 * The appropriate controls are accessed and the results list is populated
		 * depending on the above selection.
		 */
		switch (filterBySelectionIndex) {
		// the user property to filter the list by is EmailID
		case 0:

			// the results list is populated with every user
			for (int i = 0; i < mainUserList.size(); i++) {
				filterResults.add(mainUserList.get(i));
			}
			/*
			 * The list is sorted lexicographically by EmailID. If the RadioButton
			 * filterUserAscending is selected, the list is sorted in reverse lexicographic
			 * order.
			 */
			Collections.sort(filterResults, new LexicographicEmailIDComparator());
			if (filterUserAscending.isSelected()) {
				Collections.reverse(filterResults);
			}

			break;
		// the user property to filter the list by is First Name
		case 1:

			// the results list is populated with every user
			for (int i = 0; i < mainUserList.size(); i++) {
				filterResults.add(mainUserList.get(i));
			}
			/*
			 * The list is sorted lexicographically by First Name. If the RadioButton
			 * filterUserAscending is selected, the list is sorted in reverse lexicographic
			 * order.
			 */
			Collections.sort(filterResults, new LexicographicFirstNameComparator());
			if (filterUserAscending.isSelected()) {
				Collections.reverse(filterResults);
			}

			break;
		// the user property to filter the list by is Last Name
		case 2:

			// the results list is populated with every user
			for (int i = 0; i < mainUserList.size(); i++) {
				filterResults.add(mainUserList.get(i));
			}
			/*
			 * The list is sorted lexicographically by Last Name. If the RadioButton
			 * filterUserAscending is selected, the list is sorted in reverse lexicographic
			 * order.
			 */
			Collections.sort(filterResults, new LexicographicLastNameComparator());
			if (filterUserAscending.isSelected()) {
				Collections.reverse(filterResults);
			}

			break;
		// the user property to filter the list by is Role
		case 3:

			// the role to filter the list by is retrieved from the role selection ChoiceBox
			Role roleToMatch = (Role) filterUserRoleSelection.getSelectionModel().getSelectedItem();
			// users that are of the above role type are added to the results list
			for (int i = 0; i < mainUserList.size(); i++) {
				if (mainUserList.get(i).getRole().equals(roleToMatch)) {
					filterResults.add(mainUserList.get(i));
				}
			}

			/*
			 * The remaining accounts are added to the results list, unless the CheckBox
			 * used to toggle displaying only the selected role is selected.
			 */
			if (!filterUserOnlyRole.isSelected()) {
				for (int i = 0; i < mainUserList.size(); i++) {
					/*
					 * Every account in the main user list that is not already in the results list
					 * is added to the results list.
					 */
					if (!filterResults.contains(mainUserList.get(i))) {
						filterResults.add(mainUserList.get(i));
					}
				}
			}

			break;
		// the user property to filter the list by is Status
		case 4:

			// The status to filter the list by is retrieved from the status selection
			// ChoiceBox.
			Status statusToMatch = (Status) filterUserStatusSelection.getSelectionModel().getSelectedItem();
			// users that are of the above status type are added to the results list
			for (int i = 0; i < mainUserList.size(); i++) {
				if (mainUserList.get(i).getStatus().equals(statusToMatch)) {
					filterResults.add(mainUserList.get(i));
				}
			}

			/*
			 * The remaining accounts are added to the results list, unless the CheckBox
			 * used to toggle displaying only the selected status is selected.
			 */
			if (!filterUserOnlyStatus.isSelected()) {
				for (int i = 0; i < mainUserList.size(); i++) {
					/*
					 * Every account in the main user list that is not already in the results list
					 * is added to the results list.
					 */
					if (!filterResults.contains(mainUserList.get(i))) {
						filterResults.add(mainUserList.get(i));
					}
				}
			}

			break;
		}

		/*
		 * This placeholder will display if no accounts are added to the results list,
		 * implying that there were no accounts matching the search criteria.
		 */
		userTableView.setPlaceholder(new Label("No accounts matching filter criteria."));
		userTableView.setItems(filterResults); // the user TableView is set to display the filter results list
		switchToDashboard(); // the view is switched to the dashboard, resetting the form

	}

	/**
	 * Clears the current filter and search user display by resetting the user
	 * TableView to display the main user list. Called when the "Clear" button is
	 * pressed in the Dashboard.
	 */
	@FXML
	private void clearSearchAndFilter() {

		searchTextField.clear(); // the TextField used for searching for a user is cleared
		// the placeholder text for the user TableView is reset
		userTableView.setPlaceholder(new Label("Add users to the system via the \"Add User\" button above."));
		userTableView.setItems(mainUserList); // the user TableView is set to display the main user list
		resetFilterForm(); // the filter form is reset to initial configuration
	}

	/**
	 * Switches the display back to the main dashboard, clearing form data. Called
	 * by various cancel buttons on action.
	 */
	@FXML
	private void switchToDashboard() {

		/*
		 * The Add User form controls are cleared of input and error styling.
		 */
		addUserFirstNameEntry.clear();
		addUserLastNameEntry.clear();
		addUserEmailEntry.clear();
		addUserRoleEntry.getSelectionModel().clearSelection();
		addUserStatusEntry.getSelectionModel().select(0);
		resetAddUserFormErrorStyle();
		resetEditUserFormErrorStyle();

		/*
		 * The Edit User form controls are cleared of input.
		 */
		editUserFirstNameEntry.clear();
		editUserLastNameEntry.clear();
		editUserEmailEntry.clear();
		editUserRoleEntry.getSelectionModel().clearSelection();
		editUserStatusEntry.getSelectionModel().select(0);

		// the Dashboard view is set to be visible and every other view invisible
		filterUserForm.setVisible(false);
		addUserForm.setVisible(false);
		editUserForm.setVisible(false);
		dashboard.setVisible(true);

		updateCurrentStatus(); // the current status label is updated
	}

	/**
	 * Switches the display to the Filter Users form. Called when the "Filter"
	 * button is pressed in the Dashboard.
	 */
	@FXML
	private void switchToFilterUserForm() {

		/*
		 * The Filter Users form is set to be visible and the Dashboard invisible. The
		 * current status label is updated.
		 */
		dashboard.setVisible(false);
		filterUserForm.setVisible(true);
		updateCurrentStatus();
	}

	/**
	 * Switches the display to the Add User form. Called when the "Add User" button
	 * is pressed in the Dashboard.
	 */
	@FXML
	private void switchToAddUserForm() {

		/*
		 * The Add User form is set to be visible and the Dashboard invisible. The
		 * current status label is updated.
		 */
		dashboard.setVisible(false);
		addUserForm.setVisible(true);
		updateCurrentStatus();
	}

	/**
	 * Switches the display to the Edit User form. Called when "Edit User" is
	 * selected from an UserEditComboBox associated with a user in the user
	 * TableView.
	 */
	private void switchToEditUserForm(User currentUser) {

		/*
		 * The Edit User form is set to be visible and the Dashboard invisible.
		 */
		dashboard.setVisible(false);
		editUserForm.setVisible(true);

		/*
		 * The labels displaying the user-being-edited's information are updated to
		 * reflect the currently edited user.
		 */
		editUserFirstNameDisplay.setText(currentUser.getFirstName());
		editUserLastNameDisplay.setText(currentUser.getLastName());
		editUserEmailDisplay.setText(currentUser.getEmailID());
		editUserRoleDisplay.setText(currentUser.getRole().toString());
		editUserStatusDisplay.setText(currentUser.getStatus().toString());
		editUserRoleEntry.getSelectionModel().select(currentUser.getRole());
		editUserStatusEntry.getSelectionModel().select(currentUser.getStatus());

		updateCurrentStatus(); // the current status label is updated
	}

	/**
	 * Loads accounts from the storage file into a main user list and makes them
	 * visible in the user TableView. Called in various places to populate the main
	 * user list.
	 */
	private void populateAccountList() {

		mainUserList = fileIO.loadFile(STORAGE_FILE_NAME); // loads accounts from the storage .txt file into the main
															// user list
		userTableView.setItems(mainUserList); // the user TableView is set to display the main user list

		updateTotalUsers(); // the total users label is updated
		setEditBoxOnAction(); // every UserEditComboBox's onAction is reset
	}

	/**
	 * Updates the total number of users in the "Users" label. Called when a user is
	 * added or deleted to update the number of users.
	 */
	private void updateTotalUsers() {

		/*
		 * The total users label is set to display the current number of users in the
		 * system, calculated by retrieving the size of the main user list.
		 */
		int totalAccounts = mainUserList.size();
		totalUsersLabel.setText("Users: " + totalAccounts);
	}

	/**
	 * Updates the current "status" in the current status label, showing what the
	 * user of the application is currently doing. Called when the view is switched
	 * (for example, from the Dashboard to Add User form).
	 */
	@FXML
	private void updateCurrentStatus() {

		/*
		 * The text of the current status label is set to reflect where the user is in
		 * the application, calculated by the visibility of current views.
		 */
		currentStatusLabel.setStyle("-fx-text-fill: #9f9f9f");
		currentStatusLabel.setText("Currently Viewing Dashboard");

		if (filterUserForm.isVisible()) {
			currentStatusLabel.setText("Currently Filtering Users");
		}
		if (addUserForm.isVisible()) {
			currentStatusLabel.setText("Currently Adding User");
		}
		if (editUserForm.isVisible()) {
			currentStatusLabel.setText("Currently Editing User");
		}

	}

	/**
	 * Gets the index of user in the main user list from an emailID.
	 * 
	 * @param emailID
	 * @return
	 */
	private int getUserIndexFromEmailID(String emailID) {

		/*
		 * If the emailID being searched for matches an account, the index of that
		 * account in the main user list is returned.
		 */
		for (int i = 0; i < mainUserList.size(); i++) {
			if (mainUserList.get(i).getEmailID().equals(emailID)) {
				return i;
			}
		}

		return -1; // returns -1 if no accounts match the emailID being searched for

	}

	/**
	 * Toggles configuration of controls in the Filter User form based on selection
	 * of the filter selection ChoiceBox. Controls can be enabled or disabled
	 * depending on the selection, as they may only be applicable to a certain user
	 * property. Called when the filter selection ChoiceBox's selection is changed
	 * in the Filter Users form.
	 */
	private void toggleFilterForm() {

		/*
		 * Used to determine the controls to enable or disable. Its value is set by the
		 * index of the selection of the filter selection ChoiceBox.
		 * 
		 * 0 = EmailID 
		 * 1 = First Name 
		 * 2 = Last Name 
		 * 3 = Role 
		 * 4 = Status
		 */
		int filterBySelectionIndex = filterUserFilterBy.getSelectionModel().getSelectedIndex();
		/*
		 * The appropriate controls are enabled, disabled, or have their default
		 * selection changed depending on the above selection.
		 */
		switch (filterBySelectionIndex) {
		// the user property to filter the list by is EmailID
		case 0:
			/*
			 * Role and status selection are disabled as they are not applicable to
			 * filtering by emailID.
			 */
			filterUserRoleSelection.setDisable(true);
			filterUserStatusSelection.setDisable(true);

			/*
			 * The ascending and descending selection radio buttons are enabled, and
			 * descending it defaulted (lexicographic order).
			 */
			filterUserAscending.setDisable(false);
			filterUserAscending.setSelected(false);
			filterUserDescending.setDisable(false);
			filterUserDescending.setSelected(true);

			/*
			 * The CheckBoxes used to toggle only showing the selected role or status are
			 * disabled and de-selected as they are not applicable to filtering by emailID.
			 */
			filterUserOnlyRole.setSelected(false);
			filterUserOnlyRole.setDisable(true);
			filterUserOnlyStatus.setSelected(false);
			filterUserOnlyStatus.setDisable(true);
			break;
		// the user property to filter the list by is First Name
		case 1:
			/*
			 * Role and status selection are disabled as they are not applicable to
			 * filtering by first name.
			 */
			filterUserRoleSelection.setDisable(true);
			filterUserStatusSelection.setDisable(true);

			/*
			 * The ascending and descending selection radio buttons are enabled, and
			 * descending it defaulted (lexicographic order).
			 */
			filterUserAscending.setDisable(false);
			filterUserAscending.setSelected(false);
			filterUserDescending.setDisable(false);
			filterUserDescending.setSelected(true);

			/*
			 * The CheckBoxes used to toggle only showing the selected role or status are
			 * disabled and de-selected as they are not applicable to filtering by first
			 * name.
			 */
			filterUserOnlyRole.setSelected(false);
			filterUserOnlyRole.setDisable(true);
			filterUserOnlyStatus.setSelected(false);
			filterUserOnlyStatus.setDisable(true);
			break;
		// the user property to filter the list by is Last Name
		case 2:
			/*
			 * Role and status selection are disabled as they are not applicable to
			 * filtering by last name.
			 */
			filterUserRoleSelection.setDisable(true);
			filterUserStatusSelection.setDisable(true);

			/*
			 * The ascending and descending selection radio buttons are enabled, and
			 * descending it defaulted (lexicographic order).
			 */
			filterUserAscending.setDisable(false);
			filterUserAscending.setSelected(false);
			filterUserDescending.setDisable(false);
			filterUserDescending.setSelected(true);

			/*
			 * The CheckBoxes used to toggle only showing the selected role or status are
			 * disabled and de-selected as they are not applicable to filtering by last
			 * name.
			 */
			filterUserOnlyRole.setSelected(false);
			filterUserOnlyRole.setDisable(true);
			filterUserOnlyStatus.setSelected(false);
			filterUserOnlyStatus.setDisable(true);
			break;
		// the user property to filter the list by is Role
		case 3:
			// role selection is enabled and status selection is disabled
			filterUserRoleSelection.setDisable(false);
			filterUserStatusSelection.setDisable(true);

			/*
			 * The ascending and descending selection radio buttons are disabled as they are
			 * not applicable to filtering by role.
			 */
			filterUserAscending.setSelected(false);
			filterUserAscending.setDisable(true);
			filterUserDescending.setSelected(false);
			filterUserDescending.setDisable(true);

			/*
			 * The CheckBox used to toggle only showing the selected role is enabled and the
			 * CheckBox used to toggle only showing the selected status is disabled as it is
			 * not applicable to filtering by role. Both CheckBoxes are de-selected.
			 */
			filterUserOnlyRole.setSelected(false);
			filterUserOnlyRole.setDisable(false);
			filterUserOnlyStatus.setSelected(false);
			filterUserOnlyStatus.setDisable(true);
			break;
		// the user property to filter the list by is Status
		case 4:
			// status selection is enabled and role selection is disabled
			filterUserRoleSelection.setDisable(true);
			filterUserStatusSelection.setDisable(false);

			/*
			 * The ascending and descending selection radio buttons are disabled as they are
			 * not applicable to filtering by status.
			 */
			filterUserAscending.setSelected(false);
			filterUserAscending.setDisable(true);
			filterUserDescending.setSelected(false);
			filterUserDescending.setDisable(true);

			/*
			 * The CheckBox used to toggle only showing the selected status is enabled and
			 * the CheckBox used to toggle only showing the selected role is disabled as it
			 * is not applicable to filtering by status. Both CheckBoxes are de-selected.
			 */
			filterUserOnlyRole.setSelected(false);
			filterUserOnlyRole.setDisable(true);
			filterUserOnlyStatus.setSelected(false);
			filterUserOnlyStatus.setDisable(false);
			break;
		}
	}

	/**
	 * Resets the Filter User form to default configurations. Called in various
	 * places to reset the filter form.
	 */
	private void resetFilterForm() {

		/*
		 * ChoiceBoxes have their first index defaulted.
		 */
		filterUserFilterBy.getSelectionModel().select(0);
		filterUserRoleSelection.getSelectionModel().select(0);
		filterUserStatusSelection.getSelectionModel().select(0);

		/*
		 * Because the default property to filter by is EmailID, the remaining controls
		 * are toggled appropriately (see toggleFilterForm() comments for more
		 * information).
		 */

		/*
		 * Role and status selection are disabled as they are not applicable to
		 * filtering by emailID.
		 */
		filterUserRoleSelection.setDisable(true);
		filterUserStatusSelection.setDisable(true);

		/*
		 * The ascending and descending selection radio buttons are enabled, and
		 * descending it defaulted (lexicographic order).
		 */
		filterUserAscending.setSelected(false);
		filterUserAscending.setDisable(false);
		filterUserDescending.setSelected(true);
		filterUserDescending.setDisable(false);

		/*
		 * The CheckBoxes used to toggle only showing the selected role or status are
		 * disabled and de-selected as they are not applicable to filtering by emailID.
		 */
		filterUserOnlyRole.setSelected(false);
		filterUserOnlyRole.setDisable(true);
		filterUserOnlyStatus.setSelected(false);
		filterUserOnlyStatus.setDisable(true);
	}

	/**
	 * Ensures all required input is present in the Add User form on attempted
	 * addition (the "Add User" button is pressed), and determines errors and adds
	 * red styling to the controls and displays a label to highlight the error.
	 * Returns false if any required input is missing. Called by addUser().
	 * 
	 * @return
	 */
	private boolean isAddUserFormAllInputFound() {

		resetAddUserFormErrorStyle(); // present error styling is reset

		/*
		 * Remains and returns true if every control has input (no controls are missing
		 * input).
		 */
		boolean isInputAcceptable = true;

		/*
		 * Each control is checked for input. If empty, the border color of the control
		 * is set to red and the input error label is made visible with the text
		 * "Please add missing information".
		 */
		if ((addUserFirstNameEntry.getText() == "")) {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("Please add missing information.");
			addUserFirstNameEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputAcceptable = false;
		}
		if (addUserLastNameEntry.getText() == "") {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("Please add missing information.");
			addUserLastNameEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputAcceptable = false;
		}
		if (addUserEmailEntry.getText() == "") {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("Please add missing information.");
			addUserEmailEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputAcceptable = false;
		}
		if (addUserRoleEntry.getSelectionModel().isEmpty()) {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("Please add missing information.");
			addUserRoleEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputAcceptable = false;
		}
		if (addUserStatusEntry.getSelectionModel().isEmpty()) {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("Please add missing information.");
			addUserStatusEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputAcceptable = false;
		}

		return isInputAcceptable;
	}

	/**
	 * Ensures the TextField input in the Add User form is below the character limit
	 * on attempted addition (the "Add User" button is pressed), and determines
	 * which TextFields exceed the limit and adds red styling to such controls and
	 * displays a label to highlight the error. Properties inputed via a TextField
	 * include EmailID, First Name, and Last Name.
	 * 
	 * Returns false if any TextField's input exceeds the character limit. Called by
	 * addUser().
	 * 
	 * @return
	 */
	private boolean isAddUserFormInputUnderCharLimit() {

		resetAddUserFormErrorStyle(); // present error styling is reset

		/*
		 * Remains and returns true if every TextField's input does not exceed the
		 * character limit.
		 */
		boolean isInputUnderLimit = true;

		/*
		 * Each TextField's input is checked to ensure that is doesn't exceed the
		 * character limit. If it exceeds the limit, the border color of the control is
		 * set to red and the input error label is made visible with the text
		 * "The maximum allowed characters is [INPUT_CHAR_LIMIT]".
		 */
		if (addUserFirstNameEntry.getText().length() > INPUT_CHAR_LIMIT) {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("The maximum allowed characters is " + INPUT_CHAR_LIMIT + ".");
			addUserFirstNameEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputUnderLimit = false;
		}
		if (addUserLastNameEntry.getText().length() > INPUT_CHAR_LIMIT) {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("The maximum allowed characters is " + INPUT_CHAR_LIMIT + ".");
			addUserLastNameEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputUnderLimit = false;
		}
		if (addUserEmailEntry.getText().length() > INPUT_CHAR_LIMIT) {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			addUserInputError.setVisible(true);
			addUserInputError.setText("The maximum allowed characters is " + INPUT_CHAR_LIMIT + ".");
			addUserEmailEntry.setStyle("-fx-border-color: #ff9c9c");
			isInputUnderLimit = false;
		}

		return isInputUnderLimit;

	}

	/**
	 * Ensures the TextField input in the Edit User is below the character limit
	 * form on attempted addition (the "Save Changes" button is pressed), and
	 * determines errors and adds red styling to the controls and displays a label
	 * to highlight the error. Properties inputed via a TextField include EmailID,
	 * First Name, and Last Name.
	 * 
	 * Returns false if any TextField's input exceeds the character limit. Called by
	 * editUser().
	 * 
	 * @return
	 */
	private boolean isEditUserFormInputUnderCharLimit() {

		resetAddUserFormErrorStyle(); // present error styling is reset

		/*
		 * Remains and returns true if every TextField's input does not exceed the
		 * character limit.
		 */
		boolean isInputUnderLimit = true;

		/*
		 * Each TextField's input is checked to ensure that is doesn't exceed the
		 * character limit. If it exceeds the limit, the border color of the control is
		 * set to red and the input error label is made visible with the text
		 * "The maximum allowed characters is [INPUT_CHAR_LIMIT]".
		 * 
		 * The control is not checked if the input is empty as an empty control denotes
		 * no change to that property when the user is edited.
		 */
		if ((editUserFirstNameEntry.getText() != "")) {

			if (editUserFirstNameEntry.getText().length() > INPUT_CHAR_LIMIT) {
				/*
				 * The input error label is made visible, the error text is set, and red error
				 * styling is added to the control.
				 */
				editUserInputError.setVisible(true);
				editUserInputError.setText("The maximum allowed characters is " + INPUT_CHAR_LIMIT + ".");
				editUserFirstNameEntry.setStyle("-fx-border-color: #ff9c9c");
				isInputUnderLimit = false;
			}

		}
		if (editUserLastNameEntry.getText() != "") {

			if (editUserLastNameEntry.getText().length() > INPUT_CHAR_LIMIT) {
				/*
				 * The input error label is made visible, the error text is set, and red error
				 * styling is added to the control.
				 */
				editUserInputError.setVisible(true);
				editUserInputError.setText("The maximum allowed characters is " + INPUT_CHAR_LIMIT + ".");
				editUserLastNameEntry.setStyle("-fx-border-color: #ff9c9c");
				isInputUnderLimit = false;
			}

		}
		if (editUserEmailEntry.getText() != "") {
			/*
			 * The input error label is made visible, the error text is set, and red error
			 * styling is added to the control.
			 */
			if (editUserEmailEntry.getText().length() > INPUT_CHAR_LIMIT) {
				editUserInputError.setVisible(true);
				editUserInputError.setText("The maximum allowed characters is " + INPUT_CHAR_LIMIT + ".");
				editUserEmailEntry.setStyle("-fx-border-color: #ff9c9c");
				isInputUnderLimit = false;
			}

		}
		return isInputUnderLimit;

	}

	/**
	 * Ensures that an inputed email is formatted correctly and not already
	 * associated with an existing account. Returns false if an email is incorrectly
	 * formatted or already exists. Called by addUser() and editUser() (attempted
	 * addition or editing of an account).
	 * 
	 * @param textField
	 * @param label
	 * @return
	 */
	private boolean isEmailAcceptable(TextField textField, Label label) {

		// present error styling is reset in the Add User and Edit User forms
		resetAddUserFormErrorStyle();
		resetEditUserFormErrorStyle();

		// the email to be checked, retrieved from the EmailID input TextField
		String attemptedEmail = textField.getText();

		if (attemptedEmail == "") {
			return true;
		}

		/*
		 * Ensures correct formatting for an email address.
		 */
		if (!isEmailCorrectFormat(attemptedEmail)) {
			label.setText("Incorrectly formatted email.");
			textField.setStyle("-fx-border-color: #ff9c9c");
			label.setVisible(true);
			return false;
		}

		/*
		 * Ensures that the email is not already in use in another account.
		 * 
		 * The email being checked is compared against the email of each existing
		 * account. If it matches the email of an existing account, the input error
		 * label is made visible, the error text is set to "The email [attemptedEmail]
		 * is already in use with another account. Please try a different one.", and red
		 * error styling is added to the control.
		 */
		for (int i = 0; i < mainUserList.size(); i++) {
			if (attemptedEmail.equals(mainUserList.get(i).getEmailID())) {
				label.setText("The email \"" + attemptedEmail
						+ "\" is already in use with another account. Please try a different one.");
				label.setVisible(true);
				textField.clear();
				textField.setStyle("-fx-border-color: #ff9c9c");
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks an inputed email for correct format. Correct format is determined by
	 * the presence of one period (.) and one at (@) sign.
	 * 
	 * @param emailToCheck
	 * @return
	 */
	private boolean isEmailCorrectFormat(String emailToCheck) {

		/*
		 * The email is checked to ensure it contains exactly one period, returning
		 * false if not.
		 */
		int periodCounter = 0;
		for (int i = 0; i < emailToCheck.length(); i++) {
			if (emailToCheck.charAt(i) == '.') {
				periodCounter++;
			}
		}
		// if there not exactly one period in the String, the email is incorrectly
		// formatted
		if (periodCounter != 1) {
			return false;
		}

		/*
		 * The email is checked to ensure it contains exactly one at sign, returning
		 * false if not.
		 */
		int atCounter = 0;
		for (int i = 0; i < emailToCheck.length(); i++) {
			if (emailToCheck.charAt(i) == '@') {
				atCounter++;
			}

		}
		// if there not exactly one at sign in the String, the email is incorrectly
		// formatted
		if (atCounter != 1) {
			return false;
		}

		return true;
	}

	/**
	 * Resets styling in the Add User form's elements.
	 */
	private void resetAddUserFormErrorStyle() {

		/*
		 * Each control in the form is cleared of red error styling and the input error
		 * label is made invisible.
		 */
		addUserFirstNameEntry.setStyle(null);
		addUserLastNameEntry.setStyle(null);
		addUserEmailEntry.setStyle(null);
		addUserRoleEntry.setStyle(null);
		addUserStatusEntry.setStyle(null);
		addUserInputError.setVisible(false);
	}

	/**
	 * Resets styling in the Edit User form's elements.
	 */
	private void resetEditUserFormErrorStyle() {

		/*
		 * Each control in the form is cleared of red error styling and the input error
		 * label is made invisible.
		 */
		editUserFirstNameEntry.setStyle(null);
		editUserLastNameEntry.setStyle(null);
		editUserEmailEntry.setStyle(null);
		editUserRoleEntry.setStyle(null);
		editUserStatusEntry.setStyle(null);
		editUserInputError.setVisible(false);
	}

	/**
	 * Sets the onAction method for each user's UserEditComboBox.
	 */
	private void setEditBoxOnAction() {

		/*
		 * The UserEditComboBox for each user is retrieved and set the UserEditComboBox
		 * onAction method.
		 */
		for (int i = 0; i < mainUserList.size(); i++) {
			UserEditComboBox currentEditBox = mainUserList.get(i).getEditComboBox();
			currentEditBox.setOnAction(e -> {
				userEditBoxOnAction(e);
			});
		}
	}

	/**
	 * The onAction method for user UserEditComboBoxes. Called when a selection is
	 * made in a user's UserEditComboBox from the Dashboard. Action is determined
	 * depending on which selection is made.
	 * 
	 * @param e
	 */
	private void userEditBoxOnAction(Event e) {

		/*
		 * The user the UserEditComboBox is associated with is retrieved.
		 */
		UserEditComboBox currentEditBox = (UserEditComboBox) e.getSource();
		User currentUser = currentEditBox.getAssociatedAccount();
		// the index of the selection of the UserEditComboBox
		int selectionIndex = currentEditBox.getSelectionModel().getSelectedIndex();
		switch (selectionIndex) {
		// If index 0 is selected (Edit User), the view switched to the Edit User form
		case 0:
			switchToEditUserForm(currentUser);
			break;
		// If index 1 is selected (Delete User), the user is deleted
		case 1:
			deleteUser(currentUser);
			break;
		}
	}

	/**
	 * Called to initialize the controller after its root element has been
	 * completely processed.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		 * The name of the .txt file used for storage is specified and the UserFileIO
		 * member variable is instantiated, which is used to read from and write to the
		 * file. Testing is disabled.
		 */
		fileIO = new UserFileIO(USER_PROPERTY_COUNT, false);
		populateAccountList(); // the main User list is populated
		initializeMainAnchorPane(); // the main anchor pane is initialized
	}

	/**
	 * Sets the background of the main anchor pane to a light blue and initializes
	 * the Dashboard. Called on initialization.
	 */
	private void initializeMainAnchorPane() {

		mainAnchorPane.setStyle("-fx-background-color: #fcfdff");
		initializeDashboard();
	}

	/**
	 * Initializes elements of the Dashboard, including the TableView that displays
	 * users and its columns, and the ComboBox to select a search filter. Called on
	 * initialization.
	 */
	private void initializeDashboard() {

		// the User properties to be displayed in each TableView column are set
		emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("emailID"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
		roleColumn.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<User, String>("status"));
		actionColumn.setCellValueFactory(new PropertyValueFactory<User, UserEditComboBox>("editComboBox"));

		// reordering is disabled for each column
		firstNameColumn.setReorderable(false);
		lastNameColumn.setReorderable(false);
		emailColumn.setReorderable(false);
		roleColumn.setReorderable(false);
		statusColumn.setReorderable(false);
		actionColumn.setReorderable(false);

		/*
		 * The TableView is set to disallow row selection via a custom
		 * TableViewSelectionModel object.
		 */
		userTableView.setSelectionModel(new NullTableViewSelectionModel(userTableView));
		// horizontal scrolling is disabled
		userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		/*
		 * A custom placeholder is set to display when there are no users, prompting to
		 * add a user
		 */
		userTableView.setPlaceholder(new Label("Add users to the system via the \"Add User\" button above."));

		// styling when the TableView is focused is made a softer color
		userTableView.setFocusTraversable(false);
		userTableView.setStyle("-fx-faint-focus-color: transparent");
		userTableView.setStyle("-fx-focus-color: transparent");

		/*
		 * The Observable Lists used to populate various Combo/ChoiceBoxes with Role and
		 * Status choices.
		 */
		ObservableList<Role> roleChoices = FXCollections.observableArrayList();
		roleChoices.addAll(Role.SENIOR_MANAGER, Role.MANAGER, Role.LEVEL_3_ENGINEER, Role.LEVEL_2_ENGINEER,
				Role.LEVEL_1_ENGINEER);
		ObservableList<Status> statusChoices = FXCollections.observableArrayList();
		statusChoices.addAll(Status.ACTIVE_EMPLOYEE, Status.INACTIVE_EMPLOYEE, Status.DISABLED_ACCOUNT);

		/*
		 * The ComboBoxes associated with each user that allow for editing and deletion
		 * are set an onAction method.
		 */
		setEditBoxOnAction();

		// the Filter Users, Add User, and Edit User forms are initialized
		initializeFilterUserForm(roleChoices, statusChoices);
		initializeAddUserForm(roleChoices, statusChoices);
		initializeEditUserForm(roleChoices, statusChoices);
	}

	/**
	 * Initializes elements of the Filter User form. Called on initialization.
	 * 
	 * @param roleChoices
	 * @param statusChoices
	 */
	private void initializeFilterUserForm(ObservableList<Role> roleChoices, ObservableList<Status> statusChoices) {

		/*
		 * User properties are added to a ChoiceBox that enables selection of the
		 * property to filter.
		 */
		ObservableList<String> filterChoices = FXCollections.observableArrayList();
		filterChoices.addAll("Email/ID", "First Name", "Last Name", "Role", "Status");
		filterUserFilterBy.setItems(filterChoices);
		/*
		 * The remaining controls in the filter form are enabled or disabled based on
		 * filter property selection. These controls update each time a filter property
		 * selection is made.
		 */
		filterUserFilterBy.setOnAction(e -> {
			toggleFilterForm();
		});

		// the role and status selection ChoiceBox are set their list of options
		filterUserRoleSelection.setItems(roleChoices);
		filterUserStatusSelection.setItems(statusChoices);

		/*
		 * The ascending/descending RadioButton pair is configured to ensure that only
		 * one selection is possible and that de-selecting is disabled to ensure that
		 * one button is always selected.
		 */
		filterUserAscending.setOnAction(e -> {
			if (filterUserAscending.isSelected()) {
				filterUserDescending.setSelected(false);
			}
			if (!filterUserAscending.isSelected()) {
				filterUserAscending.setSelected(true);
			}
		});
		filterUserDescending.setOnAction(e -> {
			if (filterUserDescending.isSelected()) {
				filterUserAscending.setSelected(false);
			}
			if (!filterUserDescending.isSelected()) {
				filterUserDescending.setSelected(true);
			}
		});

		/*
		 * The initial configuration of controls are set.
		 */
		resetFilterForm();
		toggleFilterForm();

	}

	/**
	 * Initializes elements of the Add User form. Called on initialization.
	 * 
	 * @param roleChoices
	 * @param statusChoices
	 */
	private void initializeAddUserForm(ObservableList<Role> roleChoices, ObservableList<Status> statusChoices) {

		// the role and status selection ChoiceBox are set their list of options
		addUserRoleEntry.setItems(roleChoices);
		addUserStatusEntry.setItems(statusChoices);
		// the status selection ChoiceBox is set an initial selection, "Active Employee"
		addUserStatusEntry.getSelectionModel().select(0);
	}

	/**
	 * Initializes elements of the Edit User form. Called on initialization.
	 * 
	 * @param roleChoices
	 * @param statusChoices
	 */
	private void initializeEditUserForm(ObservableList<Role> roleChoices, ObservableList<Status> statusChoices) {

		// the role and status selection ChoiceBox are set their list of options
		editUserRoleEntry.setItems(roleChoices);
		editUserStatusEntry.setItems(statusChoices);
		// the status selection ChoiceBox is set an initial selection, "Active Employee"
		editUserStatusEntry.getSelectionModel().select(0);
	}

	/**
	 * Exits the program. Called by the "Quit" menu item under File.
	 */
	@FXML
	private void quit() {
		System.exit(0);
	}

	// CLASSES

	/**
	 * A Comparator used to sort a list of Users in lexicographic order by First
	 * Name.
	 */
	private class LexicographicFirstNameComparator implements Comparator<User> {

		@Override
		public int compare(User user1, User user2) {
			return user1.getFirstName().compareTo(user2.getFirstName());
		}

	}

	/**
	 * A Comparator used to sort a list of Users in lexicographic order by Last
	 * Name.
	 */
	private class LexicographicLastNameComparator implements Comparator<User> {

		@Override
		public int compare(User user1, User user2) {
			return user1.getLastName().compareTo(user2.getLastName());
		}

	}

	/**
	 * A Comparator used to sort a list of Users in lexicographic order by EmailID.
	 */
	private class LexicographicEmailIDComparator implements Comparator<User> {

		@Override
		public int compare(User user1, User user2) {
			return user1.getEmailID().compareTo(user2.getEmailID());
		}

	}

	/**
	 * An object of this class is used to prevent row selection in the Dashboard
	 * user list TableView. getSelectedCells() is overridden to return an empty
	 * ObservableList.
	 */
	private class NullTableViewSelectionModel extends TableView.TableViewSelectionModel {

		public NullTableViewSelectionModel(TableView tableView) {
			super(tableView);
		}

		@Override
		public ObservableList getSelectedCells() {
			return FXCollections.emptyObservableList();
		}

		@Override
		public boolean isSelected(int row, TableColumn column) {
			return false;
		}

		@Override
		public void select(int row, TableColumn column) {
		}

		@Override
		public void clearAndSelect(int row, TableColumn column) {
		}

		@Override
		public void clearSelection(int row, TableColumn column) {
		}

		@Override
		public void selectLeftCell() {
		}

		@Override
		public void selectRightCell() {
		}

		@Override
		public void selectAboveCell() {
		}

		@Override
		public void selectBelowCell() {
		}

	}
}
