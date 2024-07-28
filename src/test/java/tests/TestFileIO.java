package tests;

import frankdesilets.User_Manager.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The following test cases test correct file IO. Tests include ensuring correct
 * file loading, user writing, and incorrect format detection.
 */
class TestFileIO {
	
	/* 
	 * *---*
	 * TEST CASES
	 * *---*
	 * 
	 * 		testLoadFile()
	 * 		testWriteOneUser()
	 * 		testWriteMultipleUsers()
	 * 		testCorrectFormatDetectionOneUser()
	 * 		testCorrectFormatDetectionMultipleUsers()
	 */

	private final int USER_PROPERTY_COUNT = 5;

	/**
	 * Tests loading a list of users from a correctly formatted file.
	 */
	@Test
	void testLoadFile() {

		/*
		 * The fileIO object and user lists to compare are created.
		 */
		UserFileIO fileIO = new UserFileIO(USER_PROPERTY_COUNT, true); // testing is enabled
		// contains users that should be in the calculated list
		ObservableList<User> correctAccountList = FXCollections.observableArrayList();
		// contains users that are loaded from the file
		ObservableList<User> calculatedAccountList = FXCollections.observableArrayList();

		// the correct users are added to the correct user account list
		User user1 = new User("jacobbutler@companydomain.com", "Jacob", "Butler", Role.LEVEL_3_ENGINEER,
				Status.INACTIVE_EMPLOYEE, true);
		User user2 = new User("heatherc@companydomain.com", "Heather", "Cameron", Role.LEVEL_2_ENGINEER,
				Status.ACTIVE_EMPLOYEE, true);
		User user3 = new User("frankdesilets1@gmail.com", "Frank", "Desilets", Role.LEVEL_2_ENGINEER,
				Status.ACTIVE_EMPLOYEE, true);
		User user4 = new User("rees@icloud.com", "James", "Rees", Role.LEVEL_1_ENGINEER, Status.DISABLED_ACCOUNT, true);
		correctAccountList.addAll(user1, user2, user3, user4);

		calculatedAccountList = fileIO.loadFile("testUserList_LoadFile.txt"); // users are loaded from the file

		/*
		 * Each user in the correct account list has its properties compared to its
		 * counterpart in the loaded list. Each property should be equal.
		 */
		for (int i = 0; i < 3; i++) {
			assertEquals(correctAccountList.get(i).getEmailID(), calculatedAccountList.get(i).getEmailID());
			assertEquals(correctAccountList.get(i).getFirstName(), calculatedAccountList.get(i).getFirstName());
			assertEquals(correctAccountList.get(i).getLastName(), calculatedAccountList.get(i).getLastName());
			assertEquals(correctAccountList.get(i).getRole(), calculatedAccountList.get(i).getRole());
			assertEquals(correctAccountList.get(i).getStatus(), calculatedAccountList.get(i).getStatus());
		}
	}

	/**
	 * Tests writing one user to a .txt file.
	 */
	@Test
	void testWriteOneUser() {

		/*
		 * The fileIO object and user lists to compare are created.
		 */
		UserFileIO fileIO = new UserFileIO(USER_PROPERTY_COUNT, true); // testing is enabled
		ObservableList<User> correctAccountList = FXCollections.observableArrayList();
		ObservableList<User> calculatedAccountList = FXCollections.observableArrayList();

		// the correct user is added to the correct user account list
		User user1 = new User("frankdesilets1@gmail.com", "Frank", "Desilets", Role.LEVEL_2_ENGINEER,
				Status.ACTIVE_EMPLOYEE, true);
		correctAccountList.add(user1);

		/*
		 * The user is written to the file and a list then populated with the contents
		 * of the file. Before writing, the file is cleared of contents.
		 */
		fileIO.writeFile("testUserList.txt", correctAccountList);
		calculatedAccountList = fileIO.loadFile("testUserList.txt");

		/*
		 * The user in the correct account list has its properties compared to its
		 * counterpart in the loaded list. Each property should be equal.
		 */
		assertEquals(correctAccountList.get(0).getEmailID(), calculatedAccountList.get(0).getEmailID());
		assertEquals(correctAccountList.get(0).getFirstName(), calculatedAccountList.get(0).getFirstName());
		assertEquals(correctAccountList.get(0).getLastName(), calculatedAccountList.get(0).getLastName());
		assertEquals(correctAccountList.get(0).getRole(), calculatedAccountList.get(0).getRole());
		assertEquals(correctAccountList.get(0).getStatus(), calculatedAccountList.get(0).getStatus());

	}

	/**
	 * Tests writing multiple users to a .txt file.
	 */
	@Test
	void testWriteMultipleUsers() {

		/*
		 * The fileIO object and user lists to compare are created.
		 */
		UserFileIO fileIO = new UserFileIO(USER_PROPERTY_COUNT, true); // testing is enabled
		ObservableList<User> correctAccountList = FXCollections.observableArrayList();
		ObservableList<User> calculatedAccountList = FXCollections.observableArrayList();

		// the correct users are added to the correct user account list
		User user1 = new User("george@gmail.com", "George", "Washington", Role.SENIOR_MANAGER, Status.ACTIVE_EMPLOYEE,
				true);
		User user2 = new User("john@gmail.com", "John", "Appleseed", Role.LEVEL_2_ENGINEER, Status.INACTIVE_EMPLOYEE,
				true);
		User user3 = new User("greg@yahoo.com", "Greg", "Jones", Role.LEVEL_3_ENGINEER, Status.DISABLED_ACCOUNT, true);
		correctAccountList.addAll(user1, user2, user3);

		/*
		 * The users are written to the file and a list then populated with the contents
		 * of the file. Before writing, the file is cleared of contents.
		 */
		fileIO.writeFile("testUserList.txt", correctAccountList);
		calculatedAccountList = fileIO.loadFile("testUserList.txt");

		/*
		 * Each user in the correct account list has its properties compared to its
		 * counterpart in the loaded list. Each property should be equal.
		 */
		for (int i = 0; i < 3; i++) {
			assertEquals(correctAccountList.get(i).getEmailID(), calculatedAccountList.get(i).getEmailID());
			assertEquals(correctAccountList.get(i).getFirstName(), calculatedAccountList.get(i).getFirstName());
			assertEquals(correctAccountList.get(i).getLastName(), calculatedAccountList.get(i).getLastName());
			assertEquals(correctAccountList.get(i).getRole(), calculatedAccountList.get(i).getRole());
			assertEquals(correctAccountList.get(i).getStatus(), calculatedAccountList.get(i).getStatus());
		}

	}

	/**
	 * Tests incorrect format detection with an empty file.
	 */
	@Test
	void testCorrectFormatDetectionEmptyFile() {

		/*
		 * The fileIO object and the list to load from the file are created.
		 */
		UserFileIO fileIO = new UserFileIO(USER_PROPERTY_COUNT, true); // testing is enabled
		ObservableList<User> calculatedAccountList = FXCollections.observableArrayList();

		// the list is loaded from the file
		calculatedAccountList = fileIO.loadFile("testUserList_IncorrectFormat_EmptyFile.txt");

		/*
		 * Because the file is incorrectly formatted, the list should be empty.
		 */
		assertEquals(calculatedAccountList.isEmpty(), true);

	}

	/**
	 * Tests incorrect format detection with one incorrectly formatted user account
	 * in a file.
	 */
	@Test
	void testCorrectFormatDetectionOneUser() {

		/*
		 * The fileIO object and the list to load from the file are created.
		 */
		UserFileIO fileIO = new UserFileIO(USER_PROPERTY_COUNT, true); // testing is enabled
		ObservableList<User> calculatedAccountList = FXCollections.observableArrayList();

		// the list is loaded from the file
		calculatedAccountList = fileIO.loadFile("testUserList_IncorrectFormat_OneUser.txt");

		/*
		 * Because the file is incorrectly formatted, the list should be empty.
		 */
		assertEquals(calculatedAccountList.isEmpty(), true);

	}

	/**
	 * Tests incorrect format detection with multiple incorrectly formatted user
	 * accounts in a file.
	 */
	@Test
	void testCorrectFormatDetectionMultipleUsers() {

		/*
		 * The fileIO object and the list to load from the file are created.
		 */
		UserFileIO fileIO = new UserFileIO(USER_PROPERTY_COUNT, true);
		ObservableList<User> calculatedAccountList = FXCollections.observableArrayList();

		// the list is loaded from the file
		calculatedAccountList = fileIO.loadFile("testUserList_IncorrectFormat_MultipleUsers.txt");

		/*
		 * Because the file is incorrectly formatted, the list should be empty.
		 */
		assertEquals(calculatedAccountList.isEmpty(), true);

	}

}
