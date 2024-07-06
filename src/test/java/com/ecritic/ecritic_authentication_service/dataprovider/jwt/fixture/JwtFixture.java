package com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class JwtFixture {

    private static final String SIGNED_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOlsiZWNyaXRpYyJdLCJqdGkiOiJhODcyN2MzNC01NTg4LTQxYWMtODcyZS04OGUyZGY0NzJiZjkiLCJpc3MiOiJlY3JpdGljIiwidXNlcklkIjoiODIzZTJkYjMtODRjOS00ZDE3LTkzM2YtZjgyYTM3ZjRjZTM5IiwidXNlclJvbGUiOiJERUZBVUxUIiwiaWF0IjoxNzIwMjk2MjAyLCJleHAiOjQ4NzU5Njk4MDJ9.gpg31jCPqyv_G0NRUMZPxVoCrixHikO4pBbu0TeFW9c";
    private static final String DIFFERENT_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOlsiZWNyaXRpYyJdLCJqdGkiOiIzZDY2NDMzOS01OGQwLTQzNWItYmJiNi04MGVkZDdkYTQ4ZWYiLCJpc3MiOiJlY3JpdGljIiwidXNlcklkIjoiYWFjODY5YTEtMjUyNC00MjZhLTlmMjAtZWFmMjgwYjAxNmZmIiwidXNlclJvbGUiOiJERUZBVUxUIiwiaWF0IjoxNzIwMjk0NjIyLCJleHAiOjE3MjAzODEwMjJ9._bKz63wLTWN2GcICSWrZBTqGtfWtFz6WDgVTvx0pl6I";
    private static final String EXPIRED_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOlsiZWNyaXRpYyJdLCJqdGkiOiI0ZmUwZWM2YS0xMGY4LTRmZGYtYmQwOS01NWQ4NTQ1MzQyMWYiLCJpc3MiOiJlY3JpdGljIiwidXNlcklkIjoiMzE4MTBjOTctMzZlYi00OWUzLTg5NjEtZTJjMzhiMmEzOTU2IiwidXNlclJvbGUiOiJERUZBVUxUIiwiaWF0IjoxNzIwMjk2MzI1LCJleHAiOjE3MjAyOTYzMzV9.t2ZvcoVTUMWkNNjp4zbc_MTrCD0YoraDqV2Mh_iU1Wo";
    private static final String UNSIGNED_JWT = "eyJhbGciOiJub25lIn0.eyJqdGkiOiI0MWRjZDU2OS0yZTMyLTRjMjQtYmM1Mi0wNmYwYTc2MmFiNzQiLCJ1c2VySWQiOiIxYjhlMDE1Zi00MzJkLTQzNWItYjU4Ny1iZjc1OWI0MTFmMzYiLCJ1c2VyUm9sZSI6IkFETUlOIiwiaXNzIjoiZWNyaXRpYyIsImF1ZCI6WyJlY3JpdGljIl0sImlhdCI6MTcyMDI5NzM2NywiZXhwIjoxNzIwMjk3OTY3fQ.";

    public static SecretKey loadSecret() {
        byte[] secretBytes = Base64.getDecoder().decode("5n/QPAoKuaxEMHNNryfKr8buM7u40Yezp7FuHvg7W3c=");
        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }

    public static String loadSignedJwt() {
        return SIGNED_JWT;
    }

    public static String loadSignedWithDifferentKey() {
        return DIFFERENT_KEY;
    }

    public static String loadExpiredJwt() {
        return EXPIRED_JWT;
    }

    public static String loadUnsignedJwt() {
        return UNSIGNED_JWT;
    }
}
