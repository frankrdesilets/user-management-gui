package frankdesilets.User_Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * An object of this class is a ComboBox that enables editing and deleting of a
 * user, which is displayed in the Dashboard TableView. Each User contains one
 * UserEditComboBox, and each UserEditComboBox contains the User it is
 * associated with.
 */
public class UserEditComboBox extends ComboBox<String> {

	private User associatedAccount; // the associated User

	/**
	 * The only constructor for this class accepts a User as a parameter and
	 * appropriately sets the member variable.
	 * 
	 * @param associatedAccount
	 */
	public UserEditComboBox(User associatedAccount) {

		super();
		this.associatedAccount = associatedAccount;

		this.setPrefWidth(70); // the width of the ComboBox is set

		// options to edit and delete the user are added to the ComboBox
		ObservableList<String> actionChoices = FXCollections.observableArrayList();
		actionChoices.addAll("Edit User", "Delete User");
		this.setItems(actionChoices);

		// when nothing is selected, prompt text "Edit" is displayed
		this.setPromptText("Edit");

		// the prompt text is reset when selection is cleared
		this.setButtonCell(new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				// if there is no selection, the prompt text "Edit" is displayed
				if (empty) {
					setText("Edit");
					// otherwise, the selection is displayed
				} else {
					setText(item);
				}
			}
		});
	}

	/**
	 * Returns the associated User.
	 * 
	 * @return
	 */
	public User getAssociatedAccount() {
		return associatedAccount;
	}

}
