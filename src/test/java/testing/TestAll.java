package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAll {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		int hi = 1;
		int hello = 1;
		assertEquals(hi,hello);
	}

}
