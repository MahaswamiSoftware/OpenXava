package org.openxava.test.actions;

import org.openxava.actions.*;
import org.openxava.test.model.*;

/**
 * tmr �Hacer algo gen�rico? Podr�a haber incluso un controlador gen�rico 
 * @author Javier Paniza
 *
 */

public class InitStaffDashboardAction extends ViewBaseAction {

	public void execute() throws Exception {
		getView().setModel(new StaffDashboard());		
	}

}
