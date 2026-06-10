package platformer.code.gamelogic.player;

import java.awt.Color;
import java.awt.Graphics;

import platformer.code.gameengine.PhysicsObject;
import platformer.code.gameengine.graphics.MyGraphics;
import platformer.code.gameengine.hitbox.RectHitbox;
import platformer.code.gamelogic.Main;
import platformer.code.gamelogic.level.Level;
import platformer.code.gamelogic.tiles.Tile;

public class Player extends PhysicsObject{
	private float walkSpeed = 600;
	private int walkChange = 100;
	private int slowMuffin = 10;
	private float accSpeedX = 0;
	private float jumpPower = 1350;
	private int damage = 0;
	private boolean dashing = false;
	private int stocksLeft = 0;
	private int maxJumps = 0;
	private int jumpsTaken = 0;
	private int wallJumpMax = 0;
	private int wallJumpsTaken = 0;
	private long wallHoldTimer = 0;
	private long timeOnWallMax = 0;
	private boolean isJumping = false;

	public Player(float x, float y, Level level) {
	
		super(x, y, level.getLevelData().getTileSize(), level.getLevelData().getTileSize(), level);
		int offset =(int)(level.getLevelData().getTileSize()*0.1); //hitbox is offset by 10% of the player size.
		this.hitbox = new RectHitbox(this, offset,offset, width -(offset*7), height - offset);
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		
		movementVector.x = 0;
		if(PlayerInput.isLeftKeyDown()) {
			if(accSpeedX < -walkSpeed && !dashing) {
				accSpeedX += slowMuffin;
				if(accSpeedX > -walkSpeed) {
					accSpeedX = -walkSpeed;
				}
			}
			else if (accSpeedX > -walkSpeed) {
				accSpeedX -= walkChange;
				if(accSpeedX < -walkSpeed) {
					accSpeedX = -walkSpeed;
				}
			}
			else {
				accSpeedX = -walkSpeed;
			}
		}
		if(PlayerInput.isRightKeyDown()) {
			if(accSpeedX > walkSpeed && !dashing) {
				accSpeedX -= slowMuffin;
				if(accSpeedX < walkSpeed) {
					accSpeedX = walkSpeed;
				}
			}
			else if (accSpeedX < walkSpeed) {
				accSpeedX += walkChange;
				if(accSpeedX > walkSpeed) {
					accSpeedX = walkSpeed;
				}
			}
		}
		if(!PlayerInput.isRightKeyDown() && !PlayerInput.isLeftKeyDown()) {
			if(accSpeedX >= -100 && accSpeedX <= 100) {
				accSpeedX = 0;
			}
			else {
				accSpeedX -= (int)(accSpeedX/2);
			}
		}
		movementVector.x = accSpeedX;
		if(PlayerInput.isJumpKeyDown() && !isJumping) {
			movementVector.y = -jumpPower;
			isJumping = true;
		}
		
		isJumping = true;
		if(collisionMatrix[BOT] != null) isJumping = false;
		//if(movementVector.x != 0)
		//System.out.println(movementVector.x);
	}


	public void damageChar(int damageTaken) {
		damage+=damageTaken;
	}


	public void launchChar(int launch, int direction, int damageTaken) {
		if(direction == 0) {
			movementVector.x = 0;
			movementVector.y = (float)(-1*(1+.01*damage)*launch);
			damage+=damageTaken;
		}
		else if(direction == 1) {
			movementVector.x = (float)((1+.01*damage)*launch/2);
			movementVector.y = (float)(-1*(1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
		else if(direction == 2) {
			movementVector.x = (float)((1+.01*damage)*launch);
			movementVector.y = 0;
			damage+=damageTaken;
		}
		else if(direction == 3) {
			movementVector.x = (float)((1+.01*damage)*launch/2);
			movementVector.y = (float)((1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
		else if(direction == 4) {
			movementVector.x = 0;
			movementVector.y = (float)((1+.01*damage)*launch);
			damage+=damageTaken;
		}
		else if(direction == 5) {
			movementVector.x = (float)(-1*(1+.01*damage)*launch/2);
			movementVector.y = (float)((1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
		else if(direction == 6) {
			movementVector.x = (float)(-1*(1+.01*damage)*launch);
			movementVector.y = 0;
			damage+=damageTaken;
		}
		else if(direction == 7) {
			movementVector.x = (float)(-1*(1+.01*damage)*launch/2);
			movementVector.y = (float)(-1*(1+.01*damage)*launch/2);
			damage+=damageTaken;
		}
	}


	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		MyGraphics.fillRectWithOutline(g, (int)getX(), (int)getY(), width, height);
		
		if(Main.DEBUGGING) {
			for (int i = 0; i < closestMatrix.length; i++) {
				Tile t = closestMatrix[i];
				if(t != null) {
					g.setColor(Color.RED);
					g.drawRect((int)t.getX(), (int)t.getY(), t.getSize(), t.getSize());
				}
			}
		}
		
		hitbox.draw(g);
	}
}
