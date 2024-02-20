package org.openxava.test.actions;

import org.openxava.actions.*;

/**
 * 
 * @author Javier Paniza
 */
public class OnChangeVisitCustomerAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		int km = getView().getValueInt("km");
		String customerName = getView().getValueString("customer.name");		
		// tmr getView().setValue("description", "KM: " + km + ", CUSTOMER: " + getNewValue() + " " + customerName);
		// TMR ME QUED� POR AQU�, LO DE &#44; FUNCIONA. TENGO QUE HACER UNA SUSTITUCI�N EN EL C�DIGO QUE FORMATEE LOS VALUE
		getView().setValue("description", "KM: " + km + "&#44; CUSTOMER: " + getNewValue() + " " + customerName); // tmr
	}

}
