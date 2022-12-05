package app.flashlight.data

enum class Mode {
    MODE_0,
    MODE_1,
    MODE_2,
    MODE_3,
    MODE_4,
    MODE_5,
    MODE_6,
    MODE_7,
    MODE_8,
    MODE_9,
    MODE_10;

    companion object {
        val DEFAULT_MODE = MODE_0
        fun Mode.toDelay() = (values().size - ordinal) * 100L
    }
}
