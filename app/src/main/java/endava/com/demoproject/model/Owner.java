package endava.com.demoproject.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Owner")
public class Owner {

    public final static String LOGIN = "LOGIN";
    public final static String ID = "ID";

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(dataType = DataType.STRING, columnName = LOGIN)
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
