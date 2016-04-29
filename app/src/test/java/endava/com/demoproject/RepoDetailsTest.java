package endava.com.demoproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Owner;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.presenter.RepoListDetailPresenter;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.RepoDetailsView;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepoDetailsTest {

    @InjectMocks
    private RepoListDetailPresenter repoDetailsPresenter;
    private RepoDetailsView repoDetailsView;
    private DbHelper dbHelper;
    private Call repoCallBack;
    private Repo repo;
    private User user;
    private Owner owner;
    private UserAPI userAPI;

    @Before
    public void setUp() {
        repoDetailsView = mock(RepoDetailsView.class);
        dbHelper = mock(DbHelper.class);
        repo = mock(Repo.class);
        user = mock(User.class);
        owner = mock(Owner.class);
        repoCallBack = mock(Call.class);
        userAPI = mock(UserAPI.class);
        repoDetailsPresenter.attachView(repoDetailsView);
    }

    @Test
    public void updateRepoTest() {
        when(dbHelper.getUser()).thenReturn(user);
        when(user.getHashedCredentials()).thenReturn("hashedCredentials");
        when(repo.getOwner()).thenReturn(owner);
        when(owner.getLogin()).thenReturn("Login");
        when(repo.getName()).thenReturn("Name");
        when(userAPI.updateRepo(anyString(),anyString(),anyString())).thenReturn(repoCallBack);
        repoDetailsPresenter.setRepo(repo);
        repoDetailsPresenter.updateRepo();
        verify(userAPI).updateRepo(anyString(),anyString(),anyString());
    }

    @Test
    public void populateViewTest() {
        repoDetailsPresenter.populateView(repo);
        verify(repoDetailsView).populateView(repo);
    }

    @Test
    public void initViewTest() {
        repoDetailsPresenter.initView();
        verify(repoDetailsView).initView();
    }

    @Test
    public void updateRepoInDBTest() {
        repoDetailsPresenter.updateRepo(repo);
        verify(dbHelper).updateRepo(repo);
    }

    @Test
    public void updateRepoOnResponseTest() {
        repoDetailsPresenter.onResponse(null, Response.success(repo));
        verify(repoDetailsView).populateView(repo);
    }

    @Test
    public void updateRepoOnFailTest() {
        repoDetailsPresenter.onFailure(null, null);
        verify(repoDetailsView).networkError();
    }
}
