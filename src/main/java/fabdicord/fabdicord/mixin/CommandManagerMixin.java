package fabdicord.fabdicord.mixin;

import com.mojang.brigadier.ParseResults;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static fabdicord.fabdicord.Fabdicord.config;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Inject(method = ("execute"), at = @At("HEAD"))
    public void executeCommandInject(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfoReturnable<Integer> cir) {
        ServerPlayerEntity player = (parseResults.getContext().getSource()).getPlayer();
        //command type:server:player:command
        ServerPlayNetworking.send(
                Objects.requireNonNull(player),
                new Identifier("velocity", "fabdicord"),
                new PacketByteBuf(Unpooled.wrappedBuffer(("COMMAND:"+config.get("SERVER")+":"+player.getName().getString()+":"+command).getBytes(StandardCharsets.UTF_8))));
    }
}
