package usc.edu.sampleapp;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	// File
	private IWorkbenchAction exitAction;

	// Help
	private IWorkbenchAction helpAction;
	private IWorkbenchAction introAction;
	private IWorkbenchAction helpSearch;
	private IWorkbenchAction dynamicHelpAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchWindow window;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {

		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
		register(dynamicHelpAction);

		helpAction = ActionFactory.HELP_CONTENTS.create(window);
		register(helpAction);

		helpSearch = ActionFactory.HELP_SEARCH.create(window);
		register(helpAction);

		introAction = ActionFactory.INTRO.create(window);
		register(introAction);

		this.window = window;
	}

	protected void fillMenuBar(IMenuManager menuBar) {

		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		MenuManager classifyMenu = new MenuManager("&Classify", "classify");
		MenuManager crawlerMenu = new MenuManager("&Crawler", "crawl");
		MenuManager processMenu = new MenuManager("&Process", "process");
		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		menuBar.add(classifyMenu);
		menuBar.add(crawlerMenu);
		menuBar.add(processMenu);
		// Add a group marker indicating where action set menus will appear.
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(helpMenu);

		// File
		fileMenu.add(exitAction);
		classifyMenu.add(exitAction);
		crawlerMenu.add(new Action() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					window.getActivePage().showView(NewView.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		processMenu.add(new Action() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					window.getActivePage().showView(View.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// Help
		helpMenu.add(introAction);
		helpMenu.add(new Separator());
		helpMenu.add(helpAction);
		helpMenu.add(helpSearch);
		helpMenu.add(dynamicHelpAction);
		helpMenu.add(new Separator());
		helpMenu.add(new Action("Feedback") {
		});
		helpMenu.add(new Separator());
		helpMenu.add(new Action("Check for updates") {

			public void run() {

				Job j = new Job("Update Job") {
					@Override
					protected IStatus run(final IProgressMonitor monitor) {
						BundleContext bundleContext = Activator.getDefault()
								.getBundle().getBundleContext();
						ServiceReference reference = bundleContext
								.getServiceReference(IProvisioningAgent.SERVICE_NAME);
						final IProvisioningAgent agent = (IProvisioningAgent) bundleContext
								.getService(reference);
						new UpdateHandler().execute(agent,
								PlatformUI.getWorkbench());
						return Status.OK_STATUS;
					}
				};
				j.schedule();

			};
		});
		helpMenu.add(new Separator());
		helpMenu.add(aboutAction);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		// TODO Auto-generated method stub
		super.fillCoolBar(coolBar);
		IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle()
				| SWT.LEFT_TO_RIGHT);
		coolBar.add(toolbar);
		/*
		 * toolbar.add(introAction); toolbar.add(helpAction);
		 * toolbar.add(aboutAction);
		 */

	}
}
