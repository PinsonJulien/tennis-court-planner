package com.pinson.tennis_backend.commons.google.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDTO {
    private String domain = null;
    private String reason = null;
    private String message = null;
    private String location = null;
    private String locationType = null;
    private String extendedHelp = null;
    private String sendReport = null;
}
