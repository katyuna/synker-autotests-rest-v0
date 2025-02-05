package runners;

import org.junit.platform.suite.api.*;
import api.tests.sync_controller.*;
import api.tests.user_controller.*;

/*
@Suite
@SelectPackages("api")  // Почему-то эта аннотация не работает совсем
@IncludeTags("api")
 */

@Suite
@SelectClasses({
        //User controller
        GetUserListTestApi.class,
        DeleteUserTestApi.class,
        GetUserByIdTestApi.class,
        GetUserListTestApi.class,
        //Sync controller
        DeleteSyncTaskTestApi.class,
        UpdateSyncTaskTestApi.class,
        GetSyncTaskByIdTestApi.class,
        GetSyncTaskListTestApi.class,
        CreateNewSyncTaskTestApi.class,
        StartSyncTaskByIdTestApi.class,
        ValidateSyncTaskByIdTestApi.class
})
public class ApiTestRunner {

}