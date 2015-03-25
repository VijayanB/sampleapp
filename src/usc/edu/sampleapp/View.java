package usc.edu.sampleapp;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {
	public static final String ID = "usc.edu.sampleapp.view";

	private TableViewer viewer;

	private TableWrapData td;

	private ScrolledForm form;

	private FormToolkit toolkit;

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
			return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Senate Crawler"); //$NON-NLS-1$
		TableWrapLayout layout = new TableWrapLayout();
		// GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);
		layout.numColumns = 1;
		Section section = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR | Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 1;
		section.setLayoutData(td);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setExpanded(true);
		section.setText("Senate Crawler"); //$NON-NLS-1$
		section.setDescription("The description about the user action should goes here"); //$NON-NLS-1$
		Composite sectionClient = toolkit.createComposite(section);

		GridLayoutUtil.applyGridLayout(sectionClient).numColumns(2);
		GridDataUtil.applyGridData(sectionClient).grabExcessHorizontalSpace(
				true);
		Composite composite = toolkit.createComposite(form.getBody());
		GridLayoutUtil.applyGridLayout(sectionClient).numColumns(2);
		GridDataUtil.applyGridData(sectionClient).grabExcessHorizontalSpace(
				true);

		Label label = toolkit.createLabel(composite, "Crawler");
		GridDataUtil.applyGridData(label).grabExcessHorizontalSpace(true);

		td = new TableWrapData();
		td.rowspan = 2;
		section.setClient(sectionClient);
		label.setLayoutData(td);
		Combo comboBox = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboBox.add("test1");
		comboBox.add("new");
		comboBox.setText("new");
		td = new TableWrapData();
		td.rowspan = 2;
		comboBox.setLayoutData(td);
		toolkit.adapt(comboBox);

		this.setPartName("Crawler");
		toolkit.paintBordersFor(form.getBody());
	}

	/** * Passing the focus request to the viewer's control. */

	public void setFocus() {
		form.setFocus();
	}

	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}
}