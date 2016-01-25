package de.number26.tranx;

import java.util.*;
import java.util.stream.*;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/transactionservice/type")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FinderService {

	private final Map<Long, Transaction> transactions;

	public FinderService(final Map<Long, Transaction> transactions) {
		this.transactions = transactions;
	}

	@Path("/{type}")
	@GET
	public List<Long> findByType(@PathParam("type") final String type) {
		return transactions.entrySet().stream()
		.filter( e -> e.getValue().getType().equals(type) )
		.map(e -> e.getKey() )
		.collect(Collectors.toList());
	}

}
