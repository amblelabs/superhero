package mc.duzo.timeless.core.entities;

import mc.duzo.timeless.core.TimelessEntityTypes;
import mc.duzo.timeless.core.TimelessItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class BaseRangEntity extends ThrownItemEntity {
    public BaseRangEntity(EntityType<? extends BaseRangEntity> entityType, World world) {
        super(entityType, world);
    }

    public BaseRangEntity(World world, LivingEntity owner) {
        super(TimelessEntityTypes.BASE_RANG_ENTITY_ENTITY_TYPE, owner, world);
    }

    public BaseRangEntity(World world, double x, double y, double z) {
        super(TimelessEntityTypes.BASE_RANG_ENTITY_ENTITY_TYPE, x, y, z, world);
    }

    public BaseRangEntity(World world, PlayerEntity player, ItemStack itemStack) {
        super(TimelessEntityTypes.BASE_RANG_ENTITY_ENTITY_TYPE, player, world);
        this.setItem(itemStack.isEmpty() ? new ItemStack(Items.SNOWBALL) : itemStack);
    }

    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return itemStack.isEmpty() ? ParticleTypes.ELECTRIC_SPARK : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }

    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), 8);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, (byte)3);
            this.discard();
        }

    }
}
