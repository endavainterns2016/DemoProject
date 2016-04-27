package endava.com.demoproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.CommitModel;
import endava.com.demoproject.model.Owner;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.presenter.RepoCommitsPresenter;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.RepoCommitsView;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lbuzmacov on 27-04-16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepoCommitsPresenterTest {
    @Mock
    private DbHelper dbHelper;

    @Mock
    private UserAPI userAPI;

    @Mock
    private Call<List<CommitModel>> call;

    @Mock
    private Repo repo;

    @Mock
    private User user;

    @Mock
    private Owner owner;

    @Mock
    private CommitModel commitModel;

    @Mock
    private List<CommitModel> repoCommitList;

    @Mock
    private RepoCommitsView repoCommitsView;
    private Response<List<CommitModel>> response;
    private RepoCommitsPresenter repoCommitsPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        repoCommitsPresenter = new RepoCommitsPresenter(dbHelper, userAPI);
        repoCommitsPresenter.attachView(repoCommitsView);
        repoCommitList.add(commitModel);
    }

    @Test
    public void testAttachView() throws Exception {
        verify(repoCommitsView).initView();
    }

    @Test
    public void testDetachView() throws Exception {
        repoCommitsPresenter.detachView();
        assertTrue(repoCommitsPresenter.getSubscription().isUnsubscribed());
    }

    @Test
    public void testPopulateView() throws Exception {
        verify(repoCommitsView).showProgress();
    }

    @Test
    public void testGetRepo() throws Exception {
        verify(dbHelper).getRepoById(anyInt());
    }

    @Test
    public void testHandleCommitsListRequest() throws Exception {
        when(dbHelper.getUser()).thenReturn(user);
        when(user.getHashedCredentials()).thenReturn("credentials");
        when(repo.getOwner()).thenReturn(owner);
        when(owner.getLogin()).thenReturn("login");
        when(repo.getName()).thenReturn("name");
        when(userAPI.getCommitsList(anyString(), anyString(), anyString())).thenReturn(call);
        repoCommitsPresenter.handleCommitsListRequest(repo);
        verify(userAPI).getCommitsList(anyString(), anyString(), anyString());
    }

    @Test
    public void testLoadRepoComplete() throws Exception {
        when(userAPI.getCommitsList(anyString(), anyString(), anyString())).thenReturn(call);
        when(dbHelper.getUser()).thenReturn(user);
        when(user.getHashedCredentials()).thenReturn("credentials");
        when(repo.getOwner()).thenReturn(owner);
        when(owner.getLogin()).thenReturn("login");
        when(repo.getName()).thenReturn("name");
        repoCommitsPresenter.onNext(repo);
        verify(repoCommitsView).setRepoName(anyString());
    }


    @Test
    public void testOnResponseFailure() throws Exception {
        repoCommitsPresenter.onFailure(null, null);
        verify(repoCommitsView).hideProgress();
        verify(repoCommitsView).showError();
    }

    @Test
    public void testOnResponseBadUser() throws Exception {
        response = Response.success(null);
        repoCommitsPresenter.onResponse(null, response);
        verify(repoCommitsView).hideProgress();
        verify(repoCommitsView).showError();
    }

    @Test
    public void testOnResponseSuccess() throws Exception {
        response = Response.success(repoCommitList);
        repoCommitsPresenter.onResponse(null, response);
        verify(repoCommitsView).populateList(response.body());
        verify(repoCommitsView).hideProgress();
    }
}
