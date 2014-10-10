package app.main;

import app.mongo.models.User;
import app.utils.MustacheTemplateEngine;
import app.utils.Param;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spark.ModelAndView;
import spark.TemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;


/**
 * Created with IntelliJ IDEA.
 *
 * @author renatomoitinhodias@gmail.com
 * @since 09/10/14 15:53
 */
public class Application {

    public static void main(String[] str){

        TemplateEngine templateEngine = new MustacheTemplateEngine();
        ApplicationContext ctx = new GenericXmlApplicationContext("application-context.xml");
        User.Repository repository = (User.Repository)ctx.getBean("userRepository");


        /**
         *
         * INDEX
         *
         */
        get("/", (req, resp) -> new ModelAndView(new Param(){{
            put("list", repository.all());
        }},"welcome.mustache"),templateEngine);

       /**
        *
        * NEW USER
        *
        */
        post("/user",(req,resp)->{

            repository.save(new User(req.queryParams("name"),req.queryParams("email")));
            resp.redirect("/");
            return null;
        });

       /**
        *
        * LOAD USER
        *
        * */
        post("/user/load" ,(req,resp)->{

            User u = repository.merge(req.queryParams("id"));
            return new ModelAndView(new Param(){{
                put("user", u);
                put("action","/update");

            }}, "welcome.mustache");
        },templateEngine);

        /**
         *
         * UPDATE USER
         *
         * */
        post("/user/update" ,(req,resp)->{

            repository.update(req.queryParams("id"), new Param.Set(){{
                put("name", req.queryParams("name"));
                put("email",req.queryParams("email"));

            }});

            return new ModelAndView(new Param(){{
                put("list", repository.all());

            }}, "welcome.mustache");
        },templateEngine);


       /**
        *
        * DELETE USER
        *
        * */
        post("/user/delete" ,(req,resp)->{

            repository.delete ( repository.merge(req.queryParams("id")));
            return new ModelAndView(new Param(){{
                put("list", repository.all());

            }}, "welcome.mustache");
        },templateEngine);


       /**
        *
        * SEARCH USER NATIVE QUERY REGEX
        *
        * */
        post("/user/search", (req,resp)->

             new ModelAndView(new Param(){{
                put("list", repository.find(repository.query("{ $or:[ {'name':{$regex:'%s.*?',$options:'i' }},{'email':{$regex:'%s.*?',$options:'i'}} ]}",
                        req.queryParams("search"),req.queryParams("search"))));
                put("search", req.queryParams("search"));

            }}, "welcome.mustache")
            ,templateEngine);



    }
}
