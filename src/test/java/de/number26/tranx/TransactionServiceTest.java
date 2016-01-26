package de.number26.tranx;

import java.math.BigDecimal;
import java.util.Map;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import io.dropwizard.testing.*;
import io.dropwizard.testing.junit.*;
import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TransactionServiceTest {

	@SuppressWarnings("unchecked")
	private static final Map<Long, Transaction> transactions = (Map<Long, Transaction>) mock(Map.class);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
		.addResource(new TransactionService(transactions))
		.build();

	@SuppressWarnings("unchecked")
	@After public void tearDown(){
		reset(transactions);
	}

	@Test public void testSetTransaction() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/transaction/0024")
			.request()
			.put(Entity.json(new Transaction("foo", new BigDecimal(12), 64l)));

		// assert
		assertEquals(200, response.getStatus());
		assertEquals(OperationResult.OK, response.readEntity(OperationResult.class));

		// verify
		ArgumentCaptor<Transaction> tx = ArgumentCaptor.forClass(Transaction.class);
		verify(transactions).put(eq(24l), tx.capture());
		assertEquals("foo", tx.getValue().getType());
		assertEquals(new BigDecimal(12), tx.getValue().getAmount());
		assertEquals(64l, tx.getValue().getParentId());
		verifyNoMoreInteractions(transactions);
	}

	@Test public void testGetTransaction() {
		// train
		when(transactions.containsKey(12l)).thenReturn(true);
		when(transactions.get(12l)).thenReturn(new Transaction("bar", new BigDecimal(64), 16l));

		// invoke
		Response response = resources.client()
			.target("/transactionservice/transaction/12")
			.request()
			.get();

		// assert
		assertEquals(200, response.getStatus());
		Transaction tx = response.readEntity(Transaction.class);
		assertEquals("bar", tx.getType());
		assertEquals(new BigDecimal(64), tx.getAmount());
		assertEquals(16l, tx.getParentId());
	}

	@Test public void testGetTransactionNotFound() {
		// invoke
		Response response = resources.client()
			.target("/transactionservice/transaction/12")
			.request()
			.get();

		// assert
		assertEquals(404, response.getStatus());
	}
}
