package com.waverchat.api.v1.http.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultiMessageResponse {
    private List<String> messages;
}
