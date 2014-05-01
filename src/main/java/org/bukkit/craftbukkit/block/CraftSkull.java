package org.bukkit.craftbukkit.block;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Multiset.Entry;

import net.minecraft.server.TileEntitySkull;

import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftSkull extends CraftBlockState implements Skull {
    private final TileEntitySkull skull;
    private String player;
    private SkullType skullType;
    private byte rotation;
    private final int MAX_OWNER_LENGTH = 16;
    private static HashMap<Integer, SkullType> idToSkullType;
    private static HashMap<Byte, BlockFace> idToBlockFace;

    public CraftSkull(final Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        skull = (TileEntitySkull) world.getTileEntityAt(getX(), getY(), getZ());
        player = skull.getExtraType();
        skullType = getSkullType(skull.getSkullType());
        rotation = (byte) skull.getRotation();

        idToSkullType = new HashMap<Integer, SkullType>();
        idToSkullType.put(0, SkullType.SKELETON);
        idToSkullType.put(1, SkullType.WITHER);
        idToSkullType.put(2, SkullType.ZOMBIE);
        idToSkullType.put(3, SkullType.PLAYER);
        idToSkullType.put(4, SkullType.CREEPER);

        idToBlockFace = new HashMap<Byte, BlockFace>();
        idToBlockFace.put((byte) 0, BlockFace.NORTH);
        idToBlockFace.put((byte) 1, BlockFace.NORTH_NORTH_EAST);
        idToBlockFace.put((byte) 2, BlockFace.NORTH_EAST);
        idToBlockFace.put((byte) 3, BlockFace.EAST_NORTH_EAST);
        idToBlockFace.put((byte) 4, BlockFace.EAST);
        idToBlockFace.put((byte) 5, BlockFace.EAST_SOUTH_EAST);
        idToBlockFace.put((byte) 6, BlockFace.SOUTH_EAST);
        idToBlockFace.put((byte) 7, BlockFace.SOUTH_SOUTH_EAST);
        idToBlockFace.put((byte) 8, BlockFace.SOUTH);
        idToBlockFace.put((byte) 9, BlockFace.SOUTH_SOUTH_WEST);
        idToBlockFace.put((byte) 10, BlockFace.SOUTH_WEST);
        idToBlockFace.put((byte) 11, BlockFace.WEST_SOUTH_WEST);
        idToBlockFace.put((byte) 12, BlockFace.WEST);
        idToBlockFace.put((byte) 13, BlockFace.WEST_NORTH_WEST);
        idToBlockFace.put((byte) 14, BlockFace.NORTH_WEST);
        idToBlockFace.put((byte) 15, BlockFace.NORTH_NORTH_WEST);
    }

    // TODO: test this to see if it works!: :#3
    public static <E, T> Object getIdFromObject(HashMap<T, E> map, E value) {
        Iterator<Map.Entry<T, E>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<T, E> entry = iterator.next();
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new AssertionError(value);
    }

    static SkullType getSkullType(int id) {
        SkullType result = idToSkullType.get(id);
        if (result != null) {
            return result;
        }
        throw new AssertionError(id);
    }

    static int getSkullType(SkullType type) {
        Integer result = (Integer) getIdFromObject(idToSkullType, type);
        if (result != null) {
            return result;
        }
        throw new AssertionError(type);
    }

    static byte getBlockFace(BlockFace rotation) {
        Byte result = (Byte) getIdFromObject(idToBlockFace, rotation);
        if(result != null){
            return result;
        }
        throw new IllegalArgumentException("Invalid BlockFace rotation: " + rotation);
        
    }

    static BlockFace getBlockFace(byte rotation) {
        BlockFace result = idToBlockFace.get(rotation);
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("Invalid BlockFace rotation: "+ rotation);
    }

    public boolean hasOwner() {
        return !Strings.isNullOrEmpty(player);
    }

    public String getOwner() {
        return player;
    }

    public boolean setOwner(String name) {
        if (name == null || name.length() > MAX_OWNER_LENGTH) {
            return false;
        }
        player = name;

        if (skullType != SkullType.PLAYER) {
            skullType = SkullType.PLAYER;
        }

        return true;
    }

    public BlockFace getRotation() {
        return getBlockFace(rotation);
    }

    public void setRotation(BlockFace rotation) {
        this.rotation = getBlockFace(rotation);
    }

    public SkullType getSkullType() {
        return skullType;
    }

    public void setSkullType(SkullType skullType) {
        this.skullType = skullType;

        if (skullType != SkullType.PLAYER) {
            player = "";
        }
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            skull.setSkullType(getSkullType(skullType), player);
            skull.setRotation(rotation);
            skull.update();
        }

        return result;
    }
}
