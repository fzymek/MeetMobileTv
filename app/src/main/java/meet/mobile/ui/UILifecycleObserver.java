package meet.mobile.ui;

public interface UILifecycleObserver {

	void onStart();

	void onStop();

	void onPause();

	void onResume();

	void onDestroy();

}