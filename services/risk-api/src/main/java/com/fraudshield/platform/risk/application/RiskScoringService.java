package com.fraudshield.platform.risk.application;

import com.fraudshield.platform.risk.api.dto.CustomerAnalyzeRequest;
import com.fraudshield.platform.risk.api.dto.DeviceContext;
import com.fraudshield.platform.risk.api.dto.LoginAnalyzeRequest;
import com.fraudshield.platform.risk.api.dto.OrderAnalyzeRequest;
import com.fraudshield.platform.risk.api.dto.RegisterAnalyzeRequest;
import com.fraudshield.platform.risk.domain.Decision;
import com.fraudshield.platform.risk.domain.RiskLevel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RiskScoringService {

    private final int lowMax;
    private final int mediumMax;

    public RiskScoringService(
            @Value("${fraudshield.risk.score-thresholds.low-max}") int lowMax,
            @Value("${fraudshield.risk.score-thresholds.medium-max}") int mediumMax) {
        this.lowMax = lowMax;
        this.mediumMax = mediumMax;
    }

    public RiskEvaluationResult analyzeOrder(OrderAnalyzeRequest request) {
        ScoreBuilder score = new ScoreBuilder();
        if (request.recentAccount()) score.add(10, "RECENT_ACCOUNT");
        if (request.multipleAccountsDetected()) score.add(30, "MULTIPLE_ACCOUNTS");
        if (request.couponAbuseDetected()) score.add(20, "COUPON_ABUSE");
        if (request.unusualLocation()) score.add(15, "UNUSUAL_LOCATION");
        enrichWithDeviceSignals(score, request.device());
        return score.build(lowMax, mediumMax);
    }

    public RiskEvaluationResult analyzeLogin(LoginAnalyzeRequest request) {
        ScoreBuilder score = new ScoreBuilder();
        if (request.unusualCountry()) score.add(20, "UNUSUAL_COUNTRY");
        if (request.simultaneousLogin()) score.add(25, "SIMULTANEOUS_LOGIN");
        if (request.excessiveAttempts()) score.add(20, "EXCESSIVE_ATTEMPTS");
        enrichWithDeviceSignals(score, request.device());
        return score.build(lowMax, mediumMax);
    }

    public RiskEvaluationResult analyzeRegister(RegisterAnalyzeRequest request) {
        ScoreBuilder score = new ScoreBuilder();
        if (request.disposableEmail()) score.add(25, "DISPOSABLE_EMAIL");
        if (request.repeatedCpf()) score.add(30, "REPEATED_CPF");
        if (request.suspiciousAddress()) score.add(15, "SUSPICIOUS_ADDRESS");
        if (request.reusedPhone()) score.add(20, "REUSED_PHONE");
        if (request.massSignupDetected()) score.add(35, "MASS_SIGNUP");
        enrichWithDeviceSignals(score, request.device());
        return score.build(lowMax, mediumMax);
    }

    public RiskEvaluationResult analyzeCustomer(CustomerAnalyzeRequest request) {
        ScoreBuilder score = new ScoreBuilder();
        if (request.accountCompromised()) score.add(40, "ACCOUNT_COMPROMISED");
        if (request.loyaltyAbuseDetected()) score.add(25, "LOYALTY_ABUSE");
        if (request.promotionAbuseDetected()) score.add(20, "PROMOTION_ABUSE");
        if (request.previousChargebacks() > 0) score.add(Math.min(30, request.previousChargebacks() * 10), "PREVIOUS_CHARGEBACKS");
        return score.build(lowMax, mediumMax);
    }

    private void enrichWithDeviceSignals(ScoreBuilder score, DeviceContext device) {
        if (device == null) {
            return;
        }
        if (device.newDevice()) score.add(15, "NEW_DEVICE");
        if (device.vpnDetected()) score.add(20, "VPN_DETECTED");
        if (device.proxyDetected()) score.add(15, "PROXY_DETECTED");
        if (device.torDetected()) score.add(40, "TOR_DETECTED");
        if (device.automationDetected()) score.add(50, "BOT_DETECTED");
    }

    private static final class ScoreBuilder {
        private int total;
        private final List<String> reasonCodes = new ArrayList<>();

        private void add(int points, String code) {
            total += points;
            reasonCodes.add(code);
        }

        private RiskEvaluationResult build(int lowMax, int mediumMax) {
            int score = Math.min(total, 100);
            RiskLevel level = score <= lowMax ? RiskLevel.LOW : score <= mediumMax ? RiskLevel.MEDIUM : RiskLevel.HIGH;
            Decision decision = score <= lowMax ? Decision.APPROVE : score <= mediumMax ? Decision.REVIEW : Decision.BLOCK;
            return new RiskEvaluationResult(score, decision, level, List.copyOf(reasonCodes));
        }
    }
}
