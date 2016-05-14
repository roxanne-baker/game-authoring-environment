package model.physics;

import api.ICollisionVelocityCalculator;
import api.IEntity;
import api.ILevel;
import api.IPhysicsEngine;
import javafx.scene.image.ImageView;
import model.component.movement.Position;
import model.component.movement.Velocity;
import model.component.physics.Collision;
import model.component.physics.Gravity;
import model.component.visual.AnimatedSprite;
import model.component.visual.Sprite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of the physics engine
 *
 * @author Tom Wu and Roxanne Baker
 */
public class PhysicsEngine implements IPhysicsEngine {
	
    private CollisionHandler collisionHandler;

    public PhysicsEngine () {
    	this.collisionHandler = new CollisionHandler(new RealisticVelocityCalculator());
    }

    public PhysicsEngine (ICollisionVelocityCalculator velocityCalculator) {
        this.collisionHandler = new CollisionHandler(velocityCalculator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update (ILevel universe, double timePassed) {
    	updateEntityPositions(universe, timePassed);
        updateImageView(universe);
        collisionHandler.resetCollisionMasks(universe.getEntitiesWithComponent(Collision.class));
        collisionHandler.moveCollidingEntities(universe);
    }
    
    private void updateEntityPositions(ILevel universe, double timePassed) {
        Collection<IEntity> dynamicEntities = universe.getEntitiesWithComponents(Position.class, Velocity.class);
        dynamicEntities.stream().forEach(p -> {
            Position pos = p.getComponent(Position.class);
            Velocity velocity = p.getComponent(Velocity.class);
            double dx = timePassed * velocity.getVX();
            double dy = timePassed * velocity.getVY();
            pos.add(dx, dy);
        });
    }
    
    private void updateImageView(ILevel universe) {
        Collection<IEntity> entities = universe.getEntitiesWithComponents(Position.class, Sprite.class);
        entities.stream().forEach(p -> {
            Position pos = p.getComponent(Position.class);
            ImageView imageView = p.getComponent(Sprite.class).getImageView();
            imageView.setTranslateX(pos.getX());
            imageView.setTranslateY(pos.getY());
        });
    }

    private void applyGravity (ILevel universe, double secondsPassed) {
        Collection<IEntity> entitiesSubjectToGravity = universe.getEntitiesWithComponents(Gravity.class,
                Velocity.class);
        entitiesSubjectToGravity.stream().forEach(entity -> {
            Gravity acceleration = entity.getComponent(Gravity.class);
            Velocity velocity = entity.getComponent(Velocity.class);
            velocity.add(acceleration.getGravityX(), acceleration.getGravityY());
        });
    }

    private void applyCollisions (ILevel universe) {
        List<IEntity> collidableEntities = new ArrayList<IEntity>(
                universe.getEntitiesWithComponents(Collision.class, Sprite.class));
        List<IEntity> collidableAnimatedEntities = new ArrayList<IEntity>(
                universe.getEntitiesWithComponents(Collision.class, AnimatedSprite.class));
        
        collidableEntities.addAll(collidableAnimatedEntities);
        collisionHandler.clearCollisionComponents(collidableEntities);
        
        for (int i = 0; i < collidableEntities.size(); i++) {
            for (int j = i + 1; j < collidableEntities.size(); j++) {
                collisionHandler.addCollisionComponents(collidableEntities.get(i), collidableEntities.get(j));
            }
        }
    }
}
