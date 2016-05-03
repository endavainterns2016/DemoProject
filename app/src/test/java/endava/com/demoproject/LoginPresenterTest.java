package endava.com.demoproject;

import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoginPresenterTest {
    @Inject
    private LoginPresenter loginPresenter;
    @Mock
    private LoginView loginView;
    @Mock
    private LoginHelper loginHelper;
    @Mock
    public Resources resources;
    @Mock
    private SharedPreferencesHelper sharedPreferencesHelper;
    @Spy
    private EventContext eventContext = new EventContext("key", "");
    @Mock
    private Subject subject;
    @Mock
    private Event event;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
//        when(resources).thenReturn(ResourcesHelper.getInstance().provideResources());
//        loginPresenter = new LoginPresenter(resources, sharedPreferencesHelper, loginHelper, subject);
        DemoProjectApplication.getApplicationComponent().inject(loginPresenter);
        when(sharedPreferencesHelper.getUserName()).thenReturn("");
        loginPresenter.attachView(loginView);
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
    public void detachViewTest() {
        loginPresenter.detachView();
        verify(subject).unregisterObservers(loginPresenter);
    }

    @Test
    public void successfulLoginHandlerTest() {
        loginPresenter.getObserverKeys();
        loginPresenter.attachView(loginView);
        loginPresenter.onEvent(event);
        verify(loginView).finishLogin();
    }
    @Test
    public void credentialsErrorHandlerTest() {
        loginPresenter.getObserverKeys();
        loginPresenter.attachView(loginView);
        loginPresenter.onEvent(event);
        verify(loginView).showError(anyString());
    }
    @Test
    public void connectionErrorHandlerTest() {
        loginPresenter.getObserverKeys();
        loginPresenter.attachView(loginView);
        loginPresenter.onEvent(event);
        verify(loginView).showConnectionError();
    }
}
