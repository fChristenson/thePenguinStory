package se.fidde.thepenguinstory.util.validation;

import java.io.IOException;

import se.fidde.thepenguinstory.util.validation.HttpUrlPrefixes;
import se.fidde.thepenguinstory.util.validation.UrlValidationHandler;
import android.test.ActivityTestCase;

public class UrlValidationHandlerTest  extends ActivityTestCase{

	private final String URL = "www.google.com";
	private final String INVALID_URL = URL + "/test";

	public void testValidateUrl() throws IOException{
		boolean validUrl = UrlValidationHandler.isValidUrl(URL);
		assertTrue("is valid url", validUrl);
	}
	
	public void testValidateUrl_fail() throws IOException{
		boolean validUrl = UrlValidationHandler.isValidUrl(INVALID_URL );
		assertFalse("is not valid url", validUrl);
	}
	
   public void testIsValidFormat_http_www_prefix(){
        HttpUrlPrefixes validFormat = UrlValidationHandler.checkFormat("http://www.google.com");
        assertEquals("is valid format", HttpUrlPrefixes.HTTP_WWW_PREFIX, validFormat);
    }
   
    public void testIsValidFormat_www_prefix(){
        HttpUrlPrefixes validFormat = UrlValidationHandler.checkFormat("www.google.com");
        assertEquals("is valid format", HttpUrlPrefixes.WWW_PREFIX, validFormat);
    }
    
	public void testIsValidFormat_no_prefix(){
		HttpUrlPrefixes validFormat = UrlValidationHandler.checkFormat("google.com");
		assertEquals("is valid format", HttpUrlPrefixes.NO_PREFIX, validFormat);
	}
	
	public void testIsValidFormat_fail(){
		HttpUrlPrefixes validFormat = UrlValidationHandler.checkFormat(INVALID_URL);
		assertEquals("is not valid format", HttpUrlPrefixes.INVALID_FORMAT, validFormat);
	}
}
