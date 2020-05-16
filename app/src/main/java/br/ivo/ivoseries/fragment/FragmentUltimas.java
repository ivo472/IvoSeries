package br.ivo.ivoseries.fragment;

import br.ivo.ivoseries.adapter.AdapterChapters;
import br.ivo.ivoseries.tasks.TaskGetLastSeries;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class FragmentUltimas extends FragmentChapters {

	public FragmentUltimas( LayoutInflater inflater, ViewGroup container ){
		super(inflater, container);
		
		setAdapter( new AdapterChapters(getContext()) );
		
		TaskGetLastSeries task = new TaskGetLastSeries(this);
		task.execute();
	}
}
