package android.endava.com.demoproject.cacheableObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Subject is a class that registers and unregisters {@link Observer} and takes care about
 * caches  {@link Event} and notifies {@link Observer} when {@link Event} appears
 */
public class Subject {

    public static String subjectKey = "SUBJECT_KEY";
    private static HashMap<String, Subject> subjectMap = new HashMap<>();
    private Map<EventContext, List<Event>> events = new HashMap<>();
    private List<Observer> observers = new ArrayList<>();
    private String context;

    public Subject() {
    }

    public static Subject newInstance() {
        if (subjectMap.containsKey(subjectKey)) {
            return subjectMap.get(subjectKey);
        } else {
            Subject subject = new Subject();
            subjectMap.put(subjectKey, subject);
            return subject;
        }
    }

    /**
     * Registers {@link Observer} and notifies all cached {@link Event} if they exists
     *
     * @param observer observer instance
     */
    public void registerObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException("Observer must not be null");
        }
        addNewObserver(observer);
        fireCachedEvents(observer.getObserverKeys());
    }


    /**
     * Unregisters {@link Observer}
     *
     * @param observer observer instance
     */
    public void unregisterObservers(Observer observer) {
        if (observer == null) {
            throw new NullPointerException("Observer must not be null");
        }
        observers.remove(observer);
    }

    /**
     * Caches {@link Event} and notifies {@link Observer}
     *
     * @param e event instance
     */
    public void onNewEvent(Event e) {
        final EventContext eventKey = e.getEventKey();
        if (eventKey.isOfActiveContext(context)) {
            List<Event> eventsList = events.get(eventKey);
            if (eventsList == null) {
                eventsList = new ArrayList<>();
                events.put(eventKey, eventsList);
            }
            eventsList.add(e);
            notifyScheduledEvents(eventKey);
        }
    }

    private void fireCachedEvents(List<EventContext> observerKeys) {
        if (observerKeys != null && !observerKeys.isEmpty()) {
            for (EventContext key : observerKeys) {
                notifyScheduledEvents(key);
            }
        }
    }

    private void addNewObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    private void notifyScheduledEvents(EventContext eventContext) {
        final List<Event> eventsForObserver = this.events.get(eventContext);
        if (eventsForObserver != null && !eventsForObserver.isEmpty()) {
            final List<Observer> observers = getObserver(eventContext);
            if (observers != null && !observers.isEmpty()) {
                final Iterator<Event> iterator = eventsForObserver.iterator();
                while (iterator.hasNext()) {
                    final Event event = iterator.next();
                    for (Observer observer : observers) {
                        observer.onEvent(event);
                    }
                    if (observers.get(observers.size() - 1).isMainObserverForKey(eventContext)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private List<Observer> getObserver(EventContext key) {
        List<Observer> observersToReturn = new ArrayList<>();
        for (Observer next : observers) {
            final List<EventContext> observerKeys = next.getObserverKeys();
            if (observerKeys.contains(key)) {
                observersToReturn.add(next.isMainObserverForKey(key) ? observersToReturn.size() : 0, next);
            }
        }
        return observersToReturn;
    }

    /**
     * Used to change the active context for event.
     * Also removes all the pending events.
     *
     * @param context the new context to use.
     */
    public void setContext(String context) {
        this.context = context;
        clearEventsForOldContext();
    }

    private void clearEventsForOldContext() {
        final Iterator<Map.Entry<EventContext, List<Event>>> iterator = events.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<EventContext, List<Event>> entry = iterator.next();
            if (!entry.getKey().isOfActiveContext(context)) {
                iterator.remove();
            }
        }
    }
}