package com.p4.blockghosting.render;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Square extends Shape {

    private final Line[] edges = new Line[4];
    public final Vec3d start;
    public final Vec3d size;

    public Square(Vec3d start, int color) {
        this(new Vec3d(start.x + 0.25, start.y + 0.15, start.z + 0.25), new Vec3d(start.x + 0.75, start.y + 0.15, start.z + 0.75), color);
    }
    public Square(BlockPos pos, int color) { // this is the one to pass to for farmland rendering
        this(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), color);
    }

    public Square(Vec3d start, Vec3d end, int color) {
        this.start = start;
        this.size = new Vec3d(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
        this.edges[0] = new Line(this.start, this.start.add(this.size.getX(), 0, 0), color);
        this.edges[1] = new Line(this.start, this.start.add(0, 0, this.size.getZ()), color);
        this.edges[2] = new Line(this.start.add(this.size.getX(), 0, this.size.getZ()), this.start.add(this.size.getX(), 0, 0), color);
        this.edges[3] = new Line(this.start.add(this.size.getX(), 0, this.size.getZ()), this.start.add(0, 0, this.size.getZ()), color);
    }

    @Override
    public void render(MatrixStack matrixStack, float delta) {
        BufferBuilder buff = Tessellator.getInstance().getBuffer();
        buff.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (Line edge : this.edges) {
            edge.renderLine(matrixStack, buff, delta, prevPos.subtract(getPos()));
        }
        Tessellator.getInstance().draw();
    }

    @Override
    public Vec3d getPos() {
        return start;
    }

    @Override
    public String toString() {
        return start.toString();
    }
}
