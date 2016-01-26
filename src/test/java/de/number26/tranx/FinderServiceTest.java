package de.number26.tranx;

import java.math.BigDecimal;
import java.util.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import io.dropwizard.testing.*;
import io.dropwizard.testing.junit.*;
import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FinderServiceTest {

	private static final Map<Long, Transaction> transactions = new HashMap<Long, Transaction>();

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
		.addResource(new FinderService(transactions))
		.build();

	@Before public void setup(){
		transactions.put(4l, new Transaction("four", new BigDecimal(4), 0l));
		transactions.put(12l, new Transaction("twelve", new BigDecimal("12.12"), 4l));
		transactions.put(16l, new Transaction("sixteen", new BigDecimal("16.16"), 4l));

		transactions.put(44l, new Transaction("four", new BigDecimal(44), 0l));
		transactions.put(444l, new Transaction("four", new BigDecimal(444), 64l));
	}

	@After public void tearDown(){
		transactions.clear();
	}

	@Test public void testFindByType() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/type/four")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		@SuppressWarnings("unchecked")
		List<Integer> ids = response.readEntity(List.class);
		assertEquals(3, ids.size());
		assertTrue(ids.containsAll(Arrays.asList(4, 44, 444)));
	}

	@Test public void testByFindTypeSingle() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/type/sixteen")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		@SuppressWarnings("unchecked")
		List<Integer> ids = response.readEntity(List.class);
		assertEquals(1, ids.size());
		assertEquals(16, ids.get(0).intValue());
	}

	@Test public void testFindByTypeEmpty() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/type/foo")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		@SuppressWarnings("unchecked")
		List<Integer> ids = response.readEntity(List.class);
		assertTrue(ids.isEmpty());
	}
}
