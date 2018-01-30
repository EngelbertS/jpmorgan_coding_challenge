import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.*;

public class Trading {

	private Hashtable<String, String> hm;
	private List<String> lstInstructionsReceived;
	private List<Instruction> lstInstructionsObjects;

	public Trading() {
		hm = new Hashtable<String, String>();
		lstInstructionsReceived = new ArrayList<String>();
		lstInstructionsObjects = new ArrayList<Instruction>();
		createHashmap(hm);
	}

	public void execute() throws ParseException {

		addInstructions(lstInstructionsReceived);

		for (String instructionReceived : lstInstructionsReceived) {

			Instruction instruction = new Instruction();

			try {
				createInstructionObject(instruction, instructionReceived);
				lstInstructionsObjects.add(instruction);
			} catch (Exception e) {
				System.out.println("Instruction format error");
			}
		}

		printReport();
	}

	public void createInstructionObject(Instruction instruction, String instructionReceived) {
		String[] parts = instructionReceived.split(";");

		instruction.setEntity(parts[0]);
		instruction.setBuySell(parts[1]);
		instruction.setAgreedFx(Double.parseDouble(parts[2]));
		instruction.setCurrency(parts[3]);
		instruction.setInstructionDate(parts[4]);
		LocalDate settlementDate = transformToLocalDate(parts[5]);
		if (settlementDate == null)
			throw new NumberFormatException();
		instruction.setUnits(Integer.parseInt(parts[6]));
		instruction.setPricePerUnit(Double.parseDouble(parts[7]));
		instruction.setIdEntity();
		getSettlementDay(settlementDate, instruction.getCurrency());
		instruction.setSettlementDate(settlementDate);
		instruction.setAmountOfTradeUSD();
		instruction.setIdSettlementDate();
	}

	LocalDate transformToLocalDate(String settlementDateStr) {
		try {
			String[] settlementDateSplitArr = settlementDateStr.split(" ");
			int day = Integer.parseInt(settlementDateSplitArr[0]);
			String monthTmp = settlementDateSplitArr[1];
			int year = Integer.parseInt(settlementDateSplitArr[2]);
			int month = Integer.parseInt(hm.get(monthTmp));
			return LocalDate.of(year, month, day);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public void printReport() {
		createReportAmountInOut();
		createReportRanking();
	}

	public void createReportAmountInOut() {
		lstInstructionsObjects
				.sort(Comparator.comparing(Instruction::getBuySell).thenComparing(Instruction::getSettlementDate));

		Map<String, Double> mapDate = lstInstructionsObjects.stream().collect(Collectors.groupingBy(
				Instruction::getIdSettlementDate, Collectors.summingDouble(Instruction::getAmountOfTradeUSD)));

		List<Helper> lstSettlementDate = createHelperObject(mapDate);

		System.out.println("----------------------------Report----------------------------");
		System.out.println("");

		System.out.println("-----------Amount in USD settled incoming everyday-----------");

		for (Helper help : lstSettlementDate) {
			if (help.getbuySell().equals("S")) {
				System.out.println(help.getProperty() + ": " + Math.round(help.getSum()));
			}
		}

		System.out.println("-----------Amount in USD settled outgoing everyday-----------");

		for (Helper help : lstSettlementDate) {
			if (help.getbuySell().equals("B")) {
				System.out.println(help.getProperty() + ": " + Math.round(help.getSum()));
			}
		}
	}

	public void createReportRanking() {
		lstInstructionsObjects
				.sort(Comparator.comparing(Instruction::getBuySell).thenComparing(Instruction::getEntity));

		Map<String, Double> map = lstInstructionsObjects.stream().collect(Collectors
				.groupingBy(Instruction::getIdEntity, Collectors.summingDouble(Instruction::getAmountOfTradeUSD)));

		List<Helper> lstRanking = createHelperObject(map);

		lstRanking.sort(Comparator.comparing(Helper::getbuySell).thenComparing(Helper::getSum).reversed());

		System.out.println("");
		System.out.println("---------------------Ranking of Entities---------------------");

		System.out.println("----------Ranking outgoing----------");
		for (Helper help : lstRanking) {
			if (help.getbuySell().equals("B"))
				System.out.println(help.getProperty() + " " + Math.round(help.getSum()));
		}
		System.out.println("----------Ranking incoming----------");
		for (Helper help : lstRanking) {
			if (help.getbuySell().equals("S"))
				System.out.println(help.getProperty() + " " + Math.round(help.getSum()));
		}

	}

	public void addInstructions(List<String> instructions) {
		instructions.add("chr;B;0.22;USD;05 Jan 2016;06 Jan 2018;360;180.5");
		instructions.add("foo;B;0.50;SGP;01 Jan 2016;02 Jan 2016;200;100.25");
		instructions.add("bar;S;0.22;AED;05 Jan 2016;07 Jan 2016;450;150.5");
		instructions.add("bar;S;0.22;AED;05 Jan 2016;07 Jan 2016;450;150.5");
		instructions.add("eng;B;0.25;USD;05 Jan 2016;07 Jan 2016;350;135.5");
		instructions.add("chr;B;0.22;AED;05 Jan 2016;07 Jan 2016;560;188.5");
		instructions.add("fab;S;0.45;GBP;08 Jan 2016;07 Jan 2016;750;45.5");
		instructions.add("foo;B;0.50;SGP;01 Jan 2016;02 Jan 2016;300;100.25");
		instructions.add("bar;S;0.22;AED;05 Jan 2016;07 Jan 2016;560;150.5");
		instructions.add("chr;B;0.22;AED;05 Jan 2016;07 Jan 2016;360;180.5");
	}

	public void createHashmap(Hashtable<String, String> hm) {
		hm.put("Jan", "01");
		hm.put("Feb", "02");
		hm.put("Mar", "03");
		hm.put("Apr", "04");
		hm.put("Mai", "05");
		hm.put("Jun", "06");
		hm.put("Jul", "07");
		hm.put("Aug", "08");
		hm.put("Sep", "09");
		hm.put("Oct", "10");
		hm.put("Nov", "11");
		hm.put("Dec", "12");
	}

	public void getSettlementDay(LocalDate settlementDate, String currency) {
		// Getting the day of week for a given date
		DayOfWeek dayOfWeek = settlementDate.getDayOfWeek();

		// Using DayOfWeek to execute dependent business logic
		switch (dayOfWeek) {
		case FRIDAY:
			if (currency.equals("SAR") || currency.equals("AED")) {
				settlementDate = settlementDate.plusDays(2);
			}
			break;
		case SATURDAY:
			if (currency.equals("SAR") || currency.equals("AED")) {
				settlementDate = settlementDate.plusDays(1);
			} else {
				settlementDate = settlementDate.plusDays(2);
			}
			break;
		case SUNDAY:
			if (!(currency.equals("SAR") || currency.equals("AED"))) {
				settlementDate = settlementDate.plusDays(1);
			}
			break;
		default:
			break;
		}
	}

	List<Helper> createHelperObject(Map<String, Double> map) {
		List<Helper> list = new ArrayList<Helper>();

		for (String instr : map.keySet()) {
			Helper helper = new Helper();
			String[] parts = instr.split("_");
			helper.setbuySell(parts[0]);
			helper.setProperty(parts[1]);
			helper.setSum(map.get(instr));
			list.add(helper);
		}
		return list;
	}

	class Helper {
		String buySell;
		String property;
		double sum;

		public Helper() {

		}

		public String getbuySell() {
			return buySell;
		}

		public void setbuySell(String buySell) {
			this.buySell = buySell;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String sub) {
			this.property = sub;
		}

		public double getSum() {
			return sum;
		}

		public void setSum(double sum) {
			this.sum = sum;
		}
	}
}
