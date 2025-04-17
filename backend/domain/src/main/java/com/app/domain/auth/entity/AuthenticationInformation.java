package com.app.domain.auth.entity;

import java.util.Date;

public record AuthenticationInformation(String username, Date expiration) {

}
