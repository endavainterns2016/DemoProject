package endava.com.demoproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.presenter.LoginPresenter;
import endava.com.demoproject.view.LoginView;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoginPresenterTest {
    @InjectMocks
    private LoginPresenter loginPresenter;
    private LoginView loginView;
    private LoginHelper loginHelper;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private EventContext eventContext;
    private Subject subject;
    private Event event;

    @Before
    public void setUp() {
        loginView = mock(LoginView.class);
        loginHelper = mock(LoginHelper.class);
        sharedPreferencesHelper = mock(SharedPreferencesHelper.class);
        subject = mock(Subject.class);
        event = mock(Event.class);
        eventContext = spy(new EventContext("key", ""));
        loginPresenter.attachView(loginView);
//        when(resourcesHelper.provideResources()).thenReturn(ResourcesHelper.getInstance().provideResources());
//        when(sharedPreferencesHelper.getUserName()).thenReturn(SharedPreferencesHelper.getInstance().getUserName());
    }

    @Test
    public void bothFieldsEmptyTest() {
        assertFalse(loginPresenter.validateCredentials("", ""));
        verify(loginView).showError(anyString());
    }

    @Test
    public void passwordFieldsEmptyTest() {
        assertFalse(loginPresenter.validateCredentials("username", ""));
        verify(loginView).showError(anyString());
    }

    @Test
    public void usernameFieldsEmptyTest() {
        assertFalse(loginPresenter.validateCredentials("", "password"));
        verify(loginView).showError(anyString());
    }

    @Test
    public void loginHelperDoLoginTest() {
        loginPresenter.doLogin("username", "password", true);
        verify(loginHelper).doLogin(anyString(), anyString());
    }

    @Test
    public void rememberUserNameTest() {
        loginPresenter.rememberUserName();
        verify(sharedPreferencesHelper).rememberUserName(anyString());
    }

    @Test
    public void forgetUserNameTest() {
        loginPresenter.forgetUserName();
        verify(sharedPreferencesHelper).forgetUserName();
    }

    @Test
    public void populateViewTest() {
        loginPresenter.populateView();
        verify(loginView).populateView("", false);
    }

    @Test
    public void getObserverKeysTest() {
        assertThat((loginPresenter.getObserverKeys().size()), greaterThanOrEqualTo(3));
    }

    @Test
    public void isMainObserverForKeyTrueTest() {
        assertTrue(loginPresenter.isMainObserverForKey(loginPresenter.getObserverKeys().get(0)));
    }

    @Test
    public void isMainObserverForKeyFalseTest() {
        loginPresenter.getObserverKeys();
        assertFalse(loginPresenter.isMainObserverForKey(eventContext));
    }

    @Test
    public void attachViewTest() {
        loginPresenter.attachView(loginView);
        verify(subject).registerObserver(loginPresenter);
    }

    @Test
    public void detachViewTest() {
        loginPresenter.detachView();
        verify(subject).unregisterObservers(loginPresenter);
    }

    @Test
    public void successfulLoginHandlerTest() {
//        when(event.getEventKey()).thenReturn(new EventContext(ResourcesHelper.getInstance().provideResources().getString(R.string.successful_login_tag), null));
        loginPresenter.getObserverKeys();
        loginPresenter.attachView(loginView);
        loginPresenter.onEvent(event);
        verify(loginView).finishLogin();
    }
    @Test
    public void credentialsErrorHandlerTest() {
//        when(event.getEventKey()).thenReturn(new EventContext(ResourcesHelper.getInstance().provideResources().getString(R.string.credential_error_tag), null));
        loginPresenter.getObserverKeys();
        loginPresenter.attachView(loginView);
        loginPresenter.onEvent(event);
        verify(loginView).showError(anyString());
    }
    @Test
    public void connectionErrorHandlerTest() {
//        when(event.getEventKey()).thenReturn(new EventContext(ResourcesHelper.getInstance().provideResources().getString(R.string.connection_error_tag), null));
        loginPresenter.getObserverKeys();
        loginPresenter.attachView(loginView);
        loginPresenter.onEvent(event);
        verify(loginView).showConnectionError();
    }
}
