package com.bwc.genie.dialogs.classes;

import android.widget.AdapterView.OnItemClickListener;

public class DialogFragmentOnItemClickListener {

	private OnItemClickListener onItemClickListener;
	private boolean dismissable;
	
	public DialogFragmentOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
		dismissable = true;
	}

	public DialogFragmentOnItemClickListener(OnItemClickListener onItemClickListener, boolean dismissable) {
		this.onItemClickListener = onItemClickListener;
		this.dismissable = dismissable;
	}
	
	public boolean isDismissable() {
		return dismissable;
	}

	public void setDismissable(boolean dismissable) {
		this.dismissable = dismissable;
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}
}
