package albertesa.sample.prj.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import albertesa.sample.prj.security.CookieUtil;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class CookieUtilTest {

	@Test
	public void generateCheckValueTest() {
		String id = "111@222.333";
		String idr = id.replace("@", "%^&").replace(".", "!@#");
		String uuid = UUIDUtil.generateUUID();
		String uuidr = uuid.replace("-", "_@$%^&*)+(@_");
		System.out.println(uuidr);
		String sk = "JwtSecretKey";
		String cv = CookieUtil.generateCheckValue(idr, uuidr, sk);
		System.out.println(String.format("%s.%s", uuid, cv));
		sk = "AnotherSecretKey";
		System.out.println(CookieUtil.generateCheckValue(id, uuid, sk));
	}
}
