package com.acikek.pescatore.entity.fish.properties;

/**
 * Represents the difficulty (or "difficulty") of the Pescatore minigame.
 * @param orbitSpeed how fast, in radians/s, the fish orbits around the bobber
 * @param orbitDistance the distance, in blocks, at which the fish orbits the bobber
 * @param orbitFlipChance the chance, from {@code 0.0f} to {@code 1.0f}, that the fish will
 *                        flip its orbit direction after completing a revolution
 * @param strikeSpeed how fast, in blocks/s, the fish will rush for a nibble or bite
 * @param minNibbles the minimum amount of nibbles the fish will perform before a bite
 * @param maxNibbles the maximum amount of nibbles the fish will perform before a bite
 * @param catchDuration a multiplier for {@link MinigameFishSize#holdTime()} specifying the amount
 *                      of time, in ticks, the player has to let go and reel in the fish
 */
public record MinigameProperties(
        double orbitSpeed, double orbitDistance, float orbitFlipChance, double strikeSpeed,
        int minNibbles, int maxNibbles, float catchDuration) {

    /**
     * A slow orbit speed.
     */
    public static final double SLOW_ORBIT = Math.PI / 2;
    /**
     * A normal orbit speed.
     */
    public static final double NORMAL_ORBIT = Math.PI * 3 / 4;
    /**
     * A fast orbit speed.
     */
    public static final double FAST_ORBIT = Math.PI;

    /**
     * A close orbit distance.
     */
    public static final double CLOSE_ORBIT = 1.0;
    /**
     * A medium (normal) orbit distance.
     */
    public static final double MEDIUM_ORBIT = 1.75;
    /**
     * A far orbit distance.
     */
    public static final double FAR_ORBIT = 2.75;

    /**
     * A slow strike speed.
     */
    public static final double SLOW_STRIKE = 0.5;
    /**
     * A normal strike speed.
     */
    public static final double NORMAL_STRIKE = 1.0;
    /**
     * A quick strike speed.
     */
    public static final double QUICK_STRIKE = 1.75;

    /**
     * A slow catch duration multiplier.
     */
    public static final float SLOW_CATCH_DURATION = 1.0f;
    /**
     * A normal catch duration multiplier.
     */
    public static final float NORMAL_CATCH_DURATION = 0.75f;
    /**
     * A fast catch duration multiplier.
     */
    public static final float FAST_CATCH_DURATION = 0.5f;
    /**
     * A faster catch duration multiplier.
     */
    public static final float FASTER_CATCH_DURATION = 0.35f;

    /**
     * A minigame difficulty of 1/10.
     * <p>
     *     <b>Orbit Speed</b>: Slow<br>
     *     <b>Orbit Distance</b>: Far<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Slow<br>
     *     <b>Nibbles</b>: None<br>
     *     <b>Catch Duration</b>: Slow
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_1 = new MinigameProperties(
            SLOW_ORBIT, FAR_ORBIT, 0.0f, SLOW_STRIKE, 0, 0, SLOW_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 2/10.
     * <p>
     *     <b>Orbit Speed</b>: Slow<br>
     *     <b>Orbit Distance</b>: Far<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Slow<br>
     *     <b>Nibbles</b>: 0-1<br>
     *     <b>Catch Duration</b>: Slow
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_2 = new MinigameProperties(
            SLOW_ORBIT, FAR_ORBIT, 0.0f, SLOW_STRIKE, 0, 1, SLOW_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 3/10.
     * <p>
     *     <b>Orbit Speed</b>: Slow<br>
     *     <b>Orbit Distance</b>: Medium<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Slow<br>
     *     <b>Nibbles</b>: 0-1<br>
     *     <b>Catch Duration</b>: Slow
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_3 = new MinigameProperties(
            SLOW_ORBIT, MEDIUM_ORBIT, 0.0f, SLOW_STRIKE, 0, 1, SLOW_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 4/10.
     * <p>
     *     <b>Orbit Speed</b>: Normal<br>
     *     <b>Orbit Distance</b>: Far<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Normal<br>
     *     <b>Nibbles</b>: 0-1<br>
     *     <b>Catch Duration</b>: Normal
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_4 = new MinigameProperties(
            NORMAL_ORBIT, FAR_ORBIT, 0.0f, NORMAL_STRIKE, 0, 1, NORMAL_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 5/10.
     * <p>
     *     <b>Orbit Speed</b>: Normal<br>
     *     <b>Orbit Distance</b>: Medium<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Normal<br>
     *     <b>Nibbles</b>: 1-2<br>
     *     <b>Catch Duration</b>: Normal
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_5 = new MinigameProperties(
            NORMAL_ORBIT, MEDIUM_ORBIT, 0.0f, NORMAL_STRIKE, 1, 2, NORMAL_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 6/10.
     * <p>
     *     <b>Orbit Speed</b>: Slow<br>
     *     <b>Orbit Distance</b>: Far<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Quick<br>
     *     <b>Nibbles</b>: 0-2<br>
     *     <b>Catch Duration</b>: Normal
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_6 = new MinigameProperties(
            SLOW_ORBIT, FAR_ORBIT, 0.0f, QUICK_STRIKE, 0, 2, NORMAL_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 7/10.
     * <p>
     *     <b>Orbit Speed</b>: Normal<br>
     *     <b>Orbit Distance</b>: Close<br>
     *     <b>Orbit Flip Chance</b>: 50%<br>
     *     <b>Strike Speed</b>: Normal<br>
     *     <b>Nibbles</b>: 1-3<br>
     *     <b>Catch Duration</b>: Fast
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_7 = new MinigameProperties(
            NORMAL_ORBIT, CLOSE_ORBIT, 0.5f, NORMAL_STRIKE, 1, 3, FAST_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 8/10.
     * <p>
     *     <b>Orbit Speed</b>: Fast<br>
     *     <b>Orbit Distance</b>: Far<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Quick<br>
     *     <b>Nibbles</b>: 0-3<br>
     *     <b>Catch Duration</b>: Fast
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_8 = new MinigameProperties(
            FAST_ORBIT, FAR_ORBIT, 0.0f, QUICK_STRIKE, 0, 3, FAST_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 9/10.
     * <p>
     *     <b>Orbit Speed</b>: Fast<br>
     *     <b>Orbit Distance</b>: Close<br>
     *     <b>Orbit Flip Chance</b>: 0%<br>
     *     <b>Strike Speed</b>: Quick<br>
     *     <b>Nibbles</b>: 1-4<br>
     *     <b>Catch Duration</b>: Fast
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_9 = new MinigameProperties(
            FAST_ORBIT, CLOSE_ORBIT, 0.0f, QUICK_STRIKE, 1, 4, FAST_CATCH_DURATION
    );

    /**
     * A minigame difficulty of 10/10!
     * <p>
     *     <b>Orbit Speed</b>: Fast<br>
     *     <b>Orbit Distance</b>: Close<br>
     *     <b>Orbit Flip Chance</b>: 75%<br>
     *     <b>Strike Speed</b>: Quick<br>
     *     <b>Nibbles</b>: 1-5<br>
     *     <b>Catch Duration</b>: Faster
     * </p>
     */
    public static final MinigameProperties DIFFICULTY_10 = new MinigameProperties(
            FAST_ORBIT, CLOSE_ORBIT, 0.75f, QUICK_STRIKE, 1, 5, FASTER_CATCH_DURATION
    );
}
