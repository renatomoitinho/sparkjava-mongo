package app.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author renatomoitinhodias@gmail.com
 * @since 10/10/14 04:08
 */
public interface APIMongo<T> {
    Query query(String query);
    Query query(String query,Object... params);
    Query query(Criteria criteria);
    List<T> all();
    List<T> find(Query query);
    T merge(String id);
    void save(T t);
    void update(String id, Param.Set set);
    void delete(T t);

}
