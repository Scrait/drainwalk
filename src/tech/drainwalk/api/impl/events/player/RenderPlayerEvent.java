package tech.drainwalk.api.impl.events.player;

import com.darkmagician6.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerEntity;

@AllArgsConstructor
@Getter
public class RenderPlayerEvent implements Event {

    private final PlayerRenderer renderer;
    private final IRenderTypeBuffer buffers;
    private final PlayerEntity entityPlayer;

    public static class Pre extends RenderPlayerEvent
    {
        public Pre(PlayerRenderer renderer, IRenderTypeBuffer buffers, PlayerEntity playerEntity) {
            super(renderer, buffers, playerEntity);
        }
    }

    public static class Post extends RenderPlayerEvent
    {
        public Post(PlayerRenderer renderer, IRenderTypeBuffer buffers, PlayerEntity playerEntity) {
            super(renderer, buffers, playerEntity);
        }
    }

}
