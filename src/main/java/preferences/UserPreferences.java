package preferences;

import enums.FieldDimension;
import observer.Publisher;
import observer.Subscriber;
import observer.event.UserPreferencesEvent;
import view.theme.Theme;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public final class UserPreferences implements Publisher<UserPreferencesEvent> {

    private Locale locale;
    private Theme theme;
    private FieldDimension fieldDimension;
    private final Collection<Subscriber<UserPreferencesEvent>> subscribers = new HashSet<>();

    public UserPreferences(Locale locale, Theme theme, FieldDimension fieldDimension) {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(theme);
        Objects.requireNonNull(fieldDimension);
        this.locale = locale;
        this.theme = theme;
        this.fieldDimension = fieldDimension;
    }

    public synchronized Locale getLocale() {
        return locale;
    }

    public boolean setLocale(Locale locale) {
        if ( ! (locale == null || Objects.equals(locale, this.locale))) {
            this.locale = locale;
            notifySubscribers(UserPreferencesEvent.LOCALE_CHANGED);
            return true;
        }
        return false;
    }

    public synchronized Theme getTheme() {
        return theme;
    }

    public boolean setTheme(Theme theme) {
        if ( ! (theme == null || Objects.equals(theme, this.theme))) {
            this.theme = theme;
            notifySubscribers(UserPreferencesEvent.THEME_CHANGED);
            return true;
        }
        return false;
    }

    public synchronized FieldDimension getFieldDimension() {
        return fieldDimension;
    }

    public synchronized boolean setFieldDimension(FieldDimension fieldDimension) {
        if ( ! (fieldDimension == null || Objects.equals(fieldDimension, this.fieldDimension))) {
            this.fieldDimension = fieldDimension;
            return true;
        }
        return false;
    }

    @Override
    public synchronized void subscribe(Subscriber<UserPreferencesEvent> subscriber) {
        Objects.requireNonNull(subscriber);
        subscribers.add(subscriber);
    }

    @Override
    public synchronized void unsubscribe(Subscriber<UserPreferencesEvent> subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public synchronized void notifySubscribers(UserPreferencesEvent eventType) {
        for (Subscriber<UserPreferencesEvent> subscriber : subscribers) {
            subscriber.reactOnNotification(eventType);
        }
    }

}
