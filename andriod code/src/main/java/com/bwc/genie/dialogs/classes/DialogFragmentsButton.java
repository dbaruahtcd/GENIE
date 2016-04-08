package com.bwc.genie.dialogs.classes;

import android.view.View.OnClickListener;

/**
 *
 * @author sieben
 *
 */
//MA(2014-07-21, 401)
public class DialogFragmentsButton {
	private String buttonText;
	private OnClickListener onClickListener;
	
	public DialogFragmentsButton(String buttonText, OnClickListener onClickListener) {
		this.buttonText = buttonText;
		this.onClickListener = onClickListener;
	}

	public String getButtonText() {
		return buttonText;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}
}