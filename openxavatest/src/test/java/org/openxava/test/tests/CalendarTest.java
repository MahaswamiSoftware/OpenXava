package org.openxava.test.tests;

import java.text.*;
import java.time.*;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

public class CalendarTest extends WebDriverTestBase {

	private WebDriver driver;

	public void setUp() throws Exception {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
	}

	public void testNavigation() throws Exception {
		driver.get("http://localhost:8080/openxavatest/m/Invoice");
		wait(driver);
		try {
		    Alert alert = driver.switchTo().alert();
		    alert.accept();
		} catch (NoAlertPresentException e) {
		}
		moveToCalendarView();
		wait(driver);
		waitEvent(driver);
		prevOnCalendar();
		waitEvent(driver);
		createInvoiceEventPrevCurrentNextMonth();
		prevOnCalendar();
		waitEvent(driver);
		verifyPrevInvoiceEvent();
		
		driver.get("http://localhost:8080/openxavatest/m/InvoiceCalendar");
		wait(driver);
		moveToListView();
		wait(driver);
		setInvoiceCondition("InvoiceCalendar");
		wait(driver);
		waitEvent(driver);
		moveToCalendarView();
		wait(driver);
		waitEvent(driver);
		prevOnCalendar();
		waitEvent(driver);
		verifyConditionEvents("past", false);
		nextOnCalendar();
		waitEvent(driver);
		nextOnCalendar();
		waitEvent(driver);
		verifyConditionEvents("future", true);
		
		moveToListView();
		wait(driver);
		deteleEvents();
		
		// date properties with any name
		driver.get("http://localhost:8080/openxavatest/m/UserWithBirthday");
		wait(driver);
		try {
			WebElement buttonList = driver.findElement(By.id("ox_openxavatest_UserWithBirthday__Mode___list"));
			buttonList.click();
			wait(driver);
		} catch (NoSuchElementException e) {
		}
		moveToListView();
		wait(driver);
		moveToCalendarView();
		wait(driver);
		waitEvent(driver);
		moveToListView();
		wait(driver);
		
		//use first date property as event start time
		driver.get("http://localhost:8080/openxavatest/m/Event");
		wait(driver);
		moveToCalendarView();
		wait(driver);
		waitEvent(driver);
		List<Date> dates = setDates();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = dateFormat.format(dates.get(1));
		
		WebElement day = driver
				.findElement(By.xpath("//div[contains(@class,'fc-daygrid-day-frame') and ancestor::td[@data-date='"
						+ dateString + "']]"));
		day.click();
		wait(driver);
		List<WebElement> iconElements = driver.findElements(By.cssSelector("i.mdi.mdi-calendar"));
		if (!iconElements.isEmpty()) {
		    WebElement firstIconElement = iconElements.get(1);
		    firstIconElement.click();
		}
		Thread.sleep(500);
		
		List<WebElement> spanElements = driver.findElements(By.xpath("//div[@class='dayContainer']//span[@class='flatpickr-day ' and text()='2']"));
		if (!spanElements.isEmpty()) {
			System.out.println(spanElements.size());
		    WebElement spanElement = spanElements.get(1);
		    spanElement.click();
		}
		wait(driver);
		WebElement buttonSave = driver.findElement(By.id("ox_openxavatest_Event__CRUD___save"));
		buttonSave.click();
		wait(driver);
		WebElement buttonList = driver.findElement(By.id("ox_openxavatest_Event__Mode___list"));
		buttonList.click();
		wait(driver);
		waitEvent(driver);
		
		List<WebElement> events = driver.findElements(By.xpath("//div[contains(@class,'fc-daygrid-event-harness') and ancestor::td[@data-date='"
                + dateString + "']]"));
		assertTrue(!events.isEmpty());
		events.get(0).click();
		wait(driver);
		Thread.sleep(5000);
		WebElement element = driver.findElement(By.xpath("//a[@onclicke=\"javascript:openxava.executeAction('openxavatest', 'Event', 'Borra la entidad actual: �Est�s seguro?', false, 'CRUD.delete')\"]"));
		element.click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		wait(driver);
		WebElement buttonList2 = driver.findElement(By.id("ox_openxavatest_Event__Mode___list"));
		buttonList2.click();
		wait(driver);
	}

	public void tearDown() throws Exception {
		driver.quit();
	}

	private void waitEvent(WebDriver driver) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000));
		try {
			wait.until(ExpectedConditions.jsReturnsValue("return calendarEditor.requesting === false;"));
			Thread.sleep(500);
		} catch (Exception ex) {
		}
	}
	
	private void moveToCalendarView() throws InterruptedException {
		WebElement tabCalendar = driver.findElement(By.cssSelector(".mdi.mdi-calendar"));
		WebElement tabCalendarParent = tabCalendar.findElement(By.xpath(".."));
		String title = tabCalendarParent.getAttribute("class");
		if (!(title != null && title.equals("ox-selected-list-format"))) {
			tabCalendar.click();
		}
	}
	
	private void moveToListView() throws InterruptedException {
		WebElement tabList = driver.findElement(By.cssSelector(".mdi.mdi-table-large"));
		WebElement tabListParent = tabList.findElement(By.xpath(".."));
		String title = tabListParent.getAttribute("class");
		if (!(title != null && title.equals("ox-selected-list-format"))) {
			tabList.click();
		}	
	}

	private void nextOnCalendar() throws InterruptedException {
		WebElement next = driver.findElement(By.cssSelector(".fc-icon.fc-icon-chevron-right"));
		next.click();
	}

	private void prevOnCalendar() throws InterruptedException {
		WebElement prev = driver.findElement(By.cssSelector(".fc-icon.fc-icon-chevron-left"));
		prev.click();
	}

	private List<Date> setDates() {
		List<Date> dates = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date currentMonth = calendar.getTime();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date previousMonth = calendar.getTime();
		calendar.add(Calendar.MONTH, 2);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date nextMonth = calendar.getTime();
		dates.add(previousMonth);
		dates.add(currentMonth);
		dates.add(nextMonth);

		return dates;
	}

	private void createInvoiceEventPrevCurrentNextMonth() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> dates = setDates();
		for (int i = 0; i < dates.size(); i++) {
			if (i == 2) {
				nextOnCalendar();
				waitEvent(driver);
				wait(driver);
			}
			String dateString = dateFormat.format(dates.get(i));
			WebElement day = driver
					.findElement(By.xpath("//div[contains(@class,'fc-daygrid-day-frame') and ancestor::td[@data-date='"
							+ dateString + "']]"));
			day.click();
			wait(driver);
			createInvoice(i);
		}
	}
	
	private void createInvoice(int invoiceNUmber) throws Exception {
		WebElement inputInvoiceNumber = driver.findElement(By.id("ox_openxavatest_Invoice__number"));
		int invoiceNumber = (10 + invoiceNUmber);
		inputInvoiceNumber.sendKeys(String.valueOf(invoiceNumber));			
		WebElement inputCustomerNumber = driver.findElement(By.id("ox_openxavatest_Invoice__customer___number"));
		inputCustomerNumber.sendKeys("1");
		WebElement section2Child = driver
				.findElement(By.id("ox_openxavatest_Invoice__label_xava_view_section2_sectionName"));
		WebElement section2Parent = section2Child.findElement(By.xpath(".."));
		section2Parent.click();
		wait(driver);
		WebElement inputVAT = driver.findElement(By.id("ox_openxavatest_Invoice__vatPercentage"));
		inputVAT.sendKeys("3");
		WebElement buttonSave = driver.findElement(By.id("ox_openxavatest_Invoice__CRUD___save"));
		buttonSave.click();
		wait(driver);
		WebElement buttonList = driver.findElement(By.id("ox_openxavatest_Invoice__Mode___list"));
		buttonList.click();
		wait(driver);
		waitEvent(driver);
	}

	private void verifyPrevInvoiceEvent() throws Exception {
		WebElement currentMonthEvent = driver.findElement(By.cssSelector(
				"a.fc-event.fc-event-draggable.fc-event-resizable.fc-event-start.fc-event-end.fc-event-past.fc-daygrid-event.fc-daygrid-dot-event"));
		currentMonthEvent.click();
		wait(driver);
		WebElement invoiceNumber = driver.findElement(By.id("ox_openxavatest_Invoice__number"));
		assertEquals("10", invoiceNumber.getAttribute("value"));
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		WebElement invoiceYear = driver.findElement(By.id("ox_openxavatest_Invoice__year"));
		assertEquals(String.valueOf(year), invoiceYear.getAttribute("value"));
		WebElement buttonList = driver.findElement(By.id("ox_openxavatest_Invoice__Mode___list"));
		buttonList.click();
		wait(driver);
		waitEvent(driver);
	}
	
	
	private void setInvoiceCondition(String module) throws InterruptedException {
		WebElement inputInvoiceNumber = driver.findElement(By.id("ox_openxavatest_" + module + "__conditionValue___1"));
		inputInvoiceNumber.sendKeys("12");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].focus();", inputInvoiceNumber);
		WebElement selectInvoiceNumberCondition = driver.findElement(By.id("ox_openxavatest_" + module + "__conditionComparator___1"));
		Select select = new Select(selectInvoiceNumberCondition);
		select.selectByVisibleText("=");
		WebElement filterAction = driver.findElement(By.id("ox_openxavatest_" + module + "__List___filter"));
		filterAction.click();
	}
	
	private void verifyConditionEvents(String time, boolean isExist) {
		WebElement currentMonthEvent = null;
		try {
		    currentMonthEvent = driver.findElement(By.cssSelector(
		            "a.fc-event.fc-event-draggable.fc-event-resizable.fc-event-start.fc-event-end.fc-event-" + time + ".fc-daygrid-event.fc-daygrid-dot-event"));
		} catch (NoSuchElementException e) {}	
		assert isExist ? currentMonthEvent != null : currentMonthEvent == null;

	}
	
	private void deteleEvents() throws Exception {
		WebElement clearFilterAction = driver.findElement(By.cssSelector("td.ox-list-subheader a:has(i.mdi.mdi-eraser)"));
		clearFilterAction.click();
		wait(driver);
		for (int i = 0; i < 3; i ++) {
			WebElement element = driver.findElement(By.xpath("//a[contains(@class, 'ox-image-link') and .//i[contains(@class, 'mdi-delete')]]"));
			element.click();
			Alert alert = driver.switchTo().alert();
			alert.accept();
			wait(driver);
		}
	}
	
}
