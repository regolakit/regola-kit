 
package org.regola.webapp.action;

import org.regola.service.GenericManager;
import org.regola.util.ELFunction;
import org.regola.util.Ognl;
import org.regola.validation.LazyLoadingArrayList;
import org.regola.model.ModelPattern;
import org.regola.validation.LazyLoadingArrayList.Fetcher;
import org.regola.webapp.jsf.ColumnsDlg;
import org.regola.webapp.jsf.OrderDlg;
import org.regola.webapp.jsf.Dialog.DialogCallback;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import com.icesoft.faces.component.datapaginator.PaginatorActionEvent;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

/**
 * Questa classe offre dei servizi comuni a tutte le pagine
 * 
 * @author nicola
 *
 * @param <T>
 * @param <ID>
 * @param <F>
 */
public abstract class ListPage<T, ID extends Serializable, F extends ModelPattern> extends
		org.regola.webapp.action.BasePage
{

	private DataModel columnDataModel;
	
	private PersistentFacesState state;

	ColumnsDlg columnsDlg;

	String currentCulumn;

	Map<String, String> currentCulumnCallback;

	F filter;

	List<T> modelObjectList;

	OrderDlg orderDlg;

	protected DataModel rowDataModel;

	Set<ID> selectedId = new HashSet<ID>();

	GenericManager<T, ID> serviceManager;

	public String getCellValue()
	{
		if (rowDataModel.isRowAvailable() && columnDataModel.isRowAvailable())
		{
			int row = rowDataModel.getRowIndex();
			//int col = columnDataModel.getRowIndex();
			//log.info(String.format("Request %d,%d", row, col ));
			//Object value = Ognl.getValue(filter.getPropertiesFilter().get(col).getName(), verbali.get(row));

			Object value = null;
			
			if (getModelObjectList().get(row)!=null)
			{	
				value = Ognl.getValue(getCurrentCulumn(), getModelObjectList().get(row));
			}

			if (value != null)
				return value.toString();

		}

		// empty field.
		return "-";
	}

	/**
	 * Chiamato per visualizzare il dialogo delle colonne
	 * @param e
	 */
	public void doColumnsDlg(ActionEvent e)
	{
		getColumnsDlg().setFilter(getFilter());
		getColumnsDlg().show("Opzioni visualizzazione", "Messaggio", new DialogCallback()
		{
			public void onConfirm()
			{
				refresh();
			}

			@SuppressWarnings("unchecked")
			public void onCancel()
			{
				setFilter((F) getColumnsDlg().getOriginalFilter());
			}
		});
	}
	
	/**
	 * Chiamato per visualizzare il dialogo degli Ordinamenti
	 * @param e
	 */
	public void doOrderDlg(ActionEvent e)
	{
		getOrderDlg().setFilter(getFilter());
		getOrderDlg().show("Opzioni ordinamento", "Messaggio", new DialogCallback()
		{
			public void onConfirm()
			{
				refresh();
			}

			@SuppressWarnings("unchecked")
			public void onCancel()
			{
				setFilter((F) getOrderDlg().getOriginalFilter());
			}
		});
	}
	
	/**
	 * Chiamato a seguito della selezione di una riga
	 */
	public void onRowSelection(RowSelectorEvent event)
	{
		log.info("call back for " + event.getRow() + " " + event.isSelected());
		log.info("elementi selezionati " + selectedId.size());

		getConfirmDlg().showModal("Attenzione", "Sei sicuro di voler cancellare?", new DialogCallback()
		{
			T model2 = getCurrentModelItem();

			public void onConfirm()
			{
				log.info("Selezione verbale:" + getId( model2));
				getId( model2);
				//serviceManager.remove(verbale.getId());
				refresh();
			}

			public void onCancel()
			{
			}
		});
	}

	/**
	 * Chiamato per cancellare una riga
	 */
	public void remove(ActionEvent event)
	{
		log.info("remove");

		getConfirmDlg().showModal("Attenzione", "Sei sicuro di voler cancellare?", new DialogCallback()
		{
			T model2 = getCurrentModelItem();

			public void onConfirm()
			{
				log.info("Selezione verbale:" + getId(model2));
				serviceManager.remove(getId(model2));
				refresh();
			}

			public void onCancel()
			{
			}
		});
	}
	
	public DataModel getColumnDataModel()
	{
		return columnDataModel;
	}

	public ColumnsDlg getColumnsDlg()
	{
		return columnsDlg;
	}

	public int getCurrentCol()
	{
		if (columnDataModel.isRowAvailable())
		{
			return columnDataModel.getRowIndex();
		}

		return -1;
	}

	public String getCurrentCulumn()
	{
		return currentCulumn;
	}

	public Map<String, String> getCurrentCulumnCallback()
	{
		return currentCulumnCallback;
	}

	public T getCurrentModelItem()
	{
		int row = getCurrentRow();

		if (row == -1)
			return null;

		return getModelObjectList().get(row);

	}

	public int getCurrentRow()
	{
		if (rowDataModel.isRowAvailable())
		{
			return rowDataModel.getRowIndex();
		}

		return -1;
	}

	public F getFilter()
	{
		return filter;
	}

	@SuppressWarnings("unchecked")
	public ID getId(T model)
	{
		return (ID) Ognl.getValue("id", model);
	}

	public List<T> getModelObjectList()
	{
		return modelObjectList;
	}

	public OrderDlg getOrderDlg()
	{
		return orderDlg;
	}

	public DataModel getRowDataModel()
	{
		return rowDataModel;
	}

	public Set<ID> getSelectedId()
	{
		return selectedId;
	}

	public GenericManager<T, ID> getServiceManager()
	{
		return serviceManager;
	}

	public void init()
	{
		state = PersistentFacesState.getInstance();

		refresh();	
		
		currentCulumnCallback = new ELFunction()
		{
			@Override
			public String get(Object key)
			{
				setCurrentCulumn(key.toString());
				return "";
			}

		};
	}

	public boolean isSelected()
	{
		T model = getCurrentModelItem();

		if (model == null)
			return false;
		return selectedId.contains(getId(model));
	}

	public void paginatorListener(ActionEvent event)
	{
		PaginatorActionEvent e = (PaginatorActionEvent) event;

		if ("next".equals(e.getScrollerfacet()))
		{
			getFilter().nextPage();
		}

		if ("previous".equals(e.getScrollerfacet()))
		{
			getFilter().previousPage();
		}

		if ("last".equals(e.getScrollerfacet()))
		{
			getFilter().gotoLastPage();
		}

		if ("first".equals(e.getScrollerfacet()))
		{
			getFilter().setCurrentPage(0);
		}

		if (e.getPageIndex() > 0)
		{
			getFilter().gotoPage(e.getPageIndex() -1);
		}

		//filter.setCurrentPage(e.getPageIndex());

		event.toString();
	}

	public void refresh()
	{

		getFilter().setTotalItems(serviceManager.countFind(getFilter()));

		setModelObjectList(new LazyLoadingArrayList<T, F>(new Fetcher<T, F>()
		{
			public List<T> fetch(F filter)
			{
				return serviceManager.find(filter);
			}
		}, getFilter()));

		rowDataModel = new ListDataModel(getModelObjectList());

		//filter.getPropertiesFilter().clear();
		//filter.getPropertiesFilter().add(new PropertyFilter("id.annoAccademico"));

		columnDataModel = new ListDataModel(getFilter().getVisibleProperties());
	}

	public void refresh(ActionEvent event)
	{
		//HttpServletRequest request =  getRequest();
		//HttpSession session = getSession();
		//com.icesoft.faces.context.BridgeFacesContext context = (com.icesoft.faces.context.BridgeFacesContext)getFacesContext();
		//context.getViewNumber();
		//session.removeAttribute(context.getViewNumber() + "/com.icesoft.faces.sessionAuxiliaryData");
		//session.removeAttribute("null/com.icesoft.faces.sessionAuxiliaryData");

		refresh();
	}

	public void refresh(ValueChangeEvent e)
	{
		refresh();
	}

	public void resetFilter()
	{
		getFilter().reset();
		refresh();
	}

	public void resetFilter(ActionEvent event)
	{
		resetFilter();
	}

	public void setColumnDataModel(DataModel columnDataModel)
	{
		this.columnDataModel = columnDataModel;
	}

	public void setColumnsDlg(ColumnsDlg columnsDlg)
	{
		this.columnsDlg = columnsDlg;
	}

	public void setCurrentCulumn(String currentCulumn)
	{
		//log.info(currentCulumn);
		this.currentCulumn = currentCulumn;
	}

	public void setCurrentCulumnCallback(Map<String, String> currentCulumnCallback)
	{
		this.currentCulumnCallback = currentCulumnCallback;
	}

	public void setFilter(F filter)
	{
		this.filter = filter;
	}

	public void setModelObjectList(List<T> modelObjectList)
	{
		this.modelObjectList = modelObjectList;
	}

	public void setOrderDlg(OrderDlg orderDlg)
	{
		this.orderDlg = orderDlg;
	}

	public void setRowDataModel(DataModel rowDataModel)
	{
		this.rowDataModel = rowDataModel;
	}

	public void setSelected(boolean selected)
	{
		T model = getCurrentModelItem();

		if (model == null)
			return;

		if (selected)
		{
			log.info("seleziono " + model);
			selectedId.add(getId(model));
		} else
		{
			selectedId.remove(getId(model));
			log.info("deseleziono " + model);
		}
	}

	public void setSelectedId(Set<ID> selectedId)
	{
		this.selectedId = selectedId;
	}

	public void setServiceManager(GenericManager<T, ID> serviceManager)
	{
		this.serviceManager = serviceManager;
	}

	public PersistentFacesState getState() {
		return state;
	}

	public void setState(PersistentFacesState state) {
		this.state = state;
	}

}