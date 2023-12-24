package com.acikek.pescatore.item.trinket;

import com.acikek.pescatore.item.PescatoreItems;
import dev.emi.trinkets.api.TrinketsApi;

public class PescatoreTrinketsCompat {

    public static void register() {
        TrinketsApi.registerTrinket(PescatoreItems.PIRANHA_TOOTH_NECKLACE, PiranhaNecklaceTrinket.INSTANCE);
    }
}
