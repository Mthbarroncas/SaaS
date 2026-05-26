package com.fraudshield.platform;

import static org.assertj.core.api.Assertions.assertThat;

import com.fraudshield.platform.risk.api.dto.DeviceContext;
import com.fraudshield.platform.risk.api.dto.OrderAnalyzeRequest;
import com.fraudshield.platform.risk.application.RiskScoringService;
import com.fraudshield.platform.risk.domain.Decision;
import org.junit.jupiter.api.Test;

class RiskScoringServiceTest {

    @Test
    void shouldBlockWhenHighRiskSignalsAccumulate() {
        RiskScoringService service = new RiskScoringService(30, 60);
        OrderAnalyzeRequest request = new OrderAnalyzeRequest(
                "ord-1",
                "cus-1",
                15990,
                "BRL",
                true,
                true,
                false,
                true,
                new DeviceContext(
                        "dev-1",
                        "ua",
                        "chrome",
                        "windows",
                        "pt-BR",
                        "America/Sao_Paulo",
                        "127.0.0.1",
                        "1920x1080",
                        "canvas",
                        "webgl",
                        true,
                        true,
                        false,
                        false,
                        false
                ));

        var result = service.analyzeOrder(request);

        assertThat(result.score()).isGreaterThan(60);
        assertThat(result.decision()).isEqualTo(Decision.BLOCK);
        assertThat(result.reasonCodes()).contains("NEW_DEVICE", "VPN_DETECTED", "MULTIPLE_ACCOUNTS");
    }
}
