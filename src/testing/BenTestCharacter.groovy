package testing

import api.IEntity
import model.component.character.Health
import model.component.character.Score
import model.component.movement.Position
import model.component.movement.Velocity
import model.component.physics.Collision
import model.component.physics.Gravity
import model.component.physics.Mass
import model.component.physics.RestitutionCoefficient
import model.component.visual.Sprite
import model.entity.Entity

/**
 * Created by Tom on 4/7/2016.
 */
class BenTestCharacter {
    static IEntity run(String IMAGE_PATH) {
        IEntity character = new Entity()
        character.addComponent(new Health((double) 100))
        character.addComponent(new Score((double) 100))
        Position pos = new Position(100.0, 125.0)
        character.addComponent(pos)
        character.addComponent(new Sprite(IMAGE_PATH))
        character.addComponent(new Velocity(50.0, 0.0))
        character.addComponent(new Mass(30.0))
        character.addComponent(new Gravity(1500))
        character.addComponent(new Collision(null))
        character.addComponent(new RestitutionCoefficient(1))
        return character
    }
}