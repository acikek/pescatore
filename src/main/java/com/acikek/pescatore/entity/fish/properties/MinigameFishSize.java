package com.acikek.pescatore.entity.fish.properties;

/**
 * Represents the size of a minigame fish.
 * @param scale the scale multiplier of the entity.
 * @param holdTime how much time, in ticks, the player has to keep holding before
 *                 the fish can be caught.
 */
public record MinigameFishSize(float scale, int holdTime) {

    /**
     * A very small ("dinky") fish size.
     * <p>
     *     <b>Scale</b>: 0.5x<br>
     *     <b>Hold Time</b>: 20 ticks
     * </p>
     */
    public static final MinigameFishSize DINKY = new MinigameFishSize(0.5f, 20);

    /**
     * A regular fish size.
     * <p>
     *     <b>Scale</b>: 1.0x<br>
     *     <b>Hold Time</b>: 30 ticks
     * </p>
     */
    public static final MinigameFishSize REGULAR = new MinigameFishSize(1.0f, 30);

    /**
     * A fairly large ("fatty") fish size.
     * <p>
     *     <b>Scale</b>: 2.0x<br>
     *     <b>Hold Time</b>: 40 ticks
     * </p>
     */
    public static final MinigameFishSize FATTY = new MinigameFishSize(2.0f, 40);

    /**
     * A very large ("hawg") fish size.
     * <p>
     *     <b>Scale</b>: 3.0x<br>
     *     <b>Hold Time</b>: 50 ticks
     * </p>
     */
    public static final MinigameFishSize HAWG = new MinigameFishSize(3.0f, 50);

    /**
     * A gargantuan, behemoth fish size.
     * <p>
     *     <b>Scale</b>: 4.0x<br>
     *     <b>Hold Time</b>: 75 ticks
     * </p>
     */
    public static final MinigameFishSize GARGANTUAN = new MinigameFishSize(4.0f, 75);
}
