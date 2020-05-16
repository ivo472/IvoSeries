package br.ivo.ivoseries.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

public class ChooseLegenda extends AlertDialog.Builder implements OnClickListener {
	private EditText eLink;
	private Handler event;
	public static final int EVENT_LEGENDA = 50;

	public ChooseLegenda(Context context, Handler arg, String sNomeSerie, String sNomeLegenda ) {
		super(context);
		setTitle(sNomeSerie);
		eLink = new EditText(context);
		setView(eLink);
		eLink.setText(sNomeLegenda);
		event = arg;
		setPositiveButton("Procurar", this);
		setNegativeButton("Cancelar", this);
	}

	public void onClick(DialogInterface arg0, int arg1) {
		if (arg1 == -1 && eLink.getText().length() > 0) {
			Message msg = new Message();
			msg.what = EVENT_LEGENDA;
			Bundle data = new Bundle();
			data.putString("data", eLink.getText().toString());
			msg.setData(data);
			event.sendMessage(msg);
		}
	}
}
