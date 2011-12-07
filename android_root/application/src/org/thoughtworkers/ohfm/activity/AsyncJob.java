package org.thoughtworkers.ohfm.activity;

import android.content.Context;
import android.widget.Toast;

public abstract class AsyncJob {
	private Context context;

	public AsyncJob(Context context) {
		this.context = context;
	}
	
	final public void start() {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					job();
				}
			}).run();
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	protected abstract void job();
}
