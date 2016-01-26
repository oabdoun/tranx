package de.number26.tranx;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.fasterxml.jackson.annotation.*;

/**
 * Service to compute transaction aggregation on
 * the transaction repository.
 */
@Path("/transactionservice/sum")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AggregatorService {

	private final Map<Long, Transaction> transactions;

	public AggregatorService(final Map<Long, Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * Sum amounts on the parent-children transactions chain.
	 */
	@Path("/{parent_id}")
	@GET
	public Sum sumByParentId(@PathParam("parent_id") final long parentId) {
		return new Sum(
			transactions.entrySet().stream()
			.filter( e -> (e.getKey() == parentId) || (e.getValue().getParentId() == parentId) )
			.map( e -> e.getValue().getAmount() )
			.reduce( (a, b) -> a.add(b) )
			.orElse(BigDecimal.ZERO)
		);
	}

	/**
	 * Value object to expose the sum aggregation result.
	 */
	public static class Sum {
		private final BigDecimal sum;

		@JsonCreator
		public Sum(@JsonProperty("sum") final BigDecimal sum) { this.sum = sum; }
		public BigDecimal getSum() { return this.sum; }
	}
}
