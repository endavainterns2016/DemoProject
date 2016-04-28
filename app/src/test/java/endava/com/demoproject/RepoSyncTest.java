package endava.com.demoproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.presenter.ReposSyncPresenter;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.ReposSyncView;
import retrofit2.Call;
import retrofit2.Response;
import rx.Subscription;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepoSyncTest {


    private ReposSyncPresenter repoSyncPresenter;
    private ReposSyncView reposSyncView;
    private Subscription subscription;
    private UserAPI userAPI;
    private Call callBack;
    private User user;
    private DbHelper dbHelper;
    private List <Repo> body;
    private List <Repo> emptyBody;
    private Repo repo;

    @Before
    public void setUp() {
        repo = mock(Repo.class);
        body = Arrays.asList(repo);
        user = mock(User.class);
        reposSyncView = mock(ReposSyncView.class);
        subscription = mock(Subscription.class);
        userAPI = mock(UserAPI.class);
        callBack = mock(Call.class);
        dbHelper = mock(DbHelper.class);
        repoSyncPresenter = new ReposSyncPresenter(userAPI,dbHelper);
    }

    @Test
    public void onAttachTest() {
        repoSyncPresenter.attachView(reposSyncView);
        verify(reposSyncView).initView();
    }

    @Test
    public void onDetachTest() {
        repoSyncPresenter.loadUser();
        repoSyncPresenter.detachView();
        assertTrue(repoSyncPresenter.getSubscription().isUnsubscribed());
    }


    @Test
    public void loadUserTest() {
        assertNotNull(subscription);
    }

    @Test
    public void populateViewTest(){
        repoSyncPresenter.attachView(reposSyncView);
        repoSyncPresenter.populateView();
        verify(reposSyncView,atLeast(2)).showProgress();
    }

    @Test
    public void handleReposListRequestTest() {
        when(user.getHashedCredentials()).thenReturn("credentials");
        when(userAPI.getReposList("credentials")).thenReturn(callBack);
        repoSyncPresenter.handleReposListRequest(user);
        verify(userAPI).getReposList(anyString());
    }

    @Test
    public void onResponseWithBodyTest(){
        repoSyncPresenter.attachView(reposSyncView);
        repoSyncPresenter.onResponse(null, Response.success(body));
        verify(dbHelper).createRepos(body);
        verify(reposSyncView).populateList(body);
        verify(reposSyncView).hideProgress();
    }
    @Test
    public void onResponseWithEmptyBody(){
        repoSyncPresenter.attachView(reposSyncView);
        repoSyncPresenter.onResponse(null, Response.success(emptyBody));
        verify(reposSyncView).hideProgress();
        verify(reposSyncView).showError();
    }

    @Test
    public void onFailedResponse(){
        repoSyncPresenter.attachView(reposSyncView);
        repoSyncPresenter.onFailure(null, null);
        verify(reposSyncView).hideProgress();
        verify(reposSyncView).showError();
    }

    @Test
    public void getUserTest(){
        repoSyncPresenter.getUser();
        verify(dbHelper).getUser();
    }
}
