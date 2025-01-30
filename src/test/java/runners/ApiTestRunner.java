package runners;

import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.*;

@Suite
@SelectPackages("api")  // Указывает, что тесты должны быть из пакета api
@IncludeTags("api")  // Если нужен запуск по тегу
public class ApiTestRunner {
    @BeforeAll
    public static void setUp() {
        System.out.println("Running tests from ApiTestRunner...");
    }
}