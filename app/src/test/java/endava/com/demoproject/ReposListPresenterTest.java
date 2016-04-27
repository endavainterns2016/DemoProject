package endava.com.demoproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.presenter.ReposListPresenter;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.ReposListView;
import retrofit2.Call;
import retrofit2.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ReposListPresenterTest {

    @Mock
    private SharedPreferencesHelper sharedPreferencesHelper;
    @Mock
    private DbHelper dbHelper;
    @Mock
    private UserAPI userAPI;
    @Mock
    private Call<List<Repo>> call;
    @Mock
    private ReposListView reposListView;
    @Mock
    private Repo repo;
    @Mock
    private User user;
    @Mock
    private Subject subject;
    @Mock
    private EventContext eventContext;
    @Mock
    private List<Repo> repoList;
    private Response<List<Repo>> response;
    private ReposListPresenter reposListPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reposListPresenter = new ReposListPresenter(sharedPreferencesHelper, dbHelper, userAPI, subject);
        reposListPresenter.attachView(reposListView);
        repoList.add(repo);
    }

    @Test
    public void testGetAutoSyncEnabled() throws Exception {
        when(sharedPreferencesHelper.getAutoSyncStatus()).thenReturn(true);
        reposListPresenter.getAutoSyncEnabled();
        assertTrue(sharedPreferencesHelper.getAutoSyncStatus());
    }

    @Test
    public void testGetAutoSyncInterval() throws Exception {
        when(sharedPreferencesHelper.getAutoSyncInterval()).thenReturn(60);
        reposListPresenter.getAutoSyncInterval();
        assertEquals(sharedPreferencesHelper.getAutoSyncInterval(), 60);
    }

    @Test
    public void testOnResponseFailure() throws Exception {
        reposListPresenter.onFailure(null, null);
        verify(reposListView).hideProgress();
        verify(reposListView).showError();
    }

    @Test
    public void testOnResponseBadUser() throws Exception {
        response = Response.success(null);
        reposListPresenter.onResponse(null, response);
        verify(reposListView).hideProgress();
        verify(reposListView).showError();
    }

    @Test
    public void testOnResponseSuccess() throws Exception {
        response = Response.success(repoList);
        reposListPresenter.onResponse(null, response);
        verify(dbHelper).createRepos(response.body());
        verify(reposListView).populateList(response.body());
        verify(reposListView).hideProgress();
    }


    @Test
    public void testgetObserversKeys() throws Exception {
        assertThat(reposListPresenter.getObserverKeys().size(), equalTo(1));
    }

    @Test
    public void testIsMainObserverForKey() throws Exception {
        assertTrue(reposListPresenter.isMainObserverForKey(reposListPresenter.getObserverKeys().get(0)));
    }

    @Test
    public void testAttachView() throws Exception {
        verify(reposListView).initView();
    }

    @Test
    public void testDetachView() throws Exception {
        reposListPresenter.detachView();
        assertTrue(reposListPresenter.getSubscription().isUnsubscribed());
    }

    @Test
    public void testGetUser() throws Exception {
        verify(dbHelper).getUser();
    }

    @Test
    public void testHandleReposRequest() throws Exception {
        when(userAPI.getReposList((anyString()))).thenReturn(call);
        reposListPresenter.setUser(user);
        reposListPresenter.handleReposListRequest();
        verify(userAPI).getReposList(anyString());
    }

    @Test
    public void testPopulateView() throws Exception {
        verify(reposListView).showProgress();
    }

    @Test
    public void testOnPause() throws Exception {
        reposListPresenter.onPause();
        verify(subject).unregisterObservers(reposListPresenter);
    }

    @Test
    public void testOnresume() throws Exception {
        reposListPresenter.onResume();
        verify(subject).registerObserver(reposListPresenter);
    }
}
