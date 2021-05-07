// WARNING: IF YOU CHANGE THIS PASS DateCalendarTest.txt

openxava.getScript(openxava.contextPath + "/xava/editors/flatpickr/" + openxava.language + ".js");

openxava.addEditorInitFunction(function() {
	if (openxava.browser.htmlUnit) return;
	// $('.xava_date > input').first().change(function() {
	$('.xava_date > input').change(function() { // tmp
		var dateFormat = $(this).parent().data("dateFormat");
  		var date = $(this).val();
		if (date === "") return;
		date = date.trim(); // tmp
		console.log("[openxava.addEditorInitFunction.change] date.1=" + date); // tmp
		// tmp var separator = dateFormat.substr(-2, 1);
		var separator = dateFormat.substr(1, 1); // tmp
		console.log("[openxava.addEditorInitFunction.change] separator=" + separator); // tmp
		var idx = date.lastIndexOf(separator);
		if (idx < 0) {
			if (date.length % 2 != 0) date = " " + date;
			var year = date.substring(4);
			var middle = date.substring(2, 4);
			var first = date.substring(0, 2);
			date = first + separator + middle + separator + year;
			date = date.trim(); // tmp ME QUED� POR AQU�: ESTO ARREGLA EL PROBLEMA CON DateCalendarTest.txt. VOLVER A PASAR LOS TEST MANUALES OTRA VEZ
								// TMP   LA SUITE PASO AL 100%. LA DOC DE MIGRACI�N EST� HECHA			
		}
		console.log("[openxava.addEditorInitFunction.change] date.2=" + date); // tmp	
		idx = date.lastIndexOf(separator);
		// tmp ini
		var idxSpace = date.indexOf(' ');
		var pureDate = date;
		var time = "";
		if (idxSpace >= 0) {
			time = date.substr(idxSpace);
			pureDate = date.substr(0, idxSpace);
		}
		if (dateFormat.indexOf('Y') >= 0 && pureDate.length - idx < 4) { // tmp
  			var dateNoYear = pureDate.substring(0, idx);
  			var year = pureDate.substring(idx + 1);
  			var prefix = year > 50?"19":"20";
  			console.log("[openxava.addEditorInitFunction.change] dateNoYear=" + dateNoYear); // tmp
  			console.log("[openxava.addEditorInitFunction.change] year=" + year); // tmp
  			console.log("[openxava.addEditorInitFunction.change] prefix=" + prefix); // tmp
  			date = dateNoYear + separator + prefix + year + time; // tmp
  			console.log("[openxava.addEditorInitFunction.change] date< " + date); // tmp	  			 
  		}			
		// tmp fin
		/* tmp 
		// tmp if (dateFormat.substr(-1) === "Y" && date.length - idx < 4) {
		if (dateFormat.indexOf('Y') >= 0 && date.length - idx < 4) { // tmp
  			var dateNoYear = date.substring(0, idx);
  			var year = date.substring(idx + 1);
  			var prefix = year > 50?"19":"20";
  			console.log("[openxava.addEditorInitFunction.change] dateNoYear=" + dateNoYear); // tmp
  			console.log("[openxava.addEditorInitFunction.change] year=" + year); // tmp
  			console.log("[openxava.addEditorInitFunction.change] prefix=" + prefix); // tmp
  			// tmp date = dateNoYear + separator + prefix + year;
  			date = dateNoYear + separator + prefix + year + time; // tmp
  			console.log("[openxava.addEditorInitFunction.change] date< " + date); // tmp	  			 
  		}	
  		*/
  		$(this).val(date);
	});
	$('.flatpickr-calendar').remove();
	$('.xava_date').flatpickr({
	    allowInput: true,
	    clickOpens: false,  
	    wrap: true,
	    locale: openxava.language, 
	    onChange: function(selectedDates, dateStr, instance) {
        	if (!$(instance.input).data("datePopupJustClosed") || dateStr === $(instance.input).attr('value')) {
        		$(instance.input).data("changedCancelled", true);
        	}
        	$(instance.input).attr('value', dateStr);
        	$(instance.input).removeData("datePopupJustClosed");
    	},
    	onClose: function(selectedDates, dateStr, instance) {
	    	$(instance.input).data("datePopupJustClosed", true);
    	},    	 
	});
});

