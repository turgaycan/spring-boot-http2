package dev.turgaycan.springboothttp2.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String EMPLOYEE_LIST_URL = "https://turgay.dev:8443/rest/v10/employees";
    public static final String SSL_SERVER_PEM_FILE_NAME = "server.pem";
}
