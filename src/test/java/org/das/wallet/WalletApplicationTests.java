package org.das.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConf.class)
@SpringBootTest
class WalletApplicationTests {

    @Test
    void contextLoads() {
    }

}
