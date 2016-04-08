package com.bwc.genie.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bwc.genie.R;
import com.bwc.genie.dialogs.classes.DialogFragmentsButton;
import com.bwc.genie.dialogs.classes.DialogMessageNumbers;
import com.bwc.genie.dialogs.classes.OnDialogClickListener;

public class GeneralMessageDialogFragment extends GeneralDialogFragment {
	private DialogFragmentsButton positiveButton;
	private DialogFragmentsButton negativeButton;
	private DialogFragmentsButton neutralButton;
	
	public GeneralMessageDialogFragment() {}

	public static GeneralMessageDialogFragment newInstance(int id, String message) {
		Bundle args = new Bundle();
		args.putString("message", message);
		args.putInt("id", id);
		GeneralMessageDialogFragment fr = new GeneralMessageDialogFragment();
		fr.setArguments(args);
		return fr;
	}
	
	public static GeneralMessageDialogFragment newInstance(String title, String message) {
		Bundle args = new Bundle();
		args.putString("message", message);
		args.putString("title", title);
		args.putInt("id", DialogMessageNumbers.CUSTOM_DIALOG_FRAGMENT);
		GeneralMessageDialogFragment fr = new GeneralMessageDialogFragment();
		fr.setArguments(args);
		return fr;
	}

	public static GeneralMessageDialogFragment newInstance(String title, String message, DialogFragmentsButton positiveBtn, DialogFragmentsButton negativeBtn, DialogFragmentsButton neutralBtn) {
		Bundle args = new Bundle();
		args.putString("message", message);
		args.putString("title", title);
		args.putInt("id", DialogMessageNumbers.CUSTOM_DIALOG_FRAGMENT);
		GeneralMessageDialogFragment fr = new GeneralMessageDialogFragment();
		fr.setPositiveButton(positiveBtn);
		fr.setNegativeButton(negativeBtn);
		fr.setNeutralButton(neutralBtn);
		fr.setArguments(args);
		return fr;
	}
	
	
	private OnDialogClickListener listener;

	private MessageDialogCallbacks mCallbacks;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.general_dialog_fragment_layout, container);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		Bundle args = getArguments();
		setCancelable(false);
		TextView messageText = (TextView) view.findViewById(R.id.general_dialog_message_textview_id);
		TextView titleTextView = (TextView) view.findViewById(R.id.general_dialog_message_title_textview_id);
		Button ok = (Button) view.findViewById(R.id.ok);

		Button cancel = (Button) view.findViewById(R.id.cancel);

		switch (args.getInt("id")) {
		case DialogMessageNumbers.FATAL_ERROR:
			titleTextView.setText(R.string.MainAlertDialogTitle);
			messageText.setText(args.getString("message"));
			ok.setOnClickListener(OKbtnError_Click);
			ok.setText(R.string.OKAlertBtn);
			cancel.setVisibility(View.GONE);
			break;
		case DialogMessageNumbers.INFORMATIVE_ERROR:
			titleTextView.setText(R.string.MainAlertDialogTitle);
			messageText.setText(args.getString("message"));
			cancel.setVisibility(View.GONE);

			ok.setOnClickListener(OKbtn_Click);
			ok.setText(R.string.OKAlertBtn);
			
			break;
		case DialogMessageNumbers.WARNING:
			titleTextView.setText(R.string.WarningDialogTitle);
			messageText.setText(args.getString("message"));
			ok.setOnClickListener(OKbtn_Click);
			ok.setText(R.string.OKAlertBtn);

			cancel.setVisibility(View.GONE);
			break;
		case DialogMessageNumbers.CUSTOM_DIALOG_FRAGMENT:
			titleTextView.setText(args.getString("title"));
			messageText.setText(args.getString("message"));
			if(positiveButton == null) {
				ok.setText(R.string.DoneAlertBtn);
				ok.setOnClickListener(OKbtn_Click);
			} else {
				setUpButton(ok, positiveButton);
			}
			setUpButton(cancel, negativeButton);
			setUpButton((Button) view.findViewById(R.id.neutral_btn), neutralButton);
			break;
		case DialogMessageNumbers.DIALOG_EXIT:
			titleTextView.setText("Exit Surveys");
			messageText.setText(args.getString("message"));
			ok.setOnClickListener(Exit_Click);
			ok.setText(R.string.ExitAlertBtn);
			
			cancel.setOnClickListener(OKbtn_Click);
			cancel.setText(R.string.CancelAlertBtn);
			break;
			}
	}
	
	private void setUpButton(Button button, DialogFragmentsButton buttonLogic) {
		if(buttonLogic == null) {
			button.setVisibility(View.GONE);
			return;
		} 
		button.setVisibility(View.VISIBLE);
		button.setText(buttonLogic.getButtonText());
		button.setOnClickListener(buttonLogic.getOnClickListener());
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof MessageDialogCallbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (MessageDialogCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
		listener = null;
	}

	

	protected OnClickListener OKbtn_Click = new OnClickListener() {
		public void onClick(View v) {
			dismiss();
		}
	};

	protected OnClickListener OKbtnError_Click = new OnClickListener() {
		public void onClick(View v) {
			dismiss();
			mCallbacks.onFinishActivity();
		}
	};
	
	protected OnClickListener Exit_Click = new OnClickListener() {
		public void onClick(View view) {
			if(listener != null) {
				listener.onDialogClick(DialogMessageNumbers.DIALOG_EXIT, null);
			}
			dismiss();
		}
	};

	
	public static interface MessageDialogCallbacks{
		public void onFinishActivity();
	}
	
	private static MessageDialogCallbacks sDummyCallbacks = new MessageDialogCallbacks() {
		@Override
		public void onFinishActivity() {
		}
	};
	
	private class onDialogButtonClickListener implements OnClickListener {

		private boolean dismissable;
		private OnClickListener listener;
		public onDialogButtonClickListener (OnClickListener listener, boolean dismissable) {
			this.listener = listener;
			this.dismissable = dismissable;
		}
		public onDialogButtonClickListener (OnClickListener listener) {
			this.listener = listener;
			dismissable = true;
		}
		
		@Override
		public void onClick(View v) {
			if(dismissable)
				dismiss();
			listener.onClick(v);
		}
	}
	
	public void setPositiveButton(DialogFragmentsButton positiveButton) {
		if(positiveButton != null)
		this.positiveButton = new  DialogFragmentsButton(positiveButton.getButtonText(), new onDialogButtonClickListener(positiveButton.getOnClickListener()));
	}

	public void setNegativeButton(DialogFragmentsButton negativeButton) {
		if(negativeButton != null)
		this.negativeButton = new  DialogFragmentsButton(negativeButton.getButtonText(), new onDialogButtonClickListener(negativeButton.getOnClickListener()));
	}

	public void setNeutralButton(DialogFragmentsButton neutralButton) {
		if(neutralButton != null)
		this.neutralButton = new  DialogFragmentsButton(neutralButton.getButtonText(), new onDialogButtonClickListener(neutralButton.getOnClickListener()));
	}
}
