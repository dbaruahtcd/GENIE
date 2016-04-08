package com.bwc.genie.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.bwc.genie.dialogs.GeneralMessageDialogFragment;
import com.bwc.genie.dialogs.GeneralProgressDialogFragment;
import com.bwc.genie.dialogs.ShowDialogListener;
import com.bwc.genie.dialogs.classes.DialogFragmentsButton;
import com.bwc.genie.dialogs.classes.DialogMessageNumbers;

/**
 * implemenents {@link ShowDialogListener} to be able to show basic dialogs from related fragments 
 *
 */
public abstract class AbstractActivity extends AppCompatActivity implements
		ShowDialogListener,
	GeneralMessageDialogFragment.MessageDialogCallbacks {

	private final String DialogTag = "dialog";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}
	
	@Override
	public void onFinishActivity() {
		finish();		
	}

	public void showFatalMessage(String message) {
		if(getSupportFragmentManager().findFragmentByTag(DialogTag) != null)
			((DialogFragment) getSupportFragmentManager().findFragmentByTag(DialogTag)).dismiss();

		DialogFragment dialogFragment = null;
		dialogFragment = GeneralMessageDialogFragment.newInstance(
				DialogMessageNumbers.FATAL_ERROR, message);

		dialogFragment.show(getSupportFragmentManager(), DialogTag);
	}

	public void showInformativeMessage(String message) {
		if(getSupportFragmentManager().findFragmentByTag(DialogTag) != null)
			((DialogFragment) getSupportFragmentManager().findFragmentByTag(DialogTag)).dismiss();

		DialogFragment dialogFragment = null;
		dialogFragment = GeneralMessageDialogFragment.newInstance(
				DialogMessageNumbers.INFORMATIVE_ERROR, message);

		dialogFragment.show(getSupportFragmentManager(), DialogTag);
	}

	public void showWarning(String message) {
		if(getSupportFragmentManager().findFragmentByTag(DialogTag) != null)
			((DialogFragment) getSupportFragmentManager().findFragmentByTag(DialogTag)).dismiss();

		DialogFragment dialogFragment = null;
		dialogFragment = GeneralMessageDialogFragment.newInstance(
				DialogMessageNumbers.WARNING, message);

		dialogFragment.show(getSupportFragmentManager(), DialogTag);
	}

	/**
	 * Shows a non-dismissable progressdialog with a title and a message
	 * @param title The title of the progressdialog
	 * @param message The message of the progressdialog
	 */
	public void showProgress(String title, String message) {
		if(getSupportFragmentManager().findFragmentByTag(DialogTag) != null)
			((DialogFragment) getSupportFragmentManager().findFragmentByTag(DialogTag)).dismiss();
		DialogFragment dialogFragment = GeneralProgressDialogFragment.newInstance(title, message);
		dialogFragment.show(getSupportFragmentManager(), DialogTag);
	}

	/**
	 * Shows a non-dismissable progressdialog with a message
	 * @param message The message of the progressdialog
	 */
	public void showProgress(String message) {
		showProgress(null, message);
	}

	/**
	 * Hides the current progressdialog that is currently showing
	 */
	public void hideProgress() {
		if(getSupportFragmentManager().findFragmentByTag(DialogTag) != null)
			((DialogFragment) getSupportFragmentManager().findFragmentByTag(DialogTag)).dismiss();
	}

	public void showCustomWarningDialog(String title, String message) {
		if (getSupportFragmentManager().findFragmentByTag(DialogTag) != null)
			((DialogFragment) getSupportFragmentManager().findFragmentByTag(DialogTag)).dismiss();
		DialogFragment dialogFragment = null;
		dialogFragment = GeneralMessageDialogFragment.newInstance(title, message);
		dialogFragment.show(getSupportFragmentManager(), DialogTag);
	}


	public void showCustomDialog(String title, String message, DialogFragmentsButton positiveBtn, DialogFragmentsButton negative , DialogFragmentsButton neutral) {
		if (getSupportFragmentManager().findFragmentByTag(DialogTag) != null)
			((DialogFragment) getSupportFragmentManager().findFragmentByTag(DialogTag)).dismiss();
		DialogFragment dialogFragment = null;
		dialogFragment = GeneralMessageDialogFragment.newInstance(title, message, positiveBtn, negative , neutral);
		dialogFragment.show(getSupportFragmentManager(), DialogTag);
	}
}
