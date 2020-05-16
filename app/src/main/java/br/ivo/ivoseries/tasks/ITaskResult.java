package br.ivo.ivoseries.tasks;

import android.content.Context;

public interface ITaskResult {
	public Context getTaskContext();
	public void onTaskFinished(Object object);
}
