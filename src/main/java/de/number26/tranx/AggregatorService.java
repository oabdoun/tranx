package de.number26.tranx;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.fasterxml.jackson.annotation.*;

@Path("/transactionservice/sum")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AggregatorService {

	private final Map<Long, Transaction> transactions;

	public AggregatorService(final Map<Long, Transaction> transactions) {
		this.transactions = transactions;
	}

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

	public static class Sum {
		private final BigDecimal sum;

		@JsonCreator
		public Sum(@JsonProperty("sum") final BigDecimal sum) { this.sum = sum; }
		public BigDecimal getSum() { return this.sum; }
	}
}
