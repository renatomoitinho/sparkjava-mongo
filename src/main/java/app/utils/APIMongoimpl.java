package app.utils;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author renatomoitinhodias@gmail.com
 * @since 10/10/14 04:19
 */
public abstract class APIMongoimpl<T> implements APIMongo<T> {

    protected Class<T> clazz;
    protected MongoOperations template;

    @SuppressWarnings({"unchecked"})
    public  APIMongoimpl() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public final Query query(String query) {
        return new BasicQuery(query);
    }

    @Override
    public final Query query(String query, Object... params) {
        return new BasicQuery(String.format(query,params));
    }

    @Override
    public final Query query(Criteria criteria) {
        return new Query(criteria);
    }

    @Override
    public final List<T> all() {
        return template.findAll(clazz);
    }

    @Override
    public final List<T> find(Query query) {
        return template.find(query,clazz);
    }

    @Override
    public final T merge(String id) {
        return template.findOne(query("{'id':'%s'}", id),clazz);
    }

    @Override
    public final void save(T t) {
        template.save(t);
    }

    @Override
    public final void update(String id, Param.Set set) {

       if(set.isEmpty())
          return;

       Update update=null;
       int count= 0;
       for (Map.Entry<String, Object> entry : set.entrySet()) {
            if (count == 0)
                update = Update.update(entry.getKey(), entry.getValue());
            else
                update.set(entry.getKey(), entry.getValue());

            count++;
       }

       template.updateFirst(query("{'id':'%s'}", id),update,clazz);

    }

    @Override
    public final void delete(T t) {
        template.remove(t);
    }

    public final MongoOperations getTemplate() {
        return template;
    }

    public final void setTemplate(MongoOperations template) {
        this.template = template;
    }
}
