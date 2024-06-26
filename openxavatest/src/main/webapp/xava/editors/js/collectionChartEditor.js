/* tmr Mover a openxava*/

openxava.addEditorInitFunction(function() {
	// tmr Ha de funcionar con m�s de uno
	$('.xava_collection_chart').each( function () {
		var labels = $(this).data("labels");
		var data = $(this).data("data");
		var parentId = $(this).parent().attr("id");
		c3.generate({
		    bindto: '#' + parentId + " .xava_collection_chart", 
	 		data: {
		        columns: data, 
		        type: 'bar'
		    },
		    axis: {
		        x: {
		            type: 'category',
		            categories: labels
		        }
		    },	    
		    bar: {
		        width: {
		            ratio: 0.5 // tmr �Quitar? �Ajustar?
		        }
		    }
		});	
	});

});
