package hu.steradian.hexffs.mcplugin.fun;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import hu.steradian.hexffs.mcplugin.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.material.MaterialData;
//import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class RefrigeratorRecipe extends CookingRecipe<RefrigeratorRecipe> {

    public RefrigeratorRecipe(@NotNull ItemStack result, @NotNull Material source) {
        super(new NamespacedKey(Main.getPlugin(), "refrigerate_" + result.getType()), result, source, 0.07F, 200);
    }

    public RefrigeratorRecipe(@NotNull ItemStack result, @NotNull RecipeChoice input) {
        super(new NamespacedKey(Main.getPlugin(), "refrigerate_" + result.getType()), result, input, 0.07F, 200);
    }

    /*@Deprecated
    public RefrigeratorRecipe(@NotNull ItemStack result, @NotNull Material source) {
        this(NamespacedKey.randomKey(), result, source, 0, 0.0F, 200);
    }*/

    /** @deprecated */
    @Deprecated
    public RefrigeratorRecipe(@NotNull ItemStack result, @NotNull MaterialData source) {
        this(NamespacedKey.randomKey(), result, source.getItemType(), source.getData(), 0.0F, 200);
    }

    /** @deprecated */
    @Deprecated
    public RefrigeratorRecipe(@NotNull ItemStack result, @NotNull MaterialData source, float experience) {
        this(NamespacedKey.randomKey(), result, source.getItemType(), source.getData(), experience, 200);
    }

    /** @deprecated */
    @Deprecated
    public RefrigeratorRecipe(@NotNull ItemStack result, @NotNull Material source, int data) {
        this(NamespacedKey.randomKey(), result, source, data, 0.0F, 200);
    }

    public RefrigeratorRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source, float experience, int cookingTime) {
        this(key, result, source, 0, experience, cookingTime);
    }

    /** @deprecated */
    @Deprecated
    public RefrigeratorRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source, int data, float experience, int cookingTime) {
        this(key, result, (RecipeChoice)(new RecipeChoice.MaterialChoice(Collections.singletonList(source))), experience, cookingTime);
    }

    public RefrigeratorRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice input, float experience, int cookingTime) {
        super(key, result, input, experience, cookingTime);
    }

    @NotNull
    public RefrigeratorRecipe setInput(@NotNull MaterialData input) {
        return this.setInput(input.getItemType(), input.getData());
    }

    @NotNull
    public RefrigeratorRecipe setInput(@NotNull Material input) {
        return (RefrigeratorRecipe)super.setInput(input);
    }

    /** @deprecated */
    @Deprecated
    public RefrigeratorRecipe setInput(@NotNull Material input, int data) {
        return this.setInputChoice(new RecipeChoice.MaterialChoice(Collections.singletonList(input)));
    }

    @NotNull
    public RefrigeratorRecipe setInputChoice(@NotNull RecipeChoice input) {
        return (RefrigeratorRecipe)super.setInputChoice(input);
    }
}
