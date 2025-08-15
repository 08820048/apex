package com.xuyi.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一API响应格式
 *
 * @author xuyi
 */
@Getter
@Setter
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private long timestamp;

    /**
     * 查询统计信息（可选）
     */
    private QueryStatsDTO queryStats;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public ApiResponse(int code, String message, T data, QueryStatsDTO queryStats) {
        this(code, message, data);
        this.queryStats = queryStats;
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(401, message);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message);
    }

    // 带查询统计的响应方法
    public static <T> ApiResponse<T> successWithStats(T data, QueryStatsDTO queryStats) {
        return new ApiResponse<>(200, "操作成功", data, queryStats);
    }

    public static <T> ApiResponse<T> successWithStats(String message, T data, QueryStatsDTO queryStats) {
        return new ApiResponse<>(200, message, data, queryStats);
    }

    public static ApiResponse<Void> successWithStats(String message, QueryStatsDTO queryStats) {
        return new ApiResponse<>(200, message, null, queryStats);
    }
}
