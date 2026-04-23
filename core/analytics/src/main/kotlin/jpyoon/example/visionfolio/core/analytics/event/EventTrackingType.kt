package jpyoon.example.visionfolio.core.analytics.event

/**
 * 전체 이벤트 타입 정의.
 */
enum class EventTrackingType(val key: String) {
    VIEWED_HOME("viewed_home"),
    CLICKED_HOME_REFRESH("clicked_home_refresh"),
    CLICKED_TOGGLE_HIDE_AMOUNTS("clicked_toggle_hide_amounts"),
    CLICKED_TOGGLE_DISPLAY_CURRENCY("clicked_toggle_display_currency"),
    CLICKED_FILTER_CATEGORY("clicked_filter_category"),
    CLICKED_UPLOAD("clicked_upload"),
    CLICKED_OPEN_HOLDINGS("clicked_open_holdings"),

    VIEWED_TREND("viewed_trend"),
    CLICKED_TREND_PERIOD("clicked_trend_period"),
    CLICKED_TREND_CUSTOM_RANGE("clicked_trend_custom_range"),

    VIEWED_DIVIDEND("viewed_dividend"),
    CLICKED_DIVIDEND_TAB("clicked_dividend_tab"),
    CLICKED_OPEN_TREND("clicked_open_trend"),
    CLICKED_OPEN_GURU("clicked_open_guru"),
    VIEWED_GURU_DETAIL("viewed_guru_detail"),

    VIEWED_SETTINGS("viewed_settings"),
    CLICKED_SETTINGS_IMPORT("clicked_settings_import"),

    VIEWED_UPLOAD("viewed_upload"),

    VIEWED_ADD_HOLDING("viewed_add_holding"),

    VIEWED_TWEAKS("viewed_tweaks"),
}
