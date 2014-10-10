package app.mongo.models;

import app.utils.APIMongoimpl;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA.
 *
 * @author renatomoitinhodias@gmail.com
 * @since 09/10/14 18:04
 */
@Document(collection = "users")
public class User{

    public @Id String id;
    public String name;
    public String email;

    public @PersistenceConstructor User() {;}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static class Repository extends APIMongoimpl<User>{}
}
