package frankdesilets.User_Manager;

/**
 * This type represents the set of roles that users can belong to. Users can
 * belong to only one role at a time.
 */
public enum Role {

	SENIOR_MANAGER {
		@Override
		public String toString() {
			return "Senior Manager";
		}
	},
	MANAGER {
		@Override
		public String toString() {
			return "Manager";
		}
	},
	LEVEL_3_ENGINEER {
		@Override
		public String toString() {
			return "Level 3 Engineer";
		}
	},
	LEVEL_2_ENGINEER {
		@Override
		public String toString() {
			return "Level 2 Engineer";
		}
	},
	LEVEL_1_ENGINEER {
		@Override
		public String toString() {
			return "Level 1 Engineer";
		}
	},
	UNKNOWN;

}
