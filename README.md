# Forge-like Packets
The fabric API has its own way of handling packets, but it seperates client and server packet registration.
This causes a lot of duplicate code, and is not very nice to work with. 
This library aims to fix that by providing a forge-like packet system for fabric, which is based on fabrics packet system for maximal compatibility.

## What are Forge-like Packets?
Forge-like packets are a way to register packets in a single class, and have them automatically registered on both the client and server side.
This means each packet has a class, each class must have these three methods:
- `encode`: Encodes the packet to a FriendlyByteBuffer
- `decode`: Static methods, decodes the packet from a FriendlyByteBuffer and returns a new instance of the packet
- `handle`: Handles the packet, this is called on the client or server side when the packet is received. <br>
This method is a little bit more complicated as it takes in a Supplier of a context.

## Usage
To use this library, you first need to publish it to your local maven repository. <br>
To do this, run `./gradlew publishToMavenLocal` in the root directory of this project. <br>
Then, add this to your build.gradle:
```gradle
repositories {
    mavenLocal()
}

dependencies {
    modImplementation "me.andreasmelone:forge-like-packets:1.0.1"
}
```

This library will be jar-in-jar'd into your mod, so you don't need to worry about it being a dependency for your users.

## Example
Here is an example of how to use this library:
```java
public class ExamplePacket {
    private String message;
    
    public ExamplePacket(String message) {
        this.message = message;
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }
    
    public static ExamplePacket decode(FriendlyByteBuf buf) {
        return new ExamplePacket(buf.readUtf());
    }
    
    public void handle(Supplier<PacketContext> context) {
        context.get().enqueueWork(() -> {
            System.out.println("Received message: " + message);
        });
    }
}
