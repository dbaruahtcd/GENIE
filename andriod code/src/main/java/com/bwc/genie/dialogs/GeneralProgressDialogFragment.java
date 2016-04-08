package com.bwc.genie.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bwc.genie.R;

/**
 * Class that implements a progress dialog to show
 */
public class GeneralProgressDialogFragment extends GeneralDialogFragment {

	//codes to be used internally
	private static final String ARGS_MESSAGE = "message";
	private static final String ARGS_TITLE = "title";
	
	public GeneralProgressDialogFragment() {
		//do nothing to instantiate
	}
	
	/**
	 * Instantiates a progress dialog with a message to be shown
	 * @param message The actual message
	 * @return A newly {@linkplain GeneralProgressDialogFragment} fragment
	 */
	public static GeneralProgressDialogFragment newInstance(String message) {
		Bundle args = new Bundle();
		args.putString(ARGS_MESSAGE, message);
		GeneralProgressDialogFragment fr = new GeneralProgressDialogFragment();
		fr.setArguments(args);
		return fr;
	}
	
	/**
	 * Instantiates a progress dialog with a message and a title to be shown
	 * @param title The title to be shown
	 * @param message The actual message
	 * @return A newly {@linkplain GeneralProgressDialogFragment} fragment
	 */
	public static GeneralProgressDialogFragment newInstance(String title, String message) {
		Bundle args = new Bundle();
		args.putString(ARGS_MESSAGE, message);
		args.putString(ARGS_TITLE, title);
		GeneralProgressDialogFragment fr = new GeneralProgressDialogFragment();
		fr.setArguments(args);
		return fr;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
		return inflater.inflate(R.layout.general_progress_dialog_fragment_layout, container);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		String title = getArguments().getString(ARGS_TITLE);
		String message = getArguments().getString(ARGS_MESSAGE);
//		setCancelable(false);
		
		TextView messageTextView = (TextView) view.findViewById(R.id.general_dialog_message_textview_id);
		TextView titleTextView = (TextView) view.findViewById(R.id.general_dialog_message_title_textview_id);
		
		//initialise the views to be shown
		if(title == null)
			titleTextView.setVisibility(View.GONE);
		else
			titleTextView.setText(title);
		
		if(message == null)
			messageTextView.setText(R.string.ProgressDialogTitle);
		else
			messageTextView.setText(message);
	}
}
