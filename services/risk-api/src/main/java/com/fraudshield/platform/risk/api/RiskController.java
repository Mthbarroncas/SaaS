package com.fraudshield.platform.risk.api;

import com.fraudshield.platform.common.api.ApiResponse;
import com.fraudshield.platform.risk.api.dto.CustomerAnalyzeRequest;
import com.fraudshield.platform.risk.api.dto.EventRequest;
import com.fraudshield.platform.risk.api.dto.LoginAnalyzeRequest;
import com.fraudshield.platform.risk.api.dto.OrderAnalyzeRequest;
import com.fraudshield.platform.risk.api.dto.RegisterAnalyzeRequest;
import com.fraudshield.platform.risk.api.dto.RiskDecisionResponse;
import com.fraudshield.platform.risk.api.dto.dashboard.DashboardSummaryResponse;
import com.fraudshield.platform.risk.application.RiskAssessmentService;
import com.fraudshield.platform.risk.application.RiskScoringService;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RiskController {

    private final RiskScoringService riskScoringService;
    private final RiskAssessmentService riskAssessmentService;

    public RiskController(
            RiskScoringService riskScoringService,
            RiskAssessmentService riskAssessmentService) {
        this.riskScoringService = riskScoringService;
        this.riskAssessmentService = riskAssessmentService;
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse<Map<String, Object>> ingestEvent(@Valid @RequestBody EventRequest request) {
        return ApiResponse.success(Map.of(
                "status", "accepted",
                "eventType", request.eventType(),
                "occurredAt", request.occurredAt() != null ? request.occurredAt() : OffsetDateTime.now()));
    }

    @PostMapping("/orders/analyze")
    public ApiResponse<RiskDecisionResponse> analyzeOrder(@Valid @RequestBody OrderAnalyzeRequest request) {
        var evaluation = riskScoringService.analyzeOrder(request);
        return ApiResponse.success(riskAssessmentService.persistAssessment(
                "ORDER",
                request.orderId(),
                "ORDER_ANALYSIS",
                evaluation,
                request.device()));
    }

    @PostMapping("/login/analyze")
    public ApiResponse<RiskDecisionResponse> analyzeLogin(@Valid @RequestBody LoginAnalyzeRequest request) {
        var evaluation = riskScoringService.analyzeLogin(request);
        return ApiResponse.success(riskAssessmentService.persistAssessment(
                "CUSTOMER",
                request.customerId(),
                "LOGIN_ANALYSIS",
                evaluation,
                request.device()));
    }

    @PostMapping("/register/analyze")
    public ApiResponse<RiskDecisionResponse> analyzeRegister(@Valid @RequestBody RegisterAnalyzeRequest request) {
        var evaluation = riskScoringService.analyzeRegister(request);
        return ApiResponse.success(riskAssessmentService.persistAssessment(
                "CUSTOMER",
                request.email(),
                "REGISTER_ANALYSIS",
                evaluation,
                request.device()));
    }

    @PostMapping("/customers/analyze")
    public ApiResponse<RiskDecisionResponse> analyzeCustomer(@Valid @RequestBody CustomerAnalyzeRequest request) {
        var evaluation = riskScoringService.analyzeCustomer(request);
        return ApiResponse.success(riskAssessmentService.persistAssessment(
                "CUSTOMER",
                request.customerId(),
                "CUSTOMER_ANALYSIS",
                evaluation,
                null));
    }

    @GetMapping("/risk-score/{assessmentId}")
    public ApiResponse<Map<String, Object>> getRiskScore(@PathVariable String assessmentId) {
        return ApiResponse.success(Map.of(
                "assessmentId", assessmentId,
                "assessment", riskAssessmentService.findAssessment(assessmentId)));
    }

    @GetMapping("/alerts")
    public ApiResponse<Object> getAlerts() {
        return ApiResponse.success(riskAssessmentService.latestAlerts());
    }

    @PatchMapping("/alerts/{alertId}/acknowledge")
    public ApiResponse<Object> acknowledgeAlert(@PathVariable String alertId) {
        return ApiResponse.success(riskAssessmentService.acknowledgeAlert(alertId));
    }

    @GetMapping("/devices")
    public ApiResponse<Object> getDevices() {
        return ApiResponse.success(riskAssessmentService.latestDevices());
    }

    @GetMapping("/assessments")
    public ApiResponse<Object> getAssessments() {
        return ApiResponse.success(riskAssessmentService.latestAssessmentItems());
    }

    @GetMapping("/dashboard/summary")
    public ApiResponse<DashboardSummaryResponse> getDashboardSummary() {
        return ApiResponse.success(riskAssessmentService.dashboardSummary());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNotFound(IllegalArgumentException exception) {
        return new ApiResponse<>(java.util.UUID.randomUUID(), java.time.OffsetDateTime.now(), null, java.util.List.of(exception.getMessage()));
    }
}
