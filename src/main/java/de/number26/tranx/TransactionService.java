package de.number26.tranx;

import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.fasterxml.jackson.annotation.*;

@Path("/transactionservice/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionService {

	private final Map<Long, Transaction> transactions;

	public TransactionService(final Map<Long, Transaction> transactions) {
		this.transactions = transactions;
	}

	@Path("/{transaction_id}")
	@PUT
	public OperationResult setTransaction(
			@PathParam("transaction_id") final long transactionId, 
			@Valid final Transaction transaction) {
		transactions.put(transactionId, transaction);
		return OperationResult.OK;
	}

	@Path("/{transaction_id}")
	@GET
	public Transaction getTransaction(
			@PathParam("transaction_id") final long transactionId) {
		if (!transactions.containsKey(transactionId)) {
			throw new WebApplicationException(
				Response.status(Response.Status.NOT_FOUND)
				.type(MediaType.APPLICATION_JSON)
				.build());
		}
		return transactions.get(transactionId);
	}

	public final static class OperationResult {
		private final String status;
		
		@JsonCreator
		public OperationResult(@JsonProperty("status") String status) { this.status = status; }
		
		public String getStatus() { return this.status; }

		@Override public int hashCode() { return status.hashCode(); }
		
		@Override public boolean equals(Object o) {
			return (o != null)
						&& OperationResult.class.isAssignableFrom(o.getClass()) 
						&& this.status.equals(((OperationResult)o).status);
		}

		public static final OperationResult OK = new OperationResult("ok");
		public static final OperationResult ERROR = new OperationResult("error");
	}
}
