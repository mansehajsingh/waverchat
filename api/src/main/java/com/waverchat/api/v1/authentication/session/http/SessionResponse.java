package com.waverchat.api.v1.authentication.session.http;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionResponse {

    private String accessToken;

}
