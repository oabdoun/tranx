package de.number26.tranx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class OperationResult {
	private final String status;
	
	@JsonCreator
	public OperationResult(@JsonProperty("status") String status) { this.status = status; }
	
	public String getStatus() { return this.status; }

	@Override public int hashCode() { return status.hashCode(); }
	
	@Override public boolean equals(Object o) {
		return OperationResult.class.isAssignableFrom(o.getClass()) 
					&& this.status.equals(((OperationResult)o).status);
	}

	public static final OperationResult OK = new OperationResult("ok");
	public static final OperationResult ERROR = new OperationResult("error");
}
