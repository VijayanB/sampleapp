package usc.edu.sampleapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class NewView extends ViewPart implements ISampleAppConstants {
	public static final String ID = "usc.edu.sampleapp.newview";

	private Text text;

	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI);
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				if (text.isDisposed())
					return;
				text.append(String.valueOf((char) b));
			}
		};
		this.setPartName("Console");
		this.setTitleToolTip(this.getPartName());
		this.setTitleImage(ImageIcon.getImageIconFactory().getImage(
				IMAGE_CONSOLE_VIEW));
		final PrintStream oldOut = System.out;
		System.setOut(new PrintStream(out));
		text.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.setOut(oldOut);
			}
		});

		IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
		configureToolBar(tbm);

	}

	private void configureToolBar(IToolBarManager mgr) {
		mgr.add(new Action() {
			@Override
			public ImageDescriptor getImageDescriptor() {
				return (ImageIcon.getImageIconFactory()
						.getImageDescriptor(IMAGE_CEAR_CO));
			}

			@Override
			public String getToolTipText() {
				return "Clear Console";
			}

			public void run() {

			};
		});

	}

	public void setFocus() {
		text.setFocus();
	}
}