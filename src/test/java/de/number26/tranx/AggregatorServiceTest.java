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

public class AggregatorServiceTest {

	private static final Map<Long, Transaction> transactions = new HashMap();

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
		.addResource(new AggregatorService(transactions))
		.build();

	@Before public void setup(){
		transactions.put(4l, new Transaction("four", new BigDecimal("4"), 0l));
		transactions.put(12l, new Transaction("twelve", new BigDecimal("12.12"), 4l));
		transactions.put(16l, new Transaction("sixteen", new BigDecimal("16.16"), 4l));

		transactions.put(44l, new Transaction("four", new BigDecimal(44), 0l));
		transactions.put(444l, new Transaction("four", new BigDecimal(444), 64l));
	}

	@After public void tearDown(){
		transactions.clear();
	}

	@Test public void testSumByParentId() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/sum/4")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		AggregatorService.Sum sum = response.readEntity(AggregatorService.Sum.class);
		assertEquals(32.28d, sum.getSum().doubleValue(), 0.0d);
	}

	@Test public void testSumByParentIdNoParent() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/sum/64")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		AggregatorService.Sum sum = response.readEntity(AggregatorService.Sum.class);
		assertEquals(444, sum.getSum().intValue());
	}

	@Test public void testSumByParentIdNoChild() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/sum/12")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		AggregatorService.Sum sum = response.readEntity(AggregatorService.Sum.class);
		assertEquals(12.12d, sum.getSum().doubleValue(), 0.0d);
	}
	
	@Test public void testSumByParentIdTransaction() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/sum/128")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		AggregatorService.Sum sum = response.readEntity(AggregatorService.Sum.class);
		assertEquals(BigDecimal.ZERO, sum.getSum());
	}
}
