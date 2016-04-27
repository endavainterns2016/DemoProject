package endava.com.demoproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.User;
import endava.com.demoproject.presenter.MainPresenter;
import endava.com.demoproject.view.MainView;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by lbuzmacov on 27-04-16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainPresenterTest {

    @Mock
    private MainView mainView;

    @Mock
    private User user;

    @Mock
    private DbHelper dbHelper;

    private MainPresenter mainPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainPresenter = new MainPresenter(dbHelper);
        mainPresenter.attachView(mainView);
    }


    @Test
    public void testAttachView() throws Exception {
        verify(mainView).initView();
    }

    @Test
    public void testDetachView() throws Exception {
        mainPresenter.detachView();
        assertTrue(mainPresenter.getSubscription().isUnsubscribed());
    }

    @Test
    public void testGetUser() throws Exception {
        verify(dbHelper).getUser();
    }

    @Test
    public void testSetNavViewDetails() throws Exception {
        mainPresenter.setNavViewDetails(user);
        verify(mainView).setNavViewDetails(anyString(), anyString());
    }

    @Test
    public void testLogOut() throws Exception {
        mainPresenter.logOut();
        verify(mainView).logOutDialogShow();
    }

    @Test
    public void testOnLogOut() throws Exception {
        mainPresenter.onLogOut();
        verify(dbHelper).deleteUser((User) anyObject());
        verify(mainView).navigateToLoginView();
    }
}
