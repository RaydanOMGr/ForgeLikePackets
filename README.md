# Forge-like Packets
The fabric API has its own way of handling packets, but it seperates client and server packet registration.
This causes a lot of duplicate code, and is not very nice to work with. 
This library aims to fix that by providing a forge-like packet system for fabric, which is based on top of fabrics packet system for maximal compatibility.

## What are Forge-like Packets?
Forge-like packets are a way to register packets in a single class, and have them automatically registered on both the client and server side.
This means each packet has a class, each class must have these three methods:
- `encode`: Encodes the packet to a FriendlyByteBuffer
- `decode`: Static method, decodes the packet from a FriendlyByteBuffer and returns a new instance of the packet
- `handle`: Handles the packet, this is called on the client or server side when the packet is received.

## Usage
To use this library, you first need to publish it to your local maven repository. <br>
To do this, run `./gradlew publishToMavenLocal` in the root directory of this project. <br>
Then, add this to your build.gradle:
```gradle
repositories {
    mavenLocal()
}

dependencies {
    modImplementation include("me.andreasmelone:forge-like-packets:1.0.2")
}
```

This library will be jar-in-jar'd into your mod, so you don't need to worry about it being a dependency for your users.

## Example
Here is an example of how to use this library:
```java
// this is written in yarn because that is the standard for fabric
public class ExamplePacket {
    public static final Identifier ID = new Identifier("examplemod", "examplepacket");
    private String message;
    
    public ExamplePacket(String message) {
        this.message = message;
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeString(message);
    }
    
    public static ExamplePacket decode(FriendlyByteBuf buf) {
        return new ExamplePacket(buf.readString());
    }
    
    public void handle(PacketContext ctx) {
        ctx.enqueueWork(() -> {
            if(ctx.getDirection() == NetworkSide.CLIENTBOUND) {
                MinecraftClient.getInstance().player.sendMessage(Text.literal(message), false);
            } else {
                LogUtils.getLogger().info(message);
            }
        });
    }
}
```
```java
public class ExampleMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // registering the packet
        PacketRegistry.INSTANCE.register(
            ExamplePacket.ID,
            ExamplePacket.class,
            ExamplePacket::encode,
            ExamplePacket::decode,
            ExamplePacket::handle
        );

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // sending the packet from the client (requires you to be on a server)
            PacketRegistry.INSTANCE.sendToServer(ExamplePacket.ID, new ExamplePacket("hello from the client!"));
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // sending the packet from the server
            PacketRegistry.INSTANCE.sendTo(handler.getPlayer(), ExamplePacket.ID, new ExamplePacket("hello from the server!"));
        });
    }
}
```
