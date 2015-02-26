package edu.uclm.esi.common.androidClient.dialogs;

import android.content.Context;

public interface IDialogListener {
	static int YES = 1, NO = 0;

	Context getContext();

	void setSelectedButton(int button);

}
