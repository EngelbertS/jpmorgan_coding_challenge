import java.time.LocalDate;

public class Instruction {

	private String idEntity;
	private String idSettlementDate;
	private String entity;
	private String buySell;
	private double agreedFx;
	private String currency;
	private String instructionDate;
	private LocalDate settlementDate;
	private int units;
	private double pricePerUnit;
	private double amountOfTradeUSD;

	public void setIdEntity() {
		idEntity = buySell + "_" + entity;
	}

	public String getIdEntity() {
		return idEntity;
	}

	public String getIdSettlementDate() {
		return idSettlementDate;
	}

	public void setIdSettlementDate() {
		this.idSettlementDate = buySell +"_" +settlementDate;
	}

	public double getAmountOfTradeUSD() {
		return amountOfTradeUSD;
	}

	public void setAmountOfTradeUSD() {
		this.amountOfTradeUSD = agreedFx * units * pricePerUnit;
	}

	public String getBuySell() {
		return buySell;
	}

	public void setBuySell(String buySell) {
		this.buySell = buySell;
	}

	public double getAgreedFx() {
		return agreedFx;
	}

	public void setAgreedFx(double agreedFx) {
		this.agreedFx = agreedFx;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getInstructionDate() {
		return this.instructionDate;
	}

	public void setInstructionDate(String instructionDate) {
		this.instructionDate = instructionDate;
	}

	public LocalDate getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(LocalDate settlementDate) {
		this.settlementDate = settlementDate;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

}
