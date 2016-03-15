package android.endava.com.demoproject;

/**
 * Created by lbuzmacov on 3/15/2016.
 */
public class User {
    private Long id;
    private String name;
    private String password;

    public Long getID (){
        return id;
    }

    public String getName (){
        return name;
    }

    public String getPassword (){
        return password;
    }

    public void setID (Long id){
        this.id = id;
    }

    public void setName (String name){
        this.name = name;
    }

    public void setPassword (String password){
        this.password = password;
    }

}
