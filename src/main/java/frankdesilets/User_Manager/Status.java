package frankdesilets.User_Manager;

/**
 * This type represents the set of statuses that users can belong to. Users can
 * belong to only one status at a time.
 */
public enum Status {

	ACTIVE_EMPLOYEE {
		@Override
		public String toString() {
			return "Active Employee";
		}
	},
	INACTIVE_EMPLOYEE {
		@Override
		public String toString() {
			return "Inactive Employee";
		}
	},
	DISABLED_ACCOUNT {
		@Override
		public String toString() {
			return "Disabled Account";
		}
	},
	UNKNOWN;

}
