/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package tickserver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TickServerSimulatorTest {

    @Test
    void checkJavaVersion() {
        assertEquals(17, Runtime.version().feature(), "Java 17 needed but bot found.");
    }
}
