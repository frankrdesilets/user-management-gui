package frankdesilets.User_Manager;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * An object of this class reads from and writes to a storage .txt file
 * containing user information. It is used by the Controller to retrieve user
 * information on application launch and update the file when users are
 * modified.
 */
public class UserFileIO {

	private int currentUserProperties; // the current number of user properties, used to check file formatting
	private boolean isTest; // signifies if this object is being used for testing. used to specify file location

	/**
	 * The only constructor for this class accepts an int currentUserProperties as a
	 * parameter and appropriately sets the member variable.
	 * 
	 * @param currentUserProperties
	 */
	public UserFileIO(int currentUserProperties, boolean isTest) {
		this.currentUserProperties = currentUserProperties;
		this.isTest = isTest;
	}

	/**
	 * Parses the .txt file used for storing user information and loads and returns
	 * an ObservableList of Users. Incorrectly formatted files are not processed.
	 * 
	 * @param fileName
	 * @param isTest
	 * @return
	 */
	public ObservableList<User> loadFile(String fileName) {

		// the account list to load and return
		ObservableList<User> accountList = FXCollections.observableArrayList();

		// if file formatting is incorrect, the list is returned empty
		if (!isCorrectFormat(fileName)) {
			return accountList;
		}

		try {

			// FileReaders are created to parse each line of the file
			File currentFile;
			/*
			 * The file location is specified. Normal running and application testing have
			 * different user storage file locations.
			 */
			if (!isTest) {
				currentFile = new File("src/main/java/userstoragefiles/" + fileName);
			} else {
				currentFile = new File("src/test/java/testuserstoragefiles/" + fileName);
			}
			FileReader fileReader = new FileReader(currentFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			/*
			 * currentLine represents the current line being processed in the file. It is
			 * set to be the first line of the file, to begin processing.
			 */
			String currentLine = bufferedReader.readLine();
			// lines in the file are processed until an empty line is reached
			while (currentLine != null) {
				String[] parsedLine = currentLine.split("[,]"); // the current line is split around delimiter ','
				// a new User is created with parsed information and added to the list
				User newAccount = new User(parsedLine[0], parsedLine[1], parsedLine[2], stringToRole(parsedLine[3]),
						stringToStatus(parsedLine[4]), isTest);
				accountList.add(newAccount);
				currentLine = bufferedReader.readLine(); // the current line is incremented
			}
			// the reader is closed
			bufferedReader.close();
			fileReader.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println();
		} finally {
		}

		return accountList; // the populated account list is returned

	}

	/**
	 * Writes to the .txt file used for storing user information.
	 * 
	 * @param fileName
	 * @param accountList
	 * @param isTest
	 */
	public void writeFile(String fileName, ObservableList<User> accountList) {

		try {
			/*
			 * A FileWriter is created to facilitate writing users to the file and the current
			 * file contents are cleared. Normal running and application testing have
			 * different user storage file locations.
			 */
			FileWriter fileWriter;
			if (!isTest) {
				fileWriter = new FileWriter("src/main/java/userstoragefiles/" + fileName);
			} else {
				fileWriter = new FileWriter("src/test/java/testuserstoragefiles/" + fileName);
			}
			
			fileWriter.write("");

			// the account list to write to the file is sorted in descending order by Role
			Collections.sort(accountList, new LexicographicRoleComparator());

			/*
			 * Each account in the list is written to the file, with one account per line
			 * with properties separated by delimiter ','.
			 */
			for (int i = 0; i < accountList.size(); i++) {
				User currentAccount = accountList.get(i); // the current user in the list
				fileWriter.write(currentAccount.getEmailID() + "," + currentAccount.getFirstName() + ","
						+ currentAccount.getLastName() + "," + currentAccount.getRole().toString() + ","
						+ currentAccount.getStatus().toString());
				// a new line is started after each account if the end of the list has not been
				// encountered
				if (i != accountList.size() - 1) {
					fileWriter.write("\n");
				}

			}
			fileWriter.close(); // the writer is closed

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println();
		} finally {
		}

	}

	/**
	 * Checks a .txt file used for user storage for correct formatting.
	 * 
	 * @param fileName
	 * @param isTest
	 * @return
	 */
	public boolean isCorrectFormat(String fileName) {

		try {

			// FileReaders are created to parse each line of the file
			File currentFile;
			/*
			 * The file location is specified. Normal running and application testing have
			 * different user storage file locations.
			 */
			if (!isTest) {
				currentFile = new File("src/main/java/userstoragefiles/" + fileName);
			} else {
				currentFile = new File("src/test/java/testuserstoragefiles/" + fileName);
			}
			FileReader fileReader = new FileReader(currentFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			/*
			 * currentLine represents the current line being processed in the file. It is
			 * set to be the first line of the file, to begin processing.
			 */
			String currentLine = bufferedReader.readLine();

			// lines in the file are processed until an empty line is reached
			while (currentLine != null) {
				String[] parsedLine = currentLine.split("[,]"); // the current line is split around delimiter ','
				/*
				 * The length of the parsed line array should be equal to the number of
				 * properties in a User. If the length is not equal to this value, the file is
				 * incorrectly formatted.
				 */
				if (parsedLine.length != currentUserProperties) {
					return false;
				}
				currentLine = bufferedReader.readLine(); // the current line is incremented
			}
			// the reader is closed
			bufferedReader.close();
			fileReader.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println();
		} finally {
		}
		return true;
	}

	/**
	 * Returns the Role representation of a given String. Used to create users from
	 * text retrieved from a .txt file.
	 * 
	 * @param role
	 * @return
	 */
	private Role stringToRole(String role) {
		switch (role) {
		case "Senior Manager":
			return Role.SENIOR_MANAGER;
		case "Manager":
			return Role.MANAGER;
		case "Level 3 Engineer":
			return Role.LEVEL_3_ENGINEER;
		case "Level 2 Engineer":
			return Role.LEVEL_2_ENGINEER;
		case "Level 1 Engineer":
			return Role.LEVEL_1_ENGINEER;
		default:
			return Role.UNKNOWN;
		}
	}

	/**
	 * Returns the Status representation of a given String. Used to create users
	 * from text retrieved from a .txt file.
	 * 
	 * @param status
	 * @return
	 */
	private Status stringToStatus(String status) {
		switch (status) {
		case "Active Employee":
			return Status.ACTIVE_EMPLOYEE;
		case "Inactive Employee":
			return Status.INACTIVE_EMPLOYEE;
		case "Disabled Account":
			return Status.DISABLED_ACCOUNT;
		default:
			return Status.UNKNOWN;
		}
	}

	/**
	 * A Comparator used to sort a list of Users in descending order by Role.
	 */
	private class LexicographicRoleComparator implements Comparator<User> {

		@Override
		public int compare(User user1, User user2) {
			return user1.getRole().compareTo(user2.getRole());
		}

	}

}
