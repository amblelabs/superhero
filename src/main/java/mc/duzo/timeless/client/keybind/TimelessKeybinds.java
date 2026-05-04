package mc.duzo.timeless.client.keybind;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class TimelessKeybinds {
    static {
        ClientTickEvents.END_CLIENT_TICK.register(TimelessKeybinds::tick);
    }

    public static List<Keybind> binds = new ArrayList<>();

    public static void register(Keybind bind) {
        binds.add(bind);
    }

    public static void init() {
        new PowerKeybind(1, GLFW.GLFW_KEY_Z).register();
        new PowerKeybind(2, GLFW.GLFW_KEY_R).register();
        new PowerKeybind(3, GLFW.GLFW_KEY_G).register();
        new PowerKeybind(4, GLFW.GLFW_KEY_H).register();
        new PowerKeybind(5, GLFW.GLFW_KEY_V).register();
        new PowerKeybind(6, GLFW.GLFW_KEY_B).register();
        new PowerKeybind(7, GLFW.GLFW_KEY_N).register();
        new PowerKeybind(8, GLFW.GLFW_KEY_M).register();
        new PowerKeybind(9, GLFW.GLFW_KEY_J).register();
    }

    private static void tick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        binds.forEach(bind -> bind.tick(player));

        KeybindSync.tick(player);
        PowerRightClickHandler.tick(client);
        PowerLeftClickHandler.tick(client);
    }
}
