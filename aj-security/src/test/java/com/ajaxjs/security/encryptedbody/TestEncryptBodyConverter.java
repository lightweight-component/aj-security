package com.ajaxjs.security.encryptedbody;

import org.junit.jupiter.api.Test;

public class TestEncryptBodyConverter {
    String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKaQqW42605YaYArZTzSEz0m3OyAsCWC8eqdxqSrCGu4T1MhdRAUNEqwgFDA37essLvqTI6XhT54F58toZAYkFlWrSrAgcF7iR8fR4ldofIb+L0fYimHVzJjpyfqhw+1rpmZK44hzHjIuWkKCrl4NGKAezQBHjXxlOVEi2WGtj37AgMBAAECgYBj9sX4o3UtG9qVVXX4votVVBGaztDocmIF0JL7GLqBC6hv19CNydJoUO1xiY+6iCW5YbB4k28gQqrKmXQxKszWFdd1NTHOKS3nti8I2QNc4T9FF34YvYh/WQlRw7dHmYUl/MCm6U6yVE7XK8GoYYOyAyclXuFR+SRw8/gHsoAYoQJBAPQ3L+K47QSujMSzu8ZcdRingN25VS8r790A18WNxtK9l/7b3l8aTUXmeGcjLpQDnx158jQ32fTUki5aa2eGDp0CQQCumkCpTgcvx0Ys66aXKnpaexWGWDK/ui9hY7lRdd2XijK30Uo2TlQ1ujXYjodbJJUAELE3UAC+0yj8W8Edf093AkAzrHWyaGSmZ/SbLlieCTQxqkenIq72kzpmreX6BBy8vKcrowQzZVJSZwi08gnKAdYqG4J3MBYrKstfiXxOZFw1AkA6+3radrRwzHWFWTnWmQ/qHug/kO3b3M6CrMh+nz1zIslNVVMnk0BZQgVMmaFaBbqb4gerssf9rqGK1ogfKdGzAkBQzGcbSAtTlCAMNMnOXphIvFQ/GnxlPwwr23ysyt0k14SOwGNfND4rXM2rTzjz+2yF20tGdGgmXfwnvKOaCc1N";
    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmkKluNutOWGmAK2U80hM9JtzsgLAlgvHqncakqwhruE9TIXUQFDRKsIBQwN+3rLC76kyOl4U+eBefLaGQGJBZVq0qwIHBe4kfH0eJXaHyG/i9H2Iph1cyY6cn6ocPta6ZmSuOIcx4yLlpCgq5eDRigHs0AR418ZTlRItlhrY9+wIDAQAB";

    String encryptedText = "oxdp6jv0zgY7oUumYaYg4XQA5A45Z0hLHE1ms1YoYNYh7lrvwxDYW4DWwAagNYBaEsSu2uskRkynBU8YNHzXR9ZNJ8j5uduCl1MpHGqpsvKBWU4xGJU93vHZXlTpIM9Rfrys+2hZXvNPylW5Ws6bMDZlUmC2kSMsJ2gHb8om7I4=";

    @Test
    void testEncrypte() {
        String text = EncryptedBodyConverter.encrypt("{\"name\":\"Jack\"}", publicKey);
        System.out.println(text);
    }

    @Test
    void testDecrypt() {
        String text = EncryptedBodyConverter.decrypt(encryptedText, privateKey);
        System.out.println(text);
    }
}
