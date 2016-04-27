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
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.ReposListPresenter;
import endava.com.demoproject.view.ReposListView;
import retrofit2.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
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
    private ReposListView reposListView;
    @Mock
    private Repo repo;
    @Mock
    private EventContext eventContext;
    @Mock
    private List<Repo> repoList;
    private Response<List<Repo>> response;
    private ReposListPresenter reposListPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reposListPresenter = new ReposListPresenter(sharedPreferencesHelper, dbHelper);
        reposListPresenter.attachView(reposListView);
        repoList.add(repo);
        when(sharedPreferencesHelper.getAutoSyncStatus()).thenReturn(true);
        when(sharedPreferencesHelper.getAutoSyncInterval()).thenReturn(60);
    }

    @Test
    public void testGetAutoSyncEnabled() throws Exception {
        reposListPresenter.getAutoSyncEnabled();
        assertTrue(sharedPreferencesHelper.getAutoSyncStatus());
    }

    @Test
    public void testGetAutoSyncInterval() throws Exception {
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
        reposListPresenter.attachView(reposListView);
        verify(reposListView, times(2)).initView();
    }

    @Test
    public void testDetachView() throws Exception {
        reposListPresenter.detachView();
        assertTrue(reposListPresenter.getSubscription().isUnsubscribed());
    }

//    @Test
//    public void testLoadUser() throws Exception {
//        reposListPresenter.loadUser();
//        assertNotNull(reposListPresenter.getLoadedUser());
//    }
}
