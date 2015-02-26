package edu.uclm.esi.common.androidClient.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialogs {
	public static void showOneButtonDialog(Context context, String title, String msg, String buttonText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
        .setMessage(msg)
        .setCancelable(false)
        .setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
		return;
	}
	
	public static void showTwoButtonsDialog(final IDialogListener dialogListener, String title, String msg, String buttonYesText, String buttonNoText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogListener.getContext());
        builder.setTitle(title)
        .setMessage(msg)
        .setCancelable(false)
        .setPositiveButton(buttonYesText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                dialogListener.setSelectedButton(IDialogListener.YES);
            }
        })
        .setNegativeButton(buttonNoText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                dialogListener.setSelectedButton(IDialogListener.NO);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        
	}
}
