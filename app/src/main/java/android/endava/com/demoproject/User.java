package android.endava.com.demoproject;

/**
 * Created by lbuzmacov on 3/15/2016.
 */
public class User {
    private int id;
    private String name;
    private String password;

    public int getID (){
        return id;
    }

    public String getName (){
        return name;
    }

    public String getPassword (){
        return password;
    }

    public void setID (int id){
        this.id = id;
    }

    public void setName (String name){
        this.name = name;
    }

    public void setPassword (String password){
        this.password = password;
    }

}
