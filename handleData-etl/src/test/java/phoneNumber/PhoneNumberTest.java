package phoneNumber;

import common.util.PhoneUtil;
import org.junit.Test;


public class PhoneNumberTest {

    @Test
    public void test() throws Exception {
        System.out.println(PhoneUtil.getPhoneNumberInfo("447874269324").get("formatNumber"));
    }
}
