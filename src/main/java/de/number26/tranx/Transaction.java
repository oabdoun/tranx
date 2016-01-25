package de.number26.tranx;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class Transaction {
	@NotNull @NotEmpty private final String type;
	@NotNull private final BigDecimal amount;
	private final long parentId;

	@JsonCreator
	public Transaction(
			@JsonProperty("type") final String type, 
			@JsonProperty("amount") final BigDecimal amount, 
			@JsonProperty("parent_id") final long parentId) {
		this.type = type;
		this.amount = amount;
		this.parentId = parentId;
	}

	public String getType() { return this.type; }
	public BigDecimal getAmount() { return this.amount; }
	public long getParentId() { return this.parentId; }
}
