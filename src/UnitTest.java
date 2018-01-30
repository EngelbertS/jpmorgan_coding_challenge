import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnitTest {
	
	private Trading testObject;
	
	@Before
	public void setUp(){
		
		testObject = new Trading();	
	}
		
	@Test
	public void testSettlementDate() {
		
		LocalDate settlementDate =  LocalDate.of(2018, 01, 06);
		
		testObject.getSettlementDay(settlementDate, "USD");
		
		assertEquals(LocalDate.of(2018, 1, 8), settlementDate);
		
		settlementDate =  LocalDate.of(2018, 01, 12);
		
		testObject.getSettlementDay(settlementDate, "AED");
		
		assertEquals(LocalDate.of(2018, 1, 14), settlementDate);

		settlementDate =  LocalDate.of(2018, 01, 20);
		
		testObject.getSettlementDay(settlementDate, "SAR");
		
		assertEquals(LocalDate.of(2018, 1, 21), settlementDate);	
	}
	
	@Test
	public void testtransformToLocalDate()
	{
		assertEquals(LocalDate.of(2018, 1, 6),testObject.transformToLocalDate("06 Jan 2018"));	
	}
	
	@Test
	public void testtransformToLocalDateFail()
	{
		assertEquals(null,testObject.transformToLocalDate("06 Jan Name"));	
	}
	
	@Test
	public void testCreateHelperObject()
	{
		Map<String, Double> map = new HashMap<String, Double>();
		map.put("S_foo",25.0);
		List<Trading.Helper> list = testObject.createHelperObject(map);
		Trading.Helper helpObject = testObject.new Helper();
		helpObject.setbuySell("S");
		helpObject.setProperty("foo");
		helpObject.setSum(25.0);	
		Assert.assertEquals(helpObject, list.get(0));
	}
	
	@Test
	public void testCalculateTotalAmountOfTrade() {
		
		Instruction instruction = new Instruction();
		instruction.setAgreedFx(0.4);
		instruction.setPricePerUnit(4.2);
		instruction.setUnits(100);
		instruction.setAmountOfTradeUSD();
		assertEquals(168, instruction.getAmountOfTradeUSD(),.1);	
	}
	
	@After
	public void tear(){
		
	}
}
