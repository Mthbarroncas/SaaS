package com.fraudshield.platform.risk.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterAnalyzeRequest(
        @Email String email,
        @NotBlank String cpf,
        @NotBlank String phone,
        boolean disposableEmail,
        boolean repeatedCpf,
        boolean suspiciousAddress,
        boolean reusedPhone,
        boolean massSignupDetected,
        @Valid DeviceContext device
) {
}
