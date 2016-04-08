package com.bwc.genie.dialogs;

public interface ShowDialogListener {

	enum ID {
		CHOOSE_TYPE_OF_NEW_CUSTOMER
	}
	
	public void showFatalMessage(String message) ;
	
	public void showInformativeMessage(String message);
	
	public void showWarning(String message);
	
	public void showCustomDialog(ID id, Object[] params);
	
}
