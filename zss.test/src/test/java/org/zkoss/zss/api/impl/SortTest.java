package org.zkoss.zss.api.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.poi.ss.usermodel.ZssContext;
import org.zkoss.zss.Setup;
import org.zkoss.zss.Util;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.Sheet;

public class SortTest {
	
	@BeforeClass
	public static void setUpLibrary() throws Exception {
		Setup.touch();
	}
	
	@Before
	public void startUp() throws Exception {
		Setup.pushZssContextLocale(Locale.TAIWAN);
	}
	
	@After
	public void tearDown() throws Exception {
		Setup.popZssContextLocale();
	}
	
	
	@Test
	public void testSortWithHeaderByID2003() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xls");
		testSortWithHeaderByID(book);
	}
	
	@Test
	public void testSortWithHeaderByID2007() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xlsx");
		testSortWithHeaderByID(book);
	}
	
	@Test
	public void testSortWithHeaderByBirthYr_ZipCode_ID_2003() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xls");
		testSortWithHeaderByBirthYr_ZipCode_ID(book);
	}
	
	@Test
	public void testSortWithHeaderByBirthYr_ZipCode_ID_2007() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xlsx");
		testSortWithHeaderByBirthYr_ZipCode_ID(book);
	}
	
	@Test
	public void testSimpleSortWithNumberAndCharacterAndFormula2003() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xls");
		testSimpleSortWithNumberAndCharacterAndFormula(book);
	}
	
	@Test
	public void testSimpleSortWithNumberAndCharacterAndFormula2007() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xlsx");
		testSimpleSortWithNumberAndCharacterAndFormula(book);
	}
	
	@Test
	public void testSortWithHeader2003() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xls");
		testSortWithHeader(book);
	}
	
	@Test
	public void testSortWithHeader2007() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xlsx");
		testSortWithHeader(book);
	}
	
	@Test
	public void testSortByRowWithHeader2003() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xls");
		testSortByRowWithHeader(book);
	}
	
	@Test
	public void testSortByRowWithHeader2007() throws IOException {
		Book book = Util.loadBook(this,"book/excelsortsample.xlsx");
		testSortByRowWithHeader(book);
	}
	
	private void testSortByRowWithHeader(Book workbook) throws IOException {
		Setup.pushZssContextLocale(Locale.US);
		try{
		Sheet sheet = workbook.getSheet("MonthData");
		Ranges.range(sheet, "A1:G5").sort(false, true, false, true, null);
		
		// Header
		assertEquals("Region", Ranges.range(sheet, "A1").getCellFormatText());
		assertEquals("East", Ranges.range(sheet, "A2").getCellFormatText());
		assertEquals("West", Ranges.range(sheet, "A3").getCellFormatText());
		assertEquals("South", Ranges.range(sheet, "A4").getCellFormatText());
		assertEquals("North", Ranges.range(sheet, "A5").getCellFormatText());
		
		assertEquals("Apr", Ranges.range(sheet, "B1").getCellFormatText());
		assertEquals("1459", Ranges.range(sheet, "B2").getCellFormatText());
		assertEquals("964", Ranges.range(sheet, "B3").getCellFormatText());
		assertEquals("1482", Ranges.range(sheet, "B4").getCellFormatText());
		assertEquals("1011", Ranges.range(sheet, "B5").getCellFormatText());
		
		assertEquals("Feb", Ranges.range(sheet, "C1").getCellFormatText());
		assertEquals("563", Ranges.range(sheet, "C2").getCellFormatText());
		assertEquals("854", Ranges.range(sheet, "C3").getCellFormatText());
		assertEquals("680", Ranges.range(sheet, "C4").getCellFormatText());
		assertEquals("1082", Ranges.range(sheet, "C5").getCellFormatText());
		
		assertEquals("Jan", Ranges.range(sheet, "D1").getCellFormatText());
		assertEquals("876", Ranges.range(sheet, "D2").getCellFormatText());
		assertEquals("1028", Ranges.range(sheet, "D3").getCellFormatText());
		assertEquals("604", Ranges.range(sheet, "D4").getCellFormatText());
		assertEquals("1013", Ranges.range(sheet, "D5").getCellFormatText());
		
		assertEquals("Jun", Ranges.range(sheet, "E1").getCellFormatText());
		assertEquals("1346", Ranges.range(sheet, "E2").getCellFormatText());
		assertEquals("673", Ranges.range(sheet, "E3").getCellFormatText());
		assertEquals("874", Ranges.range(sheet, "E4").getCellFormatText());
		assertEquals("562", Ranges.range(sheet, "E5").getCellFormatText());
		
		assertEquals("Mar", Ranges.range(sheet, "F1").getCellFormatText());
		assertEquals("735", Ranges.range(sheet, "F2").getCellFormatText());
		assertEquals("1033", Ranges.range(sheet, "F3").getCellFormatText());
		assertEquals("912", Ranges.range(sheet, "F4").getCellFormatText());
		assertEquals("1037", Ranges.range(sheet, "F5").getCellFormatText());
		
		assertEquals("May", Ranges.range(sheet, "G1").getCellFormatText());
		assertEquals("915", Ranges.range(sheet, "G2").getCellFormatText());
		assertEquals("1372", Ranges.range(sheet, "G3").getCellFormatText());
		assertEquals("747", Ranges.range(sheet, "G4").getCellFormatText());
		assertEquals("769", Ranges.range(sheet, "G5").getCellFormatText());
		}finally{
			Setup.popZssContextLocale();
		}
		
	}
	
	private void testSortWithHeader(Book workbook) throws IOException {
		
		Setup.pushZssContextLocale(Locale.US);
		try{
		Sheet sheet = workbook.getSheet("WeekdaySort");
		Ranges.range(sheet, "A1:D20").sort(true, true, false, false, null);
		
		// Header
		assertEquals("Date", Ranges.range(sheet, "A1").getCellFormatText());
		assertEquals("Day", Ranges.range(sheet, "B1").getCellFormatText());
		assertEquals("UnitsSold", Ranges.range(sheet, "C1").getCellFormatText());
		assertEquals("SalesAmt", Ranges.range(sheet, "D1").getCellFormatText().trim());
		
		assertEquals("25-Dec", Ranges.range(sheet, "A2").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B2").getCellFormatText());
		assertEquals("235", Ranges.range(sheet, "C2").getCellFormatText());
		assertEquals("$421", Ranges.range(sheet, "D2").getCellFormatText().trim());
		
		assertEquals("9-Dec", Ranges.range(sheet, "A3").getCellFormatText());
		assertEquals("Fri", Ranges.range(sheet, "B3").getCellFormatText());
		assertEquals("179", Ranges.range(sheet, "C3").getCellFormatText());
		assertEquals("$320", Ranges.range(sheet, "D3").getCellFormatText().trim());
		
		assertEquals("7-Dec", Ranges.range(sheet, "A4").getCellFormatText());
		assertEquals("Wed", Ranges.range(sheet, "B4").getCellFormatText());
		assertEquals("617", Ranges.range(sheet, "C4").getCellFormatText());
		assertEquals("$1,104", Ranges.range(sheet, "D4").getCellFormatText().trim());
		
		assertEquals("5-Dec", Ranges.range(sheet, "A5").getCellFormatText());
		assertEquals("Mon", Ranges.range(sheet, "B5").getCellFormatText());
		assertEquals("591", Ranges.range(sheet, "C5").getCellFormatText());
		assertEquals("$1,058", Ranges.range(sheet, "D5").getCellFormatText().trim());
		
		assertEquals("31-Aug", Ranges.range(sheet, "A6").getCellFormatText());
		assertEquals("Wed", Ranges.range(sheet, "B6").getCellFormatText());
		assertEquals("140", Ranges.range(sheet, "C6").getCellFormatText());
		assertEquals("$251", Ranges.range(sheet, "D6").getCellFormatText().trim());
		
		assertEquals("7-Aug", Ranges.range(sheet, "A7").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B7").getCellFormatText());
		assertEquals("591", Ranges.range(sheet, "C7").getCellFormatText());
		assertEquals("$1,058", Ranges.range(sheet, "D7").getCellFormatText().trim());
		
		assertEquals("29-Jun", Ranges.range(sheet, "A8").getCellFormatText());
		assertEquals("Wed", Ranges.range(sheet, "B8").getCellFormatText());
		assertEquals("156", Ranges.range(sheet, "C8").getCellFormatText());
		assertEquals("$279", Ranges.range(sheet, "D8").getCellFormatText().trim());
		
		assertEquals("26-Jun", Ranges.range(sheet, "A9").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B9").getCellFormatText());
		assertEquals("253", Ranges.range(sheet, "C9").getCellFormatText());
		assertEquals("$453", Ranges.range(sheet, "D9").getCellFormatText().trim());
		
		assertEquals("29-May", Ranges.range(sheet, "A10").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B10").getCellFormatText());
		assertEquals("600", Ranges.range(sheet, "C10").getCellFormatText());
		assertEquals("$1,074", Ranges.range(sheet, "D10").getCellFormatText().trim());
		
		assertEquals("27-May", Ranges.range(sheet, "A11").getCellFormatText());
		assertEquals("Fri", Ranges.range(sheet, "B11").getCellFormatText());
		assertEquals("244", Ranges.range(sheet, "C11").getCellFormatText());
		assertEquals("$437", Ranges.range(sheet, "D11").getCellFormatText().trim());
		}finally{
			Setup.popZssContextLocale();
		}
	}
	
	private void testSimpleSortWithNumberAndCharacterAndFormula(Book workbook) throws IOException {
		
		Setup.pushZssContextLocale(Locale.US);
		try{
		Sheet sheet = workbook.getSheet("WeekdaySort");
		Ranges.range(sheet, "A2:D20").sort(true);
		
		assertEquals("25-Dec", Ranges.range(sheet, "A2").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B2").getCellFormatText());
		assertEquals("235", Ranges.range(sheet, "C2").getCellFormatText());
		assertEquals("$421", Ranges.range(sheet, "D2").getCellFormatText().trim());
		
		assertEquals("9-Dec", Ranges.range(sheet, "A3").getCellFormatText());
		assertEquals("Fri", Ranges.range(sheet, "B3").getCellFormatText());
		assertEquals("179", Ranges.range(sheet, "C3").getCellFormatText());
		assertEquals("$320", Ranges.range(sheet, "D3").getCellFormatText().trim());
		
		assertEquals("7-Dec", Ranges.range(sheet, "A4").getCellFormatText());
		assertEquals("Wed", Ranges.range(sheet, "B4").getCellFormatText());
		assertEquals("617", Ranges.range(sheet, "C4").getCellFormatText());
		assertEquals("$1,104", Ranges.range(sheet, "D4").getCellFormatText().trim());
		
		assertEquals("5-Dec", Ranges.range(sheet, "A5").getCellFormatText());
		assertEquals("Mon", Ranges.range(sheet, "B5").getCellFormatText());
		assertEquals("591", Ranges.range(sheet, "C5").getCellFormatText());
		assertEquals("$1,058", Ranges.range(sheet, "D5").getCellFormatText().trim());
		
		assertEquals("31-Aug", Ranges.range(sheet, "A6").getCellFormatText());
		assertEquals("Wed", Ranges.range(sheet, "B6").getCellFormatText());
		assertEquals("140", Ranges.range(sheet, "C6").getCellFormatText());
		assertEquals("$251", Ranges.range(sheet, "D6").getCellFormatText().trim());
		
		assertEquals("7-Aug", Ranges.range(sheet, "A7").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B7").getCellFormatText());
		assertEquals("591", Ranges.range(sheet, "C7").getCellFormatText());
		assertEquals("$1,058", Ranges.range(sheet, "D7").getCellFormatText().trim());
		
		assertEquals("29-Jun", Ranges.range(sheet, "A8").getCellFormatText());
		assertEquals("Wed", Ranges.range(sheet, "B8").getCellFormatText());
		assertEquals("156", Ranges.range(sheet, "C8").getCellFormatText());
		assertEquals("$279", Ranges.range(sheet, "D8").getCellFormatText().trim());
		
		assertEquals("26-Jun", Ranges.range(sheet, "A9").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B9").getCellFormatText());
		assertEquals("253", Ranges.range(sheet, "C9").getCellFormatText());
		assertEquals("$453", Ranges.range(sheet, "D9").getCellFormatText().trim());
		
		assertEquals("29-May", Ranges.range(sheet, "A10").getCellFormatText());
		assertEquals("Sun", Ranges.range(sheet, "B10").getCellFormatText());
		assertEquals("600", Ranges.range(sheet, "C10").getCellFormatText());
		assertEquals("$1,074", Ranges.range(sheet, "D10").getCellFormatText().trim());
		
		assertEquals("27-May", Ranges.range(sheet, "A11").getCellFormatText());
		assertEquals("Fri", Ranges.range(sheet, "B11").getCellFormatText());
		assertEquals("244", Ranges.range(sheet, "C11").getCellFormatText());
		assertEquals("$437", Ranges.range(sheet, "D11").getCellFormatText().trim());
		}finally{
			Setup.popZssContextLocale();
		}
	}
	
	private void testSortWithHeaderByBirthYr_ZipCode_ID(Book workbook) throws IOException {
		Sheet sheet = workbook.getSheet("SampleData");
		
		// Sort By BirthYr, ZipCode, ID
		Ranges.range(sheet, "A1:H11").sort(Ranges.range(sheet, "D1:D11"), true, null, Ranges.range(sheet, "H1:H11"), true, null, Ranges.range(sheet, "A1:A11"), true, null, true, false, false);
		
		// Header
		assertEquals("ID", Ranges.range(sheet, "A1").getCellFormatText());
		assertEquals("Surname", Ranges.range(sheet, "B1").getCellFormatText());
		assertEquals("Gender", Ranges.range(sheet, "C1").getCellFormatText());
		assertEquals("BirthYr", Ranges.range(sheet, "D1").getCellFormatText());
		assertEquals("City", Ranges.range(sheet, "E1").getCellFormatText());
		assertEquals("State", Ranges.range(sheet, "G1").getCellFormatText());
		assertEquals("ZipCode", Ranges.range(sheet, "H1").getCellFormatText());
		
		// Data
		assertEquals("5", Ranges.range(sheet, "A2").getCellFormatText());
		assertEquals("6", Ranges.range(sheet, "A3").getCellFormatText());
		assertEquals("3", Ranges.range(sheet, "A4").getCellFormatText());
		assertEquals("9", Ranges.range(sheet, "A5").getCellFormatText());
		assertEquals("4", Ranges.range(sheet, "A6").getCellFormatText());
		assertEquals("7", Ranges.range(sheet, "A7").getCellFormatText());
		assertEquals("10", Ranges.range(sheet, "A8").getCellFormatText());
		assertEquals("8", Ranges.range(sheet, "A9").getCellFormatText());
		assertEquals("2", Ranges.range(sheet, "A10").getCellFormatText());
		assertEquals("1", Ranges.range(sheet, "A11").getCellFormatText());
		
		// Name
		assertEquals("Arnold", Ranges.range(sheet, "B2").getCellFormatText());
		assertEquals("Borkowski", Ranges.range(sheet, "B3").getCellFormatText());
		assertEquals("Mcafee", Ranges.range(sheet, "B4").getCellFormatText());
		assertEquals("Foster", Ranges.range(sheet, "B5").getCellFormatText());
		assertEquals("Williams", Ranges.range(sheet, "B6").getCellFormatText());
		assertEquals("Black", Ranges.range(sheet, "B7").getCellFormatText());
		assertEquals("Cox", Ranges.range(sheet, "B8").getCellFormatText());
		assertEquals("Gentile", Ranges.range(sheet, "B9").getCellFormatText());
		assertEquals("Jones", Ranges.range(sheet, "B10").getCellFormatText());
		assertEquals("Krause", Ranges.range(sheet, "B11").getCellFormatText());
		
		// Gender
		assertEquals("female", Ranges.range(sheet, "C2").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C3").getCellFormatText());
		assertEquals("female", Ranges.range(sheet, "C4").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C5").getCellFormatText());
		assertEquals("female", Ranges.range(sheet, "C6").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C7").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C8").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C9").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C10").getCellFormatText());
		assertEquals("female", Ranges.range(sheet, "C11").getCellFormatText());
		
		// BirthYear
		assertEquals("1991", Ranges.range(sheet, "D2").getCellFormatText());
		assertEquals("1985", Ranges.range(sheet, "D3").getCellFormatText());
		assertEquals("1982", Ranges.range(sheet, "D4").getCellFormatText());
		assertEquals("1980", Ranges.range(sheet, "D5").getCellFormatText());
		assertEquals("1975", Ranges.range(sheet, "D6").getCellFormatText());
		assertEquals("1975", Ranges.range(sheet, "D7").getCellFormatText());
		assertEquals("1967", Ranges.range(sheet, "D8").getCellFormatText());
		assertEquals("1959", Ranges.range(sheet, "D9").getCellFormatText());
		assertEquals("1945", Ranges.range(sheet, "D10").getCellFormatText());
		assertEquals("1943", Ranges.range(sheet, "D11").getCellFormatText());
		
		// City
		assertEquals("Troy", Ranges.range(sheet, "E2").getCellFormatText());
		assertEquals("Grand Rapids", Ranges.range(sheet, "E3").getCellFormatText());
		assertEquals("Jupiter", Ranges.range(sheet, "E4").getCellFormatText());
		assertEquals("Austin", Ranges.range(sheet, "E5").getCellFormatText());
		assertEquals("San Diego", Ranges.range(sheet, "E6").getCellFormatText());
		assertEquals("Bedford", Ranges.range(sheet, "E7").getCellFormatText());
		assertEquals("New Castle", Ranges.range(sheet, "E8").getCellFormatText());
		assertEquals("Oakwood", Ranges.range(sheet, "E9").getCellFormatText());
		assertEquals("Bassett", Ranges.range(sheet, "E10").getCellFormatText());
		assertEquals("West Enfield", Ranges.range(sheet, "E11").getCellFormatText());
		
		// State
		assertEquals("MI", Ranges.range(sheet, "G2").getCellFormatText());
		assertEquals("MI", Ranges.range(sheet, "G3").getCellFormatText());
		assertEquals("FL", Ranges.range(sheet, "G4").getCellFormatText());
		assertEquals("TX", Ranges.range(sheet, "G5").getCellFormatText());
		assertEquals("CA", Ranges.range(sheet, "G6").getCellFormatText());
		assertEquals("MA", Ranges.range(sheet, "G7").getCellFormatText());
		assertEquals("CO", Ranges.range(sheet, "G8").getCellFormatText());
		assertEquals("VA", Ranges.range(sheet, "G9").getCellFormatText());
		assertEquals("VA", Ranges.range(sheet, "G10").getCellFormatText());
		assertEquals("ME", Ranges.range(sheet, "G11").getCellFormatText());
		
		// ZipCode
		assertEquals("48083", Ranges.range(sheet, "H2").getCellFormatText());
		assertEquals("49503", Ranges.range(sheet, "H3").getCellFormatText());
		assertEquals("33478", Ranges.range(sheet, "H4").getCellFormatText());
		assertEquals("78701", Ranges.range(sheet, "H5").getCellFormatText());
		assertEquals("92103", Ranges.range(sheet, "H6").getCellFormatText());
		assertEquals("01730", Ranges.range(sheet, "H7").getCellFormatText());
		assertEquals("81647", Ranges.range(sheet, "H8").getCellFormatText());
		assertEquals("24631", Ranges.range(sheet, "H9").getCellFormatText());
		assertEquals("24055", Ranges.range(sheet, "H10").getCellFormatText());
		assertEquals("04493", Ranges.range(sheet, "H11").getCellFormatText());
		
	}
	
	private void testSortWithHeaderByID(Book workbook) throws IOException {
		
		Sheet sheet = workbook.getSheet("SampleData");
		
		// Sort By ID
		Ranges.range(sheet, "A1:H11").sort(Ranges.range(sheet, "A1:A11"), true, null, null, false, null, null, false, null, true, false, false);
		
		// Header
		assertEquals("ID", Ranges.range(sheet, "A1").getCellFormatText());
		assertEquals("Surname", Ranges.range(sheet, "B1").getCellFormatText());
		assertEquals("Gender", Ranges.range(sheet, "C1").getCellFormatText());
		assertEquals("BirthYr", Ranges.range(sheet, "D1").getCellFormatText());
		assertEquals("City", Ranges.range(sheet, "E1").getCellFormatText());
		assertEquals("State", Ranges.range(sheet, "G1").getCellFormatText());
		assertEquals("ZipCode", Ranges.range(sheet, "H1").getCellFormatText());
		
		// Data
		
		// ID
		for(int i = 10; i > 0; i--) {
			assertEquals(String.valueOf(i), Ranges.range(sheet, 11 - i, 0).getCellFormatText());
		}
		
		// Name
		assertEquals("Cox", Ranges.range(sheet, "B2").getCellFormatText());
		assertEquals("Foster", Ranges.range(sheet, "B3").getCellFormatText());
		assertEquals("Gentile", Ranges.range(sheet, "B4").getCellFormatText());
		assertEquals("Black", Ranges.range(sheet, "B5").getCellFormatText());
		assertEquals("Borkowski", Ranges.range(sheet, "B6").getCellFormatText());
		assertEquals("Arnold", Ranges.range(sheet, "B7").getCellFormatText());
		assertEquals("Williams", Ranges.range(sheet, "B8").getCellFormatText());
		assertEquals("Mcafee", Ranges.range(sheet, "B9").getCellFormatText());
		assertEquals("Jones", Ranges.range(sheet, "B10").getCellFormatText());
		assertEquals("Krause", Ranges.range(sheet, "B11").getCellFormatText());
		
		// Gender
		assertEquals("male", Ranges.range(sheet, "C2").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C3").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C4").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C5").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C6").getCellFormatText());
		assertEquals("female", Ranges.range(sheet, "C7").getCellFormatText());
		assertEquals("female", Ranges.range(sheet, "C8").getCellFormatText());
		assertEquals("female", Ranges.range(sheet, "C9").getCellFormatText());
		assertEquals("male", Ranges.range(sheet, "C10").getCellFormatText());
		assertEquals("female", Ranges.range(sheet, "C11").getCellFormatText());
		
		// BirthYear
		assertEquals("1967", Ranges.range(sheet, "D2").getCellFormatText());
		assertEquals("1980", Ranges.range(sheet, "D3").getCellFormatText());
		assertEquals("1959", Ranges.range(sheet, "D4").getCellFormatText());
		assertEquals("1975", Ranges.range(sheet, "D5").getCellFormatText());
		assertEquals("1985", Ranges.range(sheet, "D6").getCellFormatText());
		assertEquals("1991", Ranges.range(sheet, "D7").getCellFormatText());
		assertEquals("1975", Ranges.range(sheet, "D8").getCellFormatText());
		assertEquals("1982", Ranges.range(sheet, "D9").getCellFormatText());
		assertEquals("1945", Ranges.range(sheet, "D10").getCellFormatText());
		assertEquals("1943", Ranges.range(sheet, "D11").getCellFormatText());
		
		// City
		assertEquals("New Castle", Ranges.range(sheet, "E2").getCellFormatText());
		assertEquals("Austin", Ranges.range(sheet, "E3").getCellFormatText());
		assertEquals("Oakwood", Ranges.range(sheet, "E4").getCellFormatText());
		assertEquals("Bedford", Ranges.range(sheet, "E5").getCellFormatText());
		assertEquals("Grand Rapids", Ranges.range(sheet, "E6").getCellFormatText());
		assertEquals("Troy", Ranges.range(sheet, "E7").getCellFormatText());
		assertEquals("San Diego", Ranges.range(sheet, "E8").getCellFormatText());
		assertEquals("Jupiter", Ranges.range(sheet, "E9").getCellFormatText());
		assertEquals("Bassett", Ranges.range(sheet, "E10").getCellFormatText());
		assertEquals("West Enfield", Ranges.range(sheet, "E11").getCellFormatText());
		
		// State
		assertEquals("CO", Ranges.range(sheet, "G2").getCellFormatText());
		assertEquals("TX", Ranges.range(sheet, "G3").getCellFormatText());
		assertEquals("VA", Ranges.range(sheet, "G4").getCellFormatText());
		assertEquals("MA", Ranges.range(sheet, "G5").getCellFormatText());
		assertEquals("MI", Ranges.range(sheet, "G6").getCellFormatText());
		assertEquals("MI", Ranges.range(sheet, "G7").getCellFormatText());
		assertEquals("CA", Ranges.range(sheet, "G8").getCellFormatText());
		assertEquals("FL", Ranges.range(sheet, "G9").getCellFormatText());
		assertEquals("VA", Ranges.range(sheet, "G10").getCellFormatText());
		assertEquals("ME", Ranges.range(sheet, "G11").getCellFormatText());
		
		// ZipCode
		assertEquals("81647", Ranges.range(sheet, "H2").getCellFormatText());
		assertEquals("78701", Ranges.range(sheet, "H3").getCellFormatText());
		assertEquals("24631", Ranges.range(sheet, "H4").getCellFormatText());
		assertEquals("01730", Ranges.range(sheet, "H5").getCellFormatText());
		assertEquals("49503", Ranges.range(sheet, "H6").getCellFormatText());
		assertEquals("48083", Ranges.range(sheet, "H7").getCellFormatText());
		assertEquals("92103", Ranges.range(sheet, "H8").getCellFormatText());
		assertEquals("33478", Ranges.range(sheet, "H9").getCellFormatText());
		assertEquals("24055", Ranges.range(sheet, "H10").getCellFormatText());
		assertEquals("04493", Ranges.range(sheet, "H11").getCellFormatText());
	}
	
	/**
	 * sort random number from 1:100
	 */
	@Test
	public void simpleSort() throws IOException {
		Book workbook = Util.loadBook(this,"book/blank.xlsx");
		Sheet sheet1 = workbook.getSheet("Sheet1");
		int[] rands = new int[100];
		for(int i = 0; i < 100; i++) {
			rands[i] = (int)Math.random() * 100 + 1;
			Ranges.range(sheet1, i, 0).setCellValue(rands[i]);
		}
		Ranges.range(sheet1, 0, 0, 99, 0).sort(false);
		Arrays.sort(rands);
		for(int i = 0; i < 100; i++) {
			assertEquals(rands[i], Ranges.range(sheet1, i, 0).getCellData().getDoubleValue(), 1E-8);
		}
	}

}
