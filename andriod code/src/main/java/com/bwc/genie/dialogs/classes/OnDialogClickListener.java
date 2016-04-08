package com.bwc.genie.dialogs.classes;


import com.bwc.genie.dialogs.ShowDialogListener;

public interface OnDialogClickListener{
	
	public void onDialogClick(int id, Object[] params);
	public void onDialogClick(ShowDialogListener.ID id, Object[] params);

}
