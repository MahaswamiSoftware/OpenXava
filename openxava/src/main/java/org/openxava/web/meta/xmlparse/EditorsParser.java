package org.openxava.web.meta.xmlparse;




import org.apache.commons.logging.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.util.xmlparse.*;
import org.openxava.web.meta.*;
import org.w3c.dom.*;

/**
 * @author: Javier Paniza
 */
public class EditorsParser extends ParserBase {
	private static Log log = LogFactory.getLog(EditorsParser.class);
	
	public final static String VALID_VALUES_TYPE = "valid-values";
	
	public EditorsParser(String xmlFileURL, int language) {
		super(xmlFileURL, language);
	}
	
	public static void setupEditors() throws XavaException { 
		EditorsParser defaultParser = new EditorsParser("default-editors.xml", ENGLISH);
		defaultParser.parse();		
		EditorsParser enParser = new EditorsParser("editors.xml", ENGLISH);
		enParser.parse();				
		EditorsParser esParser = new EditorsParser("editores.xml", ESPANOL);
		esParser.parse();		
	}
	
	private void addEditors(Element el) throws XavaException {
		String url = el.getAttribute(xurl[lang]);		
		if (Is.emptyString(url)) return;
		MetaEditor editor = new MetaEditor();		
		editor.setUrl(url);
		editor.setName(el.getAttribute(xname[lang])); 
		editor.setFormat(getAttributeBoolean(el, xformat[lang]));
		editor.setFrame(getAttributeBoolean(el, xwithframe[lang]));	
		editor.setAlwaysReload(getAttributeBoolean(el, xalways_reload[lang])); 
		editor.setComposite(getAttributeBoolean(el, xcomposite[lang])); 
		String dependsStereotypes = el.getAttribute(xdepends_stereotypes[lang]);
		String dependsProperties = el.getAttribute(xdepends_properties[lang]);
		if (
			!Is.emptyString(dependsStereotypes) &&
			!Is.emptyString(dependsProperties)) {
			throw new XavaException("editor_definition_error", editor.getUrl());	
		}
		editor.setDependsStereotypes(dependsStereotypes);
		editor.setDependsProperties(dependsProperties);
		editor.setIcon(el.getAttribute(xicon[lang])); 
		editor.setInitAction(el.getAttribute(xinit_action[lang])); 
		editor.setReleaseAction(el.getAttribute(xrelease_action[lang])); 
		editor.setSelectableItems(getAttributeBoolean(el, xselectable_items[lang])); 
		if (Is.emptyString(editor.getName()) && !Is.emptyString(editor.getInitAction())) {
			throw new XavaException("init_action_not_for_noname_editor"); 
		}
		if (Is.emptyString(editor.getName()) && !Is.emptyString(editor.getReleaseAction())) {
			throw new XavaException("release_action_not_for_noname_editor"); 
		}		
		fillProperties(editor, el);				
		editor.setFormatterClassName(getFormatterClass(el, editor));
		editor.setFormatterFromType(getFormatterFromType(el));
		editor.setListFormatterClassName(getListFormatterClassName(el, editor));
		editor.setType(getPropertyType(el, editor));
		
		if (editor.isFormatterFromType() && !Is.emptyString(editor.getFormatterClassName())) {
			throw new XavaException("formatter_class_and_from_type_not_compatible");
		}

		MetaWebEditors.addMetaEditor(editor);
		addEditorsForType(editor, el);
		addEditorsForStereotype(editor, el);
		addEditorsForAnnotation(editor, el); 
		addEditorsForModelProperty(editor, el);
		addEditorsForValidValues(editor, el);
		addEditorsForReferences(editor, el);
		addEditorsForReferenceModel(editor, el);
		addEditorsForCollections(editor, el);
		addEditorsForElementCollections(editor, el);
		addEditorsForCollectionModel(editor, el);
		addEditorsForTabs(editor, el); 
		addEditorsForTabModel(editor, el);
	}	
	
	private MetaSet createSet(Node n) throws XavaException {
		Element el = (Element) n;
		MetaSet a = new MetaSet();		
		a.setPropertyName(el.getAttribute(xproperty[lang]));
		a.setValue(el.getAttribute(xvalue[lang]));
		return a;
	}
	
	private String getListFormatterClassName(Element n, MetaEditor container) throws XavaException {
		NodeList l = n.getElementsByTagName(xlist_formatter[lang]);
		int c = l.getLength();
		if (c > 1) {
			throw new XavaException("no_more_1_list_formatter");	
		}
		if (c < 1) return null;
		Element el = (Element) l.item(0);
		
		// add set to list-formatter
		NodeList set = el.getElementsByTagName(xset[lang]);
		int x = set.getLength();
		for (int i = 0; i < x; i++) {
			container._addListFormatterMetaSet(createSet(set.item(i)));
		}
		
		// 
		return  el.getAttribute(xclass[lang]);
	}	


	private String getFormatterClass(Element n, MetaEditor container) throws XavaException {
		NodeList l = n.getElementsByTagName(xformatter[lang]);
		int c = l.getLength();
		if (c > 1) {
			throw new XavaException("no_more_1_formatter");
		}
		if (c < 1) return null;
		Element el = (Element) l.item(0);
		
		// add set to formatter
		NodeList set = el.getElementsByTagName(xset[lang]);
		int x = set.getLength();
		for (int i = 0; i < x; i++) {
			container._addFormatterMetaSet(createSet(set.item(i)));
		}
		
		//
		return el.getAttribute(xclass[lang]);						
	}
	
	private boolean getFormatterFromType(Element n) throws XavaException {
		NodeList l = n.getElementsByTagName(xformatter[lang]);
		int c = l.getLength();
		if (c > 1) {
			throw new XavaException("no_more_1_formatter");
		}
		if (c < 1) return false;
		Element el = (Element) l.item(0);					
		return getAttributeBoolean(el, xfrom_type[lang]);						
	}
	
	private String getPropertyType(Element n, MetaEditor container) throws XavaException {
		System.out.println("getPropertyType " + n.getNodeName() + " " + container.getName());
		NodeList l = n.getElementsByTagName(xfor_tabs[lang]);
		Element el = (Element) l.item(0);
		NodeList set = el.getElementsByTagName(xhas_type[lang]);
		int x = set.getLength();
		System.out.println("x length " + x);
		for (int i = 0; i < x; i++) {
			Element el2 = (Element) set.item(i);	
			System.out.println(el2);
			System.out.println(el2.getAttribute(xtype[lang]));
			container._addTypeMetaSet(createSet(set.item(i)));
		}	
		//en teoria no deberia retornar?
		return ((Element)set.item(0)).getAttribute(xtype[lang]);
	}
	
	
		
	private void fillProperties(MetaEditor editor, Element n) {
		NodeList l = n.getElementsByTagName(xproperty[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);					
			editor.addProperty(el.getAttribute(xname[lang]), el.getAttribute(xvalue[lang]));
		}				
	}

	
	private void addEditorsForType(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_type[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForType(el.getAttribute(xtype[lang]), editor);
		}		
	}
	
	private void addEditorsForAnnotation(MetaEditor editor, Element n) throws XavaException { 		
		NodeList l = n.getElementsByTagName(xfor_annotation[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForAnnotation(el.getAttribute(xannotation[lang]), editor);
		}		
	}

	private void addEditorsForReferenceModel(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_reference[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForReferenceModel(el.getAttribute(xmodel[lang]), editor);
		}		
	}
	
	private void addEditorsForCollectionModel(MetaEditor editor, Element n) throws XavaException { 		
		NodeList l = n.getElementsByTagName(xfor_collection[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForCollectionModel(el.getAttribute(xmodel[lang]), editor);
		}		
	}
	
	private void addEditorsForTabModel(MetaEditor editor, Element n) throws XavaException {  		
		NodeList l = n.getElementsByTagName(xfor_tab[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForTabModel(el.getAttribute(xmodel[lang]), editor);
		}		
	}			
	
	private void addEditorsForValidValues(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_valid_values[lang]);
		if (l.getLength() > 0) {
			// we save using a special internal type 
			MetaWebEditors.addMetaEditorForType(VALID_VALUES_TYPE, editor);
		}		
	}
	
	private void addEditorsForReferences(MetaEditor editor, Element n) throws XavaException { 		
		NodeList l = n.getElementsByTagName(xfor_references[lang]);
		if (l.getLength() > 0) { 
			MetaWebEditors.addMetaEditorForReferences(editor);
		}		
	}
	
	private void addEditorsForCollections(MetaEditor editor, Element n) throws XavaException {  		
		NodeList l = n.getElementsByTagName(xfor_collections[lang]);
		if (l.getLength() > 0) { 
			MetaWebEditors.addMetaEditorForCollections(editor);
		}		
	}
	
	private void addEditorsForElementCollections(MetaEditor editor, Element n) throws XavaException {  		
		NodeList l = n.getElementsByTagName(xfor_element_collections[lang]);
		if (l.getLength() > 0) { 
			MetaWebEditors.addMetaEditorForElementCollections(editor);
		}		
	}	
	
	private void addEditorsForTabs(MetaEditor editor, Element n) throws XavaException {   		
		NodeList l = n.getElementsByTagName(xfor_tabs[lang]);
		if (editor.getName().equals("Empty")) {
			System.out.println("addEditorsForTabs EditorsParser" + editor.getName());
		}
		if (l.getLength() > 0) { 
			MetaWebEditors.addMetaEditorForTabs(editor);
		}		
	}			
	
	private void addEditorsForStereotype(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_stereotype[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForStereotype(el.getAttribute(xstereotype[lang]), editor);
		}		
	}
	
	private void addEditorsForModelProperty(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_model_property[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForModelProperty(el.getAttribute(xproperty[lang]), el.getAttribute(xmodel[lang]), editor);
		}		
	}
			
	private void createEditors() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xeditor[lang]);
		int c = l.getLength();		
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);						
			addEditors(el);			
		}						
	}
			
	protected void createObjects() throws XavaException {
		createEditors();				
	}
		
}