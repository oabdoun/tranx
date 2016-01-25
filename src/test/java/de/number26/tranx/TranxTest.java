package de.number26.tranx;

import java.math.BigDecimal;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import io.dropwizard.testing.*;
import io.dropwizard.client.*;
import io.dropwizard.testing.junit.*;
import org.junit.ClassRule;
import org.junit.Test;
import static org.junit.Assert.*;

public class TranxTest {

	@ClassRule
	public static final DropwizardAppRule<TranxConfiguration> RULE =
		new DropwizardAppRule<TranxConfiguration>(TranxServer.class, ResourceHelpers.resourceFilePath("tranx.yaml"));

	@Test
	public void createTransactionReturnsOk() {
		Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

		Response response = client
		.target(String.format("http://localhost:%d/transactionservice/transaction/10", RULE.getLocalPort()))
		.request()
		.put(Entity.json(new Transaction("foo", new BigDecimal(12), 0l)));

		assertEquals(200, response.getStatus());
		assertEquals(OperationResult.OK, response.readEntity(OperationResult.class));
	}
}
