package app.flashlight.data

enum class Timeout(val valueInMinutes: Long) {
    FIVE_MINUTES(5),
    TEN_MINUTES(10),
    THIRTY_MINUTES(30),
    SIXTY_MINUTES(60),
    ENDLESS_MINUTES(Long.MAX_VALUE / 60_000);

    companion object {
        val DEFAULT_TIMEOUT = ENDLESS_MINUTES
    }
}
