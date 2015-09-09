package meet.mobile.controller;


import meet.mobile.ui.UILifecycleObserver;
import meet.mobile.utils.SimpleCache;

public interface Controller<UI> extends UILifecycleObserver {
    void initialize(UI ui);

    void saveState(Object outState);

    void restoreState(Object savedState);

    <T> SimpleCache<T> getCache();

    <T> void setCache(SimpleCache<T> cache);

    boolean hasCache();
}