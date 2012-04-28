package org.mariotaku.twidere.util;

import java.util.ArrayList;
import java.util.List;

import org.mariotaku.twidere.Constants;
import org.mariotaku.twidere.IUpdateService;
import org.mariotaku.twidere.app.TwidereApplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

public class ServiceInterface implements Constants, IUpdateService {

	private IUpdateService mService;
	private Context mContext;

	private BroadcastReceiver mStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BROADCAST_HOME_TIMELINE_UPDATED.equals(action)) {
				for (StateListener listener : mStateListeners)
					if (listener != null) {
						listener.onHomeTimelineRefreshed();
					}
			} else if (BROADCAST_MENTIONS_UPDATED.equals(action)) {
				for (StateListener listener : mStateListeners)
					if (listener != null) {
						listener.onMentionsRefreshed();
					}
			}
		}

	};

	private List<StateListener> mStateListeners = new ArrayList<StateListener>();

	private ServiceConnection mConntecion = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName service, IBinder obj) {
			mService = IUpdateService.Stub.asInterface(obj);
			IntentFilter filter = new IntentFilter() {

				{
					addAction(BROADCAST_HOME_TIMELINE_UPDATED);
					addAction(BROADCAST_MENTIONS_UPDATED);
				}
			};
			mContext.registerReceiver(mStatusReceiver, filter);
		}

		@Override
		public void onServiceDisconnected(ComponentName service) {
			mService = null;
		}
	};

	public ServiceInterface(Context context) {
		((TwidereApplication) context.getApplicationContext()).getCommonUtils().bindToService(
				mConntecion);
		mContext = context;

	}

	public void addStateListener(StateListener listener) {
		if (listener != null) {
			mStateListeners.add(listener);
		}

	}

	@Override
	public IBinder asBinder() {
		// Useless here
		return null;
	}

	@Override
	public int createFavorite(long[] account_ids, long status_id) {
		if (mService == null) return -1;
		try {
			return mService.createFavorite(account_ids, status_id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int destroyFavorite(long[] account_ids, long status_id) {
		if (mService == null) return -1;
		try {
			return mService.destroyFavorite(account_ids, status_id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int destroyStatus(long account_id, long status_id) {
		if (mService == null) return -1;
		try {
			return mService.destroyStatus(account_id, status_id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getHomeTimeline(long[] account_ids, long[] max_ids) {
		if (mService == null) return -1;
		try {
			return mService.getHomeTimeline(account_ids, max_ids);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getMentions(long[] account_ids, long[] max_ids) {
		if (mService == null) return -1;
		try {
			return mService.getMentions(account_ids, max_ids);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getMessages(long[] account_ids, long[] max_ids) {
		if (mService == null) return -1;
		try {
			return mService.getMessages(account_ids, max_ids);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public boolean hasActivatedTask() {
		if (mService == null) return false;
		try {
			return mService.hasActivatedTask();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isHomeTimelineRefreshing() {
		if (mService == null) return false;
		try {
			return mService.isHomeTimelineRefreshing();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isMentionsRefreshing() {
		if (mService == null) return false;
		try {
			return mService.isMentionsRefreshing();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void removeStateListener(StateListener listener) {
		if (listener != null) {
			mStateListeners.remove(listener);
		}
	}

	@Override
	public int retweetStatus(long[] account_ids, long status_id) {
		if (mService == null) return -1;
		try {
			return mService.retweetStatus(account_ids, status_id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int updateStatus(long[] account_ids, String content, Location location, Uri image_uri,
			long in_reply_to) {
		if (mService == null) return -1;
		try {
			return mService.updateStatus(account_ids, content, location, image_uri, in_reply_to);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public interface StateListener {

		void onHomeTimelineRefreshed();

		void onMentionsRefreshed();

	}
}
