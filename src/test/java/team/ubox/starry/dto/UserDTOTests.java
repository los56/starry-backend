package team.ubox.starry.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

public class UserDTOTests {

    @Test
    void UUIDTest() {
        UUID test = UUID.randomUUID();
        System.out.println(test);

        String rep = test.toString().replace("-", "");
        System.out.println(rep);


//        UUID test2 = UUID.fromString(sb.toString());
//        Assertions.assertEquals(test, test2);
    }
}
