package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

import lombok.*;

/**
 * 						
 * @author Javier Paniza 
 */

@Entity @Getter @Setter
public class ProductsEvaluation2 extends Identifiable {
	
	@Column(length=40) @Required
	String description;
	
	@DescriptionsList
	@ManyToOne(fetch = FetchType.LAZY)
	Family2 family;
	
	@ElementCollection
	// tmr @ListProperties("product, evaluation")
	// tmr @ListProperties("product.number, product.description, evaluation, product.color.name") // tmr AS� SIN @DescriptionsList DA ERROR, PERO NO RECUPERA EL COLOR
	// TMR ME QUED� POR AQU�, AS� FALLA
	@ListProperties("product, evaluation, product.color.name")
	Collection<ProductEvaluation2> evaluations;

}
