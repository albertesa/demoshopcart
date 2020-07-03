package albertesa.sample.prj.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import albertesa.sample.prj.security.CookieUtil;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class CookieUtilTest {

	@Test
	public void generateCheckValueTest() {
		String id = "111@222.333";
		String uuid = UUIDUtil.generateUUID();
		String sk = "JwtSecretKey";
		String cv1 = CookieUtil.generateCheckValue(id, uuid, sk);
		System.out.println(cv1);
		assertThat(cv1).isEqualTo(CookieUtil.generateCheckValue(id, uuid, sk));
		uuid = UUIDUtil.generateUUID();
		sk = "AnotherSecretKey";
		String cv2 = CookieUtil.generateCheckValue(id, uuid, sk);
		System.out.println(cv2);
		assertThat(cv2).isEqualTo(CookieUtil.generateCheckValue(id, uuid, sk));
		assertThat(cv1).isNotEqualTo(cv2);
	}
}
