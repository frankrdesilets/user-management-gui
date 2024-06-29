module frankdesilets.Account_Manager {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.base;

    opens frankdesilets.User_Manager to javafx.fxml;
    exports frankdesilets.User_Manager;
}
