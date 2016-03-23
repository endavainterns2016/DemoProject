package android.endava.com.demoproject.cacheableObserver;

import java.util.List;

/**
 * A class can implement the Observer interface when it wants to be informed of changes in observable objects.
 */
public interface Observer {
    /**
     * This method is called whenever the observable object produces new {@link Event}
     * @param e the new  instance of {@link Event}
     */
    void onEvent(Event e);

    /**
     * Returns observer identifier
     * @return List of EventContext that represents events identifiers of observer
     */
    List<EventContext> getObserverKeys();


    /**
     * Called when we need to define an observer as the <b>MAIN</b> observer for a specific key.
     * If Subject finds a <b>MAIN</b> observer,
     * the event that is registered under the {#code key} will be removed from the waiting queue.
     * If no <b>MAIN</b> observer is found, the event will be propagated to that observer but will remain
     * in the queue.
     *
     * @param key an EventContext we should check against if {@link Observer} is <b>MAIN</b>.
     *
     * @return <b>true</b> if observer is <b>MAIN</b>.
     */
    boolean isMainObserverForKey(EventContext key);
}
