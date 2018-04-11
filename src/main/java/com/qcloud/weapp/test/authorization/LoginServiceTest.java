package com.qcloud.weapp.test.authorization;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.myazure.configuration.PrimaryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import com.qcloud.weapp.authorization.*;
import com.qcloud.weapp.test.HttpMock;

@SuppressWarnings("unused")
public class LoginServiceTest {
	
	private LoginServiceTestHelper helper = new LoginServiceTestHelper();
	@Autowired
	private PrimaryConfiguration primaryConfiguration;
	 

	@Test
	public void testLoginProcess() {
		HttpMock mock = helper.createLoginHttpMock("valid-code", "valid-data", "valid-iv");
		LoginService service = new LoginService(mock.request, mock.response);
		
		try {
			UserInfo userInfo = service.login();
			assertNotNull(userInfo);
		} catch (IllegalArgumentException | LoginServiceException  e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		try {
			JSONObject body = new JSONObject(mock.getResponseText());
			assertTrue(helper.checkBodyHasMagicId(body));
			assertTrue(helper.checkBodyHasSession(body));
		} catch (JSONException e) {
			fail("invalid response body");
		}
	}

	@Test
	public void testLoginProcessWithoutCodeOrData() {
		testLoginProcessExpectError(null, "valid-data", "valid-iv");
		testLoginProcessExpectError("valid-code", null, "valid-iv");
		testLoginProcessExpectError("valid-code", "valid-data", null);
	}
	
	@Test
	public void testLoginProcessWithInvalidCodeOrData() {
		testLoginProcessExpectError("invalid-code", "valid-data", "valid-iv");
		testLoginProcessExpectError("valid-code", "invalid-data", "valid-iv");
		testLoginProcessExpectError("valid-code", "valid-data", "invalid-iv");
	}
	
	@Test
	public void testLoginProcessWithServerResponseError() {
		testLoginProcessExpectError("expect-valid-json", "valid-data", "valid-iv");
	}
	
	@Test
	public void testLoginProcessWithServer500() {
		testLoginProcessExpectError("expect-500", "valid-data", "valid-iv");
	}
	
	@Test
	public void testLoginProcessWithServerTimeout() {
		testLoginProcessExpectError("expect-timeout", "valid-data", "valid-iv");
	}
	
	private LoginServiceException testLoginProcessExpectError(String code, String encrypteData, String iv) {
		HttpMock mock = helper.createLoginHttpMock(code, encrypteData, iv);
		LoginService service = new LoginService(mock.request, mock.response);
		
		LoginServiceException errorShouldThrow = null;
		try {
			UserInfo userInfo = service.login();
		} catch (LoginServiceException e) {
			errorShouldThrow = e;
		}
		
		assertNotNull(errorShouldThrow);
		try {
			JSONObject body = new JSONObject(mock.getResponseText());
			assertTrue(helper.checkBodyHasMagicId(body));
			assertNotNull(body.get("error"));
		} catch (JSONException e) {
			fail("invalid response body");
		}
		return errorShouldThrow;
	}

	@Test
	public void testCheck() {
		HttpMock mock = helper.createCheckHttpMock("valid-id", "valid-key");
		LoginService service = new LoginService(mock.request, mock.response);
		try {
			UserInfo userInfo = service.check();
			assertNotNull(userInfo);
		} catch (IllegalArgumentException | LoginServiceException  e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		try {
			verify(mock.response, never()).getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCheckWithoutIdOrSkey() {
		testCheckExpectError(null, "valid-key", null);
		testCheckExpectError("valid-id", null, null);
	}
	
	@Test
	public void testCheckWithInvalidIdOrSkey() {
		testCheckExpectError("invalid-id", "valid-key", false);
		testCheckExpectError("valid-id", "invalid-key", false);
	}
	
	@Test
	public void testCheckWithInvalidSession() {
		testCheckExpectError("expect-60011", "valid-key", true);
		testCheckExpectError("expect-60012", "valid-key", true);
	}
	
	@Test
	public void testCheckWithServerInvalidResponse() {
		testCheckExpectError("expect-invalid-json", "valid-key", false);
	}
	
	@Test
	public void testCheckExpectError() {
		testCheckExpectError("expect-500", "valid-key", false);
	}
	
	@Test
	public void testCheckWithServerTimeout() {
		testCheckExpectError("expect-timeout", "valid-key", false);
	}
	
	private void testCheckExpectError(String id, String skey, Boolean expectInvalidSession) {
		HttpMock mock = helper.createCheckHttpMock(id, skey);
		LoginService service = new LoginService(mock.request, mock.response);
		LoginServiceException errorShouldThrow = null;
		
		try {
			UserInfo userInfo = service.check();
		} catch (LoginServiceException e) {
			errorShouldThrow = e;
		}
		
		JSONObject body = null;
		try {
			body = new JSONObject(mock.getResponseText());
			assertTrue(helper.checkBodyHasMagicId(body));
			String error = body.getString("error");
			if (expectInvalidSession != null) {
				assertEquals(error.equals("ERR_INVALID_SESSION"), expectInvalidSession);
			}
		} catch (JSONException e) {
			fail("响应格式错误::" + e.getMessage());
		}
		try {
			verify(mock.response, times(1)).getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
