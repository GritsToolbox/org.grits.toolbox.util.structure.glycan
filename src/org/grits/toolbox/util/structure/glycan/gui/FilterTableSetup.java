package org.grits.toolbox.util.structure.glycan.gui;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.grits.toolbox.core.dataShare.PropertyHandler;
import org.grits.toolbox.core.img.ImageShare;
import org.grits.toolbox.util.structure.glycan.filter.om.ComboFilter;
import org.grits.toolbox.util.structure.glycan.filter.om.Filter;
import org.grits.toolbox.util.structure.glycan.filter.om.FilterSetting;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterAnd;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterNot;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterOr;
import org.grits.toolbox.util.structure.glycan.filter.om.IntegerFilter;
import org.grits.toolbox.util.structure.glycan.filter.om.table.FilterRow;
import org.grits.toolbox.util.structure.glycan.util.FilterUtils;

public class FilterTableSetup {
	
	protected static final Image CHECKED = ImageShare.CHECKBOX_ICON_YES.createImage();
	protected static final Image UNCHECKED = ImageShare.CHECKBOX_ICON_NO.createImage();
	
	protected List<FilterChangedListener> listeners = new ArrayList<>();
	
	protected TableViewer tableViewer=null;
	protected String op = "AND";
	
	protected List<Filter> filterList;
	
	/**
	 * The full path name for the plugin's local filters.xml files
	 */
	protected String filterFile=null;
	protected ComboViewer comboOp;
	protected Button addButton;
	protected Button removeButton;
	protected ComboViewer combo;
	
	public void setFilterFile(String localFilterFile) {
		this.filterFile = localFilterFile;
	}
	
	/**
	 * add your class to the registered listeners list.
	 * When a filter is updated (edited) or a filter is removed from the table, filterChanged method will be executed on all the registered listeners
	 * 
	 * @param listener
	 */
	public void addFilterChangedListener (FilterChangedListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeFilterChangedListener (FilterChangedListener listener) {
		this.listeners.remove(listener);
	}
	
	public void loadFilters () throws Exception {
		// get all available filter option labels
        filterList = new ArrayList<>();
        if (this.filterFile != null) {
			try {
				filterList.addAll(FilterUtils.readFilters(filterFile).getFilters());
			} catch (UnsupportedEncodingException | FileNotFoundException | JAXBException e1) {
				throw e1;
			}
        }
        
		Collections.sort((List<Filter>) filterList);
	}
	
	/**
	 * 
	 * creates a composite containing controls and a tableviewer to allow the user to add filters into
	 * 
	 * @param filterComposite
	 * @throws Exception if the available filters cannot be read from "filters.xml" file
	 */
	public void createFilterTableSection (Composite filterComposite) throws Exception {
		GridLayout gl_comp = new GridLayout();
        gl_comp.numColumns = 3;
		filterComposite.setLayout(gl_comp);
		
		comboOp = new ComboViewer(filterComposite, SWT.READ_ONLY);
		comboOp.setContentProvider(new ArrayContentProvider());
		comboOp.setLabelProvider(new LabelProvider());
		comboOp.setInput(new String[]{"AND", "OR"});
		comboOp.getCombo().select(0);
		
		comboOp.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selected = comboOp.getStructuredSelection();
				op = (String)selected.getFirstElement();
				filterUpdated();
			}
		});
		
		new Label(filterComposite, SWT.NONE);
		new Label(filterComposite, SWT.NONE);
		  
        combo = new ComboViewer(filterComposite, SWT.READ_ONLY);

        combo.setContentProvider(ArrayContentProvider.getInstance());

        combo.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Filter) { 
                    return ((Filter)element).getLabel();
                }
                return super.getText(element);
            }
        });
        
        if (filterList == null) 
        	loadFilters();
        combo.setInput(filterList);
        if (filterList.size() > 0)
        	combo.getCombo().select(0);
        
        addButton = new Button(filterComposite, SWT.PUSH);
        addButton.setText("Add");
        addButton.addSelectionListener(new SelectionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableViewer != null) {
					IStructuredSelection selected = (IStructuredSelection) combo.getSelection();
					if (selected != null) {
						if (selected.getFirstElement() instanceof Filter) {
							Filter filter = (Filter) selected.getFirstElement();
							FilterRow filterRow = new FilterRow();
							filterRow.setFilter(filter.copy());
							List<FilterRow> currentInput = (List<FilterRow>)tableViewer.getInput();
							currentInput.add(filterRow);
							filterUpdated();
							tableViewer.refresh();
						}
					}
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
        
        removeButton = new Button(filterComposite, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(new SelectionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tableViewer != null) {
					IStructuredSelection selected = (IStructuredSelection) tableViewer.getSelection();
					if (selected != null) {
						if (selected.getFirstElement() instanceof FilterRow) {
							FilterRow filterRow = (FilterRow)selected.getFirstElement();
							((List<FilterRow>)tableViewer.getInput()).remove(filterRow);
							filterUpdated();
							tableViewer.refresh();
						}
					}
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});        
        
        createTable(filterComposite);
	}
	
	protected void filterUpdated() {
		for (FilterChangedListener listener : listeners) {
			listener.filterChanged();
		}
	}

	/**
	 * Return the list of filters read from the xml files.
	 * @return the list of filters or null if the method createFilterTableSection is not executed earlier
	 */
	public List<Filter> getFilterList() {
		return filterList;
	}
	
	public void setFilterList(List<Filter> filterList) {
/*		List<Filter> monosaccharidesFilterList = new ArrayList<Filter>();
		List<Filter> majorMonoFilterList = new ArrayList<Filter>();
		List<Filter> otherFilterList = new ArrayList<Filter>();
		List<Filter> lewisFilterList = new ArrayList<Filter>();
		List<Filter> cappedFilterList = new ArrayList<Filter>();
		List<Filter> lacnacFilterList = new ArrayList<Filter>();
		List<Filter> bloodGrpFilterList = new ArrayList<Filter>();
		
		List<Filter> sortedFilterList = new ArrayList<Filter>();
		
		for(Filter t_f : filterList) {
			if(t_f.getName() != null) {
				if(t_f.getName().startsWith("Major")) {
					majorMonoFilterList.add(t_f);		
				}else if(t_f.getName().startsWith("Monosaccharide")) {
					monosaccharidesFilterList.add(t_f);					
				}else if(t_f.getName().contains("Other")) {
					otherFilterList.add(t_f);
				}else if(t_f.getName().contains("Lewis")) {
					lewisFilterList.add(t_f);
				}else if(t_f.getName().contains("Blood")) {
					bloodGrpFilterList.add(t_f);
				}else if(t_f.getName().contains("Capped")) {
					cappedFilterList.add(t_f);
				}else if(t_f.getName().contains("LacNac")) {
					lacnacFilterList.add(t_f);
				}else {
					otherFilterList.add(t_f);
				}
			}
		}
		
		if(majorMonoFilterList != null && majorMonoFilterList.size() > 0) {
			Collections.sort((List<Filter>) majorMonoFilterList);
			for(Filter t_f : majorMonoFilterList) {
				sortedFilterList.add(t_f);
			}
		}
		if(otherFilterList != null && otherFilterList.size() > 0) {
			Collections.sort((List<Filter>) otherFilterList);
			for(Filter t_f : otherFilterList) {
				sortedFilterList.add(t_f);
			}
		}
		if(lacnacFilterList != null && lacnacFilterList.size() > 0) {
			Collections.sort((List<Filter>) lacnacFilterList);
			for(Filter t_f : lacnacFilterList) {
				sortedFilterList.add(t_f);
			}
		}
		if(lewisFilterList != null && lewisFilterList.size() > 0) {
			Collections.sort((List<Filter>) lewisFilterList);
			for(Filter t_f : lewisFilterList) {
				sortedFilterList.add(t_f);
			}
		}
		if(bloodGrpFilterList != null && bloodGrpFilterList.size() > 0) {
			Collections.sort((List<Filter>) bloodGrpFilterList);
			for(Filter t_f : bloodGrpFilterList) {
				sortedFilterList.add(t_f);
			}
		}
		if(cappedFilterList != null && cappedFilterList.size() > 0) {
			Collections.sort((List<Filter>) cappedFilterList);
			for(Filter t_f : cappedFilterList) {
				sortedFilterList.add(t_f);
			}
		}
		if(monosaccharidesFilterList != null && monosaccharidesFilterList.size() > 0) {
			Collections.sort((List<Filter>) monosaccharidesFilterList);
			for(Filter t_f : monosaccharidesFilterList) {
				sortedFilterList.add(t_f);
			}
		}

		if(sortedFilterList != null && sortedFilterList.size() > 0) {
			filterList = sortedFilterList;			
		}
*/
		this.filterList = filterList;			
		Collections.sort((List<Filter>) filterList);
	}
	
	/**
	 * creates a table viewer containing columns (include/exclude, min, max and selection for combo type filters) for the filter settings
	 * @param parent
	 */
	protected void createTable (Composite parent) {
		tableViewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
  
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.heightHint = 120;
		data.horizontalSpan=3;
		tableViewer.getTable().setLayoutData(data); 

		TableViewerColumn filterColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		filterColumn.getColumn().setText("Filter");
		filterColumn.getColumn().setWidth(80);
	  
		filterColumn.setLabelProvider(new ColumnLabelProvider() {
			  @Override
			  public String getText(Object element) {
				  if (element instanceof FilterRow) {
		                return ((FilterRow) element).getFilter().getLabel();
		          }
		          return super.getText(element);
			  }
		});
  
		TableViewerColumn filterDescriptionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		filterDescriptionColumn.getColumn().setText("Description");
		filterDescriptionColumn.getColumn().setWidth(120);
  
		filterDescriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof FilterRow) {
					return ((FilterRow) element).getFilter().getDescription();
				}
				return super.getText(element);
			}
		});
  
		TableViewerColumn includeExcludeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		includeExcludeColumn.getColumn().setText("Include");
		includeExcludeColumn.getColumn().setWidth(50);
  
		includeExcludeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
	  
			@Override
			public Image getImage(Object element) {
				if (element instanceof FilterRow) {
					if (((FilterRow) element).getInclude()) {
						return CHECKED;
					}
		            else 
		            	return UNCHECKED;  
				}
				return null;
				
			}
		});
		
		includeExcludeColumn.setEditingSupport(new EditingSupport(tableViewer) {
			
			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof FilterRow) {
					if (value != null) {
						Boolean current = ((FilterRow) element).getInclude();
						((FilterRow) element).setInclude((Boolean)value);
						if (current == null || !current.equals(value)) {
							filterUpdated();
							tableViewer.update(element, null);
						}
					}
				}	
			}
			
			@Override
			protected Object getValue(Object element) {
				if (element instanceof FilterRow)
					return ((FilterRow) element).getInclude();
				return null;
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(tableViewer.getTable(), SWT.CHECK); 
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		
	    TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		valueColumn.getColumn().setText("Selection");
		valueColumn.getColumn().setWidth(100);
		valueColumn.setEditingSupport(new EditingSupport(tableViewer) {
			
			ComboBoxViewerCellEditor comboBoxViewerCellEditor = null;
			
			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
					if (filter instanceof ComboFilter) {
						if (value != null) {
							Filter current = ((ComboFilter) filter).getSelected();
							((ComboFilter)filter).setSelected((Filter) value);
							if (current == null || !current.equals(value)) {
								filterUpdated();
								tableViewer.update(element, null);
							}
							tableViewer.update(element, null);
						}
					}
				}
				
			}
			
			@Override
			protected Object getValue(Object element) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
					if (filter instanceof ComboFilter) {
						return ((ComboFilter) filter).getSelected();
					}
				}
				return null;
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				if (comboBoxViewerCellEditor == null) {
					comboBoxViewerCellEditor = new ComboBoxViewerCellEditor(tableViewer.getTable(), SWT.READ_ONLY);
					comboBoxViewerCellEditor.setContentProvider(new ArrayContentProvider());
				}
				
				if (element instanceof FilterRow) {
					if (((FilterRow) element).getFilter() instanceof ComboFilter)
						comboBoxViewerCellEditor.setInput(((ComboFilter) ((FilterRow)element).getFilter()).getFilters());
				}
				
				return comboBoxViewerCellEditor;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				if (element instanceof FilterRow) 
					if (((FilterRow) element).getFilter() instanceof ComboFilter)
						return true;
				return false;
			}
		});
		
		valueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof FilterRow) {
					if (((FilterRow) element).getFilter() instanceof ComboFilter) {
						Filter selected = ((ComboFilter)((FilterRow) element).getFilter()).getSelected();
						if (selected != null)
							return selected.getLabel();
					}
				}
				
				return "";
			}
		});
		  
		TableViewerColumn minValueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		minValueColumn.getColumn().setText("Min Value");
		minValueColumn.getColumn().setWidth(50);
		minValueColumn.setEditingSupport(new EditingSupport(tableViewer) {
			
			TextCellEditor integerEditor = null;
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				if (integerEditor == null) {
					this.integerEditor = new TextCellEditor(tableViewer.getTable());
					((Text)integerEditor.getControl()).setTextLimit(PropertyHandler.LABEL_TEXT_LIMIT);
			        ControlDecoration intControlDecoration = new ControlDecoration(integerEditor.getControl(), SWT.CENTER);
			        integerEditor.setValidator(new org.grits.toolbox.core.utilShare.validator.IntegerValidator(intControlDecoration));  
				}
				
				return integerEditor;
			}
			
			@Override
			protected Object getValue(Object element) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
					if (filter instanceof IntegerFilter) {
						Integer min = ((IntegerFilter)filter).getMin();
						if (min == null) return "";
						else return min + "";
					}
				}
				return null;
			}
			
			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
					if (value != null && ((String)value).length() > 0) {
						Integer current = null;
						if (filter instanceof IntegerFilter) {
							current = ((IntegerFilter)filter).getMin();
							((IntegerFilter)filter).setMin(Integer.parseInt((String)value));
						}
						if (current == null || !current.equals(Integer.parseInt((String)value))) {
							filterUpdated();
							tableViewer.update(element, null);
						}
					} else {
						Integer current = null;
						if (filter instanceof IntegerFilter) {
							current = ((IntegerFilter)filter).getMin();
							((IntegerFilter) filter).setMin(null);
							if (current != null) {
								filterUpdated();
								tableViewer.update(element, null);
							}
						}
					}
				}
				
			}
			
			@Override
			protected boolean canEdit(Object element) {
				if (element instanceof FilterRow)
					if (((FilterRow) element).getFilter() instanceof IntegerFilter ) 
						return true;
				return false;
			}
		});
		minValueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
			        if (filter instanceof IntegerFilter) {
						Integer value = ((IntegerFilter)filter).getMin();
		        		if (value == null) return "";
		        		else return value +"";
			        }
		        	else
						return null;
			    }
				return super.getText(element);
			}
		});
		
		TableViewerColumn maxValueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		maxValueColumn.getColumn().setText("Max Value");
		maxValueColumn.getColumn().setWidth(50);
		maxValueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
			        if (filter instanceof IntegerFilter) {
			        	Integer value = ((IntegerFilter)filter).getMax();
		        		if (value == null) return "";
		        		else return value +"";
			        }
			        else
						return null;
			    }
				return super.getText(element);
			}
		});
		maxValueColumn.setEditingSupport(new EditingSupport(tableViewer) {
			
			TextCellEditor integerEditor = null;
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				if (integerEditor == null) {
					this.integerEditor = new TextCellEditor(tableViewer.getTable());
					((Text)integerEditor.getControl()).setTextLimit(PropertyHandler.LABEL_TEXT_LIMIT);
			        ControlDecoration intControlDecoration = new ControlDecoration(integerEditor.getControl(), SWT.CENTER);
			        integerEditor.setValidator(new org.grits.toolbox.core.utilShare.validator.IntegerValidator(intControlDecoration));  
			       
				}
				
				return integerEditor;
			}
			
			@Override
			protected Object getValue(Object element) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
					if (filter instanceof IntegerFilter) {
						Integer max = ((IntegerFilter)filter).getMax();
						if (max == null) return "";
						else return max + "";
					}
				}
				return null;
			}
			
			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof FilterRow) {
					Filter filter = ((FilterRow) element).getFilter();
					if (value != null && ((String)value).length() > 0) {
						Integer current = null;
						if (filter instanceof IntegerFilter) {
							current = ((IntegerFilter)filter).getMax();
							Integer maxValue = Integer.parseInt((String)value);
							((IntegerFilter)filter).setMax(maxValue);
							if (current == null || !current.equals(maxValue)) {
								filterUpdated();
								tableViewer.update(element, null);
							}
						}
					} else {
						Integer current = null;
						if (filter instanceof IntegerFilter) {
							current = ((IntegerFilter)filter).getMax();
							((IntegerFilter) filter).setMax(null);
							if (current != null) {
								filterUpdated();
								tableViewer.update(element, null);
							}
						}
					}
				}
				
			}
			
			@Override
			protected boolean canEdit(Object element) {
				if (element instanceof FilterRow)
					if (((FilterRow) element).getFilter() instanceof IntegerFilter) 
						return true;
				return false;
			}
		});
  
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object inputElement) {
				return ((List<FilterRow>)inputElement).toArray();
			}
		});
  
		tableViewer.setInput(new ArrayList<FilterRow>());
	}
	
	/**
	 * gets all the filters added into the table by the user and creates a filtersetting object containing the appropriate filters
	 * to be applied on the glycan objects
	 * 
	 * @return FilterSetting object containing an "AND" or "OR" filter based on the user's selection for the Operator
	 * @return null if the tableviewer is not initialized yet or the table is empty
	 */
	@SuppressWarnings("unchecked")
	public FilterSetting getFilterSetting() {
		if (tableViewer == null)
			return null;
		
		FilterSetting setting = new FilterSetting();
		List<Filter> elements = new ArrayList<>();
		
		List<FilterRow> rows = (List<FilterRow>)tableViewer.getInput();
		for (Iterator<FilterRow> iterator = rows.iterator(); iterator.hasNext();) {
			FilterRow filterRow = (FilterRow) iterator.next();
			Filter filterToSet = null;
			if (filterRow.getFilter() instanceof ComboFilter) {
				filterToSet = ((ComboFilter)filterRow.getFilter()).getSelected();
			} else
				filterToSet = filterRow.getFilter();
			if (filterToSet == null)
				continue; // skip this row
			// fix min/max values
			if (filterToSet instanceof IntegerFilter) {
				Integer max = ((IntegerFilter) filterToSet).getMax();
				Integer min = ((IntegerFilter) filterToSet).getMin();
				if (min != null && min < 0)
					((IntegerFilter) filterToSet).setMin(0);
				if (max != null && min != null && max < min)
					((IntegerFilter) filterToSet).setMax(null);
			}
			if (!filterRow.getInclude()) {
				// create a NOT filter
				GlycanFilterNot notFilter = new GlycanFilterNot();
				notFilter.setFilter(filterToSet);
				elements.add(notFilter);
			}
			else {
				elements.add(filterToSet);
			}
		}
		
		if (elements.isEmpty()) {
			// nothing is selected
			return null;
		}
		
		if (op.equals("AND")) {
			GlycanFilterAnd filter = new GlycanFilterAnd();
			filter.setElements(elements);
			setting.setFilter(filter);
		}
		else {
			GlycanFilterOr filter = new GlycanFilterOr();
			filter.setElements(elements);
			setting.setFilter(filter);
		}

		return setting;
	}
	
	/**
	 * clear the filters in the table
	 */
	public void resetFilters () {
		if (tableViewer != null) {
			tableViewer.setInput(new ArrayList<FilterRow>());
		}
	}

	/**
	 * This method is used to handle the case when the user is still editing the value of a cell in the table and clicks a button outside the table
	 * The cell editor does not loose focus in such a case and the latest value in the cell editor is not committed
	 * 
	 * It needs to be called before you do anything with the selected filters from the table
	 */
	public void stopEditing() {
		tableViewer.applyEditorValue();
	}

	/**
	 * This method is used when the user would like to edit existing filter selections
	 * It will populate the filter table with the filters defined in the provided filterSettings
	 * It generates list of FilterRow objects to be set as input to the table
	 * 
	 * @param filterSetting
	 */
	public void setExistingFilters(FilterSetting filterSetting) {
		if (tableViewer != null) {
			if (filterSetting != null) {
				Filter filter = filterSetting.getFilter();
				List<Filter> filters = null;
				if (filter instanceof GlycanFilterAnd) {
					op = "AND";
					comboOp.getCombo().select(0);
					filters = ((GlycanFilterAnd) filter).getElements();
				} else if (filter instanceof GlycanFilterOr) {
					op = "OR";
					comboOp.getCombo().select(1);
					filters = ((GlycanFilterOr) filter).getElements();
				}
				if (filters != null) {
					List<FilterRow> filterRows = new ArrayList<>();
					for (Filter filter2 : filters) {
						boolean include = true;
						if (filter2 instanceof GlycanFilterNot) {
							include = false;
							filter2 = ((GlycanFilterNot) filter2).getFilter();
						}
						FilterRow filterRow = new FilterRow();
						filterRow.setFilter(filter2);
						filterRow.setInclude(include);
						filterRows.add(filterRow);
					}			
					tableViewer.setInput(filterRows);
				}
			}
		}
	}

	public void setEnabled(boolean b) {
		combo.getCombo().setEnabled(b);
		comboOp.getCombo().setEnabled(b);
		tableViewer.getTable().setEnabled(b);
		addButton.setEnabled(b);
		removeButton.setEnabled(b);
	}
}
