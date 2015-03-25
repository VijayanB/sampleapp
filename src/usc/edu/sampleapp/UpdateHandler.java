package usc.edu.sampleapp;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;

// Require-Bundle: org.eclipse.equinox.p2.core|engine|operation|metadata.repository
// Feature: org.eclipse.equinox.p2.core.feature
//
// !!! Do not run from within IDE. Update only works in an exported product !!!
//
public class UpdateHandler {
  private static final String REPOSITORY_LOC = "file:///home/vijay/test12/";//
  //System.getProperty("UpdateHandler.Repo", "http://localhost/repository");

  public void execute(final IProvisioningAgent agent,
      final IWorkbench workbench) {
    Job j = new Job("Update Job details") {
      @Override
      protected IStatus run(final IProgressMonitor monitor) {
        return checkForUpdates(agent,   workbench, monitor);
      }
    };
    j.schedule();
  }

  private IStatus checkForUpdates(final IProvisioningAgent agent,
      
      final IWorkbench workbench, IProgressMonitor monitor) {

    /* 1. configure update operation */
    final ProvisioningSession session = new ProvisioningSession(agent);
    final UpdateOperation operation = new UpdateOperation(session);
    configureUpdate(operation);

    /* 2. Check for updates */

    // run check if updates are available (causing I/O)
    final IStatus status = operation.resolveModal(monitor);

    // Failed to find updates (inform user and exit)
    if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
      showMessage();
      return Status.CANCEL_STATUS;
    }

    /* 3. run installation */
    final ProvisioningJob provisioningJob = operation
        .getProvisioningJob(monitor);

    // updates cannot run from within Eclipse IDE!!!
    if (provisioningJob == null) {
  //    logger.error("Maybe you are trying to update from the Eclipse IDE? This won't work!!!");
      return Status.CANCEL_STATUS;
    }
    configureProvisioningJob(provisioningJob, workbench);

    provisioningJob.schedule();
    return Status.OK_STATUS;

  }

  private void configureProvisioningJob(ProvisioningJob provisioningJob,
      final IWorkbench workbench) {

    // Register a job change listener to track
    // installation progress and notify user upon success
    provisioningJob.addJobChangeListener(new JobChangeAdapter() {
      @Override
      public void done(IJobChangeEvent event) {
        if (event.getResult().isOK()) {
          Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
              boolean restart = MessageDialog
                  .openQuestion(Display.getDefault().getActiveShell(),
                      "Updates installed, restart?",
                      "Updates have been installed successfully, do you want to restart?");
              if (restart) {
                workbench.restart();
              }
            }
          });

        }
        super.done(event);
      }
    });

  }

  private void showMessage() {
	 Display.getDefault().syncExec(new Runnable() {

      @Override
      public void run() {
        MessageDialog
            .openWarning(Display.getDefault().getActiveShell(), "No update",
                "No updates for the current installation have been found");
      }
    });
  }

  private UpdateOperation configureUpdate(final UpdateOperation operation) {
    // create uri and check for validity
    URI uri = null;
    try {
      uri = new URI(REPOSITORY_LOC);
    } catch (final URISyntaxException e) {
   //   logger.error(e);
      return null;
    }

    // set location of artifact and metadata repo
    operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
    operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });
    return operation;
  }
} 
