package com.tech.gov.gds.testbase;

import com.tech.gov.gds.conf.AcceptanceTest;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@AcceptanceTest
@Tag("test")
@ExtendWith(SpringExtension.class)
@CommonsLog
public abstract class AcceptanceTestBase {
}
