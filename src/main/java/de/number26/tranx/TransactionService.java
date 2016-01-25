package de.number26.tranx;

import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/transactionservice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionService {

	private final Map<Long, Transaction> transactions;

	public TransactionService(final Map<Long, Transaction> transactions) {
		this.transactions = transactions;
	}

	@Path("/transaction/{transaction_id}")
	@PUT
	public OperationResult createTransaction(
			@PathParam("transaction_id") final long transactionId, 
			@Valid final Transaction transaction) {
		transactions.put(transactionId, transaction);
		return OperationResult.OK;
	}
}
