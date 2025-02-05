package runners;

import org.junit.platform.suite.api.*;

/*
@Suite
@SelectPackages("api")  // Почему-то эта аннотация не работает совсем
@IncludeTags("api")
 */

@Suite
@SelectClasses({
        api.user_controller.GetUserListTestApi.class,
})
public class ApiTestRunner {

}