package br.ivo.ivoseries.base;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class IvoBaseAdapter<T> extends BaseAdapter {

    private List<T> items;
    private final Context context;

    public IvoBaseAdapter(final Context context) {
	this.context = context;
    }

    public IvoBaseAdapter(final List<T> source, final Context context) {
	this(context);
	this.items = source;
    }

    @Override
    public int getCount() {
	if (items == null) {
	    return 0;
	}
	return items.size();
    }

    @Override
    public T getItem(final int position) {
	if (!hasItems() || position >= items.size()) {
	    return null;
	}
	return items.get(position);
    }

    @Override
    public long getItemId(final int position) {
	return position;
    }

    public List<T> getItems() {
	return items;
    }

    public void setItems(final List<T> items) {
	this.items = items;
    }

    protected Context getContext() {
	return context;
    }

    public boolean hasItems() {
	return items != null && !items.isEmpty();
    }

    protected int getIndex(final int position) {
	return position + 1;
    }  
}