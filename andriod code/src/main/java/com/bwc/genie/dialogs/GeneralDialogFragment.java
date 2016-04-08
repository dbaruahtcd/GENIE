package com.bwc.genie.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bwc.genie.R;


/**
 * extend this class to create a dialog fragment which is retained through orientation changes
 * It has a default layout with title, content and 3 buttons
 *
 */
//MA(2014-07-21, 401)
public abstract class GeneralDialogFragment extends DialogFragment {
	
	protected Context applicationContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.general_dialog_fragment_layout, container, false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.applicationContext = activity.getApplicationContext();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		applicationContext = null;
	}
	
	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setDismissMessage(null);
		super.onDestroyView();
	}
	
	
}
