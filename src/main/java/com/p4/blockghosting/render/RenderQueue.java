package com.p4.blockghosting.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class RenderQueue {
    private static int tickCounter = 0;
    private static final List<AddQueueEntry> addQueue = new ArrayList<>();
    private static final EnumMap<Layer, Map<Object, Shape>> queue = new EnumMap<>(Layer.class);

    public static void add(Layer layer, Object key, Shape shape, int life) {
        addQueue.add(new AddQueueEntry(layer, key, shape, life));
    }

    public static void addLine(Layer layer, Object key, Vec3d from, Vec3d to, int color, int life) {
        add(layer, key, new Line(from, to, color), life);
    }

    private static void doAdd(AddQueueEntry entry) {
        Map<Object, Shape> shapes = queue.computeIfAbsent(entry.layer(), k -> new LinkedHashMap<>());
        Shape oldShape = shapes.get(entry.key());
        if (oldShape != null) {
            entry.shape().prevPos = oldShape.prevPos;
        } else {
            entry.shape().prevPos = entry.shape().getPos();
        }
        entry.shape().deathTime = tickCounter + entry.life();
        shapes.put(entry.key(), entry.shape());
    }

    public static void tick() {
        queue.values().forEach(shapes -> shapes.values().forEach(shape -> shape.prevPos = shape.getPos()));
        tickCounter++;
        for (AddQueueEntry entry : addQueue) {
            doAdd(entry);
        }
        addQueue.clear();
        for (Map<Object, Shape> shapes : queue.values()) {
            Iterator<Shape> itr = shapes.values().iterator();
            while (itr.hasNext()) {
                Shape shape = itr.next();
                if (tickCounter == shape.deathTime) {
                    itr.remove();
                }
                shape.tick();
            }
        }
    }

    public static void render(Layer layer, MatrixStack matrixStack, float delta) {
        if (!queue.containsKey(layer)) return;
        queue.get(layer).values().forEach(shape -> shape.render(matrixStack, delta));
    }

    public enum Layer {
        ON_TOP
    }

    private record AddQueueEntry(Layer layer, Object key, Shape shape, int life) {}
}
