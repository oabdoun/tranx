package de.number26.tranx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.codahale.metrics.servlets.PingServlet;

public class TranxServer extends Application<TranxConfiguration> {
	
	public static void main(String[] args) throws Exception {
		new TranxServer().run(args);
	}

	private final Map<Long, Transaction> transactions = new ConcurrentHashMap<Long, Transaction>();

	@Override
	public String getName() {
		return "tranx";
	}

	@Override
	public void initialize(Bootstrap<TranxConfiguration> bootstrap) {
	}

	@Override
	public void run(TranxConfiguration configuration, Environment environment) {
		// heartbeat
		environment.servlets()
		.addServlet("ping", new PingServlet())
		.addMapping("/ping");

		// transaction service
		environment.jersey().register(new TransactionService(transactions));
		environment.jersey().register(new FinderService(transactions));
		environment.jersey().register(new AggregatorService(transactions));
	}

}
