package de.number26.tranx;

import java.math.BigDecimal;
import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import io.dropwizard.testing.*;
import io.dropwizard.client.*;
import io.dropwizard.testing.junit.*;
import org.junit.ClassRule;
import org.junit.*;
import static org.junit.Assert.*;

public class TranxTest {

	@ClassRule
	public static final DropwizardAppRule<TranxConfiguration> RULE =
		new DropwizardAppRule<TranxConfiguration>(TranxServer.class, ResourceHelpers.resourceFilePath("tranx.yaml"));

	private WebTarget client;

	@Before public void setup() {
		client = new JerseyClientBuilder(RULE.getEnvironment())
		.build("test client")
		.target(String.format("http://localhost:%d/", RULE.getLocalPort()));
	}

	@Test
	public void createTransactionReturnsOk() {
		// create "cars" transaction
		Response response = client
		.path("/transactionservice/transaction/10")
		.request()
		.put(Entity.json(new Transaction("cars", new BigDecimal(5000), 0l)));

		assertEquals(200, response.getStatus());
		assertEquals(OperationResult.OK, response.readEntity(OperationResult.class));

		// create "shopping" transaction
		response = client
		.path("/transactionservice/transaction/11")
		.request()
		.put(Entity.json(new Transaction("shopping", new BigDecimal(10000), 10l)));

		assertEquals(200, response.getStatus());
		assertEquals(OperationResult.OK, response.readEntity(OperationResult.class));

		// get "cars" transactions
		response = client
		.path("/transactionservice/type/cars")
		.request()
		.get();

		assertEquals(200, response.getStatus());
		List<Integer> ids = response.readEntity(List.class);
		assertEquals(1, ids.size());
		assertEquals(10, ids.get(0).intValue());

		// get "sum" for transaction 10
		response = client
		.path("/transactionservice/sum/10")
		.request()
		.get();

		assertEquals(200, response.getStatus());
		AggregatorService.Sum sum = response.readEntity(AggregatorService.Sum.class);
		assertEquals(15000, sum.getSum().intValue());

		// get "sum" for transaction 11
		response = client
		.path("/transactionservice/sum/11")
		.request()
		.get();

		assertEquals(200, response.getStatus());
		sum = response.readEntity(AggregatorService.Sum.class);
		assertEquals(10000, sum.getSum().intValue());
	}

}
