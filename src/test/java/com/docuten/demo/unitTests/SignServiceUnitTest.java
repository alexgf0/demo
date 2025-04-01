package com.docuten.demo.unitTests;

import com.docuten.demo.DTO.SignDto;
import com.docuten.demo.exceptions.CryptographyException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.SignatureNotProvidedException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.Keys;
import com.docuten.demo.service.KeysService;
import com.docuten.demo.service.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignServiceUnitTest {

    @Mock
    private KeysService keysService;

    @InjectMocks
    private SignService signService;

    private Keys testKeys;
    private static final String TEST_DOCUMENT_BASE64 = "VGVzdCBkb2N1bWVudCBjb250ZW50"; // "Test document content" in Base64
    private static final String VALID_SIGNATURE_BASE64 = "pPsMEAG6BjfqXcqPCBO7UB992/jbKYOzlHdNJA9qj+yhinF8I5TdDd/8l2KLXB1Z6DPAxmeS/8nJD1yQJmRq+SAd4L+VaSuZErbdaO9D6UZLmpo2Tm2iLikUreq9TabTCWbGafAmqxvzpNdeLlf08PYOc4eVXMNP5HihGl7wozXxdEaWZmmAqtSRAWm7cQzm6amM2XZFQ74Xrytywn+gpMc7onAmnTWWXi1++AYs2en2DF8nGOaCfo/9lYEvRdZr4FzFtTV94mHFur5j4fbZ7ZT/K7kPMKpO82G1hn5k3sjo+x39Sjg+P1ebbJSFuYrY2N0Psx09U5sr3JJlDRR0bQ==";
    private static final String PUBLIC_KEY_BASE64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAviYTN1xUxaDNN1lyaSZbI2a43mMgLVVw6Xb1jts2baey2QYoxG/IUjnl9aG4RytJJg/vdSu51PYsmgCpT/SO092RcvBcfNVPFrTkqzIbsOEfvSuaF+/lS+hvByBh0YdmCpKIaX5MEqC22xWPVVJT4QmuUbG6paSuQdNPUwwnii3w/D40xVxa9HxLjxxrICwuNn7qHJEEiATe4pFa5iNCgO19N66jRSJjkqM6WKJ59rixIn69ZS74D8PEBj8TS0p1FXlYmkjmJc8hvUw1innYYhydY9Eear+0I7ZymiblQi7zv7LiGxeWv+LxRM2dwKUPqmPyHscSkwG/DR5BiGVyTwIDAQAB";
    private static final String PRIVATE_KEY_BASE64 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+JhM3XFTFoM03WXJpJlsjZrjeYyAtVXDpdvWO2zZtp7LZBijEb8hSOeX1obhHK0kmD+91K7nU9iyaAKlP9I7T3ZFy8Fx81U8WtOSrMhuw4R+9K5oX7+VL6G8HIGHRh2YKkohpfkwSoLbbFY9VUlPhCa5RsbqlpK5B009TDCeKLfD8PjTFXFr0fEuPHGsgLC42fuockQSIBN7ikVrmI0KA7X03rqNFImOSozpYonn2uLEifr1lLvgPw8QGPxNLSnUVeViaSOYlzyG9TDWKedhiHJ1j0R5qv7QjtnKaJuVCLvO/suIbF5a/4vFEzZ3ApQ+qY/IexxKTAb8NHkGIZXJPAgMBAAECggEAF21kf7UiAqlhr1iXZoohv3ZX/5pR7BPzUehkr8GlbpQzTlbabf1hvnPAt+dBZ49XN1fORm7pOepMq3q6YY39jO/nDDS+CGlVkbQe2lLVo5DREsMM/LcDVrtf9o5QTvryIQpVwT2czMy9SrfYzYfTqs3BCvKtTtw/1f9dbht4wabZ5Ute7Qp4u8ANFq+xDQ9ETJNahrmsoS1CFt50hBIICohe15uiOckqndZeognwktg/GsNQ6qAht+30cPebjir/h6HjKboN44r51u/qKZlYyRNC5OqoXm0nSWipC5OtT41ARhoADyppuj+pSSzwa4fmoeBZ5pN6F3jwQtkULu/BgQKBgQDr9mcHAq1F59kQg4ZkIRuIBesLZikrvBTiTAedg3AeB6k3Z+tsFVUky5ivZ2D42w9EBqN/xLbNXLZhV5eqgE9imkOwIK/q1DidCCEwHmS1xRynHRyUjrfNZwDZzzvZbErs8CmXR98Py/h/gbQt5g8duraJGrA/D3080xR6gV7izwKBgQDOS7mnlrqAynoFA0lPZutyuNsLPUVzqcd1pgpOYuuSydC+m3tw+J9/lPbGmQj8ryRlCibd4V4UJ8ifOZUljeJN9IvanMhwp6egd8uiYEnAvkGz6ZpDF4Idylwksrx5u5b1OB8fs4xz+O29ltUYJMPA1Sgph46JvQdxIPb9zAJYgQKBgC3iVFaApsEZPJSBfvipl3jz7ajhzM+8WzNGJZTzojG0u6sSgi0aWNw22xIAwFKkBmvr/o8x6Xo1tSForw1wtf8ukEsHkC3R5tBibFG3F05hbhjxCEW7GvLOPNQeAD1fWZKJgQzx5OZle4UP68GhIv9c93HhxrP8hxX01d6cmZ7xAoGANiuKR6h7tr/0RIa1ZGU5JDnIQu5G43VMi+caw7ST+UqHRtXbFzIh58V8+29J/dgbrXB5/j9kjdRYiP783AyixSeW5cPPmk1b5fYvAadZFoSfChgoSr/MifgONBC/DxQkMqYW/iMOmZFfFC6cLCfTQzp1vln7Is4BO0KVuNUuNIECgYEAln2pJko+wEoijNrFmJx3bNLO9XP5ERBQrIP15graTVsx/QZEJi+nmpzK5wVzzTPZnYtL49+grXKb+y8HiwYpQPNL/EA6EEaaJ2fTM2Fr+zn9h3LOgBRY20GkfxGXBy8vK+e/3gF3fcZh+gAIheOrWuX/VRKHJjgRXvXNHpLcVng=";
    private UUID userId;
    private SignDto signDto;

    @BeforeEach
    public void setUp() throws Exception {
        testKeys = new Keys();
        testKeys.setPublicKey(PUBLIC_KEY_BASE64);
        testKeys.setPrivateKey(PRIVATE_KEY_BASE64);

        userId = UUID.randomUUID();

        signDto = new SignDto();
        signDto.setUserId(userId);
        signDto.setDocumentBase64(TEST_DOCUMENT_BASE64);
    }

    @Test
    public void testSignDocument_Success() throws UserNotFoundException, KeysNotFoundException, CryptographyException {
        when(keysService.get(userId)).thenReturn(testKeys);

        String signature = signService.signDocument(signDto);

        assertNotNull(signature);
        assertFalse(signature.isEmpty());

        // check that we actually called keysService.get(userId)
        verify(keysService, times(1)).get(userId);
    }

    @Test
    public void testSignDocument_UserNotFound() {
        when(keysService.get(userId)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> signService.signDocument(signDto));
        verify(keysService, times(1)).get(userId);
    }

    @Test
    public void testSignDocument_KeysNotFound() {
        when(keysService.get(userId)).thenThrow(new KeysNotFoundException());

        assertThrows(KeysNotFoundException.class, () -> signService.signDocument(signDto));
        verify(keysService, times(1)).get(userId);
    }

    @Test
    public void testVerifySignature_Success() throws Exception {
        when(keysService.get(userId)).thenReturn(testKeys);

        signDto.setDocumentSignature(VALID_SIGNATURE_BASE64);

        boolean result = signService.verifySignature(signDto);

        assertTrue(result);
        verify(keysService, times(1)).get(userId);
    }

    @Test
    public void testVerifySignature_InvalidSignature() throws Exception {
        when(keysService.get(userId)).thenReturn(testKeys);

        String validButIncorrectSignature = "hTlJYw7vNh/clWGe5PX4C8SuXNcwoSCKS26Cvbn4fRVaHs9Rrx8736qZ1J0i9wAn7VI4P1SdnKUhWFUU/UXSrsG95YTo1+SFY5WpScrj/48bb389MB5BfZyH6S61vs/0NZEY8S+YqpAm0NiS4prC24ETyJdV1EWSlZ/BAIFtvYZtVxdfopZb1lZp1lCZwqw1k8vR/WQNe5qMCQTwK/feJSwW38Q4J9xR3vzV2po+rQJbvfs2tWDWMjsnz5I9Vl2XIdjka70a+Z311g5CqsvQFeUzMtn1i91z1j/aVO3Ywyx80/Te+Br0cT+z25yt13MbKwRk9Q/wCuqappHqiJ3g7w==";

        signDto.setDocumentSignature(validButIncorrectSignature);

        reset(keysService);
        when(keysService.get(userId)).thenReturn(testKeys);

        boolean result = signService.verifySignature(signDto);

        assertFalse(result);
        verify(keysService, times(1)).get(userId);
    }

    @Test
    public void testVerifySignature_InvalidSignatureLength() {
        when(keysService.get(userId)).thenReturn(testKeys);

        signDto.setDocumentSignature(Base64.getEncoder().encodeToString("invalid-signature".getBytes()));

        CryptographyException exception = assertThrows(CryptographyException.class, () -> {
            signService.verifySignature(signDto);
        });

        assertTrue(exception.getMessage().contains("signature length"),
                "Exception should mention signature length issue");

        verify(keysService, times(1)).get(userId);
    }

    @Test
    public void testVerifySignature_SignatureNotProvided() {
        signDto.setDocumentSignature(null);

        assertThrows(SignatureNotProvidedException.class, () -> signService.verifySignature(signDto));
        verify(keysService, never()).get(signDto.getUserId());
    }

    @Test
    public void testVerifySignature_UserNotFound() {
        signDto.setDocumentSignature(VALID_SIGNATURE_BASE64);
        when(keysService.get(userId)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> signService.verifySignature(signDto));
        verify(keysService, times(1)).get(userId);
    }

    @Test
    public void testVerifySignature_KeysNotFound() {
        signDto.setDocumentSignature(VALID_SIGNATURE_BASE64);
        when(keysService.get(userId)).thenThrow(new KeysNotFoundException());

        assertThrows(KeysNotFoundException.class, () -> signService.verifySignature(signDto));
        verify(keysService, times(1)).get(userId);
    }
}