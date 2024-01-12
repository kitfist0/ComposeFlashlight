package app.flashlight.data

enum class Timeout(val valueInMinutes: Long) {
    FIVE_MINUTES(5),
    TEN_MINUTES(10),
    THIRTY_MINUTES(30),
    SIXTY_MINUTES(60),
    THOUSAND_MINUTES(1000);

    companion object {
        val DEFAULT_TIMEOUT = THOUSAND_MINUTES

        fun Timeout.toMillis(): Long = valueInMinutes * 60_000
    }
}
